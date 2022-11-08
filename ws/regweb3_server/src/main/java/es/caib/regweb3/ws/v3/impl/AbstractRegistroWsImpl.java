package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.validator.AnexoBeanValidator;
import es.caib.regweb3.persistence.validator.AnexoValidator;
import es.caib.regweb3.persistence.validator.InteresadoBeanValidator;
import es.caib.regweb3.persistence.validator.InteresadoValidator;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.AnexoConverter;
import es.caib.regweb3.ws.converter.DatosInteresadoConverter;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.utils.AuthenticatedBaseWsImpl;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author anadal
 */
public abstract class AbstractRegistroWsImpl extends AuthenticatedBaseWsImpl {

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/PermisoOrganismoUsuarioEJB/local")
    public PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;

    @EJB(mappedName = "regweb3/PendienteEJB/local")
    public PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    public InteresadoLocal interesadoEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    public IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    public JustificanteLocal justificanteEjb;

    @EJB(mappedName = "regweb3/LopdEJB/local")
    public LopdLocal lopdEjb;

    @EJB(mappedName = "regweb3/NotificacionEJB/local")
    public NotificacionLocal notificacionEjb;

    public AnexoValidator<Anexo> anexoValidator = new AnexoValidator<Anexo>();

    InteresadoValidator<Interesado> interesadoValidator = new InteresadoValidator<Interesado>();

