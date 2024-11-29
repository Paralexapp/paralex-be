package com.paralex.erp.services;

import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.entities.WalletEntity;
import com.paralex.erp.entities.WalletTransactionEntity;
import com.paralex.erp.repositories.WalletRepository;
import com.paralex.erp.repositories.WalletTransactionRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.paralex.erp.services.UserService.DATE_OF_ACTION;
import static com.paralex.erp.services.UserService.EEEE_MMMM_DD_YYYY_HH_MM_SS;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class WalletService {
    public static final String CREDIT = "Credit";
    public static final String DEBIT = "Debit";

    @Value("${mail.from.address}")
    private String fromAddress;

    @Value("${email.template.wallet-transaction}")
    private String walletTransactionNotificationTemplate;

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final UserEntity userEntity;

    @Transactional
    public WalletTransactionEntity debitWallet(@NotNull DebitWalletDto debitWalletDto) throws MessagingException, IOException {
        final var walletBalance = getWalletBalance().getBalance();

        if (debitWalletDto.getAmount() > walletBalance)
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Cannot perform transaction. Insufficient balance!");

        final var transaction = walletTransactionRepository.save(WalletTransactionEntity.builder()
                .amount(debitWalletDto.getAmount())
                .type(DEBIT)
                .walletId(findMyWallet().orElseThrow()
                        .getId())
                .creatorId(userEntity.getId())
                .build());

        final var emailBody = prepareWalletTransactionNotificationTemplate(DEBIT, "NGN", debitWalletDto.getAmount());

        emailService.sendEmail(EmailEnvelopeDto.builder()
                .fromAddress(fromAddress)
                .toAddress(userEntity.getEmail())
                .subject("Wallet " + DEBIT + " Transaction on ParalexApp")
                .emailBody(emailBody)
                .build());

        return transaction;
    }

    public String prepareWalletTransactionNotificationTemplate(
            String typeOfTransaction,
            String currency,
            int amount) throws IOException {
        final HashMap<String, Object> scopes = new HashMap<>();

        scopes.put("typeOfTransaction", typeOfTransaction);
        scopes.put("amount", amount);
        scopes.put("currency", currency);
        scopes.put(DATE_OF_ACTION, LocalDateTime.now().format(DateTimeFormatter.ofPattern(EEEE_MMMM_DD_YYYY_HH_MM_SS)));

        return emailService.prepareTemplate(emailService.loadMustacheTemplate(walletTransactionNotificationTemplate)
                        .getInputStream(),
                walletTransactionNotificationTemplate.split("..")[0], scopes);
    }

    @Transactional
    public WalletTransactionEntity topUpWallet(@NotNull TopUpWalletDto topUpWalletDto) throws MessagingException, IOException {
        final var verifyTransactionResponse = paymentService.verifyTransactionByReference((topUpWalletDto.getTransactionReference()));

        final var status = verifyTransactionResponse.getStatus();

        if (!Objects.equals(status, "SUCCESSFUL"))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid Operation carried out with invalid reference");

        final var amount = verifyTransactionResponse.getAmount();

        final var transaction = walletTransactionRepository.save(WalletTransactionEntity.builder()
                        .amount(amount)
                        .type(CREDIT)
                        .walletId(findMyWallet().orElseThrow()
                                .getId())
                        .creatorId(userEntity.getId())
                .build());

        final var emailBody = prepareWalletTransactionNotificationTemplate(CREDIT, "NGN", amount);

        emailService.sendEmail(EmailEnvelopeDto.builder()
                .fromAddress(fromAddress)
                .toAddress(userEntity.getEmail())
                .subject("Wallet " + CREDIT + " Transaction on ParalexApp")
                .emailBody(emailBody)
                .build());

        return transaction;
    }

    public List<WalletTransactionEntity> getWalletTransactions(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize());
        final var example = Example.of(WalletTransactionEntity.builder()
                .creatorId(userEntity.getId())
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time", "status"));

        return walletTransactionRepository.findAll(example, pageable)
                .getContent();
    }

    public WalletBalanceDto getWalletBalance() {
        final var sumOfWalletCredits = walletTransactionRepository.sumAmountByCreatorIdAndType(CREDIT, userEntity.getId())
                .getSum();
        final var sumOfWalletDebits = walletTransactionRepository.sumAmountByCreatorIdAndType(DEBIT, userEntity.getId())
                .getSum();
        final var balance = sumOfWalletCredits - sumOfWalletDebits;

        return WalletBalanceDto.builder()
                .balance(balance)
                .build();
    }

    public Optional<WalletEntity> findMyWallet() {
        return walletRepository.findOne(Example.of(WalletEntity.builder()
                .creatorId(userEntity.getId())
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time", "status")));
    }

    public void createWallet() {
        final var existingWallet = findMyWallet();

        if (existingWallet.isPresent())
            return;

        // INFO no incentives, no promo
        walletRepository.save(WalletEntity.builder()
                        .status(false)
                        .creatorId(userEntity.getId())
                .build());
    }
}
