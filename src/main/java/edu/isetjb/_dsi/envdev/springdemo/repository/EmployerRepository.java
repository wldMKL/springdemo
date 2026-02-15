package edu.isetjb._dsi.envdev.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.isetjb._dsi.envdev.springdemo.model.Employer;

import java.util.List;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {

    // ✅ Utilisée dans GestionController pour vérifier avant suppression d'un département
    List<Employer> findByDepartementId(Long departementId);

    // Recherche par entreprise
    List<Employer> findByEntrepriseId(Long entrepriseId);
}