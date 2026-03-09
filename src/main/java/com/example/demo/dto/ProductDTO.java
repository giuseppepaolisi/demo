package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 255, message = "Il nome deve avere tra 2 e 255 caratteri")
    private String name;

    private String description;

    @Min(0)
    private Double price;
}
