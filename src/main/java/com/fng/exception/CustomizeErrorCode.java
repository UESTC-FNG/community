package com.fng.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"你的问题找不到了，换一个试试吧"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登陆，请登陆后重试"),
    SYSTEM_ERROR(2004,"服务器冒烟了，要不待会再试试"),
    COMMENT_TYPE_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你找的评论不存在，换一个试试吧"),
    COMMENT_IS_EMPTY(2007,"评论内容为空，请重新输入"),
    TAG_NOT_VALID(2008,"输入的tag不合法，请修改后提交"),
    FILE_UPLOAD_ERROR(2009,"图片上传失败");

     String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code,String message){
    this.code=code;
    this.message=message;
    }
}


