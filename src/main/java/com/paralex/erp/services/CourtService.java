package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.AddCourtDto;
import com.paralex.erp.dtos.PaginatedRequestDto;
import com.paralex.erp.dtos.UpdateCourtDto;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.entities.CourtEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.CourtRepository;
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
public class CourtService {
    private final CourtRepository courtRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public void deleteCourt(@NotNull String id) {
        courtRepository.deleteById(id);
    }

    @Transactional
    public void updateCourt(
            @NotNull @NotEmpty @NotBlank String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId" },
                    modelClass = UpdateCourtDto.class) List<UpdateItemDto> changes) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<CourtEntity> update = criteriaBuilder.createCriteriaUpdate(CourtEntity.class);
        final Root<CourtEntity> root = update.from(CourtEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }

    public List<CourtEntity> getCourts(@NotNull PaginatedRequestDto paginatedRequestDto) {
        final var pageable = PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize(), Sort.by("time")
                .descending());

        return courtRepository.findAll(pageable)
                .getContent();
    }

    @Transactional
    public void addCourts(@NotNull @NotEmpty List<AddCourtDto> addCourtDtoList) {
        courtRepository.saveAll(addCourtDtoList.stream()
                .map(addCourtDto -> CourtEntity.builder()
                        .location(addCourtDto.getLocation())
                        .status(Boolean.TRUE.equals(addCourtDto.getStatus()))
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }
}
