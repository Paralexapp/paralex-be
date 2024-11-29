package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.AddLawPracticeAreaDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.dtos.UpdateLawPracticeAreaDto;
import com.paralex.erp.entities.LawPracticeAreaEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.LawPracticeAreaRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class LawPracticeAreaService {
    private final LawPracticeAreaRepository lawPracticeAreaRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public void addLawPracticeAreas(@NotNull @NotEmpty List<AddLawPracticeAreaDto> addLawPracticeAreaDtoList) {
        lawPracticeAreaRepository.saveAll(addLawPracticeAreaDtoList.stream()
                .map(item -> LawPracticeAreaEntity.builder()
                        .name(item.getName())
                        .status(Boolean.TRUE.equals(item.getStatus()))
                        .description(item.getDescription())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public List<LawPracticeAreaEntity> getLawPracticeAreas(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize());

        return lawPracticeAreaRepository.findAll(pageable)
                .getContent();
    }

    public void updateLawPracticeArea(
            @NotNull @NotEmpty @NotBlank String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId" },
                    modelClass = UpdateLawPracticeAreaDto.class)
            @NotNull @NotEmpty List<UpdateItemDto> changes) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<LawPracticeAreaEntity> update = criteriaBuilder.createCriteriaUpdate(LawPracticeAreaEntity.class);
        final Root<LawPracticeAreaEntity> root = update.from(LawPracticeAreaEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }

    public void deleteLawPracticeArea(@NotNull String id) {
        lawPracticeAreaRepository.deleteById(id);
    }
}
