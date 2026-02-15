package edu.isetjb._dsi.envdev.springdemo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.isetjb._dsi.envdev.springdemo.model.Departement;
import edu.isetjb._dsi.envdev.springdemo.model.Entreprise;
import edu.isetjb._dsi.envdev.springdemo.repository.DepartementRepository;
import edu.isetjb._dsi.envdev.springdemo.repository.EmployerRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des départements
 */
@Service
@Transactional
public class DepartementService {

    private final DepartementRepository departementRepository;
    private final EmployerRepository employerRepository;
    private final EntrepriseService entrepriseService;

    public DepartementService(DepartementRepository departementRepository,
                             EmployerRepository employerRepository,
                             EntrepriseService entrepriseService) {
        this.departementRepository = departementRepository;
        this.employerRepository = employerRepository;
        this.entrepriseService = entrepriseService;
    }

    /**
     * Récupère tous les départements
     */
    public List<Departement> findAll() {
        return departementRepository.findAll();
    }

    /**
     * Récupère un département par son ID
     */
    public Optional<Departement> findById(Long id) {
        return departementRepository.findById(id);
    }

    /**
     * Récupère un département par son ID ou lève une exception
     */
    public Departement findByIdOrThrow(Long id) {
        return departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département introuvable (ID=" + id + ")"));
    }

    /**
     * Compte le nombre de départements
     */
    public long count() {
        return departementRepository.count();
    }

    /**
     * Sauvegarde ou met à jour un département
     */
    public Departement save(Departement departement) {
        // Validation
        if (departement.getNom() == null || departement.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom du département est obligatoire");
        }

        // Toujours lier à l'entreprise principale
        Entreprise entreprise = entrepriseService.getEntreprisePrincipale();
        departement.setEntreprise(entreprise);

        return departementRepository.save(departement);
    }

    /**
     * Supprime un département par son ID
     * Vérifie d'abord qu'il n'y a pas d'employés affectés
     */
    public void deleteById(Long id) {
        // Vérifier qu'il n'y a pas d'employés dans ce département
        long nbEmployes = employerRepository.findByDepartementId(id).size();

        if (nbEmployes > 0) {
            throw new IllegalStateException(
                "Impossible de supprimer ce département : " + nbEmployes + " employé(s) y sont affecté(s)."
            );
        }

        departementRepository.deleteById(id);
    }

    /**
     * Récupère les départements d'une entreprise
     */
    public List<Departement> findByEntrepriseId(Long entrepriseId) {
        return departementRepository.findByEntrepriseId(entrepriseId);
    }
}