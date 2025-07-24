package com.codewithudo.quidaxchartdata.service;

import com.codewithudo.quidaxchartdata.dto.Trade;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChartService {

    private static final String QUIDAX_API_BASE_URL = "https://app.quidax.com/api/v1/markets/";
    private final RestTemplate restTemplate;

    public ChartService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Trade> getRecentTrades(String market) {
        String url = "https://app.quidax.com/api/v1/trades/" + market;

        // We assume the response is a direct list of trades.
        // If this fails, it means the response is wrapped, just like the others.
        ResponseEntity<List<Trade>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }
}