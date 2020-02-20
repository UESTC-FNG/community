package com.fng.dto;

import com.fng.exception.CustomizeException;
import com.fng.exception.ICustomizeErrorCode;
import lombok.Data;

import javax.xml.transform.Result;
import java.util.List;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

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
    public static <T> ResultDTO okOfT(T t){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("成功");
        resultDTO.setData(t);
        return resultDTO;
    }
}