    /**
     * Procesa los Anexos recibidos
     *
     * @param anexosWs
     * @param tipoOperacion 
     * @return
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    protected List<AnexoFull> procesarAnexos(List<AnexoWs> anexosWs, Long entidadID, Boolean isComunicacionSir) throws Exception, I18NValidationException, I18NException {

        List<AnexoFull> anexos = new ArrayList<AnexoFull>();

        for (AnexoWs anexoWs : anexosWs) {
            //Convertimos a anexo

            //El modo firma puede no coincidir con el contenido (versión imprimible)
            if(isComunicacionSir && anexoWs.getFicheroAnexado() != null) {
            	boolean signed = isAnexoSigned(anexoWs.getFicheroAnexado());
            	if (!signed)
            		anexoWs.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
            }
            
            AnexoFull anexoFull = AnexoConverter.getAnexoFull(anexoWs, entidadID, tipoDocumentalEjb);
            
            //Controlamos el tamanyo de los ficheros que nos adjuntan.
            Long maxUploadSizeInBytes = PropiedadGlobalUtil.getMaxUploadSizeInBytes(entidadID);
            if(maxUploadSizeInBytes!= null){ // Si no está especificada, se permite cualquier tamaño
                switch (anexoWs.getModoFirma()){
                    case 0: //SIN FIRMA
                    	anexoFull.getAnexo().setFirmaverificada(false); //Los usuarios pueden adjuntar documentos firmados sin indicar que lo están
                    	break;
                    case 1:{ //ATTACHED
                        if(anexoWs.getFicheroAnexado()!= null && (anexoWs.getFicheroAnexado().length > maxUploadSizeInBytes)) {
                            throw new I18NException("tamanyfitxerpujatsuperat", Long.toString(anexoWs.getFicheroAnexado().length/(1024*1024)),Long.toString(maxUploadSizeInBytes/(1024*1024)));
                        }
                        break;
                    }
                    case 2: { //FIRMA DETACHED
                        if(anexoWs.getFicheroAnexado()!= null && anexoWs.getFicheroAnexado().length > maxUploadSizeInBytes) {
                            throw new I18NException("tamanyfitxerpujatsuperat", Long.toString(anexoWs.getFicheroAnexado().length/(1024*1024)),Long.toString(maxUploadSizeInBytes/(1024*1024)));
                        }
                        if(anexoWs.getFirmaAnexada()!= null && anexoWs.getFirmaAnexada().length > maxUploadSizeInBytes) {
                            throw new I18NException("tamanyfitxerpujatsuperat", Long.toString(anexoWs.getFicheroAnexado().length/(1024*1024)),Long.toString(maxUploadSizeInBytes/(1024*1024)));
                        }
                    }

                }
            }

            validateAnexo(anexoFull.getAnexo(),true);
            
            anexos.add(anexoFull);
        }

        return anexos;

    }

	private boolean isAnexoSigned(byte[] contingut) {
		PdfReader reader;
		try {
			reader = new PdfReader(contingut);
			AcroFields acroFields = reader.getAcroFields();
			List<String> signatureNames = acroFields.getSignatureNames();
			if (signatureNames != null && !signatureNames.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("No se ha podido determinar si el anexo está firmado");
		}
		return false;
	}

    /**
     * @param anexo
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    protected void validateAnexo(Anexo anexo, boolean isNou) throws I18NValidationException, I18NException {

        //Validamos el anexo contra afirma

        //anexoValidator.validateStandalone(anexo);
        AnexoBeanValidator pfbv = new AnexoBeanValidator(anexoValidator);

        //final boolean isNou = true;
        pfbv.throwValidationExceptionIfErrors(anexo, isNou);
    }

    /**
     * Procesa los Interesados recibidos
     *
     * @param interesadosWs
     * @return
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    protected List<Interesado> procesarInteresados(List<InteresadoWs> interesadosWs, InteresadoLocal interesadoEjb, CatPaisLocal catPaisEjb, CatProvinciaLocal catProvinciaEjb, CatLocalidadLocal catLocalidadEjb, PersonaLocal personaEjb)
            throws Exception, I18NValidationException, I18NException {

        List<Interesado> interesados = new ArrayList<Interesado>();

        for (InteresadoWs interesadoWs : interesadosWs) {

            Interesado interesado = DatosInteresadoConverter.getInteresado(
                    interesadoWs.getInteresado(),
                    catPaisEjb, catProvinciaEjb, catLocalidadEjb);

            // Validar Interesado
            validateInteresado(interesado, catPaisEjb, interesadoEjb, personaEjb);

            // Id aleatorio
            interesado.setId((long) (Math.random() * 10000));

            if (interesadoWs.getRepresentante() == null) { // Interesado sin Representante

                // Lo añadimos al listado
                interesados.add(interesado);

            } else {// Interesado con Representante

                Interesado representante = DatosInteresadoConverter.getInteresado(
                        interesadoWs.getRepresentante(),
                        catPaisEjb, catProvinciaEjb, catLocalidadEjb);

                // Validar Interesado
                validateInteresado(representante, catPaisEjb, interesadoEjb, personaEjb);

                // Id aleatorio
                representante.setId((long) (Math.random() * 10000));
                representante.setIsRepresentante(true);

                // Lo asociamos con su Representado
                representante.setRepresentado(interesado);

                // Asignamos el Representante al Interesado
                interesado.setRepresentante(representante);

                // Los añadimos al listado
                interesados.add(interesado);
                interesados.add(representante);

            }

        }

        return interesados;

    }

    /**
     * @param interesado
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateInteresado(Interesado interesado, CatPaisLocal catPaisEjb, InteresadoLocal interesadoEjb, PersonaLocal personaEjb) throws I18NValidationException, I18NException {
        InteresadoBeanValidator ibv = new InteresadoBeanValidator(interesadoValidator, interesadoEjb, personaEjb, catPaisEjb);
        ibv.throwValidationExceptionIfErrors(interesado, false);
    }

    /**
     * Valida la obligatoriedad de los campos
     * @param numeroRegistro
     * @param entidad
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
     */
    protected Entidad validarObligatorios(String numeroRegistro, String entidad) throws  I18NException, Exception{

        // 1.- Comprobaciones de parámetros obligatórios
        if(StringUtils.isEmpty(entidad)){
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }

        if(StringUtils.isEmpty(numeroRegistro)){
            throw new I18NException("error.valor.requerido.ws", "numeroRegistro");
        }

        // 2.- Comprobar que la entidad existe y está activa
        return validarEntidad(entidad);

    }

