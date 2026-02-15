package edu.isetjb._dsi.envdev.springdemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entité représentant un employé
 */
@Data
@Entity
@Table(name = "employer")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(min = 3, max = 50, message = "Le matricule doit contenir entre 3 et 50 caractères")
    @Column(nullable = false, unique = true)
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false)
    private String prenom;

    @Email(message = "L'email n'est pas valide")
    @Column
    private String email;

    @Column
    private String telephone;

    @Column
    private String fonction;

    @Column
    private Double salaire;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Un employé appartient à une entreprise
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Entreprise entreprise;

    // Un employé appartient à un département
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departement_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Departement departement;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Constructeurs
    public Employer() {
    }

    public Employer(String matricule, String nom, String prenom) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
    }
}