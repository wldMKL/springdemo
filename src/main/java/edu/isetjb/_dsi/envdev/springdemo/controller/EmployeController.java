package edu.isetjb._dsi.envdev.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import edu.isetjb._dsi.envdev.springdemo.model.Employe;
import edu.isetjb._dsi.envdev.springdemo.repository.EmployeRepository;

@Controller
public class EmployeController {

    private final EmployeRepository employeRepository;

    public EmployeController(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    // ── Connexion ─────────────────────────────────────────────────────────────
    @GetMapping("/login")
    public String pageConnexion(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        return "login";
    }

    // ── Liste ─────────────────────────────────────────────────────────────────
    @GetMapping("/index")
    public String listerEmployes(Model model) {
        model.addAttribute("listEmployes", employeRepository.findAll());
        return "list-employes";
    }

    // ── Formulaire ajout ──────────────────────────────────────────────────────
    @GetMapping("/add")
    public String formulaireAjout(Model model) {
        if (!model.containsAttribute("employe")) {
            model.addAttribute("employe", new Employe());
        }
        return "form-employe";
    }

    // ── Enregistrement ────────────────────────────────────────────────────────
    @PostMapping("/save")
    public String enregistrerEmploye(@ModelAttribute Employe employe,
                                     RedirectAttributes redirectAttributes) {
        if (employe.getNom() == null || employe.getNom().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le nom ne peut pas être vide.");
            redirectAttributes.addFlashAttribute("employe", employe);
            return "redirect:/add";
        }
        employeRepository.save(employe);
        redirectAttributes.addFlashAttribute("successMessage",
                "L'employé « " + employe.getNom() + " » a été ajouté avec succès.");
        return "redirect:/index";
    }

    // ── Formulaire modification ───────────────────────────────────────────────
    // ✅ Pas de lambda — if/else classique, pas de problème d'injection
    @GetMapping("/edit")
    public String formulaireModification(@RequestParam("id") Long id,
                                         Model model) {
        Optional<Employe> optional = employeRepository.findById(id);

        if (optional.isPresent()) {
            model.addAttribute("employe", optional.get());
            return "edit-employe";
        } else {
            model.addAttribute("errorMessage", "Employé introuvable (id=" + id + ").");
            model.addAttribute("listEmployes", employeRepository.findAll());
            return "list-employes";
        }
    }

    // ── Mise à jour ───────────────────────────────────────────────────────────
    @PostMapping("/update")
    public String mettreAJourEmploye(@ModelAttribute Employe employe,
                                     RedirectAttributes redirectAttributes) {
        if (employe.getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employé invalide.");
            return "redirect:/index";
        }
        if (employe.getNom() == null || employe.getNom().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le nom ne peut pas être vide.");
            return "redirect:/edit?id=" + employe.getId();
        }
        employeRepository.save(employe);
        redirectAttributes.addFlashAttribute("successMessage",
                "L'employé « " + employe.getNom() + " » a été mis à jour avec succès.");
        return "redirect:/index";
    }

    // ── Suppression ───────────────────────────────────────────────────────────
    // ✅ if/else classique — RedirectAttributes fonctionne correctement
    @GetMapping("/delete")
    public String supprimerEmploye(@RequestParam("id") Long id,
                                   RedirectAttributes redirectAttributes) {
        Optional<Employe> optional = employeRepository.findById(id);

        if (optional.isPresent()) {
            String nom = optional.get().getNom();
            employeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "L'employé « " + nom + " » a été supprimé avec succès.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Employé introuvable (id=" + id + ").");
        }

        return "redirect:/index";
    }
}