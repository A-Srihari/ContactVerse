package com.srihari.contactverse.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final long MAX_BYTES = 5L * 1024 * 1024; // 5MB

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public boolean isConfigured() {
        String url = System.getenv("CLOUDINARY_URL");
        return url != null && !url.isBlank();
    }

    public ImageUploadResult upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("No file provided");
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) throw new IllegalArgumentException("Only image files are allowed");
        if (file.getSize() > MAX_BYTES) throw new IllegalArgumentException("File too large (max 5MB)");

        Map<String, Object> options = ObjectUtils.asMap(
                "folder", "contactverse/avatars",
                "unique_filename", true,
                "overwrite", true,
                "resource_type", "image"
        );

        @SuppressWarnings("unchecked")
        Map<String,Object> result = cloudinary.uploader().upload(file.getBytes(), options);

        String url = (String) result.get("secure_url");
        String publicId = (String) result.get("public_id");
        Number bytes = (Number) result.get("bytes");

        return new ImageUploadResult(url, publicId, bytes == null ? 0L : bytes.longValue());
    }

    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) return;
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception ignored) {
            // For a resume app: ignore, but in prod log it.
        }
    }

    // small DTO
    public static class ImageUploadResult {
        private final String url;
        private final String publicId;
        private final long bytes;
        public ImageUploadResult(String url, String publicId, long bytes) {
            this.url = url; this.publicId = publicId; this.bytes = bytes;
        }
        public String getUrl() { return url; }
        public String getPublicId() { return publicId; }
        public long getBytes() { return bytes; }
    }
}
