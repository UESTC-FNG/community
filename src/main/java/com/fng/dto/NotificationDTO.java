package com.fng.dto;

import com.fng.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private User notifier;
    private String outerTitle;
    private String type;
}
