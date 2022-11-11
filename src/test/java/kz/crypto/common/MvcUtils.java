package kz.crypto.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MvcUtils {

    @SneakyThrows
    public static MockHttpServletRequestBuilder postRequestWithBearer(final String path,
                                                                      final Object content,
                                                                      final String token,
                                                                      final ObjectMapper mapper) {
        return MockMvcRequestBuilders.post(path)
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder postRequestNoBearer(final String path,
                                                                    final Object content,
                                                                    final ObjectMapper mapper) {
        return MockMvcRequestBuilders.post(path)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder postRequestNoBearerNoContent(final String path) {
        return MockMvcRequestBuilders.post(path)
                .contentType(APPLICATION_JSON);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder getRequestWithBearerAndParams(
            final String path, final String token, Map<String, String> params) {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get(path)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            mockHttpServletRequestBuilder.param(entry.getKey(), entry.getValue());
        }
        return mockHttpServletRequestBuilder;
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder getRequestWithBearer(final String path,
                                                                   final String token) {
    return MockMvcRequestBuilders.get(path)
        .contentType(APPLICATION_JSON)
        .header("Authorization", "Bearer " + token);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder getRequestNoBearer(final String path) {
        return MockMvcRequestBuilders.get(path).contentType(APPLICATION_JSON);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder getRequestNoBearer(final String path,
                                                                   final Object content,
                                                                   final ObjectMapper mapper) {
        return MockMvcRequestBuilders
                .get(path)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder putRequestWithBearer(
            final String path, final Object content, final String token, final ObjectMapper mapper
    ) {
        return putRequestNoContentWithBearer(path, token).content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder putRequestNoContentWithBearer(final String path,
                                                                              final String token) {
        return MockMvcRequestBuilders.put(path)
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder putRequestNoBearer(final String path,
                                                                   final Object content,
                                                                   final ObjectMapper mapper) {
        return MockMvcRequestBuilders.put(path)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder patchRequestWithBearer(final String path,
                                                                       final Object content,
                                                                       final String token,
                                                                       final ObjectMapper mapper) {
        return MockMvcRequestBuilders.patch(path)
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder patchRequestNoBearer(final String path,
                                                                     final Object content,
                                                                     final ObjectMapper mapper) {
        return MockMvcRequestBuilders.patch(path)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder patchRequestNoContentWithBearer(final String path,
                                                                                final String token) {
        return MockMvcRequestBuilders.patch(path)
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder deleteRequestWithBearer(final String path,
                                                                        final String token) {
        return MockMvcRequestBuilders.delete(path)
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder deleteRequestNoBearer(final String path) {
        return MockMvcRequestBuilders.delete(path)
                .contentType(APPLICATION_JSON);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder deleteRequestNoBearer(final String path,
                                                                      final Object content,
                                                                      final ObjectMapper mapper) {
        return MockMvcRequestBuilders.delete(path)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(content));
    }

    @SneakyThrows
    public static ResultActions expectOk(final MockHttpServletRequestBuilder requestBuilder,
                                         final MockMvc mockMvc) {
        return mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @SneakyThrows
    public static <T> T getResponse(final MockHttpServletRequestBuilder requestBuilder,
                                    final Class<T> type, final MockMvc mockMvc,
                                    final ObjectMapper mapper) {
        final String response = expectOk(requestBuilder, mockMvc)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(response, type);
    }

    @SneakyThrows
    public static <T> T getResponse(final MockHttpServletRequestBuilder requestBuilder,
                                    final TypeReference<T> typeReference,
                                    final MockMvc mockMvc, final ObjectMapper mapper) {
        final String response = expectOk(requestBuilder, mockMvc)
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(response, typeReference);
    }

    @SneakyThrows
    public static <T> T getResponseAsync(final MockHttpServletRequestBuilder requestBuilder,
                                         final MockMvc mockMvc, final Class<T> expectedClass) {
        return expectedClass.cast(expectOk(requestBuilder, mockMvc)
                .andReturn()
                .getAsyncResult());
    }
}
