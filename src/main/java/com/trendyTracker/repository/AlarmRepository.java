package com.trendyTracker.repository;

import com.trendyTracker.domain.User;

public interface AlarmRepository {
    void createUser(User user);

    void registTemplate(User user ,String title, String content);
}
