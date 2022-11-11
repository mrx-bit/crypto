package kz.crypto.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AbstractMockMvcController.AbstractMockMvcControllerConfig.class
)
@AutoConfigureMockMvc
public abstract class AbstractMockMvcController {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("crypto-local-db")
            .withUsername("admin")
            .withPassword("admin");
    private final String mapping;
    protected String token = "token";
    @LocalServerPort
    protected int port;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;

    protected AbstractMockMvcController(final String mapping) {
        this.mapping = mapping;
    }

    @DynamicPropertySource
    static void postgresqlProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @BeforeAll
    public static void setupMain() {
        postgreSQLContainer.start();
    }


    @BeforeEach
    @AfterEach
    protected void cleanup() {
    }

    @SneakyThrows
    protected ResultActions perform(final RequestBuilder requestBuilder) {
        return this.mockMvc.perform(requestBuilder);
    }

    protected MockHttpServletRequestBuilder postRequestWithBearer(final String path, final Object content, final String token) {
        return MvcUtils.postRequestWithBearer(path, content, token, this.mapper);
    }

    protected MockHttpServletRequestBuilder postRequestWithBearer(final String path, final Object content) {
        return MvcUtils.postRequestWithBearer(path, content, this.token, this.mapper);
    }

    protected MockHttpServletRequestBuilder postRequestNoBearer(final String path, final Object content) {
        return MvcUtils.postRequestNoBearer(path, content, this.mapper);
    }

    protected MockHttpServletRequestBuilder postRequestNoBearerNoContent(final String path) {
        return MvcUtils.postRequestNoBearerNoContent(path);
    }

    protected MockHttpServletRequestBuilder postRequestWithBearerNoContent(final String path) {
        return MvcUtils.postRequestWithBearer(path, "", this.token, this.mapper);
    }

    protected MockHttpServletRequestBuilder getRequestWithBearerAndParams(final String path, final String token, Map<String, String> params) {
        return MvcUtils.getRequestWithBearerAndParams(path, token, params);
    }

    protected MockHttpServletRequestBuilder getRequestWithBearer(final String path, final String token) {
        return MvcUtils.getRequestWithBearer(path, token);
    }

    protected MockHttpServletRequestBuilder getRequestWithBearer(final String path) {
        return MvcUtils.getRequestWithBearer(path, this.token);
    }

    protected MockHttpServletRequestBuilder getRequestNoBearer(final String path) {
        return MvcUtils.getRequestNoBearer(path);
    }

    protected MockHttpServletRequestBuilder getRequestNoBearer(final String path, final Object content) {
        return MvcUtils.getRequestNoBearer(path, content, this.mapper);
    }

    protected MockHttpServletRequestBuilder putRequestWithBearer(final String path, final Object content) {
        return MvcUtils.putRequestWithBearer(path, content, this.token, this.mapper);
    }

    protected MockHttpServletRequestBuilder putRequestNoContentWithBearer(final String path) {
        return MvcUtils.putRequestNoContentWithBearer(path, this.token);
    }

    protected MockHttpServletRequestBuilder putRequestNoBearer(final String path, final Object content) {
        return MvcUtils.putRequestNoBearer(path, content, this.mapper);
    }

    protected MockHttpServletRequestBuilder patchRequestWithBearer(final String path, final Object content) {
        return MvcUtils.patchRequestWithBearer(path, content, this.token, this.mapper);
    }

    protected MockHttpServletRequestBuilder patchRequestNoBearer(final String path, final Object content) {
        return MvcUtils.patchRequestNoBearer(path, content, this.mapper);
    }

    protected MockHttpServletRequestBuilder patchRequestNoContentWithBearer(final String path) {
        return MvcUtils.patchRequestNoContentWithBearer(path, this.token);
    }

    protected MockHttpServletRequestBuilder deleteRequestWithBearer(final String path, final String token) {
        return MvcUtils.deleteRequestWithBearer(path, token);
    }

    protected MockHttpServletRequestBuilder deleteRequestWithBearer(final String path) {
        return this.deleteRequestWithBearer(path, this.token);
    }

