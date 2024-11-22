package com.paralex.erp.aspects;

import com.paralex.erp.annotations.ValidateUpdateItem;
import com.paralex.erp.dtos.UpdateItemDto;
import com.paralex.erp.dtos.ValidateUpdateItemBodyDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paralex.erp.exceptions.ForbiddenUpdateItemException;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@Log4j2
public class ValidateUpdateItemAspect {
    private final ObjectMapper objectMapper;

    public ValidateUpdateItemAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Before(value="execution(* *(.., @com.paralex.erp.annotations.ValidateUpdateItem (*), ..))")
    public void beforeAdvice(JoinPoint joinPoint) {
        var args = List.of(joinPoint.getArgs());
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final var parameterAnnotations = signature.getMethod().getParameterAnnotations();
        final ValidateUpdateItemBodyDto validateUpdateItemBodyDto = getListOfForbiddenKeys(args, parameterAnnotations);

        Objects.requireNonNull(validateUpdateItemBodyDto);

        final var modelClass = validateUpdateItemBodyDto.getModelClass();

        for (Object arg : args) {
            if (arg instanceof List<?> items) {
                final var isUpdateList = items.get(0) instanceof UpdateItemDto;

                if (isUpdateList) {
                    final var updateItems = (List<UpdateItemDto>) items;
                    final Map<String, Object> updateItemMap = new HashMap<>();

                    updateItems.forEach(item ->
                            updateItemMap.put(item.getKey(), item.getValue()));

                    final var payloadObject = objectMapper.convertValue(updateItemMap, modelClass);

                    log.info("payloadObject >> " + payloadObject);

                    final List<String> keysList = updateItems.stream()
                            .map(UpdateItemDto::getKey)
                            .toList();

                    for (String forbiddenKey : validateUpdateItemBodyDto.getKeysList()) {
                        if (keysList.contains(forbiddenKey)) {
                            throw new ForbiddenUpdateItemException("Forbidden update item key in payload");
                        }
                    }

                    ValidationUtils.invokeValidator(
                            Validator.forInstanceOf(
                                    modelClass,
                                    (form, errors) -> errors.rejectValue("403", "Invalid payload")),
                            payloadObject,
                            new SimpleErrors(modelClass));

                    break;
                }
            }
        }
    }

    private static ValidateUpdateItemBodyDto getListOfForbiddenKeys(List<Object> args, Annotation[][] parameterAnnotations) {
        for (int i = 0; i < args.size(); i++) {
            Annotation[] annotations = parameterAnnotations[i];

            for (Annotation annotation : annotations) {
                if (annotation instanceof ValidateUpdateItem validateUpdateItem) {
                    // Access annotation value here
                    // ... do something with the annotation

                    return ValidateUpdateItemBodyDto.builder()
                            .keysList(validateUpdateItem.keyList())
                            .modelClass(validateUpdateItem.modelClass())
                            .build();
                }
            }
        }

        return null;
    }
}
