package com.skillexchange.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ZoomService {

    @Value("${zoom.client-id}")
    private String clientId;

    @Value("${zoom.client-secret}")
    private String clientSecret;

    @Value("${zoom.account-id}")
    private String accountId;

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔐 Step 1: Get OAuth Access Token
    public String getAccessToken() {
        String url = "https://zoom.us/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret); // Zoom requires basic auth with client_id:client_secret
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=account_credentials&account_id=" + accountId;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("Failed to get Zoom access token");
        }
    }

    // 📅 Step 2: Create Meeting and return Join URL
    public String createMeeting(String topic, String startTimeIso, int durationMinutes) {
        String accessToken = getAccessToken();

        String url = "https://api.zoom.us/v2/users/me/meetings";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> meetingDetails = new HashMap<>();
        meetingDetails.put("topic", topic);
        meetingDetails.put("type", 2); // Scheduled Meeting
        meetingDetails.put("start_time", startTimeIso); // ISO 8601 format (UTC)
        meetingDetails.put("duration", durationMinutes); // in minutes
        meetingDetails.put("timezone", "Asia/Kolkata"); // Optional: your local time zone

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(meetingDetails, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("join_url");
        } else {
            throw new RuntimeException("Failed to create Zoom meeting: " + response.getStatusCode());
        }
    }
    public String createMeetingLink(String hostName, LocalDateTime startTime, int durationMinutes) {
    String isoUtc = startTime.atOffset(java.time.ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
    return createMeeting("Skill Exchange with " + hostName, isoUtc, durationMinutes);
}

}
