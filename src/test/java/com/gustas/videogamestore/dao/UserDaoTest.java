package com.gustas.videogamestore.dao;

import com.gustas.videogamestore.AbstractIntegrationTest;
import com.gustas.videogamestore.dao.user.UserDao;
import com.gustas.videogamestore.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.gustas.videogamestore.constants.TestConstants.INVALID_ID_NEGATIVE;
import static com.gustas.videogamestore.constants.TestConstants.VALID_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserDaoTest extends AbstractIntegrationTest {

    @Autowired
    UserDao userDao;

    @Test
    public void getValidUser() {
        User user = userDao.findById(VALID_ID);

        assertNotNull(user);
        assertEquals(VALID_ID.longValue(), user.getId().longValue());
    }

    @Test
    public void getInvalidUser() {
        User user = userDao.findById(INVALID_ID_NEGATIVE);

        assertNull(user);
    }
}
