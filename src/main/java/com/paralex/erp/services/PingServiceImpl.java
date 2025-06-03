package com.paralex.erp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PingServiceImpl {
    @Value("${server.url}")
    private String serverUrl;
    @Scheduled(fixedRate = 250000)  // 5 minutes in milliseconds
    public void pingServer() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(serverUrl, String.class);
            System.out.println("Server pinged at " + System.currentTimeMillis());
        } catch (Exception e) {
            System.err.println("Error while pinging the server: " + e.getMessage());
        }
    }
}
