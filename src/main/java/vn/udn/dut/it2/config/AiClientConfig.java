package vn.udn.dut.it2.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiClientConfig {

    @org.springframework.context.annotation.Bean
    public RestClient aiRestClient() {
        return RestClient.builder().build();
    }
}
