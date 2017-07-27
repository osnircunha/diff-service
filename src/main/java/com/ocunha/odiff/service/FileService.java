package com.ocunha.odiff.service;

import com.ocunha.odiff.dto.FileDto;
import com.ocunha.odiff.entities.FileEntity;
import com.ocunha.odiff.exception.ServiceException;
import com.ocunha.odiff.mapper.FileMapper;
import com.ocunha.odiff.model.FileType;
import com.ocunha.odiff.model.Response;
import com.ocunha.odiff.repository.FileRepository;
import com.ocunha.odiff.validator.DataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by osnircunha on 25/07/17.
 */
@Component
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DataValidator validator;

    @Autowired
    private CompareDataService compareDataService;

    /**
     * Saves a FileEntity to the database
     *
     * @param fileDto data transfer object containing the information to be saved
     * @return {@literal true} if {@link FileEntity} was saved successfully od {@literal false} otherwise
     * @throws ServiceException in case the given {@link FileDto} contains invalid data
     */
    public boolean save(FileDto fileDto) throws ServiceException {
        if (this.validator.validate(fileDto)) {
            return this.fileRepository.save(this.fileMapper.mapToEntity(fileDto)) != null;
        } else {
            throw new ServiceException("Invalid parameters");
        }
    }

    /**
     * Retrieves the files saved previously for a given id and compare they content.
     *
     * @param id the file identifier
     * @return a {@link Response} object containing the difference details between the two files
     * @throws ServiceException in case the given id is invalid
     */
    public Response compareFile(String id) throws ServiceException {
        if (!this.validator.validate(id)) {
            throw new ServiceException("Invalid parameter");
        }

        FileEntity entityLeft = this.fileRepository.findById(this.fileMapper.mapToFileId(id, FileType.LEFT));
        FileEntity entityRight = this.fileRepository.findById(this.fileMapper.mapToFileId(id, FileType.RIGHT));

        if(entityLeft == null || entityRight == null){
            throw  new ServiceException("Missing data for id " + id);
        }

        return new Response(compareDataService.compare(entityLeft.getContent(), entityRight.getContent()));
    }

}
