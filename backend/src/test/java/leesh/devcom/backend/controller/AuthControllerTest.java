package leesh.devcom.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import leesh.devcom.backend.common.GlobalProperties;
import leesh.devcom.backend.dto.LoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthController authController;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    GlobalProperties globalProperties;

    @BeforeEach
    void setUp(WebApplicationContext wac, RestDocumentationContextProvider restDocument) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(documentationConfiguration(restDocument)
                        .uris()
                            .withScheme("http")
                            .withHost(globalProperties.getServer().getAddress())
                            .withPort(globalProperties.getServer().getPort())
                        .and()
                        .operationPreprocessors()
                            .withRequestDefaults(prettyPrint())
                            .withResponseDefaults(prettyPrint()))
                .build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    public void loginTest_ok() throws Exception {

        // given
        LoginRequest data = LoginRequest.builder()
                .email("test1@gmail.com")
                .password("1111")
                .build();

        // when
        ResultActions result = mockMvc. perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(data)));

        // then
        result
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("expiry_sec").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(print());

        // create rest docs snippets
        result
                .andDo(document("login",
//                        getDocumentRequest(),
//                        getDocumentResponse(),
                        links(
                                halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("user email"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("user password")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("access_token").type(JsonFieldType.STRING).description("access token"),
                                fieldWithPath("expiry_sec").type(JsonFieldType.NUMBER).description("Expiration time of access token"),
                                fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("link to self"),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("link to profile")
                        )
                ));
    }

    @DisplayName("login test failed - invalid input value")
    @MethodSource(value = "params_loginTest_failed_01")
    @ParameterizedTest
    public void loginTest_failed_01(String email, String password) throws Exception {
        // given
        LoginRequest data = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(data)));

        // then
        result
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(res -> assertThat(Objects.requireNonNull(res.getResolvedException()).getClass()).isEqualTo(MethodArgumentNotValidException.class))
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].value").exists())
                .andExpect(jsonPath("errors[0].reason").exists())
                .andExpect(jsonPath("_links.index.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(print());

        // create rest docs snippets
        result
                .andDo(document("4000001",
//                        getDocumentRequest(),
//                        getDocumentResponse(),
                        links(
                                halLinks(),
                                linkWithRel("index").description("link to index"),
                                linkWithRel("profile").description("link to error document")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("status").optional().type(JsonFieldType.STRING).description("http status"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("error code"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("error message"),
                                fieldWithPath("errors[]").type(JsonFieldType.ARRAY).description("errors"),
                                fieldWithPath("errors[].field").type(JsonFieldType.STRING).description("error field"),
                                fieldWithPath("errors[].value").type(JsonFieldType.STRING).description("error input"),
                                fieldWithPath("errors[].reason").type(JsonFieldType.STRING).description("error reason"),
                                fieldWithPath("_links.index.href").type(JsonFieldType.STRING).description("index url"),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("document url")
                        )
                ));
    }

    static Stream<Arguments> params_loginTest_failed_01() {
        return Stream.of(
                Arguments.arguments("", ""),
                Arguments.arguments("test1@gmail.com", ""),
                Arguments.arguments(" ", " "),
                Arguments.arguments("aa", "1111")
            );
    }
}