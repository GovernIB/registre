package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.sir.core.model.AnexoSir;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.InteresadoSir;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SirEJB")
@SecurityDomain("seycon")
public class SirBean implements SirLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB public RegistroEntradaLocal registroEntradaEjb;
    @EJB public RegistroSalidaLocal registroSalidaEjb;
    @EJB public LibroLocal libroEjb;
    @EJB public OrganismoLocal organismoEjb;
    @EJB public OficinaLocal oficinaEjb;
    @EJB public CatPaisLocal catPaisEjb;
    @EJB public CatProvinciaLocal catProvinciaEjb;
    @EJB public CatLocalidadLocal catLocalidadEjb;
    @EJB public TipoDocumentalLocal tipoDocumentalEjb;

    /**
     *
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public RegistroEntrada transformarRegistroEntrada(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto)
            throws Exception, I18NException, I18NValidationException {

        Libro libro = libroEjb.findById(idLibro);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setUsuario(usuario);
        registroEntrada.setOficina(oficinaActiva);
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
        registroEntrada.setLibro(libro);

        // Organismo destino
        Organismo organismoDestino;
        if(asientoRegistralSir.getCodigoUnidadTramitacionDestino() != null){
            organismoDestino = organismoEjb.findByCodigoLigero(asientoRegistralSir.getCodigoUnidadTramitacionDestino());
            registroEntrada.setDestino(organismoDestino);
        }else{
            Oficina oficina = oficinaEjb.findByCodigo(asientoRegistralSir.getCodigoEntidadRegistralDestino());
            organismoDestino = organismoEjb.findByCodigoLigero(oficina.getOrganismoResponsable().getCodigo());
        }

        registroEntrada.setDestino(organismoDestino);
        registroEntrada.setDestinoExternoCodigo(null);
        registroEntrada.setDestinoExternoDenominacion(null);

        // RegistroDetalle
        registroEntrada.setRegistroDetalle(getRegistroDetalle(asientoRegistralSir, idIdioma, idTipoAsunto));

        // Interesados
        List<Interesado> interesados = procesarInteresados(asientoRegistralSir.getInteresados());

        // Anexos
        List<AnexoFull> anexosFull = procesarAnexos(asientoRegistralSir);

        // Registramos el Registro Entrada
        synchronized (this){
            registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuario,interesados,anexosFull);
        }

        return registroEntrada;
    }

    /**
     *
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public RegistroSalida transformarRegistroSalida(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto) throws Exception, I18NException, I18NValidationException{

        Libro libro = libroEjb.findById(idLibro);

        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setUsuario(usuario);
        registroSalida.setOficina(oficinaActiva);
        registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);
        registroSalida.setLibro(libro);

        // Organismo origen
        // TODO Esta asignación es incorrecta
        Organismo organismoOrigen;
        if(asientoRegistralSir.getCodigoUnidadTramitacionDestino() != null){
            organismoOrigen = organismoEjb.findByCodigoLigero(asientoRegistralSir.getCodigoUnidadTramitacionDestino());
            registroSalida.setOrigen(organismoOrigen);
        }

        registroSalida.setOrigenExternoCodigo(null);
        registroSalida.setOrigenExternoDenominacion(null);

        // RegistroDetalle
        registroSalida.setRegistroDetalle(getRegistroDetalle(asientoRegistralSir, idIdioma, idTipoAsunto));

        // Interesados
        List<Interesado> interesados = procesarInteresados(asientoRegistralSir.getInteresados());

        // Anexos
        List<AnexoFull> anexosFull = procesarAnexos(asientoRegistralSir);

        // Registramos el Registro Entrada
        synchronized (this){
            registroSalida = registroSalidaEjb.registrarSalida(registroSalida, usuario,interesados, anexosFull);
        }

        return registroSalida;
    }

    /**
     * Obtiene un {@link RegistroDetalle} a partir de los datos de un AsientoRegistralSir
     * @param asientoRegistralSir
     * @param idIdioma
     * @param idTipoAsunto
     * @return
     * @throws Exception
     */
    private RegistroDetalle getRegistroDetalle(AsientoRegistralSir asientoRegistralSir, Long idIdioma, Long idTipoAsunto) throws Exception{

        RegistroDetalle registroDetalle = new RegistroDetalle();

        registroDetalle.setExtracto(asientoRegistralSir.getResumen());
        registroDetalle.setTipoDocumentacionFisica(Long.valueOf(asientoRegistralSir.getDocumentacionFisica().getValue()));
        registroDetalle.setIdioma(idIdioma);
        registroDetalle.setTipoAsunto(new TipoAsunto(idTipoAsunto));
        registroDetalle.setCodigoAsunto(null);

        if(asientoRegistralSir.getTipoTransporte() != null){
            registroDetalle.setTransporte(Long.valueOf(asientoRegistralSir.getTipoTransporte().getValue()));
        }
        if(!StringUtils.isEmpty(asientoRegistralSir.getNumeroTransporte())){
            registroDetalle.setNumeroTransporte(asientoRegistralSir.getNumeroTransporte());
        }
        if(!StringUtils.isEmpty(asientoRegistralSir.getObservacionesApunte())){
            registroDetalle.setObservaciones(asientoRegistralSir.getObservacionesApunte());
        }
        if(!StringUtils.isEmpty(asientoRegistralSir.getReferenciaExterna())){
            registroDetalle.setReferenciaExterna(asientoRegistralSir.getReferenciaExterna());
        }
        if(!StringUtils.isEmpty(asientoRegistralSir.getNumeroExpediente())){
            registroDetalle.setExpediente(asientoRegistralSir.getNumeroExpediente());
        }
        if(!StringUtils.isEmpty(asientoRegistralSir.getExpone())){
            registroDetalle.setExpone(asientoRegistralSir.getExpone());
        }
        if(!StringUtils.isEmpty(asientoRegistralSir.getSolicita())){
            registroDetalle.setSolicita(asientoRegistralSir.getSolicita());
        }

        registroDetalle.setOficinaOrigen(null);
        registroDetalle.setOficinaOrigenExternoCodigo(asientoRegistralSir.getCodigoEntidadRegistralOrigen());
        registroDetalle.setOficinaOrigenExternoDenominacion(asientoRegistralSir.getDecodificacionEntidadRegistralOrigen());

        registroDetalle.setFechaOrigen(asientoRegistralSir.getFechaRegistro());

        // Interesados
        registroDetalle.setInteresados(null);

        // Anexos
        registroDetalle.setAnexos(null);

        return registroDetalle;
    }


    /**
     * Transforma una Lista de {@link InteresadoSir} en una Lista de {@link Interesado}
     * @param interesadosSir
     * @return
     * @throws Exception
     */
    private List<Interesado> procesarInteresados(List<InteresadoSir> interesadosSir) throws Exception{
        List<Interesado> interesados = new ArrayList<Interesado>();
        for (InteresadoSir interesadoSir : interesadosSir) {
            Interesado interesado = transformarInteresado(interesadoSir);

            if (interesadoSir.getRepresentante()) {
                log.info("Tiene representante");
                Interesado representante = transformarRepresentante(interesadoSir);
                representante.setIsRepresentante(true);
                representante.setRepresentado(interesado);
                interesado.setRepresentante(representante);

                interesados.add(interesado);
                interesados.add(representante);
            }else{
                interesados.add(interesado);
            }


        }
        return interesados;
    }

    /**
     * Transforma un {@link InteresadoSir} en un {@link Interesado}
     * @param interesadoSir
     * @return Interesado de tipo {@link Interesado}
     * @throws Exception
     */
    private Interesado transformarInteresado(InteresadoSir interesadoSir) throws Exception{

        Interesado interesado = new Interesado();
        interesado.setId((long) (Math.random() * 10000));
        interesado.setIsRepresentante(false);

        // Averiguamos que tipo es el Interesado
        if (StringUtils.isEmpty(interesadoSir.getRazonSocialInteresado())) {
            log.info("Persona Fisica: " + interesadoSir.getNombreInteresado());
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            log.info("Persona Juridica: " + interesadoSir.getRazonSocialInteresado());
            interesado.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (!StringUtils.isEmpty(interesadoSir.getRazonSocialInteresado())) {
            interesado.setRazonSocial(interesadoSir.getRazonSocialInteresado());
        }
        if (!StringUtils.isEmpty(interesadoSir.getNombreInteresado())) {
            interesado.setNombre(interesadoSir.getNombreInteresado());
        }
        if (!StringUtils.isEmpty(interesadoSir.getPrimerApellidoInteresado())) {
            interesado.setApellido1(interesadoSir.getPrimerApellidoInteresado());
        }
        if (!StringUtils.isEmpty(interesadoSir.getSegundoApellidoInteresado())) {
            interesado.setApellido2(interesadoSir.getSegundoApellidoInteresado());
        }
        if (interesadoSir.getTipoDocumentoIdentificacionInteresado() != null) {
            interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(interesadoSir.getTipoDocumentoIdentificacionInteresado().getValue().charAt(0)));
        }
        if (!StringUtils.isEmpty(interesadoSir.getDocumentoIdentificacionInteresado())) {
            interesado.setDocumento(interesadoSir.getDocumentoIdentificacionInteresado());
        }

        if (!StringUtils.isEmpty(interesadoSir.getCodigoPaisInteresado())) {
            try {
                interesado.setPais(catPaisEjb.findByCodigo(Long.valueOf(interesadoSir.getCodigoPaisInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(interesadoSir.getCodigoProvinciaInteresado())) {
            try {
                interesado.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(interesadoSir.getCodigoProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(interesadoSir.getCodigoMunicipioInteresado())) {
            try {
                interesado.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(interesadoSir.getCodigoMunicipioInteresado()), Long.valueOf(interesadoSir.getCodigoProvinciaInteresado())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(interesadoSir.getDireccionInteresado())) {
            interesado.setDireccion(interesadoSir.getDireccionInteresado());
        }
        if (!StringUtils.isEmpty(interesadoSir.getCodigoPostalInteresado())) {
            interesado.setCp(interesadoSir.getCodigoPostalInteresado());
        }
        if (!StringUtils.isEmpty(interesadoSir.getCorreoElectronicoInteresado())) {
            interesado.setEmail(interesadoSir.getCorreoElectronicoInteresado());
        }
        if (!StringUtils.isEmpty(interesadoSir.getTelefonoInteresado())) {
            interesado.setTelefono(interesadoSir.getTelefonoInteresado());
        }
        if (!StringUtils.isEmpty(interesadoSir.getDireccionElectronicaHabilitadaInteresado())) {
            interesado.setDireccionElectronica(interesadoSir.getDireccionElectronicaHabilitadaInteresado());
        }
        if (interesadoSir.getCanalPreferenteComunicacionInteresado() != null) {
            interesado.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(interesadoSir.getCanalPreferenteComunicacionInteresado().getValue()));
        }
        if (!StringUtils.isEmpty(interesadoSir.getObservaciones())) {
            interesado.setObservaciones(interesadoSir.getObservaciones());
        }

        return interesado;

    }

    
    /** Transforma un {@link InteresadoSir} en un {@link Interesado}
    *
    * @param representanteSir
    * @return Interesado de tipo {@link Interesado}
     */      
    private Interesado transformarRepresentante(InteresadoSir representanteSir) {

        Interesado representante = new Interesado();
        representante.setId((long) (Math.random() * 10000));
        representante.setIsRepresentante(true);

        // Averiguamos que tipo es el Representante
        if (es.caib.regweb3.utils.StringUtils.isEmpty(representanteSir.getRazonSocialRepresentante())) {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA);

        } else {
            representante.setTipo(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA);
        }

        if (!StringUtils.isEmpty(representanteSir.getRazonSocialRepresentante())) {
            representante.setRazonSocial(representanteSir.getRazonSocialRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getNombreRepresentante())) {
            representante.setNombre(representanteSir.getNombreRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getPrimerApellidoRepresentante())) {
            representante.setApellido1(representanteSir.getPrimerApellidoRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getSegundoApellidoRepresentante())) {
            representante.setApellido2(representanteSir.getSegundoApellidoRepresentante());
        }
        if (representanteSir.getTipoDocumentoIdentificacionRepresentante() != null) {
            representante.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(representanteSir.getTipoDocumentoIdentificacionRepresentante().getValue().charAt(0)));
        }
        if (!StringUtils.isEmpty(representanteSir.getDocumentoIdentificacionRepresentante())) {
            representante.setDocumento(representanteSir.getDocumentoIdentificacionRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getCodigoPaisRepresentante())) {
            try {
                representante.setPais(catPaisEjb.findByCodigo(Long.valueOf(representanteSir.getCodigoPaisRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(representanteSir.getCodigoProvinciaRepresentante())) {
            try {
                representante.setProvincia(catProvinciaEjb.findByCodigo(Long.valueOf(representanteSir.getCodigoProvinciaRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(representanteSir.getCodigoMunicipioRepresentante())) {
            try {
                representante.setLocalidad(catLocalidadEjb.findByLocalidadProvincia(Long.valueOf(representanteSir.getCodigoMunicipioRepresentante()), Long.valueOf(representanteSir.getCodigoProvinciaRepresentante())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isEmpty(representanteSir.getDireccionRepresentante())) {
            representante.setDireccion(representanteSir.getDireccionRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getCodigoPostalRepresentante())) {
            representante.setCp(representanteSir.getCodigoPostalRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getCorreoElectronicoRepresentante())) {
            representante.setEmail(representanteSir.getCorreoElectronicoRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getTelefonoRepresentante())) {
            representante.setTelefono(representanteSir.getTelefonoRepresentante());
        }
        if (!StringUtils.isEmpty(representanteSir.getDireccionElectronicaHabilitadaRepresentante())) {
            representante.setDireccionElectronica(representanteSir.getDireccionElectronicaHabilitadaRepresentante());
        }
        if (representanteSir.getCanalPreferenteComunicacionRepresentante() != null) {
            representante.setCanal(RegwebConstantes.CANALNOTIFICACION_BY_CODIGO.get(representanteSir.getCanalPreferenteComunicacionRepresentante().getValue()));
        }
        if (!StringUtils.isEmpty(representanteSir.getObservaciones())) {
            representante.setObservaciones(representanteSir.getObservaciones());
        }

        return representante;

    }

    /**
     * Transforma una lista de {@link AnexoSir} en una lista de  {@link AnexoFull}
     * @param asientoRegistralSir
     * @return Listado de {@link AnexoFull}
     * @throws Exception
     */
    private List<AnexoFull> procesarAnexos(AsientoRegistralSir asientoRegistralSir) throws Exception{

        List<AnexoFull> anexos = new ArrayList<AnexoFull>();
        HashMap<String,AnexoFull> mapAnexosFull = new HashMap<String, AnexoFull>();
        for (AnexoSir anexoSir : asientoRegistralSir.getAnexos()) {

            AnexoFull anexoFull = transformarAnexo(anexoSir, asientoRegistralSir.getEntidad().getId(), mapAnexosFull);
            mapAnexosFull.put(anexoSir.getIdentificadorFichero(), anexoFull);
            anexos = new ArrayList<AnexoFull>(mapAnexosFull.values());
        }
        // log.info(anexos.size());
        return anexos;
    }

    /**
     * Transforma un {@link AnexoSir} en un {@link Anexo}
     *
     * @param anexoSir
     * @param idEntidad
     * @param anexosProcesados Lista de anexos procesados anteriores.
     * @return Anexo tipo {@link Anexo}
     */
    private AnexoFull transformarAnexo(AnexoSir anexoSir, Long idEntidad, Map<String, AnexoFull> anexosProcesados)throws Exception {

        AnexoFull anexoFull = new AnexoFull();
        Anexo anexo = new Anexo();

        anexo.setTitulo(anexoSir.getNombreFichero());

        if (anexoSir.getValidezDocumento() != null) {
            anexo.setValidezDocumento(Long.valueOf(anexoSir.getValidezDocumento().getValue()));
        } else {// Si sicres no especifica validez del documento la fijamos a copia por defecto
            anexo.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA);
        }

        if (anexoSir.getTipoDocumento() != null) {
            anexo.setTipoDocumento(Long.valueOf(anexoSir.getTipoDocumento().getValue()));
        }
        anexo.setObservaciones(anexoSir.getObservaciones());

        //Campos NTI TODO PENDIENTE
        anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION.intValue()); // TODO Comprobar esta asignación
        anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99",idEntidad));

        /* TODO estos campos no estan deficinidos en el anexo
        if(anexoSir.getCertificado()!= null) {
            //anexo.setCertificado(anexoSir.getCertificado());
        };
        // TODO
        if(anexoSir.getFirma_Documento()!=null){
            //anexoSir.getFirma_Documento();

        };
        if(anexoSir.getTimeStamp()!= null){
            //anexoSir.getTimeStamp();
        }

        if(anexoSir.getValidacion_OCSP_Certificado()!= null) {
            //anexoSir.getValidacion_OCSP_Certificado();
        }

        if(anexoSir.getHash()!= null){
            //anexoSir.getHash());
        }
        if(anexoSir.getTipo_MIME()!= null){
            anexoSir.getTipo_MIME();
        }
        if(anexoSir.getAnexo()!= null){
            anexoSir.getAnexo();
        }*/


        /*anexo.setNombreFichero(deAnexo.getNombreFicheroAnexado()); TODO
        anexo.setIdentificadorFichero(deAnexo.getIdentificadorFichero()); TODO
        anexo.setIdentificadorDocumentoFirmado(deAnexo.getIdentificadorDocumentoFirmado()); //TODO

        */

        DocumentCustody dc= new DocumentCustody();
        SignatureCustody sc= new SignatureCustody();
        // Si el anexo tiene identificador_documento_firmado, es que es la firma de un anexo anterior.
        if (!StringUtils.isEmpty(anexoSir.getIdentificadorDocumentoFirmado()) && anexoSir.getIdentificadorDocumentoFirmado() != null) {
            String identificadorDocumentoFirmado = anexoSir.getIdentificadorDocumentoFirmado();
            if(identificadorDocumentoFirmado.equals(anexoSir.getIdentificadorFichero())){
                //Caso Firma Attached caso 5, se guarda el documento en signatureCustody, como lo especifica el API DE CUSTODIA(II)
                anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
                sc = getSignatureCustody(anexoSir, null, anexo.getModoFirma());
                anexoFull.setDocumentoCustody(null);
                anexoFull.setSignatureCustody(sc);
                anexoFull.setAnexo(anexo);

            }else{
                //Caso Firma Detached, caso 4, se guarda 1 anexo, con el doc original en documentCustody y la firma en SignatureCustody
                //anexoFull.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED);

                anexoFull = anexosProcesados.get(identificadorDocumentoFirmado);//obtenemos el documento original previamente procesado
                anexoFull.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED); // asignamos el modo de firma
                sc = getSignatureCustody(anexoSir, anexoFull.getDocumentoCustody(), anexoFull.getAnexo().getModoFirma());
                anexoFull.setSignatureCustody(sc);
                //eliminamos de los procesados el documento cuya firma es este anexo que estamos tratando ahora.
                //si no guardariamos 2 anexos, el documento original y el documento original con la firma.
                anexosProcesados.remove(identificadorDocumentoFirmado);

            }
            // Al ser un anexo con firma, si sicres no habia especificado la validez, regweb la fija a copia y aqui
            // la modifica a copia compulsada porque ya sabe que hay firma.
            if (anexoFull.getAnexo().getValidezDocumento() == RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA) {
                anexoFull.getAnexo().setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA);
            }
        } else { // El anexo no es firma de nadie
            if (anexoSir.getFirma() == null) { //Anexo normal
                anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
            } else { //La firma es un CSV.
                anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
                //anexo.setCsv(anexoSir.getFirma_Documento());
                //TODO Metadada a custodia pel csv.

            }
            dc = getDocumentCustody(anexoSir);
            anexoFull.setAnexo(anexo);
            anexoFull.setDocumentoCustody(dc);
        }


        //anexoFull.setAnexo(anexo);
        return anexoFull;

    }



    protected DocumentCustody getDocumentCustody(AnexoSir anexoSir) {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");
            log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
        }
        DocumentCustody dc = null;
        if (anexoSir.getAnexo() != null) {
            dc = new DocumentCustody();
            dc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
            dc.setMime(anexoSir.getTipoMIME());
            dc.setName(anexoSir.getNombreFichero());
        }
        return dc;
    }


    protected SignatureCustody getSignatureCustody(AnexoSir anexoSir, DocumentCustody dc,
                                                   int modoFirma) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("  ------------------------------");
            log.debug(" anexoSir.getAnexo = " + anexoSir.getAnexo());
        }

        SignatureCustody sc = null;
        if (anexoSir.getAnexo() == null) {

            if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                    || modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                String msg = "L'usuari ens indica que hi ha una firma i no ve (modoFirma = " + modoFirma + ")";
                log.error(msg, new Exception());
                throw new Exception(msg);
            }

        } else {

            if (modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
                    && modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                String msg = "L'usuari ens indica que NO hi ha una firma pero n'envia una"
                        + " (modoFirma = " + modoFirma + ")";
                log.error(msg, new Exception());
                throw new Exception(msg);
            }



            sc = new SignatureCustody();

            sc.setData(FileSystemManager.getBytesArchivo(anexoSir.getAnexo().getId()));
            sc.setMime(anexoSir.getTipoMIME());
            sc.setName(anexoSir.getNombreFichero());


            if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
                // Document amb firma adjunta
                sc.setAttachedDocument(null);

                // TODO Emprar mètode per descobrir tipus de signatura
                sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);

            } else if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                // Firma en document separat CAS 4
                if (dc == null) {
                    throw new Exception("Aquesta firma requereix el document original"
                            + " i no s'ha enviat");
                }

                sc.setAttachedDocument(false);
                // TODO Emprar mètode per descobrir tipus de signatura
                sc.setSignatureType(SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT);
            }
        }
        return sc;
    }

}