    /**
     * Valida la obligatoriedad del parámetro Entidad
     * @param codigoEntidad
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
     */
    protected Entidad validarEntidad(String codigoEntidad) throws  I18NException, Exception{

        // 1.- Comprobar que la entidad existe y está activa
        if(StringUtils.isEmpty(codigoEntidad)){
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }

        Entidad entidad = entidadEjb.findByCodigoDir3(codigoEntidad);

        if(entidad == null){
            log.info("La entidad "+codigoEntidad+" no existe.");
            throw new I18NException("registro.entidad.noExiste", codigoEntidad);
        }else if(!entidad.getActivo()){
            throw new I18NException("registro.entidad.inactiva", entidad.getNombre());
        }else if(entidad.getMantenimiento()){
            throw new I18NException("registro.entidad.mantenimiento", entidad.getNombre());
        }else if(entidad.getLibro() == null){
            throw new I18NException("entidad.libro.null", entidad.getNombre());
        }else if(!entidad.getLibro().getActivo()){
            throw new I18NException("entidad.libro.inactivo", entidad.getNombre());
        }

        //Si quedan libros pendientes de procesar no se puede registrar
        if(pendienteEjb.findPendientesProcesar(entidad.getId()).size()>0){
            throw new I18NException("registro.entidad.mantenimiento", entidad.getNombre());
        }

        // 3.- Comprobamos que el Usuario pertenece a la Entidad indicada
        if (!UsuarioAplicacionCache.get().getEntidades().contains(entidad)) {
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad.");
            throw new I18NException("registro.usuario.entidad",UsuarioAplicacionCache.get().getUsuario().getNombreCompleto(), codigoEntidad);
        }

        return entidad;

    }

    /**
     * Valida el CódigoLibro indicado
     * @param codigoLibro
     * @param entidad
     * @throws I18NException
     * @throws Exception
     */
    protected Libro validarLibro(String codigoLibro, Entidad entidad) throws  I18NException, Exception{

        Libro libro = libroEjb.findByCodigoEntidad(codigoLibro, entidad.getId());

        if (libro == null) { //No existe
            throw new I18NException("registro.libro.noExiste", codigoLibro);

        } else if (!libro.getActivo()) { //Si está inactivo
            throw new I18NException("registro.libro.inactivo", codigoLibro);
        }

        return libro;
    }

    /**
     * Valida el CódigoLibro indicado y enviar una Notificación a los Administradores si el Libro indicado no es correcto
     * @param codigoLibro
     * @param entidad
     * @throws I18NException
     * @throws Exception
     */
    protected Libro validarLibroUnico(String codigoLibro, Entidad entidad) throws  I18NException, Exception{

        /*Libro libro = libroEjb.findByCodigoEntidad(codigoLibro, entidad.getId());
        String asunto = "Integración WS errónea del usuario " + UsuarioAplicacionCache.get().getUsuario().getIdentificador();

        if (libro == null) { //No existe
            String mensaje = "El usuario "+UsuarioAplicacionCache.get().getUsuario().getIdentificador()+" ha enviado una petición de nuevo asiento registral a un Libro inexistente.";
            notificacionEjb.notificacionAdminEntidad(entidad.getId(),asunto, mensaje);

        } else if (!libro.getActivo() || !libro.equals(entidad.getLibro())) { //Si está inactivo o no es el Libro único
            String mensaje = "El usuario "+UsuarioAplicacionCache.get().getUsuario().getIdentificador()+" ha enviado una petición de nuevo asiento registral a un Libro incorrecto.";
            notificacionEjb.notificacionAdminEntidad(entidad.getId(),asunto, mensaje);
        }*/

        return entidad.getLibro();
    }

    /**
     * Valida la Oficina indicada
     * @param codigoOficina
     * @param idEntidad
     * @throws I18NException
     * @throws Exception
     */
    protected Oficina validarOficina(String codigoOficina, Long idEntidad) throws  I18NException, Exception{

        Oficina oficina = oficinaEjb.findByCodigoEntidad(codigoOficina, idEntidad);

        if (oficina == null) { //No existe
            throw new I18NException("registro.oficina.noExiste", codigoOficina);

        } else if (!oficina.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.oficina.extinguido", oficina.getNombreCompleto());
        }

        return oficina;
    }


    /**
     * Obtiene un set de los identificadores del conjunto de organismos que se le pasan por parámetro
     *
     * @return
     * @throws Exception
     */
    protected Set<Long> getOrganismosOficioRemision(Set<Organismo> organismos) throws Exception {

        // Creamos un Set solo con los identificadores
        Set<Long> organismosId = new HashSet<Long>();

        for (Organismo organismo : organismos) {
            organismosId.add(organismo.getId());

        }
        return organismosId;
    }





}
