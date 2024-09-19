package com.catpang.user.presentation;

import com.catpang.core.application.dto.UserDto;
import com.catpang.user.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Slf4j(topic = "User Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    /**
     * 현재 인증된 사용자의 정보를 조회합니다.
     *
     * @return {@link UserDto.Result} 현재 사용자의 정보
     */
    @GetMapping
    @Operation(summary = "현재 사용자 정보 조회", description = "현재 인증된 사용자의 정보를 조회합니다.")
    public UserDto.Result getUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUser(userId);
    }

    /**
     * 새로운 사용자를 생성합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.
     *
     * @param create 새로운 사용자 생성에 필요한 정보를 담은 {@link UserDto.Create} 객체
     * @return {@link UserDto.Result} 생성된 사용자 정보
     */
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    @PostMapping
    @Operation(summary = "새로운 사용자 생성", description = "새로운 사용자를 생성합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.")
    public UserDto.Result createUser(@RequestBody UserDto.Create create) {
        return userService.join(create);
    }

    /**
     * 지정된 사용자 ID에 해당하는 사용자의 정보를 업데이트합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.
     *
     * @param userId 업데이트할 사용자의 ID
     * @param update 업데이트할 정보를 담은 {@link UserDto.Update} 객체
     * @return {@link UserDto.Result} 업데이트된 사용자 정보
     */
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    @PatchMapping("/{userId}")
    @Operation(summary = "사용자 정보 업데이트", description = "지정된 사용자 ID에 해당하는 사용자의 정보를 업데이트합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.")
    public UserDto.Result updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserDto.Update update) {
        return userService.updateUser(userId, update);
    }

    /**
     * 지정된 사용자 ID에 해당하는 사용자를 삭제합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.
     *
     * @param userId 삭제할 사용자의 ID
     * @return {@link UserDto.Delete.Result} 삭제 결과 정보
     */
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    @DeleteMapping("/{userId}")
    @Operation(summary = "사용자 삭제", description = "지정된 사용자 ID에 해당하는 사용자를 삭제합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.")
    public UserDto.Delete.Result deleteUser(@PathVariable("userId") String userId) {
        return userService.deleteUser(userId);
    }

    /**
     * 지정된 사용자 ID에 해당하는 사용자의 정보를 조회합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return {@link UserDto.Result} 조회된 사용자 정보
     */
    @PreAuthorize("hasRole('MASTER_ADMIN')")
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 정보 조회", description = "지정된 사용자 ID에 해당하는 사용자의 정보를 조회합니다. 이 메서드는 'MASTER_ADMIN' 권한을 가진 사용자만 접근 가능합니다.")
    public UserDto.Result findUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }
}
