package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.UserRequestDTO;
import com.uade.ecom.model.User;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User createUser(UserRequestDTO userRequestDTO);
}
