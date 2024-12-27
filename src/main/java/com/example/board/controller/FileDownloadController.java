package com.example.board.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class FileDownloadController {

    // 파일 다운로드 처리
    @GetMapping("/files/uploads/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        // 파일이 저장된 디렉토리 경로 지정 (여기서는 'uploads' 디렉토리)
        File file = new File("uploads/" + fileName);  // 실제 저장된 경로와 일치해야 합니다.

        // 파일이 존재하는지 확인
        if (file.exists()) {
            // 파일을 리소스로 반환
            Resource resource = new FileSystemResource(file);

            // MIME 타입 추정 (파일 확장자에 맞는 타입으로 설정)
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;  // 기본 MIME 타입
            }

            // 파일 다운로드를 위해 HTTP 헤더 설정
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))  // MIME 타입 설정
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);  // 파일 내용을 바디에 담아 반환
        } else {
            // 파일이 존재하지 않으면 404 응답
            return ResponseEntity.notFound().build();
        }
    }
}
