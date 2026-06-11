package com.zenith.frontend.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {

    public static final String BASE_URL = "http://localhost:8080/api";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private ApiClient() {}

    public static AuthResponse register(String fullName, String email, String password) throws ApiException {
        Map<String, String> body = new HashMap<>();
        body.put("fullName", fullName);
        body.put("email", email);
        body.put("password", password);
        return post("/auth/register", body, AuthResponse.class, null);
    }

    public static AuthResponse login(String email, String password) throws ApiException {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        return post("/auth/login", body, AuthResponse.class, null);
    }

    public static void resetPassword(String email, String newPassword) throws ApiException {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("newPassword", newPassword);
        post("/auth/reset-password", body, JsonNode.class, null);
    }

    public static AssessmentResponse saveAssessment(int score, List<Integer> answers) throws ApiException {
        Map<String, Object> body = new HashMap<>();
        body.put("score", score);
        body.put("answers", answers);
        return post("/assessments", body, AssessmentResponse.class, SessionManager.getToken());
    }

    public static ProgressSummaryResponse getProgress() throws ApiException {
        return get("/assessments/progress", ProgressSummaryResponse.class, SessionManager.getToken());
    }

    private static <T> T get(String path, Class<T> responseType, String token) throws ApiException {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .timeout(Duration.ofSeconds(10))
                    .GET();

            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }

            HttpResponse<String> response = HTTP_CLIENT.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString());

            return parseResponse(response, responseType);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException("Tidak dapat terhubung ke server. Pastikan backend berjalan.", 0);
        }
    }

    private static <T> T post(String path, Object body, Class<T> responseType, String token) throws ApiException {
        try {
            String json = MAPPER.writeValueAsString(body);

            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json));

            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }

            HttpResponse<String> response = HTTP_CLIENT.send(
                    builder.build(),
                    HttpResponse.BodyHandlers.ofString());

            return parseResponse(response, responseType);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException("Tidak dapat terhubung ke server. Pastikan backend berjalan.", 0);
        }
    }

    private static <T> T parseResponse(HttpResponse<String> response, Class<T> responseType) throws ApiException {
        int status = response.statusCode();
        String body = response.body();

        if (status >= 200 && status < 300) {
            if (responseType == JsonNode.class) {
                try {
                    return responseType.cast(MAPPER.readTree(body));
                } catch (Exception ex) {
                    throw new ApiException("Respons server tidak valid.", status);
                }
            }
            if (body == null || body.isBlank()) {
                return null;
            }
            try {
                return MAPPER.readValue(body, responseType);
            } catch (Exception ex) {
                throw new ApiException("Respons server tidak valid.", status);
            }
        }

        String message = extractErrorMessage(body);
        throw new ApiException(message, status);
    }

    private static String extractErrorMessage(String body) {
        if (body == null || body.isBlank()) {
            return "Permintaan gagal.";
        }
        try {
            JsonNode node = MAPPER.readTree(body);
            if (node.has("message")) {
                return node.get("message").asText();
            }
        } catch (Exception ignored) {
        }
        return "Permintaan gagal.";
    }
}
