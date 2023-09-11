package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.JustificanteReferencia;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static es.caib.regweb3.utils.RegwebConstantes.*;

/**
 * Created by mgonzalez on 06/03/2019.
 * Ejb con TransactionAttributeType.REQUIRES_NEW para permitir la creación
 * de un Registro y generar el justificante conjuntamente.
 */
@Stateless(name = "AsientoRegistralEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AsientoRegistralBean implements AsientoRegistralLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private UsuarioEntidadLocal usuarioEntidadEjb;
    @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private SirEnvioLocal sirEnvioEjb;
    @EJB private JustificanteLocal justificanteEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;
    @EJB private DistribucionLocal distribucionEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;

    @Override
    public UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws I18NException {
        return usuarioEntidadEjb.comprobarUsuarioEntidad(identificador, idEntidad);
    }

    @Override
    public RegistroSalida registrarSalida(RegistroSalida registroSalida, Entidad entidad, UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws I18NException, I18NValidationException {

        return registroSalidaEjb.registrarSalida(registroSalida, entidad, usuarioEntidad, interesados, anexos, validarAnexos);
    }

    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws I18NException, I18NValidationException {

        return registroEntradaEjb.registrarEntrada(registroEntrada, entidad, usuarioEntidad, interesados, anexos, validarAnexos);

    }

    @Override
    public JustificanteReferencia obtenerReferenciaJustificante(String numeroRegistroformateado, Entidad entidad) throws I18NException {

        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidad.getId(), numeroRegistroformateado);

        if (registroEntrada != null) {

            if (registroEntrada.getRegistroDetalle().getTieneJustificanteCustodiado()) {
                String csv = registroEntrada.getRegistroDetalle().getJustificante().getCsv();
                String url = anexoEjb.getCsvValidationWeb(registroEntrada.getRegistroDetalle().getJustificante(), entidad.getId());

                return new JustificanteReferencia(csv, url);

            } else if(registroEntrada.getRegistroDetalle().getTieneJustificante()){
                throw new I18NException("registro.justificante.noCustodiado");
            } else {
                throw new I18NException("registro.justificante.noTiene");
            }
        }

        RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidad.getId(), numeroRegistroformateado);

        if (registroSalida != null) {

            if (registroSalida.getRegistroDetalle().getTieneJustificanteCustodiado()) {
                String csv = registroSalida.getRegistroDetalle().getJustificante().getCsv();
                String url = anexoEjb.getCsvValidationWeb(registroSalida.getRegistroDetalle().getJustificante(), entidad.getId());

                return new JustificanteReferencia(csv, url);

            } else if(registroSalida.getRegistroDetalle().getTieneJustificante()){
                throw new I18NException("registro.justificante.noCustodiado");
            } else {
                throw new I18NException("registro.justificante.noTiene");
            }
        }

        return null;
    }

    @Override
    public RegistroSalida procesarRegistroSalida(Long tipoOperacion, RegistroSalida registroSalida, Entidad entidad) throws I18NException, Exception, I18NValidationException {

        // Es una Notificación
        if (tipoOperacion != null && tipoOperacion.equals(TIPO_OPERACION_NOTIFICACION)) {
            //Creamos el justificante del registroSalida y lo marcamos como REGISTRO_ENVIADO_NOTIFICAR
            crearJustificanteCambioEstado(entidad, registroSalida, REGISTRO_ENVIADO_NOTIFICAR);
            registroSalida.setEstado(REGISTRO_ENVIADO_NOTIFICAR);

            // Es una Comunicación
        } else if (tipoOperacion != null && tipoOperacion.equals(TIPO_OPERACION_COMUNICACION)) {

            //Si es una Comunicación dependerá de los interesados destinatarios (solo hay un interesado)
            for (Interesado interesado : registroSalida.getRegistroDetalle().getInteresados()) {

                //Interesado es una persona física o jurídica es como el caso de notificación
                if (TIPO_INTERESADO_PERSONA_FISICA.equals(interesado.getTipo()) || TIPO_INTERESADO_PERSONA_JURIDICA.equals(interesado.getTipo())) {
                    //Creamos el justificante del registroSalida y lo marcamos como REGISTRO_VALIDO
                    crearJustificanteCambioEstado(entidad, registroSalida, REGISTRO_ENVIADO_NOTIFICAR);
                    registroSalida.setEstado(REGISTRO_ENVIADO_NOTIFICAR);

                    // Interesado es una administración
                } else if (TIPO_INTERESADO_ADMINISTRACION.equals(interesado.getTipo())) {

                    //Obtenemos las oficinas SIR a las que va dirigido el registro de Salida
                    List<OficinaTF> oficinasSIR;
                    if(multiEntidadEjb.isMultiEntidad()) {
                        oficinasSIR = registroSalidaEjb.isOficioRemisionSirMultiEntidad(registroSalida, getOrganismosOficioRemisionSalida(organismoEjb.getOrganismosRegistro(registroSalida.getOficina())),
                                registroSalida.getUsuario().getEntidad().getId());
                    }else{
                        oficinasSIR = registroSalidaEjb.isOficioRemisionSir(registroSalida, getOrganismosOficioRemisionSalida(organismoEjb.getOrganismosRegistro(registroSalida.getOficina())),
                                registroSalida.getUsuario().getEntidad().getId());
                    }

                    //No tiene oficinas en SIR
                    if (oficinasSIR.isEmpty()) {
                        //TODO hay que crear el oficio externo???
                        //Creamos el justificante del registroSalida y lo marcamos como REGISTRO_OFICIO_EXTERNO
                        crearJustificanteCambioEstado(entidad, registroSalida, REGISTRO_OFICIO_EXTERNO);
                        registroSalida.setEstado(REGISTRO_OFICIO_EXTERNO);

                    } else { //Tiene oficinas en SIR, se crear el intercambio

                        try {

                            // Crear Justificante
                            crearJustificante(entidad, registroSalida.getUsuario(), registroSalida, RegwebConstantes.REGISTRO_SALIDA, RegistroUtils.getIdiomaJustificante(registroSalida));

                            // Crear el intercambio, posteriormente se enviará
                            sirEnvioEjb.crearIntercambioSalida(registroSalida, entidad, registroSalida.getOficina(),
                                    registroSalida.getUsuario(), oficinasSIR.get(0));

                            registroSalida.setEstado(REGISTRO_OFICIO_SIR);
                            registroSalida.getRegistroDetalle().setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());

                        } catch (Exception e) {
                            throw new I18NException("registroSir.error.envio");
                        }
                    }
                }
            }
        }

        return registroSalida;
    }

    @Asynchronous
    @Override
    public void crearJustificante(Entidad entidad, UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NValidationException, I18NException {

        //Llamada asincrona al método para generar el justficante dle regitro
        //JustificanteLocal asynchJustificante = AsyncUtils.mixinAsync(justificanteEjb);
        justificanteEjb.crearJustificanteWS(entidad, usuarioEntidad, registro, tipoRegistro, idioma);

    }

    @Asynchronous
    @Override
    public void distribuirRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuario) throws I18NException {

        //DistribucionLocal asynchDistribucion = AsyncUtils.mixinAsync(distribucionEjb);

        //  Comprobamos que el usuario tiene permisos para Distribuir el registro
        if(!permisoOrganismoUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO, true)){
            throw new I18NException("registroEntrada.distribuir.error.permiso");
        }

        // Comprobamos que el RegistroEntrada se puede Distribuir
        if (!registroEntradaConsultaEjb.isDistribuir(registroEntrada.getId())) {
            throw new I18NException("registroEntrada.distribuir.noPermitido");
        }

        try{
            // Distribuimos el registro de entrada
            distribucionEjb.distribuir(registroEntrada, usuario, I18NLogicUtils.tradueix(LocaleContextHolder.getLocale(), "distribucion.ws"),null,null);

        }catch (I18NValidationException e){
            e.printStackTrace();
            throw new I18NException("registroEntrada.distribuir.error");
        }
    }

    /**
     * Transforma un conjunto de organismos a un conjunto de strings con los códigos de los organismos
     *
     * @return
     * @throws I18NException
     */
    private Set<String> getOrganismosOficioRemisionSalida(Set<Organismo> organismos) throws I18NException {

        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();

        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());

        }
        return organismosCodigo;
    }

    /**
     * Método que crea el justiifcante del registro de salida y actualiza su estado
     */
    private void crearJustificanteCambioEstado(Entidad entidad, RegistroSalida registroSalida, Long estado) throws I18NException, I18NValidationException, I18NException {
        //Crear Justificante
        crearJustificante(entidad, registroSalida.getUsuario(), registroSalida, RegwebConstantes.REGISTRO_SALIDA, RegistroUtils.getIdiomaJustificante(registroSalida));

        //Cambiar estado
        registroSalidaEjb.cambiarEstado(registroSalida.getId(), estado);
    }
}
