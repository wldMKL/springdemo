package edu.isetjb._dsi.envdev.springdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.isetjb._dsi.envdev.springdemo.model.Entreprise;
import edu.isetjb._dsi.envdev.springdemo.service.DepartementService;
import edu.isetjb._dsi.envdev.springdemo.service.EmployerService;
import edu.isetjb._dsi.envdev.springdemo.service.EntrepriseService;

@Controller
public class HomeController {

    private final EntrepriseService entrepriseService;
    private final DepartementService departementService;
    private final EmployerService employerService;

    public HomeController(EntrepriseService entrepriseService,
            DepartementService departementService,
            EmployerService employerService) {
        this.entrepriseService = entrepriseService;
        this.departementService = departementService;
        this.employerService = employerService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Entreprise entreprise = entrepriseService.getEntreprisePrincipale();

        model.addAttribute("entreprise", entreprise);
        model.addAttribute("nbDepartements", departementService.count());
        model.addAttribute("nbEmployers", employerService.count());
        model.addAttribute("departements", departementService.findAll());

        return "dashboard";
    }

    // ✅ Ajout de la route login pour corriger l'erreur 500
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
