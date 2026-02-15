package edu.isetjb._dsi.envdev.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.isetjb._dsi.envdev.springdemo.model.*;
import edu.isetjb._dsi.envdev.springdemo.service.*;

/**
 * Contrôleur principal pour la gestion de l'entreprise, des départements et des employés
 */
@Controller
public class GestionController {

    private final EntrepriseService entrepriseService;
    private final DepartementService departementService;
    private final EmployerService employerService;

    public GestionController(EntrepriseService entrepriseService,
                            DepartementService departementService,
                            EmployerService employerService) {
        this.entrepriseService = entrepriseService;
        this.departementService = departementService;
        this.employerService = employerService;
    }

    // ══════════════════════════════════════════════════════════════
    // DASHBOARD PRINCIPAL
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Entreprise entreprise = entrepriseService.getEntreprisePrincipale();

        model.addAttribute("entreprise", entreprise);
        model.addAttribute("nbDepartements", departementService.count());
        model.addAttribute("nbEmployers", employerService.count());
        model.addAttribute("departements", departementService.findAll());

        return "dashboard";
    }

    // ══════════════════════════════════════════════════════════════
    // GESTION DES INFORMATIONS DE L'ENTREPRISE
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/entreprise/edit")
    public String editEntreprise(Model model) {
        Entreprise entreprise = entrepriseService.getEntreprisePrincipale();
        model.addAttribute("entreprise", entreprise);
        return "entreprise/form";
    }

    @PostMapping("/entreprise/save")
    public String saveEntreprise(@ModelAttribute Entreprise entreprise,
                                 RedirectAttributes redirectAttributes) {
        try {
            entrepriseService.updateEntreprise(entreprise);
            redirectAttributes.addFlashAttribute("successMessage",
                "Informations de l'entreprise mises à jour avec succès.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/entreprise/edit";
        }
        return "redirect:/dashboard";
    }

    // ══════════════════════════════════════════════════════════════
    // DÉPARTEMENTS
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/departements")
    public String listerDepartements(Model model) {
        model.addAttribute("departements", departementService.findAll());
        return "departements/list";
    }

    @GetMapping("/departements/add")
    public String ajouterDepartement(Model model) {
        model.addAttribute("departement", new Departement());
        return "departements/form";
    }

    @GetMapping("/departements/edit")
    public String editerDepartement(@RequestParam Long id,
                                    // ✅ FIX 1 : RedirectAttributes au lieu de Model pour survivre au redirect
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        try {
            Departement departement = departementService.findByIdOrThrow(id);
            model.addAttribute("departement", departement);
            return "departements/form";
        } catch (RuntimeException e) {
            // ✅ FIX 1 : addFlashAttribute → le message sera visible après le redirect
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/departements";
        }
    }

    @PostMapping("/departements/save")
    public String sauvegarderDepartement(@ModelAttribute Departement departement,
                                         RedirectAttributes redirectAttributes) {
        try {
            departementService.save(departement);
            redirectAttributes.addFlashAttribute("successMessage",
                "Département « " + departement.getNom() + " » enregistré avec succès.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            // ✅ FIX 2 : Si c'est une modification (id présent), rediriger vers /edit, sinon /add
            if (departement.getId() != null) {
                return "redirect:/departements/edit?id=" + departement.getId();
            }
            return "redirect:/departements/add";
        }
        return "redirect:/departements";
    }

    @GetMapping("/departements/delete")
    public String supprimerDepartement(@RequestParam Long id,
                                       RedirectAttributes redirectAttributes) {
        try {
            departementService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                "Département supprimé avec succès.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "Erreur lors de la suppression du département.");
        }
        return "redirect:/departements";
    }

    // ══════════════════════════════════════════════════════════════
    // EMPLOYÉS
    // ══════════════════════════════════════════════════════════════

    @GetMapping("/employers")
    public String listerEmployers(Model model) {
        model.addAttribute("employers", employerService.findAll());
        model.addAttribute("departements", departementService.findAll());
        return "employers/list";
    }

    @GetMapping("/employers/add")
    public String ajouterEmployer(Model model) {
        model.addAttribute("employer", new Employer());
        model.addAttribute("departements", departementService.findAll());
        return "employers/form";
    }

    @GetMapping("/employers/edit")
    public String editerEmployer(@RequestParam Long id,
                                 // ✅ FIX 3 : RedirectAttributes au lieu de Model pour survivre au redirect
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            Employer employer = employerService.findByIdOrThrow(id);
            model.addAttribute("employer", employer);
            model.addAttribute("departements", departementService.findAll());
            return "employers/form";
        } catch (RuntimeException e) {
            // ✅ FIX 3 : addFlashAttribute → le message sera visible après le redirect
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employers";
        }
    }

    @PostMapping("/employers/save")
    public String sauvegarderEmployer(@ModelAttribute Employer employer,
                                      @RequestParam Long departementId,
                                      RedirectAttributes redirectAttributes) {
        try {
            employerService.save(employer, departementId);
            redirectAttributes.addFlashAttribute("successMessage",
                "Employé « " + employer.getPrenom() + " " + employer.getNom() + " » enregistré avec succès.");
        } catch (IllegalArgumentException | RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employers/add";
        }
        return "redirect:/employers";
    }

    @PostMapping("/employers/update")
    public String mettreAJourEmployer(@ModelAttribute Employer employer,
                                      @RequestParam Long departementId,
                                      RedirectAttributes redirectAttributes) {
        try {
            employerService.update(employer.getId(), employer, departementId);
            redirectAttributes.addFlashAttribute("successMessage",
                "Employé « " + employer.getPrenom() + " " + employer.getNom() + " » mis à jour avec succès.");
        } catch (IllegalArgumentException | RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            // ✅ FIX 4 : Vérification null avant d'utiliser employer.getId() pour éviter le NPE
            if (employer.getId() != null) {
                return "redirect:/employers/edit?id=" + employer.getId();
            }
            return "redirect:/employers";
        }
        return "redirect:/employers";
    }

    @GetMapping("/employers/delete")
    public String supprimerEmployer(@RequestParam Long id,
                                    RedirectAttributes redirectAttributes) {
        try {
            Employer employer = employerService.findByIdOrThrow(id);
            String nom = employer.getPrenom() + " " + employer.getNom();

            employerService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                "Employé « " + nom + " » supprimé avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/employers";
    }

    @GetMapping("/employers/view")
    public String voirEmployer(@RequestParam Long id,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            Employer employer = employerService.findByIdOrThrow(id);
            model.addAttribute("employer", employer);
            return "employers/view";
        } catch (RuntimeException e) {
            // ✅ Cohérence : même pattern que les autres méthodes avec redirect
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employers";
        }
    }
}