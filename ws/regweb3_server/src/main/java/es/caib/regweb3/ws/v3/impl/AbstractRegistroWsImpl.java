package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.validator.AnexoBeanValidator;
import es.caib.regweb3.persistence.validator.AnexoValidator;
import es.caib.regweb3.persistence.validator.InteresadoBeanValidator;
import es.caib.regweb3.persistence.validator.InteresadoValidator;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.AnexoConverter;
import es.caib.regweb3.ws.converter.DatosInteresadoConverter;
import es.caib.regweb3.ws.model.AnexoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.utils.AuthenticatedBaseWsImpl;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author anadal
 */
public abstract class AbstractRegistroWsImpl extends AuthenticatedBaseWsImpl {


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

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    public AnexoValidator<Anexo> anexoValidator = new AnexoValidator<Anexo>();

    InteresadoValidator<Interesado> interesadoValidator = new InteresadoValidator<Interesado>();

    /**
     * Procesa los Anexos recibidos
     *
     * @param anexosWs
     * @return
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    protected List<AnexoFull> procesarAnexos(List<AnexoWs> anexosWs, Long entidadID) throws Exception, I18NValidationException, I18NException {

        List<AnexoFull> anexos = new ArrayList<AnexoFull>();

        for (AnexoWs anexoWs : anexosWs) {
            //Convertimos a anexo

            AnexoFull anexoFull = AnexoConverter.getAnexoFull(anexoWs, entidadID, tipoDocumentalEjb);

            //Controlamos el tamanyo de los ficheros que nos adjuntan.
            Long maxUploadSizeInBytes = PropiedadGlobalUtil.getMaxUploadSizeInBytes(entidadID);
            if(maxUploadSizeInBytes!= null){ // Si no está especificada, se permite cualquier tamaño
                switch (anexoWs.getModoFirma()){
                    case 0: //SIN FIRMA
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

            validateAnexo(anexoFull, true,entidadID);

            anexos.add(anexoFull);
        }

        return anexos;

    }


    /**
     * @param anexoFull
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    protected void validateAnexo(AnexoFull anexoFull, boolean isNou, Long entidadId) throws I18NValidationException, I18NException {

        //Validamos el anexo contra afirma

        //anexoValidator.validateStandalone(anexo);
        AnexoBeanValidator pfbv = new AnexoBeanValidator(anexoValidator);

        //final boolean isNou = true;
        pfbv.throwValidationExceptionIfErrors(anexoFull.getAnexo(), isNou);
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
                log.info("interesadoWs tiene represenante");

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
    protected void validarObligatorios(String numeroRegistro, String entidad) throws  I18NException, Exception{

        // 1.- Comprobaciones de parámetros obligatórios
        if(StringUtils.isEmpty(numeroRegistro)){
            throw new I18NException("error.valor.requerido.ws", "identificador");
        }
        if(StringUtils.isEmpty(entidad)){
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }

    }

    /**
     * Valida la obligatoriedad de los campos
     * @param numeroRegistro
     * @param entidad
     * @param libro
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
     */
    protected void validarObligatorios(String numeroRegistro, String entidad, String libro) throws  I18NException, Exception{

        validarObligatorios(numeroRegistro,entidad);

        if(StringUtils.isEmpty(libro)){
            throw new I18NException("error.valor.requerido.ws", "libro");
        }

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
