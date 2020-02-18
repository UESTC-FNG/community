package com.fng.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PageDTO {
    private List<QuestionDTO> questionDTOS;
    private Boolean showPre;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;
    private Integer page;
    private List<Integer> pages=new ArrayList<>();
    private Integer totalPage;


    public void setPagination(Integer totalCount, Integer page, Integer size) {
        //容错处理
        if (page<1){
            page=1;
        }
        //计算totalPage
        totalPage=0;
        if (totalCount%size==0){
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size+1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        this.page=page;
        //计算pageList
        //先将当前页码放入
        pages.add(page);
        for (int i=1;i<=3;i++){
            if ((page-i)>0){
                pages.add(page-i);
            }
            if ((page+i)<=totalPage){
                pages.add(page+i);
            }
        }
        Collections.sort(pages);


        //判断上一页按钮
        if (page==1){
            showPre=false;
        }else{
            showPre=true;
        }

        //判断下一页按钮
        if(page==totalPage){
            showNext=false;
        }else {
            showNext=true;
        }



        //判断是否展示首页
        if (pages.contains(1)){
            showFirstPage=false;
        }else{
            showFirstPage=true;
        }

        //判断是否展示最后一页
        if (pages.contains(totalPage)){
            showEndPage=false;
        }else{
            showEndPage=true;
        }

    }
}
