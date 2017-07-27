package com.ocunha.odiff;

import com.ocunha.odiff.exception.ServiceException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by osnircunha on 26/07/17.
 */
public class ServiceExceptionMatcher extends TypeSafeMatcher<ServiceException> {

    private String actual;
    private String expected;

    private ServiceExceptionMatcher(String expected) {
        this.expected = expected;
    }

    public static ServiceExceptionMatcher assertMessage(String expected) {
        return new ServiceExceptionMatcher(expected);
    }

    @Override
    protected boolean matchesSafely(ServiceException exception) {
        actual = exception.getMessage();
        return actual.equals(expected);
    }

    @Override
    public void describeTo(Description desc) {
        desc.appendText("Actual =").appendValue(actual)
                .appendText(" Expected =").appendValue(expected);

    }
}
