package com.solbeg.wallet.service;

import com.solbeg.wallet.dto.UserDto;
import com.solbeg.wallet.repository.UserRepository;
import com.solbeg.wallet.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDto loadUserByUsername(String username) {

        return Optional
                .ofNullable(userRepository.findByUsername(username))
                .map(UserMapper.INSTANCE::convert)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
