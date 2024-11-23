package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.CourtDivisionDto;
import com.paralex.erp.dtos.CreateCourtDivisionDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.CourtDivisionEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.CourtDivisionRepository;
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
public class CourtDivisionService {
    private final CourtDivisionRepository courtDivisionRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public void deleteCourtDivision(@NotNull String id) {
        courtDivisionRepository.deleteById(id);
    }

    @Transactional
    public void updateCourtDivision(
            @NotNull @NotEmpty @NotBlank String id,
             @ValidateUpdateItem(
                     keyList = { "id", "time", "creatorId" },
                     modelClass = CourtDivisionDto.class) List<UpdateItemDto> changes) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<CourtDivisionEntity> update = criteriaBuilder.createCriteriaUpdate(CourtDivisionEntity.class);
        final Root<CourtDivisionEntity> root = update.from(CourtDivisionEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }

    public List<CourtDivisionEntity> getCourtDivisions(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize(), Sort.by("time")
                .descending());

        return courtDivisionRepository.findAll(pageable)
                .getContent();
    }

    public void createCourtDivisions(@NotNull List<CreateCourtDivisionDto> createCourtDivisionDtoList) {
        courtDivisionRepository.saveAll(createCourtDivisionDtoList.stream()
                .map(createCourtDivisionDto -> CourtDivisionEntity.builder()
                        .name(createCourtDivisionDto.getName())
                        .status(Boolean.TRUE.equals(createCourtDivisionDto.getStatus()))
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }
}
