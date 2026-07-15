package com.huangpi.platform.auth;

import com.huangpi.platform.auth.dto.AccountLoginRequest;
import com.huangpi.platform.auth.dto.SessionResponse;
import com.huangpi.platform.auth.dto.WechatLoginRequest;
import com.huangpi.platform.common.ApiResponse;
import com.huangpi.platform.security.SessionPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/account-login")
    public ApiResponse<SessionResponse> accountLogin(@Valid @RequestBody AccountLoginRequest request) {
        return ApiResponse.success(authService.loginWithAccount(request.account(), request.password()));
    }

    @PostMapping("/wechat-login")
    public ApiResponse<SessionResponse> wechatLogin(@Valid @RequestBody WechatLoginRequest request) {
        return ApiResponse.success(authService.loginWithWechat(request.code()));
    }

    @GetMapping("/session")
    public ApiResponse<SessionResponse> session(
            @AuthenticationPrincipal SessionPrincipal principal,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ApiResponse.success(authService.currentSession(principal, authorization.substring(7).trim()));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal SessionPrincipal principal) {
        authService.logout(principal);
        return ApiResponse.success();
    }
}
