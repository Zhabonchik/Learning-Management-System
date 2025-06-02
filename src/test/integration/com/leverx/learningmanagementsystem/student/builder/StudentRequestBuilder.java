package com.leverx.learningmanagementsystem.student.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.leverx.learningmanagementsystem.utils.ITUtils.PAGE;
import static com.leverx.learningmanagementsystem.utils.ITUtils.PAGE_SIZE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Component
public class StudentRequestBuilder {

    @Autowired
    protected ObjectMapper objectMapper;

    public MockHttpServletRequestBuilder buildGetAllRequest(String path, String page, String pageSize) {
        return get(path)
                .param(PAGE, page)
                .param(PAGE_SIZE, pageSize);
    }

    public MockHttpServletRequestBuilder buildGetByIdRequest(String path, String id) {
        return get(path + "/" + id);
    }

    public MockHttpServletRequestBuilder buildCreateRequest(String path, MediaType contentType, Object content) throws JsonProcessingException {
        return post(path)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(content));
    }

    public MockHttpServletRequestBuilder buildPostRequest(String path) {
        return post(path);
    }

    public MockHttpServletRequestBuilder buildUpdateByIdRequest(String path, String id, MediaType contentType, Object content) throws JsonProcessingException {
        return put(path + "/" + id)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(content));
    }

    public MockHttpServletRequestBuilder buildDeleteByIdRequest(String path, String id) {
        return delete(path + "/" + id);
    }
}
