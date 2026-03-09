package com.example.demo.mapper;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();
    }

    public ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    /**
     * Converte una lista di entità in lista di DTO.
     *
     * Stream API: itera sulla lista, applica toDTO() a ogni elemento, raccoglie il risultato.
     * È equivalente a un ciclo for, ma più conciso e funzionale.
     *
     * streams().map(fn).collect() è un pattern che vedrai ovunque in Java enterprise.
     */
    public List<ProductDTO> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toDTO)  // this::toResponse è un method reference, equivale a p -> this.toResponse(p)
                .toList();
    }
}
