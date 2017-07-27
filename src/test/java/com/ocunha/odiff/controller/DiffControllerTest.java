package com.ocunha.odiff.controller;

import com.ocunha.odiff.ServiceExceptionMatcher;
import com.ocunha.odiff.util.TestContentUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link DiffController}
 *
 * Created by osnircunha on 26/07/17.
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void postFile_should_returnResponseOk_when_postValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(TestContentUtils.getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    public void postFile_should_returnResponseBadRequest_when_postEmptyData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(""))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    public void postFile_should_returnResponseInternalServerError_when_postInvalidData() throws Exception {
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Invalid parameters"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(" "))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }

    @Test
    public void postFile_should_returnResponseInternalServerError_when_postInvalidIdUrl() throws Exception {
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Invalid parameters"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/ /left")
                .content(TestContentUtils.getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }

    @Test
    public void postFile_should_returnResponseInternalServerError_when_postInvalidTypeUrl() throws Exception {
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Invalid parameters"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/invalid")
                .content(TestContentUtils.getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }

    @Test
    public void getDiff_should_returnResponseOkAndResultAreEqualsMessage_when_equalFilesWereSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(TestContentUtils.getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(TestContentUtils.getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDiff_should_returnResponseOkAndResultHasDifferentByteLenghtMessage_when_equalFilesAreSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(TestContentUtils.getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(TestContentUtils.getJsonEncoded23Lines1316BytesEmailUpperCase()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Different size. Left is 2064 bytes and Right is 1316 bytes"));
    }

    @Test
    public void getDiff_should_returnResponseOkAndResultHasDifferentLineNumberMessage_when_filesWithDifferentLineNumberAreSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(TestContentUtils.getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(TestContentUtils.getJsonEncoded37Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Different line numbers. Left has 36 lines and Right has 37 lines"));
    }

    @Test
    public void getDiff_should_returnResponseOkAndResultHasDifferentContentMessage_when_filesWithDifferentLineContentAreSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(TestContentUtils.getJsonEncoded37Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(TestContentUtils.getJsonEncoded37Lines2064BytesEmailUpperCase()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("5 difference(s) found. Line(s): 6, 13, 20, 27, 34"));
    }

    @Test
    public void getDiff_should_returnResponseInternalServerError_when_filesIdWasNotSaved() throws Exception{
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Missing data for id 123"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

}