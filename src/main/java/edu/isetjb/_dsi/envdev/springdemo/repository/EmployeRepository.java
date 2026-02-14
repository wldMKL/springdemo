package edu.isetjb._dsi.envdev.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.isetjb._dsi.envdev.springdemo.model.Employe;

@Repository // Annotation adéquate
public interface EmployeRepository extends JpaRepository<Employe, Integer> {
    // Spring Boot implémente automatiquement les méthodes CRUD (save, findAll, findById, delete...)
}