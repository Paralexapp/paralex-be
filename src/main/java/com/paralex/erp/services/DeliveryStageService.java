package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.DeliveryStageDocument;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.DeliveryStageRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class DeliveryStageService {
    private final DeliveryStageRepository deliveryStageRepository;
    private final MongoTemplate mongoTemplate;
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
        final var query = Query.query(Criteria.where("id").is(id));
        final var update = new Update();

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));

        mongoTemplate.updateFirst(query, update, DeliveryStageDocument.class);
    }

    public List<DeliveryStageDocument> getDeliveryStageForDrivers(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final GetDeliveryStageDto getDeliveryStageDto = GetDeliveryStageDto.builder()
                .forDriver(true)
                .build();

        getDeliveryStageDto.setPageNumber(paginatedRequestDto.getPageNumber());
        getDeliveryStageDto.setPageSize(paginatedRequestDto.getPageSize());

        return getDeliveryStages(getDeliveryStageDto);
    }

    public List<DeliveryStageDocument> getDeliveryStages(@NotNull GetDeliveryStageDto getDeliveryStageDto) {
        final var pageable = PageRequest.of(getDeliveryStageDto.getPageNumber(), getDeliveryStageDto.getPageSize(), Sort.by("time")
                .descending());
        final var example = Example.of(DeliveryStageDocument.builder()
                .forAdmin(getDeliveryStageDto.isForAdmin())
                .forDriver(getDeliveryStageDto.isForDriver())
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time"));

        return deliveryStageRepository.findAll(example, pageable)
                .getContent();
    }

    public void createDeliveryStages(@NotNull List<CreateDeliveryStageDto> createDeliveryStageDtoList) {
        deliveryStageRepository.saveAll(createDeliveryStageDtoList.stream().map(createDeliveryStageDto -> DeliveryStageDocument.builder()
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

    public Optional<DeliveryStageDocument> findDeliveryStageAtInitial() {
        return deliveryStageRepository.findOne(Example.of(DeliveryStageDocument.builder()
                        .initial(true)
                .build(), ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time", "creatorId")));
    }

    public Optional<DeliveryStageDocument> findDeliveryStageById(@NotNull String id) {
        return deliveryStageRepository.findById(id);
    }
}
