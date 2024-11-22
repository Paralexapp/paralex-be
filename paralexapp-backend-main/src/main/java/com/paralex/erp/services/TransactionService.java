package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.AddTransactionDto;
import com.paralex.erp.dtos.FindTransactionDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.dtos.UpdateTransactionDto;
import com.paralex.erp.entities.TransactionEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public Optional<TransactionEntity> getTransactionById(@NotNull String id) {
        return transactionRepository.findById(id);
    }

    public void createTransactions(@NotNull @NotEmpty List<AddTransactionDto> addTransactionDtoList) {
        transactionRepository.saveAll(addTransactionDtoList.stream()
                .map(item -> TransactionEntity.builder()
                        .name(item.getTransactionItemId())
                        .amount(item.getAmount())
                        .transactionItemId(item.getTransactionItemId())
                        .status(Boolean.TRUE.equals(item.getStatus()))
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public List<TransactionEntity> getTransactions(@NotNull FindTransactionDto findTransactionDto) {
        final var pageable = PageRequest.of(findTransactionDto.getPageNumber(), findTransactionDto.getPageSize(), Sort.by("time")
                .descending());
        final var example = Example.of(TransactionEntity.builder()
                .transactionItemId(findTransactionDto.getTransactionItemId())
                .build(), ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                .withIgnorePaths("id", "time"));

        return transactionRepository.findAll(example, pageable)
                .getContent();
    }

    @Transactional
    public void updateTransaction(
            @NotNull String id,
            @ValidateUpdateItem(
                  keyList = { "id", "time", "creatorId" },
                  modelClass = UpdateTransactionDto.class) List<UpdateItemDto> changes) {
        final var criteriaBuilder = entityManager.getCriteriaBuilder();
        final var update = criteriaBuilder.createCriteriaUpdate(TransactionEntity.class);
        final var root = update.from(TransactionEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update)
                .executeUpdate();
    }

    public void deleteTransaction(@NotNull String id) {
        transactionRepository.deleteById(id);
    }
}
