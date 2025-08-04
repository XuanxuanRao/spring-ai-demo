package org.example.ai.client;

import org.example.ai.client.config.FileClientConfig;
import org.example.ai.domain.FileClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author chenxuanrao06@gmail.com
 */
@FeignClient(value = "FileClient", url = "${service.file.url}", configuration = FileClientConfig.class)
public interface FileClient {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileClientResponse upload(MultipartFile file);

    @GetMapping("/download")
    ResponseEntity<Resource> download(@RequestParam String fileUrl);

}
