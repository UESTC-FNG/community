package com.fng.controller;

import com.fng.dto.CommentCreateDTO;
import com.fng.dto.CommentDTO;
import com.fng.dto.ResultDTO;
import com.fng.enums.CommentTypeEnums;
import com.fng.exception.CustomizeErrorCode;
import com.fng.model.Comment;
import com.fng.model.User;

import com.fng.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.fng.exception.CustomizeErrorCode.NO_LOGIN;

@Controller
public class CommentController {

    @Autowired()
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(NO_LOGIN);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setCommentator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setLikeCount(0L);
        if (comment.getContent()==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        commentService.insert(comment);
        ResultDTO ok = ResultDTO.okOf();
        return ok;
    }


    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List> comment(@PathVariable(name="id")Long id){
        List<CommentDTO> commentDTOList = commentService.listByTargetId(id, CommentTypeEnums.COMMENT);
        return ResultDTO.okOfT(commentDTOList);
    }
}
