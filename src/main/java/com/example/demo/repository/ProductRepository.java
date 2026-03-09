package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

    // Tutti i prodotti di un utente
    List<Product> findByUser(User user);

    // Con ricerca per nome
    List<Product> findByUserAndNameContainingIgnoreCase(User user, String name);

    // Utile per verificare ownership prima di update/delete
    Optional<Product> findByIdAndUser(Long id, User user);
}