package com.ocunha.odiff.service;

import com.ocunha.odiff.ServiceExceptionMatcher;
import com.ocunha.odiff.dto.FileDto;
import com.ocunha.odiff.entities.FileEntity;
import com.ocunha.odiff.entities.FileId;
import com.ocunha.odiff.mapper.FileMapper;
import com.ocunha.odiff.model.Response;
import com.ocunha.odiff.repository.FileRepository;
import com.ocunha.odiff.validator.DataValidator;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link FileService}
 *
 * Created by osnircunha on 26/07/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private DataValidator validator;

    @Mock
    private CompareDataService compareDataService;

    @InjectMocks
    public FileService fileService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void save_should_throwServiceException_when_FileDtoHasInvalidData() {
        Mockito.when(validator.validate(Matchers.any(FileDto.class))).thenReturn(Boolean.FALSE);

        expectedException.expect(ServiceExceptionMatcher.assertMessage("Invalid parameters"));

        this.fileService.save(new FileDto());
    }

    @Test
    public void save_should_completeSuccessfully_when_FileDtoIsValid() {
        Mockito.when(validator.validate(Matchers.any(FileDto.class))).thenReturn(Boolean.TRUE);

        this.fileService.save(new FileDto());

        Mockito.verify(fileRepository, Mockito.times(1)).save(Matchers.any(FileEntity.class));
    }

    @Test
    public void compareFile_should_throwServiceException_when_IdIsInvalid() throws Exception {
        Mockito.when(validator.validate("")).thenReturn(Boolean.FALSE);

        expectedException.expect(ServiceExceptionMatcher.assertMessage("Invalid parameter"));

        this.fileService.compareFile("");
    }

    @Test
    public void compareFile_should_completeSuccessfully_when_IsValid() {
        String actualId = "123";

        Mockito.when(validator.validate(actualId)).thenReturn(Boolean.TRUE);
        Mockito.when(fileRepository.findById(Matchers.any(FileId.class))).thenReturn(new FileEntity());
        Mockito.when(fileRepository.findById(Matchers.any(FileId.class))).thenReturn(new FileEntity());

        String expectedResult = "Content are equals.";

        Mockito.when(compareDataService.compare(Matchers.any(), Matchers.any())).thenReturn(expectedResult);

        Response response = this.fileService.compareFile(actualId);

        Assert.assertThat(response.getResult(), org.hamcrest.Matchers.is(expectedResult));
    }


}