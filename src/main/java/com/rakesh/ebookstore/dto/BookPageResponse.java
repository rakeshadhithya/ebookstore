package com.rakesh.ebookstore.dto;

import java.util.List;

public record BookPageResponse(List<BookDto> bookDtos,
                               Integer pageNumber,
                               Integer pageSize,
                               Long totalElements,
                               Integer totalPages,
                               Boolean isLast ) {
    
}
