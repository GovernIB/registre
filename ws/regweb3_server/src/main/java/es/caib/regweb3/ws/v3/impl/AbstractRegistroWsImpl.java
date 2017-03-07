package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
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
import java.util.List;


/**
 * @author anadal
 */
public abstract class AbstractRegistroWsImpl extends AuthenticatedBaseWsImpl {


    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

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

            validateAnexo(anexoFull.getAnexo(), true);

/*
         anexo = anexoEjb.actualizarAnexoConArchivos(anexo.getId(),,
             ,
             ,
             anexoWs.getTamanoFicheroAnexado(), , ,
             
            ,
             anexoWs.getTamanoFirmaAnexada(), anexoWs.getModoFirma(), 
             anexoWs.getFechaCaptura().getTime());
         */

            // Actualizamos el anexo y le asociamos los archivos
            //anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, tipoRegistro);

            anexos.add(anexoFull);
        }

        return anexos;

    }


    /**
     * @param anexo
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    protected void validateAnexo(Anexo anexo, boolean isNou) throws I18NValidationException, I18NException {
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
     * @param usuario
     * @param entidad
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
     */
    protected void validarObligatorios(String numeroRegistro, String usuario, String entidad) throws  I18NException, Exception{

        // 1.- Comprobaciones de parámetros obligatórios
        if(StringUtils.isEmpty(numeroRegistro)){
            throw new I18NException("error.valor.requerido.ws", "identificador");
        }

        if(StringUtils.isEmpty(usuario)){
            throw new I18NException("error.valor.requerido.ws", "usuario");
        }

        if(StringUtils.isEmpty(entidad)){
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }

    }


}
