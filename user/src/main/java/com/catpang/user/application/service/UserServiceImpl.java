package com.catpang.user.application.service;

import com.catpang.core.application.dto.UserDto;
import com.catpang.core.domain.model.DeliveryType;
import com.catpang.core.domain.model.RoleEnum;
import com.catpang.user.domain.model.User;
import com.catpang.user.domain.repository.CompanyDeliveryManagerRepository;
import com.catpang.user.domain.repository.HubDeliveryManagerRepository;
import com.catpang.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapperService userMapperService;

    private final UserRepository userRepository;
    private final HubDeliveryManagerRepository hubDeliveryManagerRepository;
    private final CompanyDeliveryManagerRepository companyDeliveryManagerRepository;

    /**
     * 요청에 맞는 권한 및 배송 타입 부여 후 User테이블에 저장하는 메서드
     *
     * @param userCreate 회원가입 시 받는 dto
     * @return 반환 값 없음.
     */
    @Transactional
    @Override
    public UserDto.Result join(UserDto.Create userCreate) {
        RoleEnum role = RoleEnum.fromRoleCode(userCreate.roleCode());
        User user = userRepository.save(userMapperService.toUserEntity(userCreate, role));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            // 인증된 사용자의 경우
            user.setCreatedBy(authentication.getName());
        } else {
            // 인증되지 않은 사용자의 경우 (회원가입 시)
            user.setCreatedBy(user.getId().toString());
        }

        // TODO: 존재하는 허브인지 FeignClient로 요청
        if (user.getRole().equals(RoleEnum.DELIVERY)) {
            switch (DeliveryType.fromType(userCreate.deliveryType())) {
                case HUB_DELIVERY_MANAGER ->
                        hubDeliveryManagerRepository.save(userMapperService.toHubDeliveryManager(user));
                case COMPANY_DELIVERY_MANAGER ->
                        companyDeliveryManagerRepository.save(userMapperService.toCompanyDeliveryManager(user, userCreate.hubId()));
            }
        }
        return userMapperService.fromUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto.Result getUser(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow();
        return userMapperService.fromUser(user);
    }

    @Transactional
    @Override
    public UserDto.Result updateUser(String userId, UserDto.Update update) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow();
        user.setName(update.name() != null ? update.name() : user.getName());
        user.setEmail(update.email() != null ? update.email() : user.getEmail());
        user.setRole(update.role() != null ? RoleEnum.fromRoleCode(update.role()) : user.getRole());
        return userMapperService.fromUser(user);
    }

    @Transactional
    @Override
    public UserDto.Delete.Result deleteUser(String userId) {
        String deleterId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow();
        user.softDelete();
        return userMapperService.fromDeleter(userRepository.save(user), deleterId);
    }
}
