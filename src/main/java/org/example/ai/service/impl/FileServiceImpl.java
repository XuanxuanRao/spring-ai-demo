package org.example.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.ai.domain.po.FileUploadHistory;
import org.example.ai.mapper.FileUploadHistoryMapper;
import org.example.ai.repository.FileRepository;
import org.example.ai.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author chenxuanrao06@gmail.com
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileUploadHistoryMapper, FileUploadHistory> implements FileService {

    private final FileRepository fileRepository;

    @Transactional
    public String upload(String chatId, MultipartFile file) {
        String url = fileRepository.save(file);
        var history = FileUploadHistory.builder()
                .fileUrl(url)
                .chatId(chatId)
                .build();
        save(history);
        return url;
    }

    @Override
    public Map.Entry<Resource, String> download(String chatId) {
        var history = lambdaQuery().eq(FileUploadHistory::getChatId, chatId).last("limit 1").one();
        return fileRepository.load(history.getFileUrl());
    }

}
