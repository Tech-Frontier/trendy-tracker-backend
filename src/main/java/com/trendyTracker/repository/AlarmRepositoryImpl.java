package com.trendyTracker.repository;

import org.springframework.stereotype.Repository;

import com.trendyTracker.domain.Template;
import com.trendyTracker.domain.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepository{
    private final EntityManager em;

    @Override
    public void createUser(User user) {
        em.persist(user);
    }

    @Override
    public void registTemplate(User user, String title, String content) {
        Template template = new Template();
        template.addTemplate( title, content);
        
        em.persist(template);
    }
    
}
