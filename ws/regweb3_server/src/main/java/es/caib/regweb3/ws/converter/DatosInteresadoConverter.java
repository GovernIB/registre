package es.caib.regweb3.ws.converter;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.persistence.ejb.CatLocalidadLocal;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.DatosInteresadoWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.v3.impl.CommonConverter;
import org.fundaciobit.genapp.common.i18n.I18NException;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
public class DatosInteresadoConverter extends CommonConverter {

    /**
     * Convierte un {@link es.caib.regweb3.ws.model.DatosInteresadoWs} en un {@link es.caib.regweb3.model.Interesado}
     * @param datosInteresadoWs
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public static Interesado getInteresado(DatosInteresadoWs datosInteresadoWs,
        CatPaisLocal catPaisEjb, CatProvinciaLocal catProvinciaEjb,
        CatLocalidadLocal catLocalidadEjb)
            throws Exception, I18NException{

        if (datosInteresadoWs == null){
            return  null;
        }

        Interesado interesado = procesarInteresado(datosInteresadoWs,
            catPaisEjb, catProvinciaEjb, catLocalidadEjb);


        return  interesado;
    }

    /**
     * Convierte un {@link es.caib.regweb3.model.Interesado} en un {@link es.caib.regweb3.ws.model.InteresadoWs}
     * @param interesado
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public static InteresadoWs getInteresadoWs(Interesado interesado)  throws Exception{

        if (interesado == null){
            return  null;
        }

        InteresadoWs interesadoWs =  new InteresadoWs();

        interesadoWs.setInteresado(procesarDatosInteresadoWs(interesado));

        // Representante
        if(interesado.getRepresentante() != null){

            DatosInteresadoWs datosRepresentanteWs = procesarDatosInteresadoWs(interesado.getRepresentante());

            interesadoWs.setRepresentante(datosRepresentanteWs);
        }

        return interesadoWs;
    }

    private static Interesado procesarInteresado(DatosInteresadoWs datosInteresadoWs,
        CatPaisLocal catPaisEjb, CatProvinciaLocal catProvinciaEjb, 
        CatLocalidadLocal catLocalidadEjb)
            throws Exception, I18NException{

        Interesado interesado = new Interesado();

        if(datosInteresadoWs.getTipoInteresado() != null){interesado.setTipo(datosInteresadoWs.getTipoInteresado());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getNombre())){interesado.setNombre(datosInteresadoWs.getNombre());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getApellido1())){interesado.setApellido1(datosInteresadoWs.getApellido1());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getApellido2())){interesado.setApellido2(datosInteresadoWs.getApellido2());}
        
        if(datosInteresadoWs.getTipoDocumentoIdentificacion() != null) {
          interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(datosInteresadoWs.getTipoDocumentoIdentificacion()));
        }
        
        if(!StringUtils.isEmpty(datosInteresadoWs.getDocumento())){interesado.setDocumento(datosInteresadoWs.getDocumento());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getRazonSocial())){interesado.setRazonSocial(datosInteresadoWs.getRazonSocial());}
        if(datosInteresadoWs.getPais() != null){interesado.setPais(getPais(datosInteresadoWs.getPais(), catPaisEjb));}
        if(datosInteresadoWs.getProvincia() != null){interesado.setProvincia(getProvincia(datosInteresadoWs.getProvincia(), catProvinciaEjb));}
        if(datosInteresadoWs.getLocalidad() != null && datosInteresadoWs.getProvincia() != null){interesado.setLocalidad(getLocalidad(datosInteresadoWs.getLocalidad(), datosInteresadoWs.getProvincia(), catLocalidadEjb));}
        if(!StringUtils.isEmpty(datosInteresadoWs.getDireccion())){interesado.setDireccion(datosInteresadoWs.getDireccion());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getCp())){interesado.setCp(datosInteresadoWs.getCp());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getEmail())){interesado.setEmail(datosInteresadoWs.getEmail());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getTelefono())){interesado.setTelefono(datosInteresadoWs.getTelefono());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getDireccionElectronica())){interesado.setDireccionElectronica(datosInteresadoWs.getDireccionElectronica());}
        if(datosInteresadoWs.getCanal() != null){interesado.setCanal(datosInteresadoWs.getCanal());}
        if(!StringUtils.isEmpty(datosInteresadoWs.getObservaciones())){interesado.setObservaciones(datosInteresadoWs.getObservaciones());}

        return interesado;

    }


    /**
     * Convierte un {@link es.caib.regweb3.model.Interesado} en un {@link es.caib.regweb3.ws.model.DatosInteresadoWs}
     * @param interesado
     * @return
     * @throws Exception
     */
    private static DatosInteresadoWs procesarDatosInteresadoWs(Interesado interesado) throws Exception{

        DatosInteresadoWs datosInteresadoWs = new DatosInteresadoWs();

        datosInteresadoWs.setTipoInteresado(interesado.getTipo());

        if(!StringUtils.isEmpty(interesado.getNombre())){datosInteresadoWs.setNombre(interesado.getNombre());}
        if(!StringUtils.isEmpty(interesado.getApellido1())){datosInteresadoWs.setApellido1(interesado.getApellido1());}
        if(!StringUtils.isEmpty(interesado.getApellido2())){datosInteresadoWs.setApellido2(interesado.getApellido2());}

        if(interesado.getTipoDocumentoIdentificacion() != null) {
            datosInteresadoWs.setTipoDocumentoIdentificacion(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion()).toString());
        }

        if(!StringUtils.isEmpty(interesado.getDocumento())){datosInteresadoWs.setDocumento(interesado.getDocumento());}
        if(!StringUtils.isEmpty(interesado.getRazonSocial())){datosInteresadoWs.setRazonSocial(interesado.getRazonSocial());}
        if(interesado.getPais() != null){datosInteresadoWs.setPais(interesado.getPais().getCodigoPais());}
        if(interesado.getProvincia() != null){datosInteresadoWs.setProvincia(interesado.getProvincia().getCodigoProvincia());}
        if(interesado.getLocalidad() != null && interesado.getProvincia() != null){datosInteresadoWs.setLocalidad(interesado.getLocalidad().getCodigoLocalidad());}
        if(!StringUtils.isEmpty(interesado.getDireccion())){datosInteresadoWs.setDireccion(interesado.getDireccion());}
        if(!StringUtils.isEmpty(interesado.getCp())){datosInteresadoWs.setCp(interesado.getCp());}
        if(!StringUtils.isEmpty(interesado.getEmail())){datosInteresadoWs.setEmail(interesado.getEmail());}
        if(!StringUtils.isEmpty(interesado.getTelefono())){datosInteresadoWs.setTelefono(interesado.getTelefono());}
        if(!StringUtils.isEmpty(interesado.getDireccionElectronica())){datosInteresadoWs.setDireccionElectronica(interesado.getDireccionElectronica());}
        if(interesado.getCanal() != null){datosInteresadoWs.setCanal(interesado.getCanal());}
        if(!StringUtils.isEmpty(interesado.getObservaciones())){datosInteresadoWs.setObservaciones(interesado.getObservaciones());}

        return datosInteresadoWs;
    }
}
