package edu.isetjb._dsi.envdev.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.isetjb._dsi.envdev.springdemo.model.Departement;
import edu.isetjb._dsi.envdev.springdemo.service.DepartementService;

@Controller
@RequestMapping("/departements")
public class DepartementController {

    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @GetMapping
    public String listerDepartements(Model model) {
        model.addAttribute("departements", departementService.findAll());
        return "departements/list";
    }

    @GetMapping("/add")
    public String ajouterDepartement(Model model) {
        model.addAttribute("departement", new Departement());
        return "departements/form";
    }

    @GetMapping("/edit")
    public String editerDepartement(@RequestParam Long id,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            Departement departement = departementService.findByIdOrThrow(id);
            model.addAttribute("departement", departement);
            return "departements/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/departements";
        }
    }

    @PostMapping("/save")
    public String sauvegarderDepartement(@ModelAttribute Departement departement,
            RedirectAttributes redirectAttributes) {
        try {
            departementService.save(departement);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Département « " + departement.getNom() + " » enregistré avec succès.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            if (departement.getId() != null) {
                return "redirect:/departements/edit?id=" + departement.getId();
            }
            return "redirect:/departements/add";
        }
        return "redirect:/departements";
    }

    @GetMapping("/delete")
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
                    "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/departements";
    }
}
