package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.CreateDeliveryStageDto;
import com.paralex.erp.dtos.DeliveryStageDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.DeliveryStageEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.DeliveryStageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
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
public class DeliveryStageService {
    private final DeliveryStageRepository deliveryStageRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public void deleteDeliveryStage(@NotNull String id) {
        deliveryStageRepository.deleteById(id);
    }

    @Transactional
    public void updateDeliveryStage(
            @NotNull @NotEmpty @NotBlank String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId" },
                    modelClass = DeliveryStageDto.class) List<UpdateItemDto> changes) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<DeliveryStageEntity> update = criteriaBuilder.createCriteriaUpdate(DeliveryStageEntity.class);
        final Root<DeliveryStageEntity> root = update.from(DeliveryStageEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }

    public List<DeliveryStageEntity> getDeliveryStages(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize(), Sort.by("time")
                .descending());

        return deliveryStageRepository.findAll(pageable)
                .getContent();
    }

    public void createDeliveryStages(@NotNull List<CreateDeliveryStageDto> createDeliveryStageDtoList) {
        deliveryStageRepository.saveAll(createDeliveryStageDtoList.stream().map(createDeliveryStageDto -> DeliveryStageEntity.builder()
                        .name(createDeliveryStageDto.getName())
                        .status(createDeliveryStageDto.isStatus())
                        .initial(createDeliveryStageDto.isInitial())
                        .terminal(createDeliveryStageDto.isTerminal())
                        .forDriver(createDeliveryStageDto.isForDriver())
                        .forAdmin(createDeliveryStageDto.isForAdmin())
                        .shouldNotify(createDeliveryStageDto.isShouldNotify())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }
}
