package com.ocunha.odiff.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <code>{@link FileDto}</code> is a data transfer object to map a {@link com.ocunha.odiff.entities.FileEntity} on controller layer
 *
 * Created by osnircunha on 25/07/17.
 */
@Data
@EqualsAndHashCode
public class FileDto {

    private String id;

    private String content;

    private String type;
}
