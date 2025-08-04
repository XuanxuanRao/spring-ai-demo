package org.example.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.ai.domain.po.FileUploadHistory;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService extends IService<FileUploadHistory> {
    String upload(String chatId, MultipartFile file);

    Map.Entry<Resource, String> download(String chatId);
}
