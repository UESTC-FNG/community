package com.fng.dto;

import com.fng.exception.CustomizeException;
import com.fng.exception.ICustomizeErrorCode;
import lombok.Data;

@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code,String message){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(ICustomizeErrorCode customizeErrorCode) {
        return errorOf(customizeErrorCode.getCode(),customizeErrorCode.getMessage());
    }

    public static ResultDTO okOf(){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("成功");
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e){
        return ResultDTO.errorOf(e.getCode(),e.getMessage());

    }
}
