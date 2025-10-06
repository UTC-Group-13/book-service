package org.example.bookservice.dto.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class SearchRequest {
    private int page = 0;     // mặc định trang đầu tiên
    private int size = 10;    // mặc định 10 bản ghi / trang
    private String sortBy;    // tên cột sắp xếp
    private String sortDir;   // ASC / DESC

    // ✅ Hàm tạo Pageable mặc định
    public Pageable toPageable() {
        String sortColumn = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
        String sortDirection = (sortDir == null || sortDir.isBlank()) ? "DESC" : sortDir;

        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(sortDirection)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                sortColumn
        );

        return PageRequest.of(page, size, sort);
    }
}
