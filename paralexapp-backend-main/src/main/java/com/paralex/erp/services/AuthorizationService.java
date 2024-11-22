package com.paralex.erp.services;

import com.paralex.erp.documents.AuthorizationRecordDocument;
import com.paralex.erp.dtos.AddAuthorizationRecordDto;
import com.paralex.erp.dtos.DeleteAuthorizationRecordDto;
import com.paralex.erp.dtos.FindAuthorizationRecordDto;
import com.paralex.erp.dtos.GetEvaluationRecordDto;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.repositories.AuthorizationRepository;
import com.paralex.erp.dtos.AddAuthorizationRecordDto;
import com.paralex.erp.dtos.DeleteAuthorizationRecordDto;
import com.paralex.erp.dtos.FindAuthorizationRecordDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class AuthorizationService {
    public static final String AUTHORIZATION = "Authorization";
    public static final String DENY = "Deny";
    public static final String ALLOW = "Allow";
    public static final String CREATE = "Create";
    public static final String READ = "Read";
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final String STATUS = "status";
    private final AuthorizationRepository authorizationRepository;
    private final MongoTemplate mongoTemplate;
    private final UserEntity userEntity;

    public List<AuthorizationRecordDocument> getEvaluationRecords(List<GetEvaluationRecordDto> getEvaluationRecordDtoList) {
        var criteriaList = getEvaluationRecordDtoList.stream()
                .map(getEvaluationRecordDto -> {
                    final Criteria c = new Criteria();
                    final var m = new HashMap<String, Object>();

                    m.putIfAbsent("resource", getEvaluationRecordDto.getResource());
                    m.putIfAbsent("principal", getEvaluationRecordDto.getPrincipal());
                    m.putIfAbsent("action", getEvaluationRecordDto.getAction());
                    m.putIfAbsent(STATUS, getEvaluationRecordDto.getStatus());

                    for (var entry : m.entrySet()) {
                        Optional.ofNullable(entry.getValue())
                                .ifPresent(t -> c.and(entry.getKey()).is(entry.getValue()));
                    }

                    return c;
                })
                .toList();

        return mongoTemplate.find(
                Query.query(
                        new Criteria().orOperator(criteriaList
                                .toArray(new Criteria[0]))),
                AuthorizationRecordDocument.class);
    }

    public List<AuthorizationRecordDocument> getAuthorizationRecords(FindAuthorizationRecordDto findAuthorizationRecordDto) {
        final String resource = findAuthorizationRecordDto.getResource();
        final String status = findAuthorizationRecordDto.getStatus();
        final String principal = findAuthorizationRecordDto.getPrincipal();
        final Integer pageNumber = findAuthorizationRecordDto.getPageNumber();
        final Integer pageSize = findAuthorizationRecordDto.getPageSize();

        var authorizationRecord = AuthorizationRecordDocument.builder()
                .resource(resource)
                .status(status)
                .principal(principal)
                .build();

        final Example<AuthorizationRecordDocument> example = Example.of(
                authorizationRecord,
                ExampleMatcher.matchingAll()
                        .withIgnoreNullValues()
                        .withIgnorePaths("time", "id", "targets"));

        final Pageable pageable = PageRequest.of(
                pageNumber, pageSize,
                Sort.by("time").descending());

        return authorizationRepository.findAll(example, pageable)
                .getContent();
    }

    public void addAuthorizationRecord(List<AddAuthorizationRecordDto> authorizationRecordDtoList) {
        var user = userEntity;

        authorizationRepository.saveAll(authorizationRecordDtoList.stream()
                .map(addAuthorizationRecordDto -> AuthorizationRecordDocument.builder()
                        .resource(addAuthorizationRecordDto.getResource())
                        .status(addAuthorizationRecordDto.getStatus())
                        .action(addAuthorizationRecordDto.getAction())
                        .principal(addAuthorizationRecordDto.getPrincipal())
                        .targets(addAuthorizationRecordDto.getTargets())
                        .creatorId(user.getId())
                        .build())
                .toList());
    }

    public void denyPrincipalForAuthorizationRecord(String id) {
        var query = Query.query(Criteria.where("id").is(id));
        var update = Update.update(STATUS, DENY);

        log.info("update >> " + update);

        mongoTemplate.updateFirst(query, update, AuthorizationRecordDocument.class);
    }

    public void allowPrincipalForAuthorizationRecord(String id) {
        var query = Query.query(Criteria.where("id").is(id));
        var update = Update.update(STATUS, ALLOW);

        mongoTemplate.updateFirst(query, update, AuthorizationRecordDocument.class);
    }

    public void removeAuthorizationRecordBy(String id) {
        authorizationRepository.deleteById(id);
    }

    public void removeAuthorizationRecordBy(@NotNull DeleteAuthorizationRecordDto deleteAuthorizationRecordDto) {
        final var document = AuthorizationRecordDocument.builder()
                .resource(deleteAuthorizationRecordDto.getResource())
                .action(deleteAuthorizationRecordDto.getAction())
                .status(deleteAuthorizationRecordDto.getStatus())
                .principal(deleteAuthorizationRecordDto.getPrincipal())
                .build();
        final Example<AuthorizationRecordDocument> example = Example.of(document, ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "time", "targets", "creatorId"));

        final AuthorizationRecordDocument authorizationDocument = authorizationRepository.findOne(example)
                .orElseThrow();

        authorizationRepository.delete(authorizationDocument);
    }

    public void removeAuthorizationRecordBy(@NotNull List<DeleteAuthorizationRecordDto> deleteAuthorizationRecordDtoList) {
        final var criteriaList = deleteAuthorizationRecordDtoList.stream().map(deleteAuthorizationRecordDto -> Criteria.where("resource")
                        .is(deleteAuthorizationRecordDto.getResource())
                        .and("action")
                        .is(deleteAuthorizationRecordDto.getAction())
                        .and(STATUS)
                        .is(deleteAuthorizationRecordDto.getStatus())
                        .and("principal")
                        .is(deleteAuthorizationRecordDto.getPrincipal()))
                .toList();

        mongoTemplate.findAllAndRemove(
                Query.query(
                        new Criteria().andOperator(criteriaList
                                .toArray(new Criteria[0]))),
                AuthorizationRecordDocument.class);
    }
}
