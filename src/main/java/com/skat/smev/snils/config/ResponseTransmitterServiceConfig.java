package com.skat.smev.snils.config;


import com.skat.smev.snils.transmitter.impl.ResponseTransmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ResponseTransmitterServiceConfig {

    @Value("${transmitter.service.url:}")
    private String url;

    @Autowired
    private RestTemplate restTmpl;

    @Bean(name = "responseTransmitterService")
    public ResponseTransmitterService getResponseTransmitterService() {
        String transmitterUrl = (url != null) ? url.trim() : "";
        return new ResponseTransmitterService(restTmpl, transmitterUrl);
    }
}
