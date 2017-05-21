package io.acari.asyncrest;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAsync
public class Config {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }

    @Bean
    public PoorMansExecutor poorMansExecutor(){
        return new PoorMansExecutor();
    }

    @Bean
    public Sampler sampler(){
        return s -> true;
    }
}
