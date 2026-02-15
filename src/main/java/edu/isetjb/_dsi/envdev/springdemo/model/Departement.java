package edu.isetjb._dsi.envdev.springdemo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "DEPARTEMENT")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column
    private String description;
    
    // Un département appartient à une entreprise
    @ManyToOne
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;
    
    // Un département peut avoir plusieurs employés
    @OneToMany(mappedBy = "departement")
    private List<Employer> employers = new ArrayList<>();
    
    // Constructeurs
    public Departement() {
    }
    
    public Departement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
}