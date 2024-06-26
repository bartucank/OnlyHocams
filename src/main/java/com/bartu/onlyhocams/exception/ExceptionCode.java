package com.bartu.onlyhocams.exception;

public enum ExceptionCode {
    THIS_USERNAME_ALREADY_EXISTS("This username already exists"),
    INVALID_REQUEST("Invalid request"),
    THIS_EMAIL_ALREADY_EXISTS("This email already exists."),
    EMAIL_CANNOT_BE_NULL("Email cannot be empty."),
    NAME_CANNOT_BE_NULL("Name cannot be empty."),
    PASS_CANNOT_BE_NULL("Password cannot be empty."),
    USERNAME_CANNOT_BE_NULL("Username cannot be empty."),
    CONTENT_CANNOT_BE_EMPTY("Please write something :)"),
    USER_UNAUTHORIZED("Unauthorized user."),
    CATEGORY_NULL("Category not found."),
    POST_NOT_FOUND("Post not found."),
    COMMENT_NOT_FOUND("Comment not found."),
    USER_NOT_FOUND("User not found."),
    FILE_UPLOAD_FAILED("File upload failed."),
    TITLE_CANNOT_BE_EMPTY(""),
    DOCUMENT_NOT_FOUND("Document not found."),
    NOTE_NOT_FOUND("Note not found."),
    TYPE_CANNOT_BE_EMPTY("Type cannot be empty."), INSUFFICIENT_BALANCE("Insufficient balance."),

    REVIEW_NOT_FOUND("Review not found."),;
    private final String description;

    ExceptionCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
