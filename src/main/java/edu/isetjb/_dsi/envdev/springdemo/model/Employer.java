package edu.isetjb._dsi.envdev.springdemo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "EMPLOYER")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String matricule;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column
    private String email;
    
    @Column
    private String telephone;
    
    @Column
    private String fonction;
    
    @Column
    private Double salaire;
    
    // Un employé appartient à une entreprise (obligatoire)
    @ManyToOne
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;
    
    // Un employé appartient à un département (obligatoire)
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