package com.huangpi.platform.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huangpi.platform.common.BusinessException;
import com.huangpi.platform.common.ErrorCode;
import com.huangpi.platform.config.AppProperties;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WechatCodeExchangeService {

    private final AppProperties properties;
    private final RestClient restClient;

    public WechatCodeExchangeService(AppProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = restClientBuilder.build();
    }

    public WechatIdentity exchange(String code) {
        if (properties.wechat().mockEnabled()) {
            String openid = "mock_" + UUID.nameUUIDFromBytes(code.getBytes(StandardCharsets.UTF_8));
            return new WechatIdentity(openid, null);
        }
        if (isBlank(properties.wechat().appId()) || isBlank(properties.wechat().appSecret())) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "微信登录配置缺失");
        }

        WechatSessionResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.weixin.qq.com")
                        .path("/sns/jscode2session")
                        .queryParam("appid", properties.wechat().appId())
                        .queryParam("secret", properties.wechat().appSecret())
                        .queryParam("js_code", code)
                        .queryParam("grant_type", "authorization_code")
                        .build())
                .retrieve()
                .body(WechatSessionResponse.class);

        if (response == null || isBlank(response.openid()) || response.errcode() != null) {
            String message = response == null ? "微信登录服务无响应" : "微信登录失败: " + response.errmsg();
            throw new BusinessException(ErrorCode.UNAUTHORIZED, message);
        }
        return new WechatIdentity(response.openid(), response.unionid());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record WechatIdentity(String openid, String unionid) {
    }

    private record WechatSessionResponse(
            String openid,
            String unionid,
            @JsonProperty("session_key") String sessionKey,
            Integer errcode,
            String errmsg) {
    }
}
