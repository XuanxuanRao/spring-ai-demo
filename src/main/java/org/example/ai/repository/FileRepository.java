package org.example.ai.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileRepository {
    String save(MultipartFile file);

    Map.Entry<Resource, String> load(String chatId);
}