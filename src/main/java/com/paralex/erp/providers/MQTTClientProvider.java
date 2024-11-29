//package com.paralex.erp.providers;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.eclipse.paho.mqttv5.client.MqttClient;
//import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
//import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
//import org.eclipse.paho.mqttv5.common.MqttException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.annotation.Validated;
//
//import javax.annotation.PostConstruct;
//import java.io.File;
//
//@RequiredArgsConstructor
//@Validated
//@Component
//@Log4j2
//public class MQTTClientProvider {
//    public final MQTTCredentialsProvider mqttCredentialsProvider;
//    private MqttClient mqttClient;
//
//    @Value("${MQTT_CLIENT_ID}")
//    private String clientId;
//
//    // INFO https://kubernetes.io/docs/tasks/inject-data-application/environment-variable-expose-pod-information/
//    @PostConstruct
//    public void setClientId() {
//        clientId = getClientId();
//    }
//
//    @Bean(destroyMethod = "disconnect")
//    public MqttClient mqttClient() throws MqttException {
//        if (mqttClient == null)
//            mqttClient = connect();
//
//        return mqttClient;
//    }
//
//    public MqttClient connect() throws MqttException {
//        final MqttConnectionOptions options = getConnectionProperties();
//        final MqttClient client = getClient(options.getServerURIs(), clientId);
//
//        client.connect(options);
//
//        return client;
//    }
//
//    public MqttClient getClient(
//            @NotNull @NotEmpty @NotBlank String[] serverURI,
//            @NotNull @NotEmpty @NotBlank String clientId) throws MqttException {
//        final var persistencePath = System.getProperty("user.home") + "/mqtt_persistence";
//        final var file = new File(persistencePath);
//
//        assert file.exists() || file.mkdir();
//
//        return new MqttClient(serverURI[0], clientId, new MqttDefaultFilePersistence(persistencePath));
//    }
//
//    public MqttConnectionOptions getConnectionProperties() {
//        return makeConnectionProperties();
//    }
//
//    private MqttConnectionOptions makeConnectionProperties() {
//        log.info("[mqttCredentials] {} at {}", mqttCredentialsProvider, this.getClass());
//        final String uri = mqttCredentialsProvider.getUri();
//        final String username = mqttCredentialsProvider.getUsername();
//        final String password = mqttCredentialsProvider.getPassword();
//        final int connectionTimeout = mqttCredentialsProvider.getConnectionTimeout();
//        final int keepAliveInterval = mqttCredentialsProvider.getKeepAliveInterval();
//        final boolean automaticReconnect = mqttCredentialsProvider.isAutomaticReconnect();
//        final boolean cleanStart = mqttCredentialsProvider.isCleanStart();
//
//        final MqttConnectionOptions options = new MqttConnectionOptions();
//
//        options.setServerURIs(new String[]{uri});
//        options.setPassword(password.getBytes());
//        options.setUserName(username);
//        options.setAutomaticReconnect(automaticReconnect);
//        options.setAutomaticReconnectDelay(10, 30);
//        options.setConnectionTimeout(connectionTimeout);
//        options.setKeepAliveInterval(keepAliveInterval);
//        options.setCleanStart(cleanStart);
//
//        return options;
//    }
//
//    private String getClientId() {
//        return clientId;
//    }
//}
