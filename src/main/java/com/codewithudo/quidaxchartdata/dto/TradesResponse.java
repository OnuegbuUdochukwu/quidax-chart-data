package com.codewithudo.quidaxchartdata.dto;

import lombok.Data;

import java.util.List;

@Data
public class TradesResponse {
    // This DTO assumes the response is a simple object containing the list of trades.
    // Based on other Quidax endpoints, it likely has "status" and "data" fields,
    // but we can start with this and adjust if needed.
    // For now, let's assume the API directly returns the list
    // under a key, for instance, "trades".
    // Let's refine based on the more likely structure.

    // A more realistic DTO based on other endpoints:
    private String status;
    private List<Trade> data;
}