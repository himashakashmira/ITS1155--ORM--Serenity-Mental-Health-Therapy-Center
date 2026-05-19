package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.UserDTO;

public interface AuthService {

    UserDTO login(String username, String plainPassword);

    boolean createUser(UserDTO userDTO);
}
