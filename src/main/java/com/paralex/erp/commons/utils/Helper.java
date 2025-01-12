package com.paralex.erp.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVWriter;
import com.paralex.erp.configs.JwtService;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.exceptions.NotFoundException;
import com.paralex.erp.ledger.documents.Ledger;
import com.paralex.erp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class Helper<T> {
    @Value("${payment-gateway.secret-key}")
    private  String payStackKey;


    private final JwtService jwtService;
    private final UserRepository userRepository;

    public final PasswordEncoder encoder = new BCryptPasswordEncoder();
    public UserEntity extractLoggedInCustomer(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        return userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));
    }
    public String encodePassword(String password) {

        return encoder.encode(password);
    }
    public boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

        return email != null && pattern.matcher(email).matches();
    }
    public boolean isPasswordCorrect(String password, String encodedPassword){
        return encoder.matches(password, encodedPassword);
    }
    public boolean isPasswordValidInLengthAndInFormat(String password) {
        String PASSWORD_PATTERN ="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$";
        return password.matches(PASSWORD_PATTERN);
    }
    public boolean isPhoneNumberValid(String phoneNumber){
        String VALID_PHONE_NUMBER = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
        return phoneNumber.matches(VALID_PHONE_NUMBER);
    }
    public String writeAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
    public <R> R mapToClass(Object js, Class<R> type) throws ParseException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(this.writeAsString(js), type); // Safe
    }

    public  JSONObject parseJson(String js) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(js);
    }

    public JSONObject makeRequestWithRedirect(String req, String url) throws IOException {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + payStackKey);

        // Create a new instance of HttpClient for handling redirects
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        final HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new DefaultRedirectStrategy())
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        // Create an HttpEntity with headers only if the request is a POST
        HttpEntity<String> httpEntity = null;
        if (req != null) {
            httpEntity = new HttpEntity<>(req, headers);
        } else {
            httpEntity = new HttpEntity<>(headers);
        }

        // Make the request based on the presence of request body
        ResponseEntity<JSONObject> response;
        if (req != null) {
            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
        } else {
            response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        }

        return response.getBody();
    }

    public boolean isPasswordStrong(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        if (password != null && pattern.matcher(password).matches()) {
            return true;
        }
        throw new IllegalArgumentException("Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character and must be at least 8 characters long.");

    }

    public File exportToExcel(List<Ledger> list) throws IOException {

        File file = new File("ledger_report.xlsx");
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Report");
            Row row;
            int count = 0;
            row = sheet.createRow(count++);
            Cell header = row.createCell(0);
            header.setCellValue("Transaction Type");

            Cell header1 = row.createCell(1);
            header1.setCellValue("Previous Balance");

            Cell header2= row.createCell(2);
            header2.setCellValue("New Balance");

            Cell header3= row.createCell(3);
            header3.setCellValue("Description");

            Cell header4= row.createCell(4);
            header4.setCellValue("Transaction Reference");

            Cell header5= row.createCell(5);
            header5.setCellValue("Transaction Status");

            Cell header6= row.createCell(6);
            header6.setCellValue("Transaction Date");

            Cell header7= row.createCell(7);
            header6.setCellValue("Amount");

            for(Ledger each:list){
                row = sheet.createRow(count++);
                int cellId = 0;

                Cell type = row.createCell(cellId++);
                type.setCellValue(each.getType());

                Cell previousBalance =  row.createCell(cellId++);
                previousBalance.setCellValue(each.getPreviousBalance().toString());

                Cell newBalance =  row.createCell(cellId++);
                newBalance.setCellValue(each.getNewBalance().toString());

                Cell description =  row.createCell(cellId++);
                description.setCellValue(each.getDescription());

                Cell reference =  row.createCell(cellId++);
                reference.setCellValue(each.getTransactionRef());

                Cell status =  row.createCell(cellId++);
                status.setCellValue(each.getStatus());

                Cell date =  row.createCell(cellId++);
                date.setCellValue(each.getCreatedAt().toString());

                Cell total =  row.createCell(cellId++);
                total.setCellValue(each.getAmount().toString());
            }
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();

            return file;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public File  exportToCsv(List<Ledger> list) throws IOException {

        File file = new File("ledger_report.csv");
        FileWriter stringWriter = new  FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        String header = "Transaction Type,Previous Balance,New Balance,Description,Transaction Reference,Transaction Status,Transaction Date,Amount";
        csvWriter.writeNext(header.split(","));

        for(Ledger each:list){

            String next = each.getType() +","+each.getPreviousBalance().toString() +","+ each.getNewBalance().toString() +","+each.getDescription() +","+each.getTransactionRef() +","+each.getStatus() +","+each.getCreatedAt().toString()+","+each.getAmount();

            csvWriter.writeNext(next.split(","));
        }
        csvWriter.close();
        return file;
    }


}

