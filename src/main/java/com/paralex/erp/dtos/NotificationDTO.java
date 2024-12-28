package com.paralex.erp.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private String title;
    private String message;

    public NotificationDTO(String title, String message) {
        this.title = title;
        this.message = message;
    }

}
