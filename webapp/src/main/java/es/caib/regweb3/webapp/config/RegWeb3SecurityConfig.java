package es.caib.regweb3.webapp.config;

import es.caib.regweb3.webapp.config.keycloak.PreauthKeycloakUserDetails;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAttributes2GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleMappableAttributesRetriever;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.j2ee.J2eePreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static es.caib.regweb3.utils.RegwebConstantes.*;


//@Configuration
@EnableWebSecurity
public class RegWeb3SecurityConfig extends WebSecurityConfigurerAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String ROLE_PREFIX = "";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(preauthAuthProvider()).
                jee().j2eePreAuthenticatedProcessingFilter(preAuthenticatedProcessingFilter());
        http.logout().
                addLogoutHandler(getLogoutHandler()).
                logoutRequestMatcher(new AntPathRequestMatcher("/logout")).
                invalidateHttpSession(true).
                logoutSuccessUrl("/").
                permitAll(false);
        http.authorizeRequests().
                antMatchers("/js/**").permitAll().
                antMatchers("/img/**").permitAll().
                antMatchers("/css/**").permitAll().
                antMatchers("/error.jsp").permitAll().
                antMatchers("/anexo/guardarScan/**").permitAll().
                antMatchers("/anexo/scanwebresource/**").permitAll().
                antMatchers("/public/versio").permitAll().
                /* ----- RWE_SUPERADMIN ----- */
                antMatchers("/entidad/new").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/entidad/list").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/entidad/**/eliminar").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/entidad/**/anular").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/entidad/**/activar").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/usuario/list").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/usuario/**/edit").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/usuario/**/delete").hasAuthority(RWE_SUPERADMIN).
                antMatchers("/dir3/**").hasAuthority(RWE_SUPERADMIN).
                /* ----- RWE_USUARI ----- */
                antMatchers("/usuarioEntidad/**/edit").hasAuthority(RWE_USUARI).
                /* ----- RWE_ADMIN ----- */
                antMatchers("/entidad/permisos/**").hasAuthority(RWE_ADMIN).
                antMatchers("/entidad/procesarPendientes").hasAuthority(RWE_ADMIN).
                antMatchers("/usuarioEntidad/**").hasAuthority(RWE_ADMIN).
                antMatchers("/entidad/**/sincronizar").hasAuthority(RWE_ADMIN).
                antMatchers("/entidad/**/actualizar").hasAuthority(RWE_ADMIN).
                antMatchers("/entidad/procesarlibroorganismo/**").hasAuthority(RWE_ADMIN).
                antMatchers("/tipoAsunto/**").hasAuthority(RWE_ADMIN).
                antMatchers("/usuario/existeUsuario").hasAuthority(RWE_ADMIN).
                antMatchers("/organismo/**").hasAuthority(RWE_ADMIN).
                antMatchers("/tipoDocumental/**").hasAuthority(RWE_ADMIN).
                antMatchers("/modeloOficioRemision/**").hasAuthority(RWE_ADMIN).
                antMatchers("/libro/**").hasAuthority(RWE_ADMIN).
                antMatchers("/sir/**").hasAuthority(RWE_ADMIN).
                antMatchers("/cola/**").hasAuthority(RWE_ADMIN).
                antMatchers("/pendiente/**").hasAuthority(RWE_ADMIN).
                /* ----- RWE_SUPERADMIN y RWE_ADMIN ----- */
                antMatchers("/entidad/**/edit").hasAnyAuthority(RWE_SUPERADMIN,RWE_ADMIN).
                antMatchers("/usuario/new").hasAnyAuthority(RWE_SUPERADMIN,RWE_ADMIN).
                antMatchers("/propiedadGlobal/**").hasAnyAuthority(RWE_SUPERADMIN,RWE_ADMIN).
                antMatchers("/plugin/**").hasAnyAuthority(RWE_SUPERADMIN,RWE_ADMIN).
                antMatchers("/configuracion/**").hasAnyAuthority(RWE_SUPERADMIN,RWE_ADMIN).
                /* ----- RWE_SUPERADMIN, RWE_ADMIN y RWE_USUARI ----- */
                antMatchers("/informe/**").hasAnyAuthority(RWE_SUPERADMIN,RWE_ADMIN,RWE_USUARI).
                antMatchers("/persona/**").hasAnyAuthority(RWE_SUPERADMIN,RWE_ADMIN,RWE_USUARI).
                /* ----- RWE_USUARI ----- */
                antMatchers("/registroEntrada/**").hasAuthority(RWE_USUARI).
                antMatchers("/registroSalida/**").hasAuthority(RWE_USUARI).
                antMatchers("/oficioRemision/**").hasAuthority(RWE_USUARI).
                antMatchers("/plantilla/**").hasAuthority(RWE_USUARI).
                anyRequest().authenticated();
        http.cors();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
        PreAuthenticatedAuthenticationProvider preauthAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preauthAuthProvider.setPreAuthenticatedUserDetailsService(preAuthenticatedGrantedAuthoritiesUserDetailsService());

        return preauthAuthProvider;
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(ROLE_PREFIX);
    }

    @Bean
    public PreAuthenticatedGrantedAuthoritiesUserDetailsService preAuthenticatedGrantedAuthoritiesUserDetailsService() {

        return new PreAuthenticatedGrantedAuthoritiesUserDetailsService() {
            protected UserDetails createUserDetails(Authentication token, Collection<? extends GrantedAuthority> authorities) {

                if (token.getDetails() instanceof KeycloakWebAuthenticationDetails) {
                    KeycloakWebAuthenticationDetails keycloakWebAuthenticationDetails = (KeycloakWebAuthenticationDetails)token.getDetails();
                    return new PreauthKeycloakUserDetails(
                            token.getName(),
                            "N/A",
                            true,
                            true,
                            true,
                            true,
                            authorities,
                            keycloakWebAuthenticationDetails.getKeycloakPrincipal());
                } else {
                    return new User(token.getName(), "N/A", true, true, true, true, authorities);
                }
            }
        };
    }

    @Bean
    public J2eePreAuthenticatedProcessingFilter preAuthenticatedProcessingFilter() throws Exception {
        J2eePreAuthenticatedProcessingFilter preAuthenticatedProcessingFilter = new J2eePreAuthenticatedProcessingFilter();
        preAuthenticatedProcessingFilter.setAuthenticationDetailsSource(authenticationDetailsSource());
        preAuthenticatedProcessingFilter.setAuthenticationManager(authenticationManager());
        preAuthenticatedProcessingFilter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
        return preAuthenticatedProcessingFilter;
    }

    @Bean
    public AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> authenticationDetailsSource() {
        J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource authenticationDetailsSource = new J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource() {
            @Override
            public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(HttpServletRequest context) {
                Collection<String> j2eeUserRoles = getUserRoles(context);

                Collection<? extends GrantedAuthority> userGas = j2eeUserRoles2GrantedAuthoritiesMapper.getGrantedAuthorities(j2eeUserRoles);

                if (logger.isDebugEnabled()) {
                    logger.debug("J2EE roles [" + j2eeUserRoles + "] mapped to Granted Authorities: [" + userGas + "]");
                }

                PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails result;

                if (context.getUserPrincipal() instanceof KeycloakPrincipal) {

                    KeycloakPrincipal keycloakPrincipal=(KeycloakPrincipal)context.getUserPrincipal();
                    KeycloakSecurityContext session = keycloakPrincipal.getKeycloakSecurityContext();
                    AccessToken accessToken = session.getToken();

                    log.debug("accessToken.getPreferredUsername(): " + accessToken.getPreferredUsername());
                    log.debug("accessToken.getIssuedFor(): " + accessToken.getIssuedFor());
                    log.debug("accessToken.getIssuer(): " + accessToken.getIssuer());
                    log.debug("ROLES: " + accessToken.getRealmAccess().getRoles());

                    Set<String> roles = accessToken.getRealmAccess().getRoles();

                   /* KeycloakPrincipal<?> keycloakPrincipal = ((KeycloakPrincipal<?>)context.getUserPrincipal());
                    Set<String> roles = keycloakPrincipal.getKeycloakSecurityContext().getToken().getResourceAccess(
                            keycloakPrincipal.getKeycloakSecurityContext().getToken().getIssuedFor()).getRoles();*/

                    log.info("roles" + roles.toString());

                    result = new KeycloakWebAuthenticationDetails(
                            context,
                            j2eeUserRoles2GrantedAuthoritiesMapper.getGrantedAuthorities(roles),
                            keycloakPrincipal);
                } else {
                    result = new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, userGas);
                }

                return result;
            }
        };
        SimpleMappableAttributesRetriever mappableAttributesRetriever = new SimpleMappableAttributesRetriever();
        mappableAttributesRetriever.setMappableAttributes(new HashSet<String>());
        authenticationDetailsSource.setMappableRolesRetriever(mappableAttributesRetriever);
        SimpleAttributes2GrantedAuthoritiesMapper attributes2GrantedAuthoritiesMapper = new SimpleAttributes2GrantedAuthoritiesMapper();
        attributes2GrantedAuthoritiesMapper.setAttributePrefix(ROLE_PREFIX);
        authenticationDetailsSource.setUserRoles2GrantedAuthoritiesMapper(attributes2GrantedAuthoritiesMapper);
        return authenticationDetailsSource;
    }

    @Bean
    public LogoutHandler getLogoutHandler() {
        return new LogoutHandler() {
            @Override
            public void logout(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication) {
                try {
                    request.logout();
                } catch (ServletException ex) {
                    log.error("Error al sortir de l'aplicació", ex);
                }
            }
        };
    }

    @SuppressWarnings("serial")
    public static class KeycloakWebAuthenticationDetails extends PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails {
        private KeycloakPrincipal<?> keycloakPrincipal;
        public KeycloakWebAuthenticationDetails(
                HttpServletRequest request,
                Collection<? extends GrantedAuthority> authorities,
                KeycloakPrincipal<?> keycloakPrincipal) {
            super(request, authorities);
            this.keycloakPrincipal = keycloakPrincipal;
        }
        public KeycloakPrincipal<?> getKeycloakPrincipal() {
            return keycloakPrincipal;
        }
    }

}
