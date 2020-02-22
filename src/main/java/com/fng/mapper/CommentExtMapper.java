package com.fng.mapper;

import com.fng.model.Comment;

public interface CommentExtMapper {
    Long insertAndOutId(Comment comment);
}
