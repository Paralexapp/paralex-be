package com.paralex.erp.aspects;

import com.paralex.erp.annotations.AuthorizationPolicy;
import com.paralex.erp.annotations.RequiredAuthorizationRecord;
import com.paralex.erp.documents.AuthorizationRecordDocument;
import com.paralex.erp.dtos.GetEvaluationRecordDto;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.services.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Aspect
@Component
@Log4j2
public class AuthorizationRecordAspect {
    private final HttpServletRequest httpServletRequest;
    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Before(value="@annotation(com.paralex.erp.annotations.AuthorizationPolicy)")
    public void beforeAdvice(JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final var declaredAnnotation = signature.getMethod().getAnnotation(AuthorizationPolicy.class);
        final var records = Arrays.stream(declaredAnnotation.records()).toList();
        final List<AuthorizationRecordDocument> evaluationRecords = new ArrayList<>();
        var user = (UserEntity) request.getAttribute("user");

        for (var requiredRecord : records) {
            evaluationRecords.addAll(fetchMatchingEvaluationRecords(requiredRecord, user.getId()));
        }

        log.info("evaluationRecords >> " + evaluationRecords);

        // INFO Implicit OR
        if (evaluationRecords.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var permittedForAll = evaluationRecords.stream()
                .anyMatch(evaluationRecord -> evaluationRecord.getTargets()
                        .contains("*"));

        if (permittedForAll) {
            log.info("permittedForAll >> " + true);
            return;
        }

        var isMatching = evaluationRecords.stream()
                .anyMatch(evaluationRecord -> {
                    try {
                        return evaluationRecord.getTargets()
                                .contains(getPrincipal(declaredAnnotation));
                    } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | IOException e) {
                        log.info(e);
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error occurred processing authorization", e);
                    }
                });

        log.info("[isMatching] " + isMatching);

        if (!isMatching)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

/*
    // INFO Use to store data in request
    // https://shzhangji.com/blog/2022/07/05/store-custom-data-in-spring-mvc-request-context/
*/
    public String getPrincipal(AuthorizationPolicy declaredAnnotation) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, NoSuchFieldException {
        var pathToTarget = declaredAnnotation.pathToTarget();
        // INFO use interceptor to store information in the request context
        var principalInBody = declaredAnnotation.targetInBody();

        String principal = null;

        if (principalInBody) {
            final var clazz = declaredAnnotation.bodyClass();
            final var parsedObjectBody = parseObjectBody(clazz);
            final var field = parsedObjectBody.getClass()
                    .getDeclaredField(pathToTarget);

            principal = (String) field.get(parsedObjectBody);
        }

        var principalInRequestParam = declaredAnnotation.targetInRequestParam();

        if (principalInRequestParam) {
            principal = getPrincipalFromRequestParam(pathToTarget);
        }

        var principalInPathVariable = declaredAnnotation.targetInPathVariable();

        if (principalInPathVariable) {
            principal = getPrincipalFromPathVariable(pathToTarget, declaredAnnotation.pathVariablePattern());
        }

        log.info("[principal] " + principal);

        return principal;
    }

    public String getPrincipalFromPathVariable(String path, String pattern) {
        log.info("path >> " + path + " pattern >> " + pattern + " uri >> " + httpServletRequest.getRequestURI());

        var ant = new AntPathMatcher();
        var pathVariableMap = ant.extractUriTemplateVariables(
                pattern, httpServletRequest.getRequestURI());

        log.info("map >> " + pathVariableMap);

        return pathVariableMap.get(path);
    }

    public String getPrincipalFromRequestParam(String path) {
        return httpServletRequest.getParameter(path);
    }

    public Object parseObjectBody(Class<?> clazz) throws IOException {
        return objectMapper.readValue(httpServletRequest.getReader(), clazz);
    }

    public List<AuthorizationRecordDocument> fetchMatchingEvaluationRecords(
            RequiredAuthorizationRecord requiredAuthorizationRecord, String principal) {
        return authorizationService.getEvaluationRecords(List.of(
                GetEvaluationRecordDto.builder()
                        .resource(requiredAuthorizationRecord.resource())
                        .status(requiredAuthorizationRecord.status())
                        .action(requiredAuthorizationRecord.action())
                        .principal(principal)
                        .build()));
    }
}

