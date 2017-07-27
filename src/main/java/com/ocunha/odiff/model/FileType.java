package com.ocunha.odiff.model;

import lombok.Getter;

import java.util.Arrays;

/**
 * Created by osnircunha on 25/07/17.
 */
@Getter
public enum FileType {
    LEFT("left"),
    RIGHT("right");

    private String name;

    FileType(String name) {
        this.name = name;
    }

    public static FileType fromName(String name) {
        return Arrays.stream(FileType.values()).filter(fileType -> fileType.name.equals(name)).findFirst().orElse(null);
    }

}
