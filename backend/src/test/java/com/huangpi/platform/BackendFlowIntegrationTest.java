package com.huangpi.platform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BackendFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void completesAuthenticationProductReviewAndLogoutFlow() throws Exception {
        String merchantToken = accountLogin("merchant", "123456", "merchant");

        mockMvc.perform(get("/api/auth/session").header("Authorization", bearer(merchantToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.role").value("merchant"))
                .andExpect(jsonPath("$.data.user.merchantId").isNotEmpty());

        String categoryId = findCategoryId(merchantToken, "simiao_rice");
        String fileId = uploadImage(merchantToken);

        String title = "端到端测试丝苗米";
        String createBody = objectMapper.writeValueAsString(Map.of(
                "title", title,
                "categoryId", categoryId,
                "summary", "用于验证商家提交和政府审核闭环的丝苗米产品。",
                "content", "测试详情",
                "coverFileId", fileId,
                "imageFileIds", new String[]{fileId},
                "address", "黄陂镇测试产地",
                "contactPhone", "13800138000"));

        MvcResult createResult = mockMvc.perform(post("/api/merchant/products")
                        .header("Authorization", bearer(merchantToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.status").value("draft"))
                .andReturn();
        String productId = json(createResult).path("data").path("id").asText();

        mockMvc.perform(post("/api/merchant/products/{id}/submit", productId)
                        .header("Authorization", bearer(merchantToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        String adminToken = accountLogin("admin", "123456", "admin");
        String reviewId = findReviewId(adminToken, title);

        mockMvc.perform(post("/api/admin/reviews/{id}/approve", reviewId)
                        .header("Authorization", bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"资料完整，同意发布\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        String userToken = wechatLogin("integration-user-code");
        MvcResult productsResult = mockMvc.perform(get("/api/products")
                        .header("Authorization", bearer(userToken))
                        .param("keyword", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(1))
                .andReturn();
        assertThat(json(productsResult).path("data").path("list").get(0).path("title").asText()).isEqualTo(title);

        mockMvc.perform(get("/api/home").header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.featuredCategories.length()").value(5))
                .andExpect(jsonPath("$.data.recommendedMapPoints.length()").value(4))
                .andExpect(jsonPath("$.data.activities.length()").value(1));

        mockMvc.perform(get("/api/map/points").header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(4));

        mockMvc.perform(get("/api/routes").header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].mapPointIds.length()").value(4));

        MvcResult activitiesResult = mockMvc.perform(get("/api/activities")
                        .header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.list[0].status").value("signup_open"))
                .andReturn();
        String activityId = json(activitiesResult).path("data").path("list").get(0).path("id").asText();

        String signupBody = objectMapper.writeValueAsString(Map.of(
                "name", "测试用户",
                "phone", "13800138001",
                "peopleCount", 2,
                "remark", "端到端测试报名"));
        mockMvc.perform(post("/api/activities/{id}/signups", activityId)
                        .header("Authorization", bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.peopleCount").value(2));

        mockMvc.perform(post("/api/activities/{id}/signups", activityId)
                        .header("Authorization", bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(40900));

        mockMvc.perform(get("/api/me/signups").header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].activityId").value(activityId));

        String favoriteBody = objectMapper.writeValueAsString(Map.of("targetType", "product", "targetId", productId));
        MvcResult favoriteResult = mockMvc.perform(post("/api/favorites")
                        .header("Authorization", bearer(userToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(favoriteBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.targetType").value("product"))
                .andReturn();
        String favoriteId = json(favoriteResult).path("data").path("id").asText();

        mockMvc.perform(get("/api/me/favorites").header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].targetId").value(productId));

        mockMvc.perform(delete("/api/favorites/{id}", favoriteId).header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        mockMvc.perform(get("/api/me/favorites").header("Authorization", bearer(userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));

        mockMvc.perform(get("/api/merchant/dashboard").header("Authorization", bearer(adminToken)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(40300));

        mockMvc.perform(post("/api/auth/logout").header("Authorization", bearer(merchantToken)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/session").header("Authorization", bearer(merchantToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(40100));
    }

    private String accountLogin(String account, String password, String expectedRole) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("account", account, "password", password));
        MvcResult result = mockMvc.perform(post("/api/auth/account-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.user.role").value(expectedRole))
                .andReturn();
        return json(result).path("data").path("token").asText();
    }

    private String wechatLogin(String code) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("code", code));
        MvcResult result = mockMvc.perform(post("/api/auth/wechat-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.role").value("user"))
                .andReturn();
        return json(result).path("data").path("token").asText();
    }

    private String findCategoryId(String token, String code) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/product-categories").header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn();
        for (JsonNode category : json(result).path("data")) {
            if (code.equals(category.path("code").asText())) {
                return category.path("id").asText();
            }
        }
        throw new AssertionError("Category not found: " + code);
    }

    private String uploadImage(String token) throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "rice.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake-png-content".getBytes(StandardCharsets.UTF_8));
        MvcResult result = mockMvc.perform(multipart("/api/files")
                        .file(file)
                        .param("businessType", "merchant_product")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fileId").isNotEmpty())
                .andReturn();
        return json(result).path("data").path("fileId").asText();
    }

    private String findReviewId(String token, String title) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/admin/reviews")
                        .header("Authorization", bearer(token))
                        .param("status", "pending")
                        .param("pageSize", "50"))
                .andExpect(status().isOk())
                .andReturn();
        for (JsonNode review : json(result).path("data").path("list")) {
            if (title.equals(review.path("title").asText())) {
                return review.path("id").asText();
            }
        }
        throw new AssertionError("Review not found for: " + title);
    }

    private JsonNode json(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }
}
