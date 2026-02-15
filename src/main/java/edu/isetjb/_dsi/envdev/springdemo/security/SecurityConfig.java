package edu.isetjb._dsi.envdev.springdemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité Spring Security
 * - Authentification en mémoire avec 2 utilisateurs (admin et employe)
 * - Autorisation basée sur les rôles (ADMIN, EMPLOYE)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails employe = User.builder()
                .username("employe")
                .password(encoder.encode("1234"))
                .roles("EMPLOYE")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("1234"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(employe, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Ressources publiques
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                // URLs d'administration (modification/suppression)
                .requestMatchers(
                    "/entreprise/**",
                    "/departements/add", "/departements/save",
                    "/departements/edit", "/departements/delete",
                    "/employers/add", "/employers/save",
                    "/employers/edit", "/employers/update", "/employers/delete"
                ).hasRole("ADMIN")

                // URLs accessibles à tous les authentifiés (consultation)
                .requestMatchers(
                    "/dashboard",
                    "/departements",
                    "/employers", "/employers/view"
                ).authenticated()

                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)  // ✅ Redirection cohérente vers dashboard
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
}