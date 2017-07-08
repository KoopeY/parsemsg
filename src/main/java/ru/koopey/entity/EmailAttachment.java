package ru.koopey.entity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmailAttachment {
    private String originalFileName;
    private String filename;
    private String extension;
    private String content;

    public EmailAttachment(String originalFileName, String content) {
        this.originalFileName = originalFileName;
        this.content = content;
    }

    public EmailAttachment(String originalFileName, String filename, String extension, String content) {
        this.originalFileName = originalFileName;
        this.filename = filename;
        this.extension = extension;
        this.content = content;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getFilename() {
        return filename;
    }

    public String getExtension() {
        return extension;
    }

    public String getContent() {
        return content;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(this);
        } catch(Exception e) {
            System.out.println("Error while make json EmailAttachment " + e.getMessage());
        }

        return super.toString();
    }
}
