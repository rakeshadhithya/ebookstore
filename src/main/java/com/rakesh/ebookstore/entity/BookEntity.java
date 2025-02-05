package com.rakesh.ebookstore.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEntity {
    
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE)
    private Integer bookId;

    @Column(nullable = false, length=200)
    @NotBlank(message = "Please provide book title")
    private String title;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Please provide book author name")
    private String author;
    
    //assigned by backed based on uploaded file name
    @Column(nullable = false, length = 100)
    private String coverPhotoName;

    @Column(nullable = false, length = 300)
    @NotBlank(message = "Please privide description for book")
    private String description;

    @ElementCollection
    @CollectionTable(name = "book_genere")
    private Set<String> genere;

    @Column(nullable = false)
    @NotNull
    private Integer price;
    
}
