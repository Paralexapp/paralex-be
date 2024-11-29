package com.paralex.erp.services;

import com.paralex.erp.documents.TransactionRequestDocument;
import com.paralex.erp.documents.TransactionRequestSubmissionDocument;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.TransactionRequestRepository;
import com.paralex.erp.repositories.TransactionRequestSubmissionRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.aggregation.VariableOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.StringWriter;
import java.time.ZoneOffset;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class TransactionRequestService {
    private final TransactionRequestRepository transactionRequestRepository;
    private final TransactionRequestSubmissionRepository transactionRequestSubmissionRepository;
    private final MongoTemplate mongoTemplate;
    private final UserEntity userEntity;
    private final PaymentService paymentService;
    private final TransactionService transactionService;

    // https://www.baeldung.com/apache-commons-csv
    public String downloadTransactions(@NotNull DateTimeRequestDto dateTimeRequestDto) throws IOException {
        final var transactions = getTransactionRequests(dateTimeRequestDto);
        final StringWriter sw = new StringWriter();
        final CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("ID", "Transaction Reference", "Processed", "Suspended", "Amount Paid", "Original Amount", "Transaction Name", "Transaction ID", "User ID", "Transaction Time")
                .build();

        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
            for (final var transaction : transactions) {
                printer.printRecord(
                        transaction.getId(),
                        transaction.getTransactionReference(),
                        transaction.isProcessed(),
                        transaction.isSuspended(),
                        transaction.getAmountPaid(),
                        transaction.getTransaction().getAmount(),
                        transaction.getTransaction().getName(),
                        transaction.getTransaction().getId(),
                        transaction.getCreatorId(),
                        transaction.getTime().toString());
            }
        }

        return sw.toString();
    }

    public void suspendTransactionRequest(@NotNull String id) {
        final var query = Query.query(Criteria.where("id").is(id));
        final var update = new Update();

        update.set("suspended", true);
        update.set("processed", true);

        mongoTemplate.updateFirst(query, update, TransactionRequestDocument.class);
    }

    public void processTransactionRequest(@NotNull String id) {
        final var query = Query.query(Criteria.where("id").is(id));
        final var update = new Update();

        update.set("suspended", false);
        update.set("processed", true);

        mongoTemplate.
                updateFirst(query, update, TransactionRequestDocument.class);
    }

    public List<TransactionRequestDto> getTransactionRequests(@NotNull DateTimeRequestDto dateTimeRequestDto) {
        final var startDate = dateTimeRequestDto.getStart();
        final var endDate = dateTimeRequestDto.getEnd();

        var where = new Criteria();

        where
            .and("time")
            .gte(startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
            .lte(endDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());

        var convertToTimeStage = addFields()
                .addField("time")
                .withValueOf(ConvertOperators.ToLong.toLong("time"))
                .build();
        var matchStage = match(where);
        final var lookupStage = lookup().from("transactionRequestSubmissions")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("id")
                        .forField("_id"))
                .pipeline(match(Criteria.expr(() ->
                        Document.parse("{ '$eq': ['$transactionRequestId', '$$id'] }")))
                )
                .as("submissions");
        var sortStage = sort(Sort.by("time").descending());

        final TypedAggregation<TransactionRequestDocument> aggregation = newAggregation(
                TransactionRequestDocument.class,
                convertToTimeStage,
                matchStage,
                lookupStage,
                sortStage)
                .withOptions(AggregationOptions.builder()
                        .allowDiskUse(true)
                        .build());

        return mongoTemplate.aggregate(aggregation, TransactionRequestDto.class)
                .getMappedResults();
    }

    public List<TransactionRequestDto> getMyTransactionRequests(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto) {
        return getTransactionRequests(dateTimePaginatedRequestDto, userEntity.getId());
    }

    public List<TransactionRequestDto> getTransactionRequests(@NotNull DateTimePaginatedRequestDto dateTimePaginatedRequestDto, @Nullable String creatorId) {
        final var pageNumber = dateTimePaginatedRequestDto.getPageNumber();
        final var pageSize = dateTimePaginatedRequestDto.getPageSize();
        final var startDate = dateTimePaginatedRequestDto.getStartDate();
        final var endDate = dateTimePaginatedRequestDto.getEndDate();

        var where = new Criteria();

        if (startDate != null && endDate != null)
            where = where
                    .and("time")
                    .gte(startDate.toInstant(ZoneOffset.UTC)
                            .toEpochMilli())
                    .lte(endDate.toInstant(ZoneOffset.UTC)
                            .toEpochMilli());

        if (creatorId != null)
            where.and("creatorId")
                    .is(creatorId);

        final var convertToTimeStage = addFields()
                .addField("time")
                .withValueOf(ConvertOperators.ToLong.toLong("time"))
                .build();
        final var matchStage = match(where);
        final var lookupStage = lookup().from("transactionRequestSubmissions")
                .let(VariableOperators.Let.ExpressionVariable.newVariable("id")
                        .forField("_id"))
                .pipeline(match(Criteria.expr(() ->
                        Document.parse("{ '$eq': ['$transactionRequestId', '$$id'] }")))
                        )
                .as("submissions");
        final var sortStage = sort(Sort.by("time").descending());
        final var skipStage = skip(pageNumber.longValue() * pageSize.longValue());
        final var limitStage = limit(pageSize);

        final TypedAggregation<TransactionRequestDocument> aggregation = newAggregation(
                TransactionRequestDocument.class,
                convertToTimeStage,
                matchStage,
                lookupStage,
                sortStage,
                skipStage,
                limitStage)
                .withOptions(AggregationOptions.builder()
                        .allowDiskUse(true)
                .build());

        return mongoTemplate.aggregate(aggregation, TransactionRequestDto.class)
                .getMappedResults();
    }

    public void submitTransactionRequest(@NotNull SubmitTransactionRequestDto submitTransactionRequestDto
    ) {
        final var verifiedTransactionDetails = paymentService.verifyTransactionByReference(submitTransactionRequestDto.getTransactionReference());

        final var transaction = transactionService.getTransactionById(submitTransactionRequestDto.getTransactionId())
                .orElseThrow();

        if (transaction.getAmount() != submitTransactionRequestDto.getAmountPaid()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        final var transactionRequest = transactionRequestRepository.save(TransactionRequestDocument.builder()
                        .transactionReference(submitTransactionRequestDto.getTransactionReference())
                        .transaction(transaction)
                        .creatorId(userEntity.getId())
                .build());

        transactionRequestSubmissionRepository.saveAll(submitTransactionRequestDto.getSubmissions()
                .stream()
                .map(item -> TransactionRequestSubmissionDocument.builder()
                        .value(item.getValue())
                        .label(item.getLabel())
                        .transactionRequirementId(item.getTransactionRequirementId())
                        .optionId(item.getOptionId())
                        .optionLabel(item.getLabel())
                        .transactionRequestId(transactionRequest.getId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());

        paymentService.addPaymentHistory(AddPaymentHistoryDto.builder()
                        .transactionId(verifiedTransactionDetails.getId())
                        .transactionReference(verifiedTransactionDetails.getReference())
                        .domain(verifiedTransactionDetails.getDomain())
                        .status(verifiedTransactionDetails.getStatus())
                        .receiptNumber(verifiedTransactionDetails.getReceiptNumber())
                        .amount(verifiedTransactionDetails.getAmount())
                        .channel(verifiedTransactionDetails.getChannel())
                    .target("Transaction & Entities")
                        .targetId(transactionRequest.getId())
                    .gatewayResponse(verifiedTransactionDetails)
                .build());
    }
}
