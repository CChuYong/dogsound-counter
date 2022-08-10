package kr.swmaestro.dogsoundcounter.infrastructure.restapi.entities;

import lombok.Value;

@Value
public class ApiResponse {
    private final Boolean succeed;
    private final String message;

    public static ApiResponse succeed(String message) {
        return new ApiResponse(true, message);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }
}
