package com.paralex.erp.services;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.*;
import com.paralex.erp.entities.LawFirmEntity;
import com.paralex.erp.entities.LawFirmMemberEntity;
import com.paralex.erp.entities.LawFirmPracticeAreaEntity;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.LawFirmMemberRepository;
import com.paralex.erp.repositories.LawFirmPracticeAreaRepository;
import com.paralex.erp.repositories.LawFirmRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class LawFirmService {
    private final LawFirmRepository lawFirmRepository;
    private final LawFirmPracticeAreaRepository lawFirmPracticeAreaRepository;
    private final LawFirmMemberRepository lawFirmMemberRepository;
    private final EntityManager entityManager;
    private final UserEntity userEntity;

    public Long countAllOrBetweenTime(CountDto countDto) {
        final LocalDateTime startDate = countDto.getStartDate();
        final LocalDateTime endDate = countDto.getEndDate();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final Root<LawFirmEntity> root = criteriaQuery.from(LawFirmEntity.class);
        final List<Predicate> predicateList = new ArrayList<>();

        criteriaQuery.select(criteriaBuilder.count(root));

        if (startDate != null && endDate != null)
            predicateList.add(criteriaBuilder.between(root.get("time"), startDate, endDate));

        return entityManager
                .createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[0])))
                .getSingleResult();
    }

    // TODO also use authorization
    public void removeLawFirmMember(@NotNull @NotBlank String id) {
        isMemberOfLawFirm(userEntity.getId()).orElseThrow();

        lawFirmMemberRepository.deleteById(id);
    }

    public void addLawFirmMember(@NotNull @NotEmpty List<AddLawFirmMemberDto> addLawFirmMemberDtoList) {
        isMemberOfLawFirm(userEntity.getId()).orElseThrow();

        lawFirmMemberRepository.saveAll(addLawFirmMemberDtoList.stream()
                .map(item -> LawFirmMemberEntity.builder()
                        .userId(item.getUserId())
                        .lawFirmId(item.getLawFirmId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public void deleteLawFirmPracticeArea(@NotNull String id) {
        isMemberOfLawFirm(userEntity.getId()).orElseThrow();

        lawFirmPracticeAreaRepository.deleteById(id);
    }

    public void addLawFirmPracticeAreas(@NotNull @NotEmpty List<AddLawFirmPracticeAreaDto> addLawFirmPracticeAreaDtoList) {
        isMemberOfLawFirm(userEntity.getId()).orElseThrow();

        lawFirmPracticeAreaRepository.saveAll(addLawFirmPracticeAreaDtoList.stream()
                .map(item -> LawFirmPracticeAreaEntity.builder()
                        .lawPracticeAreaId(item.getLawPracticeAreaId())
                        .lawFirmId(item.getLawFirmId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    // TODO implement payments
    // TODO implement notifications
    // TODO add authorization
    public List<LawFirmEntity> findLawFirmsAroundLocation(@NotNull FindLawyersNearLocation findLawyersNearLocation) {
        return lawFirmRepository.searchLawFirms(
                findLawyersNearLocation.getLatitude(),
                findLawyersNearLocation.getLongitude(),
                findLawyersNearLocation.getPageSize(),
                findLawyersNearLocation.getPageNumber() * findLawyersNearLocation.getPageSize());
    }


    public void deleteLawFirm(@NotNull String id) {
        isMemberOfLawFirm(userEntity.getId()).orElseThrow();

        lawFirmRepository.deleteById(id);
    }

    public void updateLawFirm(
            @NotNull String id,
            @ValidateUpdateItem(
                    keyList = { "id", "time", "creatorId" },
                    modelClass = UpdateLawFirmDto.class)
            @NotNull @NotEmpty List<UpdateItemDto> changes) {
        isMemberOfLawFirm(userEntity.getId()).orElseThrow();

        final var criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<LawFirmEntity> update = criteriaBuilder.createCriteriaUpdate(LawFirmEntity.class);
        final Root<LawFirmEntity> root = update.from(LawFirmEntity.class);

        changes.forEach(change -> update.set(change.getKey(), change.getValue()));
        update.where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(update).executeUpdate();
    }

    public Optional<LawFirmEntity> getMyLawFirm() {
        return lawFirmRepository.findByCreatorId(userEntity.getId());
    }

    public List<LawFirmEntity> getLawFirms(@NotNull PaginatedRequestDto paginatedRequestDto) {
        return lawFirmRepository.findAll(PageRequest.of(paginatedRequestDto.getPageNumber(), paginatedRequestDto.getPageSize()))
                .getContent();
    }

    @Transactional
    public void createLawFirm(@NotNull CreateLawFirmDto createLawFirmDto) {
        final var lawFirm = lawFirmRepository.save(LawFirmEntity.builder()
                .name(createLawFirmDto.getName())
                .location(new Point(createLawFirmDto.getLatitude(), createLawFirmDto.getLongitude()))
                .status(true)
                .creatorId(userEntity.getId())
                .build());

        lawFirmPracticeAreaRepository.saveAll(createLawFirmDto.getPracticeAreas()
                .stream()
                .map(item -> LawFirmPracticeAreaEntity.builder()
                        .lawPracticeAreaId(item)
                        .lawFirmId(lawFirm.getId())
                        .creatorId(userEntity.getId())
                        .build())
                .toList());
    }

    public Optional<LawFirmMemberEntity> isMemberOfLawFirm(@NotNull @NotBlank String userId) {
        return lawFirmMemberRepository.findOne(Example.of(LawFirmMemberEntity.builder()
                        .userId(userId)
                .build(), ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                .withIgnorePaths("id", "time", "creatorId")));
    }
}
