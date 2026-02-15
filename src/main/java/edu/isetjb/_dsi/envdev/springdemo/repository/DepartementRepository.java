package edu.isetjb._dsi.envdev.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.isetjb._dsi.envdev.springdemo.model.Departement;

import java.util.List;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {

    // Recherche par entreprise
    List<Departement> findByEntrepriseId(Long entrepriseId);
}