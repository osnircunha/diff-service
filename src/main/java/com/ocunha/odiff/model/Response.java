package com.ocunha.odiff.model;

import lombok.Data;
import lombok.NonNull;

/**
 * <code>{@link Response}</code> is user do model the http results.
 *
 * Created by osnircunha on 25/07/17.
 */
@Data
public class Response {

    @NonNull
    private String result;
}
