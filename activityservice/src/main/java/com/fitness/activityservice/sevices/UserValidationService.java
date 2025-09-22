package com.fitness.activityservice.sevices;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public boolean validateUserId(String userId) {
        try{
           return Boolean.TRUE.equals(userServiceWebClient.get()
                   .uri("/api/users/{userId}/validate", userId)
                   .retrieve()
                   .bodyToMono(Boolean.class)
                   .block());
        }
        catch (WebClientResponseException e){
            if (e.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                throw new BadRequestException("Bad Request");
            }
            if (e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new NotFoundException("Not Found");
            }
        }
        return false;
    }
}
