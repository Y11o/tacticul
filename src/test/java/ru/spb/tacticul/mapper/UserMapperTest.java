package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.spb.tacticul.dto.UserDTO;
import ru.spb.tacticul.model.User;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testUserToUserDTO() {
        User user = new User(1L, "testUser", "test@example.com", "password");

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.id());
        assertEquals(user.getLogin(), userDTO.login());
        assertEquals(user.getEmail(), userDTO.email());
    }

    @Test
    void testUserDTOToUser() {
        UserDTO userDTO = new UserDTO(1L, "testUser", "test@example.com");

        User user = userMapper.userDTOToUser(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.id(), user.getId());
        assertEquals(userDTO.login(), user.getLogin());
        assertEquals(userDTO.email(), user.getEmail());
    }
}
