package com.codewithudo.quidaxchartdata.service;

import com.codewithudo.quidaxchartdata.dto.Trade;
import com.codewithudo.quidaxchartdata.dto.TradesResponse;
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

        // 1. Tell RestTemplate to expect our new TradesResponse object
        TradesResponse response = restTemplate.getForObject(url, TradesResponse.class);

        // 2. Unwrap the list of trades from inside the response object
        if (response != null && "success".equals(response.getStatus())) {
            return response.getData();
        }

        // Return an empty list if the call fails or data is null
        return java.util.Collections.emptyList();
    }
}