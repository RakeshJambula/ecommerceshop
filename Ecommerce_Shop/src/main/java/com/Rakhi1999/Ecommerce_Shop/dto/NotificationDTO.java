package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;
}
