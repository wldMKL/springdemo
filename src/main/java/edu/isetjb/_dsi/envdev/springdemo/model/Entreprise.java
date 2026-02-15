package edu.isetjb._dsi.envdev.springdemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une entreprise
 */
@Data
@Entity
@Table(name = "entreprise")
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(min = 2, max = 200, message = "Le nom doit contenir entre 2 et 200 caractères")
    @Column(nullable = false)
    private String nom;

    @Size(max = 300, message = "L'adresse ne peut pas dépasser 300 caractères")
    @Column
    private String adresse;

    @Pattern(regexp = "^[0-9]{8}$|^$", message = "Le téléphone doit contenir exactement 8 chiffres")
    @Column
    private String telephone;

    @Email(message = "L'email n'est pas valide")
    @Column
    private String email;

    // ✅ @ToString.Exclude et @EqualsAndHashCode.Exclude pour éviter les boucles infinies Lombok
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Departement> departements = new ArrayList<>();

    // ✅ Pas de CascadeType.ALL ici — on gère les employés indépendamment
    @OneToMany(mappedBy = "entreprise", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
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