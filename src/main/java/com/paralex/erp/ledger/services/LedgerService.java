package com.paralex.erp.ledger.services;

import com.paralex.erp.commons.utils.Helper;
import com.paralex.erp.configs.ApiKey;
import com.paralex.erp.configs.ApiKeyRepository;
import com.paralex.erp.dtos.FailedResponse;
import com.paralex.erp.dtos.OkResponse;
import com.paralex.erp.dtos.UnathuorizedAccess;
import com.paralex.erp.exceptions.BadRequest;
import com.paralex.erp.ledger.documents.Ledger;
import com.paralex.erp.ledger.documents.LedgerFilterDTO;
import com.paralex.erp.ledger.documents.PageableLedgerDTO;
import com.paralex.erp.ledger.repositories.LedgerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LedgerService<T> {
    private final LedgerRepository repo;
    private final Helper helper;
    private final ApiKeyRepository apiKeyRepository;
    public File exportToExcel(LedgerFilterDTO filterParams, HttpServletRequest req) throws IOException {

        if(req.getHeaders("apiKey") == null){
            throw new UnathuorizedAccess("ApiKey is required");
        }
        String apiKey = req.getHeader("apiKey");

        Optional<ApiKey> key = apiKeyRepository.findByTestKey(apiKey);
        if(key.isPresent()){
            throw new BadRequest("Use a Production Api Key");
        }
        Optional<ApiKey> prodKey = apiKeyRepository.findByProdKeyAndIsEnabled(apiKey, true);

        if(prodKey.isEmpty()){throw new UnathuorizedAccess("Unauthorized access");}

        if(filterParams.getType() != null){
            if (filterParams.getType().strip().length() == 0){filterParams.setType(null);}
        }
        LocalDateTime firstDate = LocalDateTime.of(1333,1,1,12,0,0);
        LocalDateTime lastDate = LocalDateTime.of(3333,1,1,12,0,0);

            List<Ledger> filteredResult = null;
            if(filterParams.getWalletId() == null){
                throw new BadRequest("Wallet id is required");
            }

            if(filterParams.getType() != null){
                if(filterParams.getType().equalsIgnoreCase("credit") && filterParams.getStartDate() != null && filterParams.getEndDate() != null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT", filterParams.getStartDate(),filterParams.getEndDate());
                    return helper.exportToExcel(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() != null && filterParams.getEndDate() != null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",filterParams.getStartDate(),filterParams.getEndDate());
                    return helper.exportToExcel(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getEndDate() == null  && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndType(filterParams.getWalletId(), "DEBIT");
                    return helper.exportToExcel(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getEndDate() == null  && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndType(filterParams.getWalletId(), "CREDIT");
                    return helper.exportToExcel(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getStartDate() == null && filterParams.getEndDate() !=null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT",firstDate, filterParams.getEndDate());
                    return helper.exportToExcel(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() == null && filterParams.getEndDate() !=null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",firstDate,filterParams.getEndDate());
                    return helper.exportToExcel(filteredResult);
                }


                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getStartDate() != null && filterParams.getEndDate() == null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT",filterParams.getStartDate(), lastDate);
                    return helper.exportToExcel(filteredResult);
                }


                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() != null && filterParams.getEndDate() == null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",filterParams.getStartDate(), lastDate);
                    return helper.exportToExcel(filteredResult);
                }
            }

            else{
                if( filterParams.getEndDate() != null && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndCreatedAtBetween(filterParams.getWalletId(),firstDate, filterParams.getEndDate());
                    return helper.exportToExcel(filteredResult);
                }

                if(filterParams.getEndDate() == null && filterParams.getStartDate() != null ){
                    filteredResult = repo.findAllByWalletIdAndCreatedAtBetween(filterParams.getWalletId(),filterParams.getStartDate(),lastDate);
                    return helper.exportToExcel(filteredResult);
                }
                else{
                    filteredResult = repo.findAllByWalletId(filterParams.getWalletId());
                    return helper.exportToExcel(filteredResult);
                }
            }
            return null;
        }
    public File exportToCsv(LedgerFilterDTO filterParams,HttpServletRequest req) throws IOException {
        if(req.getHeaders("apiKey") == null){
            throw new UnathuorizedAccess("ApiKey is required");
        }
        String apiKey = req.getHeader("apiKey");

        Optional<ApiKey> key = apiKeyRepository.findByTestKey(apiKey);
        if(key.isPresent()){
            throw new BadRequest("Use a Production Api Key");
        }
        Optional<ApiKey> prodKey = apiKeyRepository.findByProdKeyAndIsEnabled(apiKey, true);

        if(prodKey.isEmpty()){throw new UnathuorizedAccess("Unauthorized access");}


        if(filterParams.getType() != null){
            if (filterParams.getType().strip().length() == 0){filterParams.setType(null);}
        }
        LocalDateTime firstDate = LocalDateTime.of(1333,1,1,12,0,0);
        LocalDateTime lastDate = LocalDateTime.of(3333,1,1,12,0,0);
            List<Ledger> filteredResult = null;
            if(filterParams.getWalletId() == null){
                throw new BadRequest("Wallet id is required");
            }
            if(filterParams.getType() != null){
                if(filterParams.getType().equalsIgnoreCase("credit") && filterParams.getStartDate() != null && filterParams.getEndDate() != null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT", filterParams.getStartDate(),filterParams.getEndDate());
                    return helper.exportToCsv(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() != null && filterParams.getEndDate() != null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",filterParams.getStartDate(),filterParams.getEndDate());
                    return helper.exportToCsv(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getEndDate() == null  && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndType(filterParams.getWalletId(), "DEBIT");
                    return helper.exportToCsv(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getEndDate() == null  && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndType(filterParams.getWalletId(), "CREDIT");
                    return helper.exportToCsv(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getStartDate() == null && filterParams.getEndDate() !=null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT",firstDate, filterParams.getEndDate());
                    return helper.exportToCsv(filteredResult);
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() == null && filterParams.getEndDate() !=null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",firstDate,filterParams.getEndDate());
                    return helper.exportToCsv(filteredResult);
                }


                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getStartDate() != null && filterParams.getEndDate() == null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT",filterParams.getStartDate(), lastDate);
                    return helper.exportToCsv(filteredResult);
                }


                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() != null && filterParams.getEndDate() == null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",filterParams.getStartDate(), lastDate);
                    return helper.exportToCsv(filteredResult);
                }
            }

            else{
                if( filterParams.getEndDate() != null && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndCreatedAtBetween(filterParams.getWalletId(),firstDate, filterParams.getEndDate());
                    return helper.exportToCsv(filteredResult);
                }

                if( filterParams.getEndDate() == null && filterParams.getStartDate() != null ){
                    filteredResult = repo.findAllByWalletIdAndCreatedAtBetween(filterParams.getWalletId(),filterParams.getStartDate(), lastDate);
                    return helper.exportToCsv(filteredResult);
                }
                else{
                    filteredResult = repo.findAllByWalletId(filterParams.getWalletId());
                    return helper.exportToCsv(filteredResult);
                }
            }
            return null;

    }
    public T filterLeger(PageableLedgerDTO filterParams, int page, int size, int start, HttpServletRequest req){
        try{
            if(req.getHeaders("apiKey") == null){
                throw new UnathuorizedAccess("ApiKey is required");
            }
            String apiKey = req.getHeader("apiKey");

            Optional<ApiKey> key = apiKeyRepository.findByTestKey(apiKey);
            if(key.isPresent()){
                OkResponse okResponse = new OkResponse<>();
                okResponse.setStatusCode(HttpStatusCode.valueOf(200));
                okResponse.setDateTime(LocalDateTime.now().toString());
                okResponse.setResponse("Operation successfully recieved. Use a production key to execute operation");
                okResponse.setData(null);
                return (T) okResponse;
            }
            Optional<ApiKey> prodKey = apiKeyRepository.findByProdKeyAndIsEnabled(apiKey, true);

            if(prodKey.isEmpty()){throw new UnathuorizedAccess("Unauthorized access");}

            Pageable pageable =  PageRequest.of(page,size);

            if(filterParams.getType() != null){
                if (filterParams.getType().strip().length() == 0){filterParams.setType(null);}
            }
            LocalDateTime firstDate = LocalDateTime.of(1333,1,1,12,0,0);
            LocalDateTime lastDate = LocalDateTime.of(3333,1,1,12,0,0);

            OkResponse response = new OkResponse();
            response.setResponse("Ledgers fetched successfully");
            response.setStatusCode(HttpStatusCode.valueOf(200));
            response.setDateTime(LocalDateTime.now().toString());
            List<Ledger> filteredResult = null;
            if(filterParams.getWalletId() == null){
                throw new BadRequest("Wallet id is required");
            }
            if(filterParams.getType() != null){
                if(filterParams.getType().equalsIgnoreCase("credit") && filterParams.getStartDate() != null && filterParams.getEndDate() != null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT", filterParams.getStartDate(),filterParams.getEndDate());
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() != null && filterParams.getEndDate() != null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",filterParams.getStartDate(),filterParams.getEndDate());
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getEndDate() == null  && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndType(filterParams.getWalletId(), "DEBIT");
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }

                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getEndDate() == null  && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndType(filterParams.getWalletId(), "CREDIT");
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }

                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getStartDate() == null && filterParams.getEndDate() !=null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT",firstDate, filterParams.getEndDate());
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }

                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() == null && filterParams.getEndDate() !=null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",firstDate,filterParams.getEndDate());
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }


                if(filterParams.getType().equalsIgnoreCase("credit")  && filterParams.getStartDate() != null && filterParams.getEndDate() == null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "CREDIT",filterParams.getStartDate(), lastDate);
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }


                if(filterParams.getType().equalsIgnoreCase("debit")  && filterParams.getStartDate() != null && filterParams.getEndDate() == null){
                    filteredResult = repo.findAllByWalletIdAndTypeAndCreatedAtBetween(filterParams.getWalletId(), "DEBIT",filterParams.getStartDate(), lastDate);
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }
            }

            else{
                if( filterParams.getEndDate() != null && filterParams.getStartDate() == null ){
                    filteredResult = repo.findAllByWalletIdAndCreatedAtBetween(filterParams.getWalletId(),firstDate, filterParams.getEndDate());
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }

                if(filterParams.getEndDate() == null && filterParams.getStartDate() != null ){
                    filteredResult = repo.findAllByWalletIdAndCreatedAtBetween(filterParams.getWalletId(),filterParams.getStartDate(), lastDate);
                    response.setData(this.getPageable(pageable, start, filteredResult));
                    return (T) response;
                }
                else{
                filteredResult = repo.findAllByWalletId(filterParams.getWalletId());
                    response.setData(this.getPageable(pageable, start, filteredResult));
                return (T) response;
                }
            }
            return null ;
        }
        catch (Exception e){
            FailedResponse failedResponse = new FailedResponse();
            failedResponse.setDebugMessage(e.getMessage());
            failedResponse.setResponse("Could not fetch ledgers");
            failedResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            failedResponse.setDateTime(LocalDateTime.now().toString());
            return (T) failedResponse;
        }
    }
    public Page<Ledger> getPageable(Pageable pageable, int start, List<Ledger> requests) throws InvocationTargetException, IllegalAccessException {

        int end = Math.min((start + pageable.getPageSize()), requests.size());
        return new PageImpl<>(requests.subList(start,end),pageable,requests.size());

    }
}
