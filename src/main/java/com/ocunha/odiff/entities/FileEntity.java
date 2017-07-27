package com.ocunha.odiff.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * <code>{@link FileEntity}</code> models a file content to be save on database.
 *
 * Created by osnircunha on 25/07/17.
 */
@Data
@EqualsAndHashCode
@Entity
public class FileEntity {

    @EmbeddedId
    private FileId id;

    @Lob
    @Column(length=100000)
    private byte[] content;

}
