package com.ssafy.s10p31s102be.admin.utils;

import com.ssafy.s10p31s102be.admin.infra.entity.SystemNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScheduleWebClient {
    private final WebClient webClient;

    public Mono<String> addNotification(SystemNotification systemNotification) {
        String url = "/notification";
        return webClient
                .post()
                .uri(url)
                .bodyValue(systemNotification)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, e -> {
                    // Log the detailed error response
                    System.err.println("Error response code: " + e.getStatusCode());
                    System.err.println("Error response body: " + e.getResponseBodyAsString());
                });
    }


    public Mono<String> putNotification( Integer id, Integer period ){
        String url = "/notification/" + id + "?period=" + period;
        return webClient
                .put()
                .uri(url)
                .body(null)
                .retrieve()

                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, e -> {
                    // Log the detailed error response
                    System.err.println("Error response code: " + e.getStatusCode());
                    System.err.println("Error response body: " + e.getResponseBodyAsString());
                });
    }

    public Mono<String> deleteNotification( Integer id ){
        String url = "/notification/" + id;
        return webClient
                .delete()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, e -> {
                    // Log the detailed error response
                    System.err.println("Error response code: " + e.getStatusCode());
                    System.err.println("Error response body: " + e.getResponseBodyAsString());
                });
    }
}
