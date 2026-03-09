package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Genera costruttore con tutti i campi final → Spring li inietta (constructor injection)
@Slf4j
@Transactional(readOnly = true) // Default readOnly=true per tutti i metodi: ottimizza le query di lettura

public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Metodo privato riutilizzabile — recupera l'utente dal SecurityContext
    // Spring Security lo popola automaticamente dopo il login
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();  // il Principal è l'oggetto User perché implementa UserDetails
    }

    public List<ProductDTO> getAll() {
        return productMapper.toResponseList(productRepository.findByUser(getCurrentUser()));
    }

    public List<ProductDTO> searchByName(String name) {
        return productMapper.toResponseList(productRepository.findByUserAndNameContainingIgnoreCase(getCurrentUser(), name));
    }

    public ProductDTO getById(Long id) {
        return productMapper.toDTO(
                productRepository.findByIdAndUser(id, getCurrentUser())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Product not found with id: " + id
                        ))
        );
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        product.setUser(getCurrentUser());  // aggancia l'utente corrente
        log.info("Salvataggio prodotto '{}' per utente id: {}",
                productDTO.getName(), getCurrentUser().getId());
        return productMapper.toDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO details) {
        Product p = productRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato, id: " + id));
        p.setName(details.getName());
        p.setPrice(details.getPrice());
        p.setDescription(details.getDescription());
        return productMapper.toDTO(productRepository.save(p));
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato, id: " + id));
        productRepository.delete(product);
    }
}