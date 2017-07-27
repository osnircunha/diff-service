package com.ocunha.odiff.mapper;

import com.ocunha.odiff.dto.FileDto;
import com.ocunha.odiff.entities.FileEntity;
import com.ocunha.odiff.entities.FileId;
import com.ocunha.odiff.model.FileType;
import org.springframework.stereotype.Component;

/**
 * <code>{@link FileMapper}</code> used to map data transfer objects to entities.
 *
 * Created by osnircunha on 25/07/17.
 */
@Component
public class FileMapper {

    public FileEntity mapToEntity(FileDto dto) {
        FileEntity entity = new FileEntity();
        entity.setContent(dto.getContent().getBytes());

        FileId id = new FileId();
        id.setId(dto.getId());
        id.setType(FileType.fromName(dto.getType()));

        entity.setId(id);
        return entity;
    }

    public FileId mapToFileId(String id, FileType type) {
        FileId fileId = new FileId();
        fileId.setId(id);
        fileId.setType(type);

        return fileId;
    }
}
