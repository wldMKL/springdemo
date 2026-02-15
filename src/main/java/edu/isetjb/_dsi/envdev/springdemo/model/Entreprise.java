package edu.isetjb._dsi.envdev.springdemo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ENTREPRISE")
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column
    private String adresse;

    @Column
    private String telephone;

    @Column
    private String email;

    // Une entreprise possède plusieurs départements
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Departement> departements = new ArrayList<>();

    // Une entreprise possède plusieurs employés
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<Employer> employers = new ArrayList<>();

    // Constructeurs
    public Entreprise() {
    }

    public Entreprise(String nom, String adresse, String telephone, String email) {
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
    }
}