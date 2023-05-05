package com.nufemit.service;

import com.nufemit.exception.AuthenticationException;
import com.nufemit.model.User;
import com.nufemit.model.dto.LoginDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.nufemit.utils.CredentialsUtils.createToken;
import static com.nufemit.utils.CredentialsUtils.encrypt;
import static java.lang.Boolean.TRUE;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;

    public List<User> getUsers(String searchBox) {
        if (searchBox == null || searchBox.isBlank()) {
            return userRepository.findTop25By();
        }
        return userRepository.findBySearchBox(searchBox, searchBox, searchBox, searchBox);
    }

    public User getUsersById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Boolean createUser(User user) {
        user.setPassword(encrypt(user.getPassword()));
        userRepository.save(user);
        return TRUE;
    }

    public ResponseDTO loginUser(LoginDTO loginDTO) {
        return userRepository.findByEmailAndPassword(loginDTO.getEmail(), encrypt(loginDTO.getPassword()))
                .map(user -> createToken(user.getId(), user.getEmail(), user.getPassword()))
                .map(token -> ResponseDTO.builder().token(token).build())
                .orElseThrow(AuthenticationException::new);
    }
}
