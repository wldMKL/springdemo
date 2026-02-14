package edu.isetjb._dsi.envdev.springdemo.controller;

// 1. TOUS LES IMPORTS NÉCESSAIRES
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.isetjb._dsi.envdev.springdemo.model.Employe;
import edu.isetjb._dsi.envdev.springdemo.repository.EmployeRepository;

import java.util.Optional;

@Controller
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    // ── 1. Page de Connexion ───────────────────────────────────────────
    @GetMapping("/login")
    public String pageConnexion() {
        return "login"; // Pointe vers login.html
    }

    // ── 2. Affichage de la liste (index) ───────────────────────────────
    @GetMapping("/index")
    public String listerEmployes(Model model) {
        model.addAttribute("listEmployes", employeRepository.findAll());
        return "list-employes"; // Pointe vers list-employes.html
    }

    // ── 3. Formulaire d'ajout (vide) ───────────────────────────────────
    @GetMapping("/add")
    public String formulaireAjout(Model model) {
        // On crée un employé vide. Par défaut, un int vaut 0.
        // Cela permet à notre HTML de savoir qu'il s'agit d'un nouvel ajout.
        model.addAttribute("employe", new Employe());
        return "form-employe"; // Réutilise le même formulaire pour ajout et modif
    }

    // ── 4. Enregistrement (Sauvegarde ou Mise à jour) ──────────────────
    @PostMapping("/save")
    public String enregistrerEmploye(@ModelAttribute Employe employe, RedirectAttributes redirectAttributes) {
        // Petite validation de sécurité
        if (employe.getNom() == null || employe.getNom().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le nom est obligatoire.");
            return "redirect:/add"; // Retourne au formulaire si erreur
        }

        // JPA est malin : si l'employé a un matricule existant, il fait un UPDATE.
        // Sinon, il fait un INSERT.
        employeRepository.save(employe);

        redirectAttributes.addFlashAttribute("successMessage", "Employé enregistré avec succès !");
        return "redirect:/index";
    }

    // ── 5. Formulaire de modification (Pré-rempli) ─────────────────────
    // Notez l'utilisation de @PathVariable pour correspondre à /edit/{id}
    @GetMapping("/edit/{id}")
    public String formulaireModification(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        // On cherche l'employé par son matricule (Integer car JPA utilise des objets)
        Optional<Employe> employe = employeRepository.findById(id);

        if (employe.isPresent()) {
            // S'il existe, on l'envoie au formulaire
            model.addAttribute("employe", employe.get());
            return "form-employe";
        } else {
            // S'il n'existe pas, on redirige avec une erreur
            redirectAttributes.addFlashAttribute("errorMessage", "Employé introuvable.");
            return "redirect:/index";
        }
    }

    // ── 6. Suppression ─────────────────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String supprimerEmploye(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        // On vérifie d'abord s'il existe pour éviter une erreur
        if (employeRepository.existsById(id)) {
            employeRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employé supprimé.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer : Employé introuvable.");
        }
        return "redirect:/index";
    }
}