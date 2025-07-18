package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.CreateTransactionItemDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.dtos.UpdateTransactionItemDto;
import com.paralex.erp.entities.TransactionItemEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.TransactionItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class TransactionItemService {
    private final TransactionItemRepository transactionItemRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public void createTransactionItems(@NotNull @NotEmpty List<CreateTransactionItemDto> createTransactionItemDtoList) {
        transactionItemRepository.saveAll(createTransactionItemDtoList.stream()
                .map(item -> TransactionItemEntity.builder()
                        .name(item.getName())
                        .status(Boolean.TRUE.equals(item.getStatus()))
                        .creatorId(userEntity.getEmail())
                        .build())
                .toList());
    }

    public void getTransactionItems(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize(), Sort.by("time")
                .descending());

        transactionItemRepository.findAll(pageable)
                .getContent();
    }

    @Transactional
    public void updateTransactionItem(
            @NotNull String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId" },
                    modelClass = UpdateTransactionItemDto.class) List<UpdateItemDto> changes) {
        final var criteriaBuilder = entityManager.getCriteriaBuilder();
        final var update = criteriaBuilder.createCriteriaUpdate(TransactionItemEntity.class);
        final var root = update.from(TransactionItemEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }

    public void deleteTransactionItem(@NotNull String id) {
        transactionItemRepository.deleteById(id);
    }
}
