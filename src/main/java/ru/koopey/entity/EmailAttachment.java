package ru.koopey.entity;

public record EmailAttachment(String originalFileName, String filename, String extension, String content) {

  public EmailAttachment(String originalFileName, String filename) {
    this(originalFileName, filename, null, null);
  }
}
