package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderTimelineDTO {
    private Long id;
    private String status;
    private String comment;
    private LocalDateTime timestamp;
}
