package com.trendyTracker.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendyTracker.domain.Template;
import com.trendyTracker.domain.User;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class AlarmRepositoryImplTest {
    @Autowired
    private EntityManager em;

    @Test
    void testCreateUser() {
        // arrage 
        User user = new User();
        user.addUser("wlstncjs1!@#");

        Template template = new Template();
        template.addTemplate( "hello", "nojam");
        user.setTemplates(List.of(template));
        em.persist(user);

        // act
        em.remove(user);
        em.flush();

        Template template2 = em.find(Template.class, template.getId());

        // assert
        Assertions.assertThat(template2).isNull();
    }

    @Test
    void testRegistTemplate() {

    }
}
