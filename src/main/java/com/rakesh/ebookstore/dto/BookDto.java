package com.rakesh.ebookstore.dto;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    
    //auto generated
    private Integer bookId;

    @NotBlank(message = "Please enter book title for book dto")
    private String title;

    @NotBlank(message = "Please enter book author for book dto")
    private String author;
    
    //assigned by backend based on uploaded file name
    private String coverPhotoName;

    //extra attribute than entity, assigned by backend
    private String coverPhotoUrl;

    @NotBlank(message = "Please enter description for book dto")
    private String description;

    @NotNull(message = "Please give the genre list for book dto")
    private Set<String> genere;

    @NotNull(message = "Please give the price for book dto")
    private Integer price;
    
}
