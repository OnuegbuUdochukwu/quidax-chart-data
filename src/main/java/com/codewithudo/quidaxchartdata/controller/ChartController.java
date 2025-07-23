package com.codewithudo.quidaxchartdata.controller;

import com.codewithudo.quidaxchartdata.dto.Trade;
import com.codewithudo.quidaxchartdata.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/charts")
public class ChartController {
    public ChartService chartService;

    @Autowired
    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/{market}/trades")
    public List<Trade> getTradesForMarket(@PathVariable String market) {
        return chartService.getRecentTrades(market);
    }
}
