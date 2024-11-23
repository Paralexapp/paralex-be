package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.TransactionRequirementEntity;
import com.paralex.erp.entities.TransactionRequirementOptionEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.TransactionRequirementOptionRepository;
import com.paralex.erp.repositories.TransactionRequirementRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class TransactionRequirementService {
    private final TransactionRequirementRepository transactionRequirementRepository;
    private final TransactionRequirementOptionRepository transactionRequirementOptionRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public List<TransactionRequirementOptionEntity> getTransactionRequirementOptions(@NotNull String transactionRequirementId) {
        final var example = Example.of(TransactionRequirementOptionEntity.builder()
                        .transactionRequirementId(transactionRequirementId)
                .build(), ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                .withIgnorePaths("id", "time", "status"));

        return transactionRequirementOptionRepository.findAll(example);
    }

    public void addRequirementOptions(@NotNull @NotEmpty List<AddTransactionRequirementOptionDto> addTransactionRequirementOptionDtoList) {
        transactionRequirementOptionRepository.saveAll(addTransactionRequirementOptionDtoList.stream()
                .map(item -> TransactionRequirementOptionEntity.builder()
                        .label(item.getLabel())
                        .status(item.getStatus())
                        .data(item.getData())
                        .transactionRequirementId(item.getTransactionRequirementId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public void updateTransactionRequirementOption(
            @NotNull String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId", "transactionRequirementId" },
                    modelClass = UpdateTransactionRequirementOptionDto.class) List<UpdateItemDto> changes) {
        final var criteriaBuilder = entityManager.getCriteriaBuilder();
        final var update = criteriaBuilder.createCriteriaUpdate(TransactionRequirementOptionEntity.class);
        final var root = update.from(TransactionRequirementOptionEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update)
                .executeUpdate();
    }

    public void deleteTransactionRequirementOption(@NotNull String id) {
       transactionRequirementOptionRepository.deleteById(id);
    }

    public void addTransactionRequirements(@NotNull List<AddTransactionRequirementDto> addTransactionRequirementDtoList) {
        final var requirements = transactionRequirementRepository.saveAll(addTransactionRequirementDtoList.stream()
                .map(item -> TransactionRequirementEntity.builder()
                        .label(item.getLabel())
                        .type(item.getType())
                        .errorMessage(item.getErrorMessage())
                        .description(item.getDescription())
                        .index(item.getIndex())
                        .step(item.getStep())
                        .required((Boolean.TRUE.equals(item.getRequired())))
                        .hasOption(Boolean.TRUE.equals(item.getHasOption()))
                        .multiple(Boolean.TRUE.equals(item.getMultiple()))
                        .transactionId(item.getTransactionId())
                        .status(Boolean.TRUE.equals(item.getStatus()))
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
        
        final List<TransactionRequirementOptionEntity> options = new ArrayList<>();

        for (var i = 0; i < addTransactionRequirementDtoList.size(); i++) {
            final var requirement = addTransactionRequirementDtoList.get(i);

            if (Boolean.TRUE.equals(requirement.getHasOption())){
                Objects.requireNonNull(requirement.getOptions());

                final int finalI = i;

                requirement.getOptions()
                        .forEach(item -> options.add(TransactionRequirementOptionEntity.builder()
                                .label(item.getLabel())
                                .status(item.getStatus())
                                .data(item.getData())
                                .transactionRequirementId(requirements.get(finalI).getId())
                                .creatorId(userEntity.getId())
                                .build()));
            }
        }

        transactionRequirementOptionRepository.saveAll(options);
    }

    public List<TransactionRequirementEntity> getTransactionRequirements(@NotNull FindTransactionRequirementDto findTransactionRequirementDto) {
        final var builder = TransactionRequirementEntity.builder();

        Optional.ofNullable(findTransactionRequirementDto.getStep())
                .ifPresent(item -> builder.step(findTransactionRequirementDto.getStep()));

        Optional.ofNullable(findTransactionRequirementDto.getTransactionId())
                .ifPresent(item -> builder.transactionId(findTransactionRequirementDto.getTransactionId()));

        final var entity = builder.build();
        final var example = Example.of(entity);

        return transactionRequirementRepository.findAll(example);
    }

    @Transactional
    public void updateTransactionRequirement
            (@NotNull String id,
             @ValidateUpdateItem(
                     keyList = { "id", "time", "creatorId" },
                     modelClass = UpdateTransactionRequirementDto.class) List<UpdateItemDto> changes) {
        final var criteriaBuilder = entityManager.getCriteriaBuilder();
        final var update = criteriaBuilder.createCriteriaUpdate(TransactionRequirementEntity.class);
        final var root = update.from(TransactionRequirementEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update)
                .executeUpdate();
    }

    public void deleteTransactionRequirement(@NotNull String id) {
        transactionRequirementRepository.deleteById(id);
    }
}
