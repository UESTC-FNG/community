package com.fng.mapper;

import com.fng.model.User;

public interface NotificationExtMapper {
    int unreadCountById(User user);
}