    protected MockHttpServletRequestBuilder deleteRequestNoBearer(final String path) {
        return MvcUtils.deleteRequestNoBearer(path);
    }

    protected MockHttpServletRequestBuilder deleteRequestNoBearer(final String path, final Object content) {
        return MvcUtils.deleteRequestNoBearer(path, content, this.mapper);
    }

    protected ResultActions expectOk(final MockHttpServletRequestBuilder requestBuilder) {
        return MvcUtils.expectOk(requestBuilder, this.mockMvc);
    }

    protected String asJson(final Object object) {
        return this.asJson(object, this.mapper);
    }

    @SneakyThrows
    protected String asJson(final Object object, final ObjectMapper objectMapper) {
        return objectMapper.writeValueAsString(object);
    }

    protected String getUrl() {
        return "http://localhost:" + this.port + this.mapping;
    }

    protected String buildUrl(final Object... appendixes) {
        if (appendixes.length == 1) {
            return this.getUrl() + "/" + appendixes[0];
        }

        final Stream<String> url = Stream.of(this.getUrl());
        final Stream<String> appendixesStream = Stream.of(appendixes).map(Object::toString);
        return Stream.concat(url, appendixesStream).collect(Collectors.joining("/"));
    }

    protected <T> T getResponse(final MockHttpServletRequestBuilder requestBuilder, final Class<T> type) {
        return MvcUtils.getResponse(requestBuilder, type, this.mockMvc, this.mapper);
    }

    protected <T> T getResponse(final MockHttpServletRequestBuilder requestBuilder, final TypeReference<T> typeReference) {
        return MvcUtils.getResponse(requestBuilder, typeReference, this.mockMvc, this.mapper);
    }

    protected <T> T getResponseAsync(final MockHttpServletRequestBuilder requestBuilder, final Class<T> expectedClass) {
        return MvcUtils.getResponseAsync(requestBuilder, this.mockMvc, expectedClass);
    }

    @SneakyThrows
    protected void expect404(final MockHttpServletRequestBuilder builder) {
        this.mockMvc.perform(builder).andExpect(status().is(SC_NOT_FOUND));
    }

    @SneakyThrows
    protected void expect404Async(final MockHttpServletRequestBuilder builder) {
        this.mockMvc
                .perform(asyncDispatch(this.mockMvc.perform(builder).andExpect(request().asyncStarted()).andReturn()))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    protected void expect403(final MockHttpServletRequestBuilder requestBuilder) {
        this.mockMvc.perform(requestBuilder).andExpect(status().is(SC_FORBIDDEN));
    }

    @SneakyThrows
    protected void expect401(final MockHttpServletRequestBuilder requestBuilder) {
        this.mockMvc.perform(requestBuilder).andExpect(status().is(SC_UNAUTHORIZED));
    }

    @SneakyThrows
    protected void expect400(final MockHttpServletRequestBuilder requestBuilder) {
        this.mockMvc.perform(requestBuilder).andExpect(status().is(SC_BAD_REQUEST));
    }

    @SneakyThrows
    protected void expect500(final MockHttpServletRequestBuilder requestBuilder) {
        this.mockMvc.perform(requestBuilder).andExpect(status().is(SC_INTERNAL_SERVER_ERROR));
    }

    @SneakyThrows
    protected void expect302(final MockHttpServletRequestBuilder requestBuilder) {
        this.mockMvc.perform(requestBuilder).andExpect(status().is(SC_FOUND));
    }

    protected <T> void assertResponseEquals(final MockHttpServletRequestBuilder requestBuilder, final Class<T> type, final T expected) {
        Assertions.assertEquals(expected, this.getResponse(requestBuilder, type));
    }

    protected <T> void assertResponseEquals(final MockHttpServletRequestBuilder requestBuilder, final TypeReference<T> type,
                                            final T expected) {
        Assertions.assertEquals(expected, this.getResponse(requestBuilder, type));
    }

    @Component
    static class AbstractMockMvcControllerConfig {

        @Bean
        @ConditionalOnMissingBean(ObjectMapper.class)
        public ObjectMapper objectMapper() {
            return json()
                    .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS)
                    .modulesToInstall(new Jdk8Module())
                    .build();
        }

    }

}
