package com.example.board.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class FileDownloadController {

    // 파일 다운로드 요청 처리 (경로 추적 취약점 포함)
    @GetMapping("/files/uploads")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws IOException {
        // 사용자의 입력을 그대로 경로에 사용 (경로 추적 취약점 발생 가능)
        File file = new File("uploads/" + fileName);  

        if (file.exists()) {
            Resource resource = new FileSystemResource(file);

            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            System.out.println("Target file path: " + file.getAbsolutePath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
