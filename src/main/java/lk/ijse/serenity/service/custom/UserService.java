package lk.ijse.serenity.service.custom;

import lk.ijse.serenity.dto.UserDTO;

public interface UserService {
    UserDTO searchUser(String username);

    boolean saveUser(UserDTO userDTO);

    boolean updateUser(UserDTO userDTO);
}