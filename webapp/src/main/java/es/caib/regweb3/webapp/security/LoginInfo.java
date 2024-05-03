package es.caib.regweb3.webapp.security;

import es.caib.regweb3.model.*;
import es.caib.regweb3.utils.Dir3Caib;
import es.caib.regweb3.webapp.utils.DenominacionComparador;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.*;

/**
 * Informació disponible durant el cicle de vida de l'aplicació en la Sessio HTTP.
 * <p>
 * Exemple d'us:
 * JAVA:   LoginInfo loginInfo = LoginInfo.getInstance();
 * JSTL:   ${loginInfo.usuarioAutenticado.nombreCompleto}
 * <p>
 * Des de qualsevol lloc: Controller o JSP.
 *
 * @author anadal
 */
public class LoginInfo {

    private Usuario usuarioAutenticado;
    private List<Rol> rolesAutenticado;
    private Rol rolActivo;
    private List<Entidad> entidades;
    private Entidad entidadActiva;
    private UsuarioEntidad usuarioEntidadActivo;
    private Boolean registrosMigrados;
    private List<Organismo> organismosConsultaEntrada;
    private List<Organismo> organismosConsultaSalida;
    private List<Organismo> organismosResponsable;
    private TreeSet<Oficina> oficinasAcceso;
    private LinkedHashSet<Oficina> oficinasRegistroEntrada;
    private LinkedHashSet<Oficina> oficinasRegistroSalida;
    private LinkedHashSet<Oficina> oficinasConsultaEntrada;
    private LinkedHashSet<Oficina> oficinasConsultaSalida;
    private LinkedHashSet<Oficina> oficinasResponsable;
    private Oficina oficinaActiva;
    private LinkedHashSet<Organismo> organismosOficinaActiva;
    private List<Plantilla> plantillasEntrada;
    private List<Plantilla> plantillasSalida;
    private Configuracion configuracion;
    private Boolean enlaceDir3 = false;
    private String ayudaUrl = "";
    private Dir3Caib dir3Caib;

    final User springSecurityUser;
    final Collection<GrantedAuthority> springRoles;


    public LoginInfo(Usuario usuarioAutenticado, User springSecurityUser, Collection<GrantedAuthority> springRoles) {
        this.usuarioAutenticado = usuarioAutenticado;
        this.springSecurityUser = springSecurityUser;
        this.springRoles = springRoles;
        entidades = new ArrayList<Entidad>();
        organismosConsultaEntrada = new ArrayList<Organismo>();
        organismosConsultaSalida = new ArrayList<Organismo>();
        organismosResponsable = new ArrayList<Organismo>();
        oficinasAcceso =  new TreeSet<>(new DenominacionComparador());
        oficinasRegistroEntrada = new LinkedHashSet<Oficina>();
        oficinasRegistroSalida = new LinkedHashSet<Oficina>();
        oficinasConsultaEntrada = new LinkedHashSet<Oficina>();
        oficinasConsultaSalida = new LinkedHashSet<Oficina>();
        oficinasResponsable = new LinkedHashSet<Oficina>();
        plantillasEntrada =  new ArrayList<Plantilla>();
        plantillasSalida =  new ArrayList<Plantilla>();
    }

    public Entidad getEntidadActiva() {
        return entidadActiva;
    }

    /**
     * Aquest és l'únic mètode necessari per canviar d'entitat a part
     * d'actualitzar el token
     *
     * @param entidadNueva
     */
    public void cambioEntidadActiva(Entidad entidadNueva, UsuarioEntidad usuarioNuevo) {

        // TODO Aqui s'ha de fer tot lo necessari per Canviar d'Entitat
        this.entidadActiva = entidadNueva;
        this.usuarioEntidadActivo = usuarioNuevo;

    }


    public Long getUsuarioAutenticadoID() {
        Usuario ue = getUsuarioAutenticado();
        if (ue == null) {
            return null;
        } else {
            return ue.getId();
        }
    }


    public String getUserName() {
        Usuario ue = getUsuarioAutenticado();
        if (ue == null) {
            return null;
        } else {
            return ue.getIdentificador();
        }
    }




    public User getSpringSecurityUser() {
        return springSecurityUser;
    }


    public List<Entidad> getEntidades() {
        return entidades;
    }




    public Collection<GrantedAuthority> getSpringRoles() {
        return springRoles;
    }


    public UsernamePasswordAuthenticationToken generateToken() {
        UsernamePasswordAuthenticationToken authToken;
        Collection<GrantedAuthority> roles = getSpringRoles();
        authToken = new UsernamePasswordAuthenticationToken(this.springSecurityUser, "", roles);
        authToken.setDetails(this);
        return authToken;
    }



    public Oficina getOficinaActiva() {
        return oficinaActiva;
    }


