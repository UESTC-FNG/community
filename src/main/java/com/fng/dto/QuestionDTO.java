package com.fng.dto;

import com.fng.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commitCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;
}
