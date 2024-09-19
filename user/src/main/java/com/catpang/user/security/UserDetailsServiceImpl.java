package com.catpang.user.security;

import com.catpang.core.application.dto.UserDto;
import com.catpang.user.domain.model.User;
import com.catpang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증을 처리하고 UserDetails 객체를 반환하는 서비스 구현 클래스.
 * <p>
 * 이 클래스는 {@link UserDetailsService} 인터페이스를 구현하여 DB에서 회원 정보를 조회하고
 * {@link UserDetailsImpl} 객체를 반환하는 역할을 합니다.
 * 또한 사용자 정보를 Redis 캐시에 저장하여 빠른 조회를 가능하게 합니다.
 */
@Slf4j(topic = "UserDetails Service")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 닉네임을 기반으로 DB에서 사용자 정보를 조회한 후, {@link UserDetailsImpl} 객체를 반환합니다.
     *
     * @param nickname 사용자 닉네임
     * @return {@link UserDetailsImpl} 객체
     * @throws UsernameNotFoundException 사용자가 존재하지 않는 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with nickname: " + nickname));
        return new UserDetailsImpl(user);
    }

    /**
     * 사용자 ID를 기반으로 DB에서 사용자 정보를 조회한 후, {@link UserDetailsImpl} 객체를 반환합니다.
     * 조회된 사용자 정보는 Redis 캐시에 저장됩니다.
     *
     * @param id 사용자 ID
     * @return {@link UserDetailsImpl} 객체
     * @throws UsernameNotFoundException 사용자가 존재하지 않는 경우 예외 발생
     */
    @Cacheable(cacheNames = "userDetails", key = "args[0]")
    public UserDto.CachingInfo loadUserById(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserDto.CachingInfo.builder()
                .userId(String.valueOf(user.getId()))
                .nickname(user.getNickname())
                .role(String.valueOf(user.getRole()))
                .build();
    }
}
