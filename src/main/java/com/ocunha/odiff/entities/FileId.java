package com.ocunha.odiff.entities;

import com.ocunha.odiff.model.FileType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * <code>{@link FileId}</code> is a composed id to allow two files being saved using the same id but with different types.
 * Created by osnircunha on 25/07/17.
 */
@Data
@EqualsAndHashCode
@Embeddable
public class FileId implements Serializable{

    private String id;

    private FileType type;
}
