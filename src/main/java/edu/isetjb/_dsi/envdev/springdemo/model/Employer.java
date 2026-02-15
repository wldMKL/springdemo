package edu.isetjb._dsi.envdev.springdemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Entité représentant un employé
 */
@Data
@Entity
@Table(name = "EMPLOYER")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le matricule est obligatoire")
    @Size(min = 3, max = 20, message = "Le matricule doit contenir entre 3 et 20 caractères")
    @Column(nullable = false, unique = true)
    private String matricule;
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @Column(nullable = false)
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    @Column(nullable = false)
    private String prenom;
    
    @Email(message = "L'email n'est pas valide")
    @Column
    private String email;
    
    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir exactement 8 chiffres")
    @Column
    private String telephone;
    
    @Size(max = 100, message = "La fonction ne peut pas dépasser 100 caractères")
    @Column
    private String fonction;
    
    @DecimalMin(value = "0.0", message = "Le salaire doit être positif ou nul")
    @Column
    private Double salaire;
    
    // Un employé appartient à une entreprise (obligatoire)
    @NotNull(message = "L'entreprise est obligatoire")
    @ManyToOne
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;
    
    // Un employé appartient à un département (obligatoire)
    @NotNull(message = "Le département est obligatoire")
    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;
    
    // Constructeurs
    public Employer() {
    }
    
    public Employer(String matricule, String nom, String prenom, String fonction) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.fonction = fonction;
    }
}