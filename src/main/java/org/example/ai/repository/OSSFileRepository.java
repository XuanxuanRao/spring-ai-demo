package org.example.ai.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.client.FileClient;
import org.example.ai.domain.FileClientResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author chenxuanrao06@gmail.com
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OSSFileRepository implements FileRepository {

    private final FileClient fileClient;

    @Override
    public String save(MultipartFile file) {
        FileClientResponse response = fileClient.upload(file);
        if (!Objects.equals(response.getCode(), FileClientResponse.SUCCESS)) {
            return null;
        }
        log.info("上传文件 {} 成功: {}", response.getData().getName(), response.getData().getUrl());
        return response.getData().getUrl();
    }

    @Override
    public Map.Entry<Resource, String> load(String fileUrl) {
        ResponseEntity<Resource> response = fileClient.download(fileUrl);
        ContentDisposition contentDisposition = response.getHeaders().getContentDisposition();

        // 解析 Content-Disposition，获取文件名
        String fileName = contentDisposition.getFilename();

        if (response.getStatusCode().is2xxSuccessful()) {
            return new AbstractMap.SimpleEntry<>(response.getBody(), fileName);
        } else {
            log.error("文件下载失败: {}", response.getStatusCode());
            throw new RuntimeException("文件下载失败");
        }
    }
}
