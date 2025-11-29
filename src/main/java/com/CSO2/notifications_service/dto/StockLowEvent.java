package com.CSO2.notifications_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockLowEvent {
    private String productId;
    private Integer stockLevel;
}
