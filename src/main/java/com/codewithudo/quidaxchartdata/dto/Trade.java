package com.codewithudo.quidaxchartdata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Trade {

    private String id;
    private String price;
    private String volume;

    @JsonProperty("created_at")
    private String createdAt;
}