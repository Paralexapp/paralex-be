package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.documents.DeliveryPackageCategoryDocument;
import com.paralex.erp.dtos.AddDeliveryPackageCategoryDto;
import com.paralex.erp.dtos.FindDeliveryPackageCategoryDto;
import com.paralex.erp.dtos.UpdateDeliveryPackageCategoryDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.DeliveryPackageCategoryRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class DeliveryPackageCategoryService {
    private final DeliveryPackageCategoryRepository deliveryPackageCategoryRepository;
    private final MongoTemplate mongoTemplate;
    private final UserEntity userEntity;

    public void addDeliveryPackageCategories(@NotNull @NotEmpty List<AddDeliveryPackageCategoryDto> addDeliveryPackageCategoryDtoList) {
        deliveryPackageCategoryRepository.saveAll(addDeliveryPackageCategoryDtoList.stream()
                .map(item -> DeliveryPackageCategoryDocument.builder()
                        .name(item.getName())
                        .description(item.getDescription())
                        .status(Objects.requireNonNullElse(item.getStatus(), false))
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public List<DeliveryPackageCategoryDocument> findDeliveryPackageCategories(@NotNull FindDeliveryPackageCategoryDto findDeliveryPackageCategoryDto) {
        final var pageable = PageRequest.of(findDeliveryPackageCategoryDto.getPageNumber(), findDeliveryPackageCategoryDto.getPageSize(), Sort.by("time")
                .descending());

        return deliveryPackageCategoryRepository.findAll(pageable)
                .getContent();
    }

    public void updateDeliveryPackageCategory(
            @NotNull String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId" },
                    modelClass = UpdateDeliveryPackageCategoryDto.class) List<UpdateItemDto> changes) {
        final var query = Query.query(Criteria.where("id").is(id));
        final var update = new Update();

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));

        mongoTemplate.updateFirst(query, update, DeliveryPackageCategoryDocument.class);
    }

    public void deleteDeliveryPackageCategory(@NotNull String id) {
        deliveryPackageCategoryRepository.deleteById(id);
    }
}
