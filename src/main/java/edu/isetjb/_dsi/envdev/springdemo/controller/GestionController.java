package edu.isetjb._dsi.envdev.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.isetjb._dsi.envdev.springdemo.model.*;
import edu.isetjb._dsi.envdev.springdemo.repository.*;

@Controller
public class GestionController {

    private final EntrepriseRepository entrepriseRepository;
    private final DepartementRepository departementRepository;
    private final EmployerRepository employerRepository;

    public GestionController(EntrepriseRepository entrepriseRepository,
                            DepartementRepository departementRepository,
                            EmployerRepository employerRepository) {
        this.entrepriseRepository = entrepriseRepository;
        this.departementRepository = departementRepository;
        this.employerRepository = employerRepository;
    }

    // ══════════════════════════════════════════════════════════════
    // DASHBOARD PRINCIPAL
    // ══════════════════════════════════════════════════════════════
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Récupérer l'entreprise (toujours la première, ID = 1)
        Entreprise entreprise = entrepriseRepository.findById(1L)
                .orElse(new Entreprise("Mon Entreprise", "", "", ""));
        
        model.addAttribute("entreprise", entreprise);
        model.addAttribute("nbDepartements", departementRepository.count());
        model.addAttribute("nbEmployers", employerRepository.count());
        model.addAttribute("departements", departementRepository.findAll());
        
        return "dashboard";
    }

    // ══════════════════════════════════════════════════════════════
    // GESTION DES INFORMATIONS DE L'ENTREPRISE
    // ══════════════════════════════════════════════════════════════
    
    @GetMapping("/entreprise/edit")
    public String editEntreprise(Model model) {
        Entreprise entreprise = entrepriseRepository.findById(1L)
                .orElse(new Entreprise());
        model.addAttribute("entreprise", entreprise);
        return "entreprise/form";
    }
    
    @PostMapping("/entreprise/save")
    public String saveEntreprise(@ModelAttribute Entreprise entreprise,
                                 RedirectAttributes redirectAttributes) {
        // S'assurer que c'est toujours l'ID 1
        entreprise.setId(1L);
        entrepriseRepository.save(entreprise);
        redirectAttributes.addFlashAttribute("successMessage", 
            "Informations de l'entreprise mises à jour avec succès.");
        return "redirect:/dashboard";
    }

    // ══════════════════════════════════════════════════════════════
    // DÉPARTEMENTS
    // ══════════════════════════════════════════════════════════════
    
    @GetMapping("/departements")
    public String listerDepartements(Model model) {
        model.addAttribute("departements", departementRepository.findAll());
        return "departements/list";
    }
    
    @GetMapping("/departements/add")
    public String ajouterDepartement(Model model) {
        model.addAttribute("departement", new Departement());
        return "departements/form";
    }
    
    @GetMapping("/departements/edit")
    public String editerDepartement(@RequestParam Long id, Model model) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));
        model.addAttribute("departement", departement);
        return "departements/form";
    }
    
    @PostMapping("/departements/save")
    public String sauvegarderDepartement(@ModelAttribute Departement departement,
                                        RedirectAttributes redirectAttributes) {
        // Toujours lier à l'entreprise principale (ID = 1)
        Entreprise entreprise = entrepriseRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Entreprise introuvable"));
        
        departement.setEntreprise(entreprise);
        departementRepository.save(departement);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Département « " + departement.getNom() + " » enregistré avec succès.");
        return "redirect:/departements";
    }
    
    @GetMapping("/departements/delete")
    public String supprimerDepartement(@RequestParam Long id,
                                      RedirectAttributes redirectAttributes) {
        // Vérifier qu'il n'y a pas d'employés dans ce département
        long nbEmployes = employerRepository.findByDepartementId(id).size();
        
        if (nbEmployes > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Impossible de supprimer ce département : " + nbEmployes + " employé(s) y sont affecté(s).");
        } else {
            departementRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Département supprimé avec succès.");
        }
        
        return "redirect:/departements";
    }

    // ══════════════════════════════════════════════════════════════
    // EMPLOYÉS
    // ══════════════════════════════════════════════════════════════
    
    @GetMapping("/employers")
    public String listerEmployers(Model model) {
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("departements", departementRepository.findAll());
        return "employers/list";
    }
    
    @GetMapping("/employers/add")
    public String ajouterEmployer(Model model) {
        model.addAttribute("employer", new Employer());
        model.addAttribute("departements", departementRepository.findAll());
        return "employers/form";
    }
    
    @GetMapping("/employers/edit")
    public String editerEmployer(@RequestParam Long id, Model model) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));
        model.addAttribute("employer", employer);
        model.addAttribute("departements", departementRepository.findAll());
        return "employers/form";
    }
    
    @PostMapping("/employers/save")
    public String sauvegarderEmployer(@ModelAttribute Employer employer,
                                      @RequestParam Long departementId,
                                      RedirectAttributes redirectAttributes) {
        // Toujours lier à l'entreprise principale (ID = 1)
        Entreprise entreprise = entrepriseRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Entreprise introuvable"));
        
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));
        
        employer.setEntreprise(entreprise);
        employer.setDepartement(departement);
        employerRepository.save(employer);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Employé « " + employer.getPrenom() + " " + employer.getNom() + " » enregistré avec succès.");
        return "redirect:/employers";
    }
    
    @GetMapping("/employers/delete")
    public String supprimerEmployer(@RequestParam Long id,
                                   RedirectAttributes redirectAttributes) {
        Employer employer = employerRepository.findById(id).orElse(null);
        
        if (employer != null) {
            String nom = employer.getPrenom() + " " + employer.getNom();
            employerRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Employé « " + nom + " » supprimé avec succès.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Employé introuvable.");
        }
        
        return "redirect:/employers";
    }
    
    // Voir les détails d'un employé
    @GetMapping("/employers/view")
    public String voirEmployer(@RequestParam Long id, Model model) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));
        model.addAttribute("employer", employer);
        return "employers/view";
    }
}