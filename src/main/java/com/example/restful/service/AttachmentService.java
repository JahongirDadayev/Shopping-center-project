package com.example.restful.service;

import com.example.restful.entity.DbAttachment;
import com.example.restful.entity.DbAttachmentContent;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.AttachmentContentRepository;
import com.example.restful.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    public List<DbAttachment> getAttachments() {
        return attachmentRepository.findAll();
    }

    public ResponseEntity<Resource> getAttachmentContent(Integer id) {
        Optional<DbAttachment> optionalDbAttachment = attachmentRepository.findById(id);
        if (optionalDbAttachment.isPresent()) {
            Optional<DbAttachmentContent> optionalDbAttachmentContent = attachmentContentRepository.findByDbAttachment_Id(id);
            if (optionalDbAttachmentContent.isPresent()) {
                DbAttachment attachment = optionalDbAttachment.get();
                DbAttachmentContent attachmentContent = optionalDbAttachmentContent.get();
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.getContentType())).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getOriginalName() + "\"").body(new ByteArrayResource(attachmentContent.getBytes()));
            } else {
                throw new RuntimeException("The attachmentContent for the attachment you entered could not be found");
            }
        } else {
            throw new RuntimeException("No files matching the id you entered were found");
        }
    }

    public List<Result> postAttachmentContent(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        List<MultipartFile> fileList = new ArrayList<>();
        while (fileNames.hasNext()) {
            fileList.add(request.getFile(fileNames.next()));
        }
        List<Result> resultList = new ArrayList<>();
        if (fileList.size() != 0) {
            int count = 1;
            for (MultipartFile file : fileList) {
                DbAttachment attachment = new DbAttachment();
                attachment.setOriginalName(file.getOriginalFilename());
                attachment.setSize(file.getSize());
                attachment.setContentType(file.getContentType());
                try {
                    attachmentRepository.save(attachment);
                    DbAttachmentContent attachmentContent = new DbAttachmentContent();
                    attachmentContent.setBytes(file.getBytes());
                    attachmentContent.setDbAttachment(attachment);
                    try {
                        attachmentContentRepository.save(attachmentContent);
                        resultList.add(new Result(count + ") File information named " + file.getName() + " is saved. The id of this file is: " + attachment.getId(), true));
                    } catch (Exception e) {
                        e.printStackTrace();
                        attachmentRepository.delete(attachment);
                        resultList.add(new Result(count + ") The file named " + file.getName() + " was not saved due to errors", false));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resultList.add(new Result(count + ") The file named " + file.getName() + " was not saved due to errors", false));
                }
                count++;
            }
        } else {
            resultList.add(new Result("You did not send any files", false));
        }
        return resultList;
    }


    public Result updateAttachmentContent(Integer id, MultipartHttpServletRequest request) throws CloneNotSupportedException {
        Optional<DbAttachment> optionalDbAttachment = attachmentRepository.findById(id);
        if (optionalDbAttachment.isPresent()) {
            Optional<DbAttachmentContent> optionalDbAttachmentContent = attachmentContentRepository.findByDbAttachment_Id(id);
            if (optionalDbAttachmentContent.isPresent()) {
                Iterator<String> fileNames = request.getFileNames();
                List<MultipartFile> fileList = new ArrayList<>();
                while (fileNames.hasNext()) {
                    fileList.add(request.getFile(fileNames.next()));
                }
                if (fileList.size() == 1) {
                    MultipartFile file = fileList.get(0);
                    DbAttachment attachment = optionalDbAttachment.get();
                    DbAttachment oldAttachment = (DbAttachment) attachment.clone();
                    attachment.setOriginalName(file.getOriginalFilename());
                    attachment.setSize(file.getSize());
                    attachment.setContentType(attachment.getContentType());
                    try {
                        attachmentRepository.save(attachment);
                        DbAttachmentContent attachmentContent = optionalDbAttachmentContent.get();
                        attachmentContent.setBytes(file.getBytes());
                        attachmentContent.setDbAttachment(attachment);
                        try {
                            attachmentContentRepository.save(attachmentContent);
                            return new Result("File data named " + file.getName() + " was saved", true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            attachmentRepository.save(oldAttachment);
                            return new Result("Error dbAttachmentContent table", false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new Result("Error dbAttachment table", false);
                    }

                } else {
                    return new Result("It is not possible to send more than one file", false);
                }
            } else {
                return new Result("Error dbAttachmentContent table", false);
            }
        } else {
            return new Result("Could not find the file that matches the id you entered", false);
        }
    }

    public Result deleteAttachmentContent(Integer id) {
        Optional<DbAttachment> optionalDbAttachment = attachmentRepository.findById(id);
        if (optionalDbAttachment.isPresent()) {
            Optional<DbAttachmentContent> optionalDbAttachmentContent = attachmentContentRepository.findByDbAttachment_Id(id);
            if (optionalDbAttachmentContent.isPresent()) {
                try {
                    DbAttachment attachment = optionalDbAttachment.get();
                    DbAttachmentContent attachmentContent = optionalDbAttachmentContent.get();
                    attachmentContentRepository.delete(attachmentContent);
                    return new Result(attachment.getOriginalName() + " file information has been deleted", true);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result("Could not delete file information that matches the id you entered", false);
                }
            } else {
                return new Result("Error dbAttachmentContent table", false);
            }
        } else {
            return new Result("Could not find the file that matches the id you entered", false);
        }
    }
}
