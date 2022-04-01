package com.example.restful.repository;

import com.example.restful.entity.DbAttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentContentRepository extends JpaRepository<DbAttachmentContent, Integer> {
    Optional<DbAttachmentContent> findByDbAttachment_Id(Integer dbAttachment_id);
}
