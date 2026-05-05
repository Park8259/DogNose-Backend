package com.dognose.platform.file;

public record FileResponse(
        String originalFilename,
        String storedFilename,
        String contentType,
        long size,
        String url
) {
}
