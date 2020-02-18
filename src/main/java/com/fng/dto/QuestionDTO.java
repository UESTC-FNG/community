package com.fng.dto;

import com.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private Long gmtcreate;
    private Long gmtmodified;
    private Integer creator;
    private Integer commitcount;
    private Integer viewcount;
    private Integer likecount;
    private String tag;
    private User user;
}
