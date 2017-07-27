package com.ocunha.odiff.exception;

/**
 * <code>{@link ServiceException}</code> is used for report errors caught on service layer.
 *
 * Created by osnircunha on 25/07/17.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }
}
