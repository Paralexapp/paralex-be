package com.paralex.erp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paralex.erp.dtos.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class PaymentGatewayService {
    public static final String DEFAULT_COUNTRY = "nigeria";
    public static final String BEARER = "Bearer";
    public static final String ERROR = "[Error] {}";
    public static final String RESPONSE_CODE_RESPONSE_BODY = "[responseCode] {} | [responseBody] {}";
    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;

    @Value("${payment-gateway.base-url}")
    private String baseUrl;
    @Value("${payment-gateway.secret-key}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public VerifyTransactionByReferenceResponseDto verifyTransactionByReference(@NotNull String transactionReference) {
        final var url = Objects.requireNonNull(HttpUrl.parse(baseUrl))
                .newBuilder()
                .addPathSegment("transaction")
                .addPathSegment("verify")
                .addPathSegment(transactionReference)
                .build();
        final Request request = new Request.Builder()
                .url(url.url())
                .get()
                .addHeader(HttpHeaders.AUTHORIZATION, BEARER + " " + secretKey)
                .build();

        final Call call = okHttpClient.newCall(request);

        try(final Response response = call.execute()) {
            final int responseCode = response.code();
            final ResponseBody responseBody = response.body();
            final var jsonNode = objectMapper.readTree(responseBody.byteStream());

            log.info(RESPONSE_CODE_RESPONSE_BODY, responseCode, jsonNode.toPrettyString());

            Objects.requireNonNull(responseBody);

            if (!response.isSuccessful() || unSuccessfulResponse(jsonNode)) {
                final String errorBody = jsonNode.toPrettyString();

                log.error(ERROR, errorBody);

                throw new ResponseStatusException(HttpStatusCode.valueOf(responseCode), errorBody);
            }

            return objectMapper.convertValue(jsonNode, new TypeReference<>() {});
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    public CreateCustomerResponseDto findOrCreateCustomer(@NotNull CreateCustomerDto createCustomerDto) throws JsonProcessingException {
        final var emailOrCode = createCustomerDto.getEmail();

        CreateCustomerResponseDto customer = null;

        try {
            customer = findCustomerBy(emailOrCode);
        } catch (Exception e) {
            log.info("find customer exception {}", e.getMessage());
        }

        return Objects.requireNonNullElse(customer, createCustomer(createCustomerDto));
    }

    public CreateCustomerResponseDto findCustomerBy(@NotNull String emailOrCode) {
        final var url = Objects.requireNonNull(HttpUrl.parse(baseUrl))
                .newBuilder()
                .addPathSegment("customer")
                .addPathSegment(emailOrCode)
                .build();
        final Request request = new Request.Builder()
                .url(url.url())
                .get()
                .addHeader(HttpHeaders.AUTHORIZATION, BEARER + " " + secretKey)
                .build();

        final Call call = okHttpClient.newCall(request);

        try(final Response response = call.execute()) {
            final int responseCode = response.code();
            final ResponseBody responseBody = response.body();
            final var jsonNode = objectMapper.readTree(responseBody.byteStream());

            log.info(RESPONSE_CODE_RESPONSE_BODY, responseCode, jsonNode.toPrettyString());

            Objects.requireNonNull(responseBody);

            if (!response.isSuccessful() || unSuccessfulResponse(jsonNode)) {
                final String errorBody = jsonNode.toPrettyString();

                log.error(ERROR, errorBody);

                throw new ResponseStatusException(HttpStatusCode.valueOf(responseCode), errorBody);
            }

            return objectMapper.convertValue(jsonNode, new TypeReference<>() {});
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    public SendPaymentLinkResponseDto sendPaymentLink(@NotNull SendPaymentLinkDto sendPaymentLinkDto) throws JsonProcessingException {
        final var url = Objects.requireNonNull(HttpUrl.parse(baseUrl))
                .newBuilder()
                .addPathSegment("paymentrequest")
                .build();
        final var body = RequestBody.create(
                objectMapper.writeValueAsBytes(sendPaymentLinkDto),
                MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        final Request request = new Request.Builder()
                .url(url.url())
                .post(body)
                .addHeader(HttpHeaders.AUTHORIZATION, BEARER + " " + secretKey)
                .build();

        final Call call = okHttpClient.newCall(request);

        try(final Response response = call.execute()) {
            final int responseCode = response.code();
            final ResponseBody responseBody = response.body();
            final var jsonNode = objectMapper.readTree(responseBody.byteStream());

            log.info(RESPONSE_CODE_RESPONSE_BODY, responseCode, jsonNode.toPrettyString());

            Objects.requireNonNull(responseBody);

            if (!response.isSuccessful() || unSuccessfulResponse(jsonNode)) {
                final String errorBody = jsonNode.toPrettyString();

                log.error(ERROR, errorBody);

                throw new ResponseStatusException(HttpStatusCode.valueOf(responseCode), errorBody);
            }

            return objectMapper.convertValue(jsonNode, new TypeReference<>() {});
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    public CreateCustomerResponseDto createCustomer(@NotNull CreateCustomerDto createCustomerDto) throws JsonProcessingException {
        final var url = Objects.requireNonNull(HttpUrl.parse(baseUrl))
                .newBuilder()
                .addPathSegments("customer")
                .build();
        final var body = RequestBody.create(objectMapper.writeValueAsBytes(createCustomerDto),
                MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));

        final var request = new Request.Builder()
                .url(url.url())
                .post(body)
                .addHeader(HttpHeaders.AUTHORIZATION, BEARER + " " + secretKey)
                .build();

        final Call call = okHttpClient.newCall(request);

        try(final Response response = call.execute()) {
            log.info(RESPONSE_CODE_RESPONSE_BODY, response.code(), objectMapper.readTree(response.body().byteStream()).toPrettyString());

            final int responseCode = response.code();
            final ResponseBody responseBody = response.body();

            Objects.requireNonNull(responseBody);

            final var jsonNode = objectMapper.readTree(responseBody.byteStream());

            if (!response.isSuccessful() || unSuccessfulResponse(jsonNode)) {
                final String errorBody = jsonNode.toPrettyString();

                log.error(ERROR, errorBody);

                throw new ResponseStatusException(HttpStatusCode.valueOf(responseCode), errorBody);
            }

            return objectMapper.convertValue(jsonNode.get("data"), new TypeReference<>() {});
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    public NameEnquiryResponseDto nameEnquiry(@NotNull NameEnquiryDto nameEnquiryDto) {
        final var urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl))
                .newBuilder()
                .addPathSegments("bank/resolve")
                .addQueryParameter("account_number", nameEnquiryDto.getAccountNumber())
                .addQueryParameter("bank_code", nameEnquiryDto.getBankCode());

        final Request request = new Request.Builder()
                .url(urlBuilder.build()
                        .url())
                .get()
                .addHeader(HttpHeaders.AUTHORIZATION, BEARER + " " + secretKey)
                .build();

        final Call call = okHttpClient.newCall(request);

        try(final Response response = call.execute()) {
            final int responseCode = response.code();
            final ResponseBody responseBody = response.body();
            final var jsonNode = objectMapper.readTree(responseBody.byteStream());

            log.info("[responseCode] {} | [responseBody] {}", responseCode, jsonNode.toPrettyString());

            Objects.requireNonNull(responseBody);

            if (!response.isSuccessful() || unSuccessfulResponse(jsonNode)) {
                final String errorBody = jsonNode.toPrettyString();

                log.error("[Error] {}", errorBody);

                throw new ResponseStatusException(HttpStatusCode.valueOf(responseCode), errorBody);
            }

            final PaymentGatewayNameEnquiryResponseDto nameEnquiryResponse = objectMapper.convertValue(jsonNode.get("data"), new TypeReference<>() {});

            return NameEnquiryResponseDto.builder()
                    .accountName(nameEnquiryResponse.getAccountName())
                    .accountNumber(nameEnquiryResponse.getAccountNumber())
                    .bankId(nameEnquiryResponse.getBankId())
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    public BankDto getBanks(@NotNull GetBanksDto getBanksDto) {
        final var country = Objects.requireNonNull(getBanksDto.getCountry(), DEFAULT_COUNTRY);
        final var pageSize = getBanksDto.getPageSize();
        final var next = getBanksDto.getNext();
        final var previous = getBanksDto.getPrevious();

        final var urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl))
                .newBuilder()
                .addPathSegments("bank")
                .addQueryParameter("country", country)
                .addQueryParameter("perPage", String.valueOf(pageSize))
                .addQueryParameter("use_cursor", String.valueOf(true));

        if (next != null)
            urlBuilder.addQueryParameter("next", next);

        if (previous != null)
            urlBuilder.addQueryParameter("previous", previous);

        final Request request = new Request.Builder()
                .url(urlBuilder.build()
                        .url())
                .get()
                .addHeader(HttpHeaders.AUTHORIZATION, BEARER + " " + secretKey)
                .build();

        final Call call = okHttpClient.newCall(request);

        try(final Response response = call.execute()) {
            final int responseCode = response.code();
            final ResponseBody responseBody = response.body();
            final var jsonNode = objectMapper.readTree(responseBody.byteStream());

            log.info("[responseCode] {} | [responseBody] {}", responseCode, jsonNode.toPrettyString());

            Objects.requireNonNull(responseBody);

            if (!response.isSuccessful() || unSuccessfulResponse(jsonNode)) {
                final String errorBody = jsonNode.toPrettyString();

                log.error("[Error] {}", errorBody);

                throw new ResponseStatusException(HttpStatusCode.valueOf(responseCode), errorBody);
            }

            final PaymentGatewayGetBankResponseDto getBanksResponse = objectMapper.convertValue(jsonNode, new TypeReference<>() {});

            return BankDto.builder()
                    .data(getBanksResponse.getData())
                    .meta(getBanksResponse.getMeta())
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    private static boolean unSuccessfulResponse(JsonNode jsonNode) {
        return !jsonNode.has("status") ||
                !Objects.equals(jsonNode.get("status").asBoolean(), true) ||
                !jsonNode.has("data");
    }
}
