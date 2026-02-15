package edu.isetjb._dsi.envdev.springdemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un département
 */
@Data
@Entity
@Table(name = "departement")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du département est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false)
    private String nom;

    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column
    private String description;

    // Un département appartient à une entreprise
    @NotNull(message = "L'entreprise est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Entreprise entreprise;

    // Un département peut avoir plusieurs employés
    // ✅ @ToString.Exclude pour éviter les boucles infinies Lombok
    @OneToMany(mappedBy = "departement")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Employer> employers = new ArrayList<>();

    // Constructeurs
    public Departement() {
    }

    public Departement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
}