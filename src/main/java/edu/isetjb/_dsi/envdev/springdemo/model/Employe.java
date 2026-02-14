package edu.isetjb._dsi.envdev.springdemo.model; // Vérifie que ce package correspond bien au haut de ton fichier

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity //
@Table
(name = "EMPLOYE") 
public class Employe {

    @Id
    private int matricule;
    private String nom;

    // Constructeur vide (Obligatoire pour JPA)
    public Employe() {
    }

    // Constructeur avec arguments
    public Employe(int matricule, String nom) {
        this.matricule = matricule;
        this.nom = nom;
    }

    // Getters et Setters (nécessaires si tu n'utilises pas Lombok)
    public int getMatricule() {
        return matricule;
    }

    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}