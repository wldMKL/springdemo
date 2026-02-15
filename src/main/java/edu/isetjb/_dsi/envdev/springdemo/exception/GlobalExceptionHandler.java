package edu.isetjb._dsi.envdev.springdemo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Gestionnaire global des exceptions pour l'application
 * Intercepte les exceptions non gérées et fournit une réponse appropriée
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les IllegalArgumentException (erreurs de validation métier)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e,
                                       RedirectAttributes redirectAttributes,
                                       HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        
        // Rediriger vers la page précédente ou dashboard par défaut
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/dashboard";
    }

    /**
     * Gère les IllegalStateException (erreurs d'état)
     */
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalState(IllegalStateException e,
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/dashboard";
    }

    /**
     * Gère les RuntimeException génériques
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e,
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", 
            "Une erreur s'est produite : " + e.getMessage());
        
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/dashboard";
    }

    /**
     * Gère toutes les autres exceptions non prévues
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception e, Model model) {
        model.addAttribute("errorMessage", 
            "Une erreur inattendue s'est produite. Veuillez réessayer ou contacter l'administrateur.");
        model.addAttribute("errorDetails", e.getMessage());
        return "error/500";
    }
}