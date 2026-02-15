package edu.isetjb._dsi.envdev.springdemo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.isetjb._dsi.envdev.springdemo.model.Entreprise;
import edu.isetjb._dsi.envdev.springdemo.repository.EntrepriseRepository;

import java.util.Optional;

/**
 * Service pour la gestion de l'entreprise
 */
@Service
@Transactional
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    public EntrepriseService(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    /**
     * Récupère l'entreprise principale (ID = 1)
     * Si elle n'existe pas, en crée une par défaut
     */
    public Entreprise getEntreprisePrincipale() {
        return entrepriseRepository.findById(1L)
                .orElseGet(() -> {
                    Entreprise entreprise = new Entreprise(
                        "Mon Entreprise", 
                        "Adresse non renseignée", 
                        "", 
                        ""
                    );
                    entreprise.setId(1L);
                    return entrepriseRepository.save(entreprise);
                });
    }

    /**
     * Met à jour les informations de l'entreprise principale
     */
    public Entreprise updateEntreprise(Entreprise entreprise) {
        // Validation
        if (entreprise.getNom() == null || entreprise.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'entreprise est obligatoire");
        }

        // S'assurer que c'est toujours l'ID 1
        entreprise.setId(1L);
        return entrepriseRepository.save(entreprise);
    }

    /**
     * Compte le nombre d'entreprises (devrait toujours être 1)
     */
    public long count() {
        return entrepriseRepository.count();
    }
}