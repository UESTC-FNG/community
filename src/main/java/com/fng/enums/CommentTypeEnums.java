package com.fng.enums;

public enum CommentTypeEnums {
    QUESTION(1),COMMENT(2);
    private int type;
    CommentTypeEnums(int type){
        this.type=type;
    }

    public static boolean isExit(Integer type) {
        for (CommentTypeEnums value : CommentTypeEnums.values()) {
            if (value.type == type){
                return true;
            }
        }
        return false;
    }

    public int getType(){
        return type;
    }
}
