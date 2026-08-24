package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.UserRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Address;
import com.uade.ecom.model.User;
import com.uade.ecom.repository.AddressRepository;
import com.uade.ecom.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun usuario con id " + id));
    }

    @Override
    public User createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setNombre(userRequestDTO.getNombre());
        user.setEmail(userRequestDTO.getEmail());
        user.setRol(userRequestDTO.getRol());

        if (userRequestDTO.getDireccionId() != null) {
            Address address = addressRepository.findById(userRequestDTO.getDireccionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontro ninguna direccion con id " + userRequestDTO.getDireccionId()));
            user.setAddress(address);
        }

        return userRepository.save(user);
    }
}
