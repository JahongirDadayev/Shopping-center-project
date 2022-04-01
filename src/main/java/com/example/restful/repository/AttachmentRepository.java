package com.example.restful.repository;

import com.example.restful.entity.DbAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<DbAttachment, Integer> {
}
