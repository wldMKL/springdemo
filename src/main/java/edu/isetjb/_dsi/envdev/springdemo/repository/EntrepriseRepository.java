package edu.isetjb._dsi.envdev.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.isetjb._dsi.envdev.springdemo.model.Entreprise;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    // Spring Data JPA fournit automatiquement findById, save, count, findAll...
}