    public void setOficinaActiva(Oficina oficinaActiva) {

        // TODO Aqui s'ha de fer tot lo necessari per Canviar d'Oficina

        this.oficinaActiva = oficinaActiva;
    }

    public UsuarioEntidad getUsuarioEntidadActivo() {
        return usuarioEntidadActivo;
    }

    public void setUsuarioEntidadActivo(UsuarioEntidad usuarioEntidadActivo) {
        this.usuarioEntidadActivo = usuarioEntidadActivo;
    }



    public void setRolActivo(Rol rolActivo) {

        // TODO Aqui s'ha de fer tot lo necessari per Canviar de Rol

        this.rolActivo = rolActivo;
    }

    public void setUsuarioAutenticado(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public List<Rol> getRolesAutenticado() {
        return rolesAutenticado;
    }

    public void setRolesAutenticado(List<Rol> rolesAutenticado) {
        this.rolesAutenticado = rolesAutenticado;
    }

    public void setEntidades(List<Entidad> entidades) {
        this.entidades = entidades;
    }

    public void setEntidadActiva(Entidad entidadActiva) {
        this.entidadActiva = entidadActiva;
    }

    public Boolean getRegistrosMigrados() {
        return registrosMigrados;
    }

    public void setRegistrosMigrados(Boolean registrosMigrados) {
        this.registrosMigrados = registrosMigrados;
    }

    public List<Organismo> getOrganismosConsultaEntrada() {
        return organismosConsultaEntrada;
    }

    public void setOrganismosConsultaEntrada(List<Organismo> organismosConsultaEntrada) {
        this.organismosConsultaEntrada = organismosConsultaEntrada;
    }

    public List<Organismo> getOrganismosConsultaSalida() {
        return organismosConsultaSalida;
    }

    public void setOrganismosConsultaSalida(List<Organismo> organismosConsultaSalida) {
        this.organismosConsultaSalida = organismosConsultaSalida;
    }

    public List<Organismo> getOrganismosResponsable() {
        return organismosResponsable;
    }

    public void setOrganismosResponsable(List<Organismo> organismosResponsable) {
        this.organismosResponsable = organismosResponsable;
    }

    public TreeSet<Oficina> getOficinasAcceso() {
        return oficinasAcceso;
    }

    public void setOficinasAcceso(TreeSet<Oficina> oficinasAcceso) {
        this.oficinasAcceso = oficinasAcceso;
    }

    public LinkedHashSet<Oficina> getOficinasRegistroEntrada() {
        return oficinasRegistroEntrada;
    }

    public void setOficinasRegistroEntrada(LinkedHashSet<Oficina> oficinasRegistroEntrada) {
        this.oficinasRegistroEntrada = oficinasRegistroEntrada;
    }

    public LinkedHashSet<Oficina> getOficinasRegistroSalida() {
        return oficinasRegistroSalida;
    }

    public void setOficinasRegistroSalida(LinkedHashSet<Oficina> oficinasRegistroSalida) {
        this.oficinasRegistroSalida = oficinasRegistroSalida;
    }

    public LinkedHashSet<Oficina> getOficinasConsultaEntrada() {
        return oficinasConsultaEntrada;
    }

    public void setOficinasConsultaEntrada(LinkedHashSet<Oficina> oficinasConsultaEntrada) {
        this.oficinasConsultaEntrada = oficinasConsultaEntrada;
    }

    public LinkedHashSet<Oficina> getOficinasConsultaSalida() {
        return oficinasConsultaSalida;
    }

    public void setOficinasConsultaSalida(LinkedHashSet<Oficina> oficinasConsultaSalida) {
        this.oficinasConsultaSalida = oficinasConsultaSalida;
    }

    public LinkedHashSet<Oficina> getOficinasResponsable() {
        return oficinasResponsable;
    }

    public void setOficinasResponsable(LinkedHashSet<Oficina> oficinasResponsable) {
        this.oficinasResponsable = oficinasResponsable;
    }

    public LinkedHashSet<Organismo> getOrganismosOficinaActiva() {
        return organismosOficinaActiva;
    }

    public void setOrganismosOficinaActiva(LinkedHashSet<Organismo> organismosOficinaActiva) {
        this.organismosOficinaActiva = organismosOficinaActiva;
    }

    public List<Plantilla> getPlantillasEntrada() {
        return plantillasEntrada;
    }

    public void setPlantillasEntrada(List<Plantilla> plantillasEntrada) {
        this.plantillasEntrada = plantillasEntrada;
    }

    public List<Plantilla> getPlantillasSalida() {
        return plantillasSalida;
    }

    public void setPlantillasSalida(List<Plantilla> plantillasSalida) {
        this.plantillasSalida = plantillasSalida;
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }

    public Boolean getEnlaceDir3() {
        return enlaceDir3;
    }

    public void setEnlaceDir3(Boolean enlaceDir3) {
        this.enlaceDir3 = enlaceDir3;
    }

    public Rol getRolActivo() {
        return rolActivo;
    }

    public String getAyudaUrl() {
        return ayudaUrl;
    }

    public void setAyudaUrl(String ayudaUrl) {
        this.ayudaUrl = ayudaUrl;
    }

    public Dir3Caib getDir3Caib() {
        return dir3Caib;
    }

    public void setDir3Caib(Dir3Caib dir3Caib) {
        this.dir3Caib = dir3Caib;
    }

    public static LoginInfo getInstance() throws LoginException {
        Object obj;
        try {
            obj = SecurityContextHolder.getContext().getAuthentication().getDetails();
        } catch (Exception e) {
            // TODO traduccio
            throw new LoginException("Error intentant obtenir informació de Login.", e);
        }

        if (obj == null) {
            // TODO traduccio
            throw new LoginException("La informació de Login és buida");
        }

        if (obj instanceof LoginInfo) {
            return (LoginInfo) obj;
        } else {
            // TODO traduccio
            throw new LoginException("La informació de Login no és del tipus esperat."
                    + " Hauria de ser de tipus " + LoginInfo.class.getName() + " i és del tipus "
                    + obj.getClass().getName());
        }
    }



    /**
     * Resetea los datos para del usuario autenticado
     */
    public void resetDatos(){
        this.entidades = new ArrayList<Entidad>();
        this.entidadActiva = null;
        this.registrosMigrados = null;
        this.organismosConsultaEntrada =  new ArrayList<Organismo>();
        this.organismosConsultaSalida =  new ArrayList<Organismo>();
        this.organismosResponsable =  new ArrayList<Organismo>();
        this.oficinasRegistroEntrada = new LinkedHashSet<Oficina>();
        this.oficinasRegistroSalida = new LinkedHashSet<Oficina>();
        this.oficinasConsultaEntrada = new LinkedHashSet<Oficina>();
        this.oficinasConsultaSalida = new LinkedHashSet<Oficina>();
        this.oficinasAcceso =  new TreeSet<>(new DenominacionComparador());
        this.oficinasResponsable= new LinkedHashSet<Oficina>();
        this.plantillasEntrada =  new ArrayList<Plantilla>();
        this.plantillasSalida =  new ArrayList<Plantilla>();
        this.oficinaActiva = null;
        this.organismosOficinaActiva = null;
        this.configuracion = null;
        this.enlaceDir3 = false;
        this.ayudaUrl = "";
    }

    /**
     * Resetea los datos para del usuario autenticado
     */
    public void resetOficinas(){
        this.organismosConsultaEntrada =  new ArrayList<Organismo>();
        this.organismosConsultaSalida =  new ArrayList<Organismo>();
        this.organismosResponsable =  new ArrayList<Organismo>();
        this.oficinasRegistroEntrada = new LinkedHashSet<Oficina>();
        this.oficinasRegistroSalida = new LinkedHashSet<Oficina>();
        this.oficinasConsultaEntrada = new LinkedHashSet<Oficina>();
        this.oficinasConsultaSalida = new LinkedHashSet<Oficina>();
        this.oficinasAcceso = new TreeSet<>(new DenominacionComparador());
        this.oficinasResponsable= new LinkedHashSet<Oficina>();
        this.plantillasEntrada =  new ArrayList<Plantilla>();
        this.plantillasSalida =  new ArrayList<Plantilla>();
        this.oficinaActiva = null;
        this.organismosOficinaActiva = null;
        this.enlaceDir3 = false;
        this.ayudaUrl = "";
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "usuarioAutenticado=" + usuarioAutenticado.getNombreCompleto() +
                ", rolesAutenticado=" + Arrays.toString(rolesAutenticado.toArray()) +
                ", rolActivo=" + rolActivo.getNombre() +
                ", entidades=" + Arrays.toString(entidades.toArray()) +
                ", entidadActiva=" + entidadActiva.getNombre() +
                ", usuarioEntidadActivo=" + usuarioEntidadActivo.getNombreCompleto() +
                ", registrosMigrados=" + registrosMigrados +
                ", oficinasRegistroEntrada=" + Arrays.toString(oficinasRegistroEntrada.toArray()) +
                ", oficinaActiva=" + oficinaActiva.getDenominacion() +
                ", organismosOficinaActiva=" + Arrays.toString(organismosOficinaActiva.toArray()) +
                ", configuracion=" + configuracion +
                ", springSecurityUser=" + springSecurityUser.getUsername() +
                ", springRoles=" + springRoles +
                '}';
    }
}
