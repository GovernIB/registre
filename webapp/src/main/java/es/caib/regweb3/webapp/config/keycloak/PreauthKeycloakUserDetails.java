package es.caib.regweb3.webapp.config.keycloak;

import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PreauthKeycloakUserDetails extends User {

    private KeycloakPrincipal<?> keycloakPrincipal;

    public PreauthKeycloakUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities, KeycloakPrincipal<?> keycloakPrincipal) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.keycloakPrincipal = keycloakPrincipal;
    }

    public KeycloakPrincipal<?> getKeycloakPrincipal() {
        return keycloakPrincipal;
    }

    public String getGivenName() {
        if (keycloakPrincipal != null) {
            return ((KeycloakPrincipal<?>)keycloakPrincipal).getKeycloakSecurityContext().getToken().getGivenName();
        } else {
            return null;
        }
    }

    public String getFamilyName() {
        if (keycloakPrincipal != null) {
            return ((KeycloakPrincipal<?>)keycloakPrincipal).getKeycloakSecurityContext().getToken().getFamilyName();
        } else {
            return null;
        }
    }

    public String getFullName() {
        if (keycloakPrincipal != null) {
            return ((KeycloakPrincipal<?>)keycloakPrincipal).getKeycloakSecurityContext().getToken().getName();
        } else {
            return null;
        }
    }

    public String getEmail() {
        if (keycloakPrincipal != null) {
            return ((KeycloakPrincipal<?>)keycloakPrincipal).getKeycloakSecurityContext().getToken().getEmail();
        } else {
            return null;
        }
    }
}
