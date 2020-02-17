package com.model;

import lombok.Data;

@Data
public class Question {
    private String title;
    private String description;
    private Long gmtcreate;
    private Long gmtmodified;
    private Integer creator;
    private Integer commitcount;
    private Integer viewcount;
    private Integer likecount;
    private String tag;


}
