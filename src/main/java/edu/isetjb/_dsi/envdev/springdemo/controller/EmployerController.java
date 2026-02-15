package edu.isetjb._dsi.envdev.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.isetjb._dsi.envdev.springdemo.model.Employer;
import edu.isetjb._dsi.envdev.springdemo.service.DepartementService;
import edu.isetjb._dsi.envdev.springdemo.service.EmployerService;

@Controller
@RequestMapping("/employers")
public class EmployerController {

    private final EmployerService employerService;
    private final DepartementService departementService;

    public EmployerController(EmployerService employerService, DepartementService departementService) {
        this.employerService = employerService;
        this.departementService = departementService;
    }

    @GetMapping
    public String listerEmployers(Model model) {
        model.addAttribute("employers", employerService.findAll());
        model.addAttribute("departements", departementService.findAll());
        return "employers/list";
    }

    @GetMapping("/add")
    public String ajouterEmployer(Model model) {
        model.addAttribute("employer", new Employer());
        model.addAttribute("departements", departementService.findAll());
        return "employers/form";
    }

    @GetMapping("/edit")
    public String editerEmployer(@RequestParam Long id,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            Employer employer = employerService.findByIdOrThrow(id);
            model.addAttribute("employer", employer);
            model.addAttribute("departements", departementService.findAll());
            return "employers/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employers";
        }
    }

    @PostMapping("/save")
    public String sauvegarderEmployer(@ModelAttribute Employer employer,
            @RequestParam(required = false) Long departementId,
            RedirectAttributes redirectAttributes) {
        // ✅ Vérification que departementId n'est pas null avant traitement
        if (departementId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Veuillez sélectionner un département.");
            redirectAttributes.addFlashAttribute("departements", departementService.findAll());
            return "redirect:/employers/add";
        }
        try {
            employerService.save(employer, departementId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Employé « " + employer.getPrenom() + " " + employer.getNom() + " » enregistré avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employers/add";
        }
        return "redirect:/employers";
    }

    @PostMapping("/update")
    public String mettreAJourEmployer(@ModelAttribute Employer employer,
            @RequestParam(required = false) Long departementId,
            RedirectAttributes redirectAttributes) {
        // ✅ Vérification null sur l'ID de l'employé
        if (employer.getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "ID de l'employé manquant.");
            return "redirect:/employers";
        }

        // ✅ Vérification que departementId n'est pas null
        if (departementId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Veuillez sélectionner un département.");
            return "redirect:/employers/edit?id=" + employer.getId();
        }

        try {
            employerService.update(employer.getId(), employer, departementId);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Employé « " + employer.getPrenom() + " " + employer.getNom() + " » mis à jour avec succès.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employers/edit?id=" + employer.getId();
        }
        return "redirect:/employers";
    }

    @GetMapping("/delete")
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

    @GetMapping("/view")
    public String voirEmployer(@RequestParam Long id,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            Employer employer = employerService.findByIdOrThrow(id);
            model.addAttribute("employer", employer);
            return "employers/view";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employers";
        }
    }
}
