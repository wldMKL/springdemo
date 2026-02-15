package edu.isetjb._dsi.envdev.springdemo.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) pour Employer
 * Utilisé pour transférer les données entre le contrôleur et la vue
 * sans exposer directement l'entité JPA
 */
@Data
public class EmployerDTO {
    
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String fonction;
    private Double salaire;
    
    // Informations du département
    private Long departementId;
    private String departementNom;
    
    // Informations de l'entreprise
    private Long entrepriseId;
    private String entrepriseNom;
    
    // Nom complet pour l'affichage
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    // Matricule formaté
    public String getMatriculeFormate() {
        return "#" + matricule;
    }
    
    // Salaire formaté
    public String getSalaireFormate() {
        if (salaire == null) {
            return "Non renseigné";
        }
        return String.format("%.2f TND", salaire);
    }
}