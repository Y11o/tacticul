package ru.spb.tacticul.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.spb.tacticul.dto.UserDTO;
import ru.spb.tacticul.model.User;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToUserDTO() {
        User user = User.builder()
                .id(1L)
                .login("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.id());
        assertEquals(user.getLogin(), userDTO.login());
        assertEquals(user.getEmail(), userDTO.email());
    }

    @Test
    void shouldMapUserDTOToUser() {
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .login("testUser")
                .email("test@example.com")
                .build();

        User user = userMapper.userDTOToUser(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.id(), user.getId());
        assertEquals(userDTO.login(), user.getLogin());
        assertEquals(userDTO.email(), user.getEmail());
    }
}