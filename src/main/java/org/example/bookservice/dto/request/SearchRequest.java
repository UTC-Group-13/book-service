package org.example.bookservice.dto.request;

import lombok.Data;

@Data
public class SearchRequest {
    private int page = 0;     // mặc định trang đầu tiên
    private int size = 10;    // mặc định 10 bản ghi / trang
    private String sortBy;    // tên cột sắp xếp
    private String sortDir;   // ASC / DESC
}
