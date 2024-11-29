//package com.paralex.erp.providers;
//
//import com.paralex.erp.dtos.MQTTCredentialsDto;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Data
//@Component
//public class MQTTCredentialsProvider {
//    @Value("${mqtt.uri}")
//    private String uri;
//
//    @Value("${mqtt.username}")
//    private String username;
//
//    @Value("${mqtt.password}")
//    private String password;
//
//    @Value("${mqtt.connectionTimeout}")
//    private int connectionTimeout;
//
//    @Value("${mqtt.keepAliveInterval}")
//    private int keepAliveInterval;
//
//    @Value("${mqtt.automaticReconnect}")
//    private boolean automaticReconnect;
//
//    @Value("${mqtt.cleanStart}")
//    private boolean cleanStart;
//
//    @Bean
//    public MQTTCredentialsDto mqttCredentials() {
//        return MQTTCredentialsDto.builder()
//                .uri(uri)
//                .username(username)
//                .password(password)
//                .connectionTimeout(connectionTimeout)
//                .keepAliveInterval(keepAliveInterval)
//                .automaticReconnect(automaticReconnect)
//                .cleanStart(cleanStart)
//                .build();
//    }
//
//    public MQTTCredentialsDto getCredentials() {
//        return MQTTCredentialsDto.builder()
//                .uri(uri)
//                .username(username)
//                .password(password)
//                .connectionTimeout(connectionTimeout)
//                .keepAliveInterval(keepAliveInterval)
//                .automaticReconnect(automaticReconnect)
//                .cleanStart(cleanStart)
//                .build();
//    }
//}
