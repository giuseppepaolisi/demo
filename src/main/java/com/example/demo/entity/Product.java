package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "products")
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @PositiveOrZero
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)  // LAZY = non caricare l'utente finché non serve
    @JoinColumn(name = "user_id", nullable = false)  // nome della colonna FK nel DB
    private User user;
}