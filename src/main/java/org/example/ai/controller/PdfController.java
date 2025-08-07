package org.example.ai.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.example.ai.enums.BusinessType;
import org.example.ai.domain.ApiResponse;
import org.example.ai.service.FileService;
import org.example.ai.service.HistoryService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.example.ai.constant.DocumentConstant.CHAT_ID_META_DATA;


/**
 * @author chenxuanrao06@gmail.com
 */
@RestController
@RequestMapping("/ai/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final FileService fileService;
    private final VectorStore vectorStore;
    private final ChatClient pdfClient;
    private final HistoryService historyService;

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt, String chatId) {
        historyService.saveHistory(BusinessType.PDF, chatId);
        return pdfClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "chat_id == '" + chatId + "'"))
                .stream()
                .content();
    }

    @PostMapping("/upload/{chatId}")
    public ApiResponse<Void> upload(@PathVariable String chatId, @RequestParam("file") MultipartFile file) {
        try {
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return ApiResponse.error("文件类型错误，请上传PDF文件");
            }
            String fileUrl = fileService.upload(chatId, file);
            if (StrUtil.isBlank(fileUrl)) {
                return ApiResponse.error("文件上传失败，请重试");
            }
            write2VectorStore(file.getResource(), chatId);
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/file/{chatId}")
    public ResponseEntity<Resource> download(@PathVariable String chatId) {
        var file = fileService.download(chatId);
        String fileName = URLEncoder.encode(file.getValue(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.getKey());
    }

    private void write2VectorStore(Resource resource, String chatId) {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        List<Document> documents = pdfReader.read();
        documents.forEach(doc -> doc.getMetadata().put(CHAT_ID_META_DATA, chatId));
        System.out.println("读取到的文档数量: " + documents.size());
        vectorStore.add(documents);
    }

}
