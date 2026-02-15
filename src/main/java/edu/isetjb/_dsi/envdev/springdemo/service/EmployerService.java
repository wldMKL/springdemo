package edu.isetjb._dsi.envdev.springdemo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.isetjb._dsi.envdev.springdemo.model.Departement;
import edu.isetjb._dsi.envdev.springdemo.model.Employer;
import edu.isetjb._dsi.envdev.springdemo.model.Entreprise;
import edu.isetjb._dsi.envdev.springdemo.repository.EmployerRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des employés
 */
@Service
@Transactional
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final EntrepriseService entrepriseService;
    private final DepartementService departementService;

    public EmployerService(EmployerRepository employerRepository,
                          EntrepriseService entrepriseService,
                          DepartementService departementService) {
        this.employerRepository = employerRepository;
        this.entrepriseService = entrepriseService;
        this.departementService = departementService;
    }

    /**
     * Récupère tous les employés
     */
    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    /**
     * Récupère un employé par son ID
     */
    public Optional<Employer> findById(Long id) {
        return employerRepository.findById(id);
    }

    /**
     * Récupère un employé par son ID ou lève une exception
     */
    public Employer findByIdOrThrow(Long id) {
        return employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé introuvable (ID=" + id + ")"));
    }

    /**
     * Compte le nombre d'employés
     */
    public long count() {
        return employerRepository.count();
    }

    /**
     * Sauvegarde ou met à jour un employé
     */
    public Employer save(Employer employer, Long departementId) {
        // Validations
        if (employer.getMatricule() == null || employer.getMatricule().isBlank()) {
            throw new IllegalArgumentException("Le matricule est obligatoire");
        }
        if (employer.getNom() == null || employer.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (employer.getPrenom() == null || employer.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }

        // Récupérer l'entreprise et le département
        Entreprise entreprise = entrepriseService.getEntreprisePrincipale();
        Departement departement = departementService.findByIdOrThrow(departementId);

        // Affecter les relations
        employer.setEntreprise(entreprise);
        employer.setDepartement(departement);

        return employerRepository.save(employer);
    }

    /**
     * Met à jour un employé existant
     */
    public Employer update(Long id, Employer employer, Long departementId) {
        // Vérifier que l'employé existe
        Employer existing = findByIdOrThrow(id);

        // Mettre à jour les champs
        existing.setMatricule(employer.getMatricule());
        existing.setNom(employer.getNom());
        existing.setPrenom(employer.getPrenom());
        existing.setEmail(employer.getEmail());
        existing.setTelephone(employer.getTelephone());
        existing.setFonction(employer.getFonction());
        existing.setSalaire(employer.getSalaire());

        // Mettre à jour le département si fourni
        if (departementId != null) {
            Departement departement = departementService.findByIdOrThrow(departementId);
            existing.setDepartement(departement);
        }

        return employerRepository.save(existing);
    }

    /**
     * Supprime un employé par son ID
     */
    public void deleteById(Long id) {
        if (!employerRepository.existsById(id)) {
            throw new RuntimeException("Employé introuvable (ID=" + id + ")");
        }
        employerRepository.deleteById(id);
    }

    /**
     * Récupère les employés d'un département
     */
    public List<Employer> findByDepartementId(Long departementId) {
        return employerRepository.findByDepartementId(departementId);
    }

    /**
     * Récupère les employés d'une entreprise
     */
    public List<Employer> findByEntrepriseId(Long entrepriseId) {
        return employerRepository.findByEntrepriseId(entrepriseId);
    }
}