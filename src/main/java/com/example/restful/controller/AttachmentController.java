package com.example.restful.controller;

import com.example.restful.entity.DbAttachment;
import com.example.restful.payload.result.Result;
import com.example.restful.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping(value = "/attachment")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;

    @GetMapping
    public List<DbAttachment> getAttachments() {
        return attachmentService.getAttachments();
    }

    @GetMapping(path = "/attachmentId/{id}")
    public ResponseEntity<Resource> getAttachmentContent(@PathVariable Integer id) {
        return attachmentService.getAttachmentContent(id);
    }

    @PostMapping
    public List<Result> postAttachmentContent(MultipartHttpServletRequest request) {
        return attachmentService.postAttachmentContent(request);
    }

    @PutMapping(path = "/{id}")
    public Result updateAttachmentContent(@PathVariable Integer id, MultipartHttpServletRequest request) throws CloneNotSupportedException {
        return attachmentService.updateAttachmentContent(id, request);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteAttachmentContent(@PathVariable Integer id){
        return attachmentService.deleteAttachmentContent(id);
    }
}
