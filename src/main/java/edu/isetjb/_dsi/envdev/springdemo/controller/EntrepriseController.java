package edu.isetjb._dsi.envdev.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.isetjb._dsi.envdev.springdemo.model.Entreprise;
import edu.isetjb._dsi.envdev.springdemo.service.EntrepriseService;

@Controller
@RequestMapping("/entreprise")
public class EntrepriseController {

    private final EntrepriseService entrepriseService;

    public EntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @GetMapping("/edit")
    public String editEntreprise(Model model) {
        Entreprise entreprise = entrepriseService.getEntreprisePrincipale();
        model.addAttribute("entreprise", entreprise);
        return "entreprise/form";
    }

    @PostMapping("/save")
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
}
