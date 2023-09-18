package com.trendyTracker.Appservice.repository;

import com.trendyTracker.Appservice.domain.User;

public interface UserRepository {
    void createUser(User user);

    void registTemplate(User user ,String title, String content);
}
