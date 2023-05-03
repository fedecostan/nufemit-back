package com.nufemit.service;

import com.nufemit.model.User;
import com.nufemit.model.dto.LoginDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static com.nufemit.utils.CredentialsUtils.createToken;
import static com.nufemit.utils.CredentialsUtils.encrypt;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;

    public User createUser(User user) {
        user.setPassword(encrypt(user.getPassword()));
        return userRepository.save(user);
    }

    public ResponseDTO loginUser(LoginDTO loginDTO) {
        return userRepository.findByEmailAndPassword(loginDTO.getEmail(), encrypt(loginDTO.getPassword()))
                .map(user -> createToken(user.getEmail(), user.getPassword()))
                .map(token -> ResponseDTO.builder().token(token).build())
                .orElseThrow(EntityNotFoundException::new);
    }

}
