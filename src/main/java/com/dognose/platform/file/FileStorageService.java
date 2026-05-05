package com.dognose.platform.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg",
            "jpeg",
            "png",
            "webp"
    );

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public FileResponse storeImage(MultipartFile file) {
        validateImage(file);

        try {
            Files.createDirectories(uploadRoot);

            String originalFilename = cleanFilename(file.getOriginalFilename());
            String extension = getExtension(originalFilename);
            String storedFilename = UUID.randomUUID() + "." + extension;
            Path targetPath = uploadRoot.resolve(storedFilename).normalize();

            if (!targetPath.startsWith(uploadRoot)) {
                throw new IllegalArgumentException("파일 저장 경로가 올바르지 않습니다.");
            }

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return new FileResponse(
                    originalFilename,
                    storedFilename,
                    file.getContentType(),
                    file.getSize(),
                    "/uploads/" + storedFilename
            );
        } catch (IOException exception) {
            throw new IllegalStateException("파일 저장에 실패했습니다.", exception);
        }
    }

    public Path getUploadRoot() {
        return uploadRoot;
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지 파일이 없습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("jpg, png, webp 이미지만 업로드할 수 있습니다.");
        }

        String originalFilename = cleanFilename(file.getOriginalFilename());
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않은 이미지 확장자입니다.");
        }
    }

    private String cleanFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "image";
        }
        return Paths.get(filename).getFileName().toString();
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new IllegalArgumentException("이미지 파일 확장자가 필요합니다.");
        }
        return filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
