package com.prac.ktb.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/attachments";

    public String saveProfileImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedName = UUID.randomUUID() + ext;

        try {
            File dest = new File(uploadDir + "/profileImage", savedName);
            file.transferTo(dest);

            return "/attachments/profileImage/" + savedName; // 웹에서 접근할 수 있는 경로 반환
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("프로필 이미지 저장 실패", e);
        }
    }

    public String savePostThumbnail(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedName = UUID.randomUUID() + ext;

        try {
            File dest = new File(uploadDir + "/postThumbnail", savedName);
            file.transferTo(dest);

            return "/attachments/postThumbnail/" + savedName; // 웹에서 접근할 수 있는 경로 반환
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("게시물 이미지 저장 실패", e);
        }
    }
}

