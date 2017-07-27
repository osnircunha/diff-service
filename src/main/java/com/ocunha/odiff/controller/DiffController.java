package com.ocunha.odiff.controller;

import com.ocunha.odiff.dto.FileDto;
import com.ocunha.odiff.model.Response;
import com.ocunha.odiff.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <code>{@link DiffController}</code> is a Rest controler that provides endpoint to submit files and compare them.
 *
 * Created by osnircunha on 25/07/17.
 */
@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/{id}/{type}", method = RequestMethod.PUT)
    public ResponseEntity<Response> postFile(@PathVariable("id") String id, @PathVariable("type") String type, @RequestBody String content) {
        FileDto dto = new FileDto();
        dto.setId(id);
        dto.setType(type);
        dto.setContent(content);

        this.fileService.save(dto);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}")
    public ResponseEntity<Response> getDiff(@PathVariable("id") String id) {
        Response response = this.fileService.compareFile(id);
        return ResponseEntity.ok(response);
    }

}
