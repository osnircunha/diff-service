package com.ocunha.odiff.service;

import com.ocunha.odiff.util.TestContentUtils;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test class for {@link CompareDataService}
 *
 * Created by osnircunha on 27/07/17.
 */
public class CompareDataServiceTest {

    CompareDataService compareDataService = new CompareDataService();

    @Test
    public void compare_should_returnEqualsMessage_when_byteArraysContentAreSame() {
        byte[] left = TestContentUtils.getJsonEncoded36Lines2064Bytes().getBytes();
        byte[] right = TestContentUtils.getJsonEncoded36Lines2064Bytes().getBytes();

        String result = this.compareDataService.compare(left, right);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(equalTo("Content are equals.")));
    }

    @Test
    public void compare_should_returnDifferentSizeMessage_when_byteArraysContentAreSame() {
        byte[] left = TestContentUtils.getJsonEncoded23Lines1316BytesEmailUpperCase().getBytes();
        byte[] right = TestContentUtils.getJsonEncoded36Lines2064Bytes().getBytes();

        String result = this.compareDataService.compare(left, right);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(equalTo("Different size. Left is 1316 bytes and Right is 2064 bytes")));
    }

    @Test
    public void compare_should_returnDifferentLineNumberMessage_when_byteArraysContentAreSame() {
        byte[] left = TestContentUtils.getJsonEncoded36Lines2064Bytes().getBytes();
        byte[] right = TestContentUtils.getJsonEncoded37Lines2064Bytes().getBytes();

        String result = this.compareDataService.compare(left, right);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(equalTo("Different line numbers. Left has 36 lines and Right has 37 lines")));
    }

    @Test
    public void compare_should_returnTotalOfDifferenceMessage_when_byteArraysContentAreSame() {
        byte[] left = TestContentUtils.getJsonEncoded37Lines2064Bytes().getBytes();
        byte[] right = TestContentUtils.getJsonEncoded37Lines2064BytesEmailUpperCase().getBytes();

        String result = this.compareDataService.compare(left, right);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(equalTo("5 difference(s) found. Line(s): 6, 13, 20, 27, 34")));
    }
}