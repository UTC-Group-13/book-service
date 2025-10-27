package org.example.bookservice.constant;

import lombok.Getter;
import org.example.bookservice.dto.exception.BusinessException;

@Getter
public enum ErrorCode {
    AUTHOR_NOT_FOUND("Không tìm thấy tác giả", "AUTHOR_NOT_FOUND"),
    AUTHOR_ALREADY_EXISTS("Tác giả đã tồn tại", "AUTHOR_ALREADY_EXISTS"),
    BOOK_NOT_FOUND("Không tìm thấy sách", "BOOK_NOT_FOUND"),
    BOOK_ALREADY_EXISTS("Sách đã tồn tại", "BOOK_ALREADY_EXISTS"),
    INVALID_IMAGE_TYPE("Không đúng với loại ảnh", "INVALID_IMAGE_TYPE"),
    IMAGE_NOT_FOUND("Không tìm thấy ảnh", "IMAGE_NOT_FOUND"),
    FILE_STORAGE_ERROR("Lỗi lưu trữ file", "FILE_STORAGE_ERROR"),
    SEND_EMAIL_ERROR("Gửi email thất bại", "SEND_EMAIL_ERROR"),
    ADMIN_ALREADY_EXISTS("Tài khoản quản trị đã tồn tại", "ADMIN_ALREADY_EXISTS"),
    ADMIN_NOT_EXISTS("Tài khoản quản trị không tồn tại", "ADMIN_NOT_EXISTS"),
    TOKEN_NOT_FOUND("Token không hợp lệ", "TOKEN_NOT_FOUND"),
    TOKEN_EXPIRED("Token quá hạn", "TOKEN_EXPIRED"),
    ;

    private final String message;
    private final String code;

    ErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public static ErrorCode toBuildError(String code){
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        throw new BusinessException("ErrorCode not found", "ERROR_CODE_NOT_FOUND");
    }
}
