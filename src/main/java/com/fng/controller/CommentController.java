package com.fng.controller;

import com.fng.dto.CommentCreateDTO;
import com.fng.dto.ResultDTO;
import com.fng.exception.CustomizeErrorCode;
import com.fng.model.Comment;
import com.fng.model.User;

import com.fng.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
}
