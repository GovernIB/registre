package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.CatLocalidad;
import es.caib.regweb3.model.Persona;
import es.caib.regweb3.persistence.ejb.CatLocalidadLocal;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.persistence.ejb.EntidadLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.model.PersonaWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

/**
 * @author anadal
 */
public class PersonaConverter extends CommonConverter {


    public static Persona getPersona(PersonaWs persona,
                                     CatPaisLocal catPaisEjb, CatProvinciaLocal catProvinciaEjb,
                                     CatLocalidadLocal catLocalidadEjb,
                                     EntidadLocal entidadEjb) throws Exception, I18NException {
        if (persona == null) {
            return null;
        }

        Persona personabean = new Persona();

        personabean.setId(persona.getId());
        personabean.setNombre(persona.getNombre());
        personabean.setApellido1(persona.getApellido1());
        personabean.setApellido2(persona.getApellido2());
        personabean.setRazonSocial(persona.getRazonSocial());

        personabean.setTipoDocumentoIdentificacion(
                RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(
                        persona.getTipoDocumentoIdentificacionNTI()));
        personabean.setDocumento(persona.getDocumento());
        personabean.setPais(getPais(persona.getPaisDir3ID(), catPaisEjb));
        personabean.setProvincia(getProvincia(persona.getProvinciaDir3ID(), catProvinciaEjb));
        personabean.setLocalidad(getLocalidad(persona.getLocalidadDir3ID(),
                persona.getProvinciaDir3ID(), persona.getCodigoEntidadGeograficaDir3ID(),
                catLocalidadEjb));

        personabean.setDireccion(persona.getDireccion());
        personabean.setCp(persona.getCp());
        personabean.setEmail(persona.getEmail());
        personabean.setTelefono(persona.getTelefono());
        personabean.setDireccionElectronica(persona.getDireccionElectronica());
        personabean.setCanal(persona.getCanal());

        personabean.setTipo(persona.getTipoPersonaID());

   /* personabean.setRepresentado(getPersona(persona.getRepresentado(),
        tipoDocumentoIdentificacionEjb, catPaisEjb, catProvinciaEjb, catLocalidadEjb,
        canalNotificacionEjb, tipoPersonaEjb, entidadEjb));
    personabean.setRepresentante(getPersona(persona.getRepresentante(),
        tipoDocumentoIdentificacionEjb, catPaisEjb, catProvinciaEjb, catLocalidadEjb,
        canalNotificacionEjb, tipoPersonaEjb, entidadEjb));
    personabean.setIsRepresentante(persona.getIsRepresentante());
*/
        personabean.setObservaciones(persona.getObservaciones());

        personabean.setEntidad(getEntidad(persona.getEntidadDir3ID(), entidadEjb));

        personabean.setGuardarInteresado(persona.isGuardarInteresado());

        return personabean;
    }

    public static PersonaWs getPersonaWs(Persona persona) throws Exception {
        if (persona == null) {
            return null;
        }

        PersonaWs iws = new PersonaWs();

        iws.setId(persona.getId());
        iws.setNombre(persona.getNombre());
        iws.setApellido1(persona.getApellido1());
        iws.setApellido2(persona.getApellido2());
        iws.setRazonSocial(persona.getRazonSocial());
        iws.setTipoDocumentoIdentificacionNTI(
                RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(
                        persona.getTipoDocumentoIdentificacion()));
        iws.setDocumento(persona.getDocumento());
        iws.setPaisDir3ID(getPaisDir3ID(persona.getPais()));
        iws.setProvinciaDir3ID(getProvinciaDir3ID(persona.getProvincia()));

        CatLocalidad localidad = persona.getLocalidad();

        iws.setLocalidadDir3ID(getLocalidadDir3ID(localidad));

        iws.setCodigoEntidadGeograficaDir3ID(getCodigoEntidadGeograficaDir3ID(localidad));

        iws.setDireccion(persona.getDireccion());
        iws.setCp(persona.getCp());
        iws.setEmail(persona.getEmail());
        iws.setTelefono(persona.getTelefono());
        iws.setDireccionElectronica(persona.getDireccionElectronica());
        iws.setCanal(persona.getCanal());

        iws.setTipoPersonaID(persona.getTipo());

   /* iws.setRepresentado(getPersonaWs(persona.getRepresentado()));
    iws.setRepresentante(getPersonaWs(persona.getRepresentante()));
    iws.setIsRepresentante(persona.getIsRepresentante());*/

        iws.setObservaciones(persona.getObservaciones());

        iws.setEntidadDir3ID(getEntidadCodigoDir3(persona.getEntidad()));

        iws.setGuardarInteresado(persona.isGuardarInteresado());

        return iws;
    }


}
