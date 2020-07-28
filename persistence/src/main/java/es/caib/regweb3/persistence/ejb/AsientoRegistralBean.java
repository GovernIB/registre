package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.JustificanteReferencia;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.common.proxy.plugins.async.AsyncUtils;

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
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AsientoRegistralBean implements AsientoRegistralLocal {

    protected final Logger log = Logger.getLogger(getClass());

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

    @Override
    public UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws Exception, I18NException {
        return usuarioEntidadEjb.comprobarUsuarioEntidad(identificador, idEntidad);
    }

    @Override
    public RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                          UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws Exception, I18NException, I18NValidationException {

        return registroSalidaEjb.registrarSalida(registroSalida, usuarioEntidad, interesados, anexos, validarAnexos);
    }

    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                            UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws Exception, I18NException, I18NValidationException {

        return registroEntradaEjb.registrarEntrada(registroEntrada, usuarioEntidad, interesados, anexos, validarAnexos);

    }

    @Override
    public JustificanteReferencia obtenerReferenciaJustificante(String numeroRegistroformateado, Entidad entidad) throws Exception, I18NException {

        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoConAnexos(entidad.getCodigoDir3(), numeroRegistroformateado);
        RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateadoConAnexos(entidad.getCodigoDir3(), numeroRegistroformateado);

        if (registroEntrada != null) {

            if (registroEntrada.getRegistroDetalle().getTieneJustificante()) {
                String csv = registroEntrada.getRegistroDetalle().getJustificante().getCsv();
                String url = anexoEjb.getUrlValidation(registroEntrada.getRegistroDetalle().getJustificante(), entidad.getId());

                return new JustificanteReferencia(csv, url);

            } else {
                throw new I18NException("registro.justificante.noTiene");
            }

        } else if (registroSalida != null) {

            if (registroSalida.getRegistroDetalle().getTieneJustificante()) {
                String csv = registroSalida.getRegistroDetalle().getJustificante().getCsv();
                String url = anexoEjb.getUrlValidation(registroSalida.getRegistroDetalle().getJustificante(), entidad.getId());
                return new JustificanteReferencia(csv, url);
            } else {
                throw new I18NException("registro.justificante.noTiene");
            }
        }

        return null;
    }

    @Override
    public RegistroSalida procesarRegistroSalida(Long tipoOperacion, RegistroSalida registroSalida) throws I18NException, Exception, I18NValidationException {

        // Es una Notificación
        if (tipoOperacion != null && tipoOperacion.equals(TIPO_OPERACION_NOTIFICACION)) {
            //Creamos el justificante del registroSalida y lo marcamos como DISTRIBUIDO
            crearJustificanteCambioEstado(registroSalida, REGISTRO_DISTRIBUIDO);
            registroSalida.setEstado(REGISTRO_DISTRIBUIDO);

            // Es una Comunicación
        } else if (tipoOperacion != null && tipoOperacion.equals(TIPO_OPERACION_COMUNICACION)) {

            //Si es una Comunicación dependerá de los interesados destinatarios (solo hay un interesado)
            for (Interesado interesado : registroSalida.getRegistroDetalle().getInteresados()) {

                //Interesado es una persona física o jurídica es como el caso de notificación
                if (TIPO_INTERESADO_PERSONA_FISICA.equals(interesado.getTipo())
                        || TIPO_INTERESADO_PERSONA_JURIDICA.equals(interesado.getTipo())) {
                    //Creamos el justificante del registroSalida y lo marcamos como DISTRIBUIDO
                    crearJustificanteCambioEstado(registroSalida, REGISTRO_DISTRIBUIDO);
                    registroSalida.setEstado(REGISTRO_DISTRIBUIDO);

                    // Interesado es una administración
                } else if (TIPO_INTERESADO_ADMINISTRACION.equals(interesado.getTipo())) {
                    //Obtenemos las oficinas SIR a las que va dirigido el registro de Salida
                    List<OficinaTF> oficinasSIR = registroSalidaEjb.isOficioRemisionSir(registroSalida, getOrganismosOficioRemisionSalida(organismoEjb.getByOficinaActiva(registroSalida.getOficina(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)));
                    //No tiene oficinas en SIR
                    if (oficinasSIR.isEmpty()) {
                        //TODO hay que crear el oficio externo???
                        //Creamos el justificante del registroSalida y lo marcamos como REGISTRO_OFICIO_EXTERNO
                        crearJustificanteCambioEstado(registroSalida, REGISTRO_OFICIO_EXTERNO);
                        registroSalida.setEstado(REGISTRO_OFICIO_EXTERNO);
                        registroSalida.getRegistroDetalle().setIdentificadorIntercambio("-1");

                    } else { //Tiene oficinas en SIR, se envia el registro via SIR.

                        try {

                            OficioRemision oficioRemision = sirEnvioEjb.enviarIntercambio(REGISTRO_SALIDA, registroSalida.getId(),
                                    registroSalida.getOficina(), registroSalida.getUsuario(), oficinasSIR.get(0).getCodigo());
                            registroSalidaEjb.cambiarEstado(registroSalida.getId(), REGISTRO_OFICIO_SIR);

                            registroSalida.setEstado(REGISTRO_OFICIO_SIR);
                            registroSalida.getRegistroDetalle().setIdentificadorIntercambio(oficioRemision.getIdentificadorIntercambio());

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
    public void crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NValidationException, I18NException {

        //Llamada asincrona al método para generar el justficante dle regitro
        JustificanteLocal asynchJustificante = AsyncUtils.mixinAsync(justificanteEjb);
        asynchJustificante.crearJustificante(usuarioEntidad, registro, tipoRegistro, idioma);

    }

    @Asynchronous
    @Override
    public void distribuirRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuario) throws Exception, I18NException {

        DistribucionLocal asynchDistribucion = AsyncUtils.mixinAsync(distribucionEjb);

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
            asynchDistribucion.distribuir(registroEntrada, usuario);

        }catch (Exception e){
            e.printStackTrace();
            throw new I18NException("registroEntrada.distribuir.error");
        } catch (I18NValidationException e) {
            e.printStackTrace();
            throw new I18NException("registroEntrada.distribuir.error");
        }
    }

    /**
     * Transforma un conjunto de organismos a un conjunto de strings con los códigos de los organismos
     *
     * @return
     * @throws Exception
     */
    private Set<String> getOrganismosOficioRemisionSalida(Set<Organismo> organismos) throws Exception {

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
    private void crearJustificanteCambioEstado(RegistroSalida registroSalida, Long estado) throws Exception, I18NValidationException, I18NException {
        //Crear Justificante
        crearJustificante(registroSalida.getUsuario(), registroSalida, RegwebConstantes.REGISTRO_SALIDA, "ca");

        //Cambiar estado
        registroSalidaEjb.cambiarEstado(registroSalida.getId(), estado);
    }
}
