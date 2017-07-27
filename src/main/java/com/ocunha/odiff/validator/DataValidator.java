package com.ocunha.odiff.validator;

import com.ocunha.odiff.dto.FileDto;
import com.ocunha.odiff.model.FileType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by osnircunha on 25/07/17.
 */
@Component
public class DataValidator {

    public boolean validate(FileDto dto) {
        return validate(dto.getContent()) && validate(dto.getId()) && validate(dto.getType()) && FileType.fromName(dto.getType()) != null;
    }

    public boolean validate(String value) {
        return StringUtils.isNotBlank(value);
    }
}
