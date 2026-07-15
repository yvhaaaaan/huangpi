package com.huangpi.platform.auth;

import com.huangpi.platform.auth.dto.SessionResponse;
import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.domain.AccountStatus;
import com.huangpi.platform.domain.RevokedTokenEntity;
import com.huangpi.platform.domain.UserEntity;
import com.huangpi.platform.domain.UserRole;
import com.huangpi.platform.repository.AccountUserRepository;
import com.huangpi.platform.repository.MerchantRepository;
import com.huangpi.platform.repository.RevokedTokenRepository;
import com.huangpi.platform.repository.UserRepository;
import com.huangpi.platform.security.JwtService;
import com.huangpi.platform.security.SessionPrincipal;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AccountUserRepository accountUserRepository;
    private final MerchantRepository merchantRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final WechatCodeExchangeService wechatCodeExchangeService;

    public AuthService(
            UserRepository userRepository,
            AccountUserRepository accountUserRepository,
            MerchantRepository merchantRepository,
            RevokedTokenRepository revokedTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            WechatCodeExchangeService wechatCodeExchangeService) {
        this.userRepository = userRepository;
        this.accountUserRepository = accountUserRepository;
        this.merchantRepository = merchantRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.wechatCodeExchangeService = wechatCodeExchangeService;
    }

    @Transactional
    public SessionResponse loginWithAccount(String username, String password) {
        var account = accountUserRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "账号或密码错误"));
        UserEntity user = account.getUser();
        if (!passwordEncoder.matches(password, account.getPasswordHash()) || user.getStatus() != AccountStatus.ENABLED) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "账号或密码错误");
        }
        if (user.getRole() != UserRole.MERCHANT && user.getRole() != UserRole.ADMIN) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "该账号不支持账号密码登录");
        }
        Long merchantId = resolveMerchantId(user);
        return issueSession(user, merchantId);
    }

    @Transactional
    public SessionResponse loginWithWechat(String code) {
        var identity = wechatCodeExchangeService.exchange(code);
        UserEntity user = userRepository.findByOpenid(identity.openid()).orElseGet(() -> {
            UserEntity created = new UserEntity();
            created.setOpenid(identity.openid());
            created.setUnionid(identity.unionid());
            created.setNickname("微信用户");
            created.setRole(UserRole.USER);
            created.setStatus(AccountStatus.ENABLED);
            return userRepository.save(created);
        });
        if (user.getStatus() != AccountStatus.ENABLED) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账号已被禁用");
        }
        return issueSession(user, null);
    }

    @Transactional(readOnly = true)
    public SessionResponse currentSession(SessionPrincipal principal, String token) {
        UserEntity user = userRepository.findById(principal.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        return toResponse(token, principal.expiresAt(), user, principal.merchantId());
    }

    @Transactional
    public void logout(SessionPrincipal principal) {
        if (revokedTokenRepository.existsByJti(principal.jti())) {
            return;
        }
        RevokedTokenEntity revokedToken = new RevokedTokenEntity();
        revokedToken.setJti(principal.jti());
        revokedToken.setExpiresAt(principal.expiresAt());
        revokedToken.setRevokedAt(Instant.now());
        revokedTokenRepository.save(revokedToken);
    }

    private SessionResponse issueSession(UserEntity user, Long merchantId) {
        JwtService.IssuedToken issuedToken = jwtService.issue(user, merchantId);
        return toResponse(issuedToken.token(), issuedToken.expiresAt(), user, merchantId);
    }

    private SessionResponse toResponse(String token, Instant expiresAt, UserEntity user, Long merchantId) {
        var userInfo = new SessionResponse.UserInfo(
                user.getId().toString(),
                user.getNickname(),
                user.getRole().name().toLowerCase(),
                merchantId == null ? null : merchantId.toString());
        return new SessionResponse(token, expiresAt.toEpochMilli(), userInfo);
    }

    private Long resolveMerchantId(UserEntity user) {
        if (user.getRole() != UserRole.MERCHANT) {
            return null;
        }
        return merchantRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN, "商家账号未绑定商家资料"))
                .getId();
    }
}
