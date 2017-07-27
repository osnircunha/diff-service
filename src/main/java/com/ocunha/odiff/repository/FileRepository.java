package com.ocunha.odiff.repository;

import com.ocunha.odiff.entities.FileEntity;
import com.ocunha.odiff.entities.FileId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <code>{@link FileRepository}</code> provides CRUD methods for {@link FileEntity}
 * <p>
 * Created by osnircunha on 25/07/17.
 */
public interface FileRepository extends JpaRepository<FileEntity, FileId> {

    /**
     * Return a single {@link FileEntity} matching the give {@link FileId} or {@literal null} if none was found
     *
     * @param id can not be {@literal null}
     * @return a single entity matching the given {@link FileId} or {@literal null} if none was found.
     */
    FileEntity findById(FileId id);
}
