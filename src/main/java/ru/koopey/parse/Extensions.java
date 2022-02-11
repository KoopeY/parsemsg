package ru.koopey.parse;

public enum Extensions {
  EML("eml"),
  MSG("msg");

  private final String extension;

  Extensions(String extension) {
    this.extension = extension;
  }

  public String extension() {
    return this.extension;
  }
}
