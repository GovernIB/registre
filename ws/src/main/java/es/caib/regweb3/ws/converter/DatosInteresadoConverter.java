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
        if(StringUtils.isNotEmpty(datosInteresadoWs.getNombre())){interesado.setNombre(datosInteresadoWs.getNombre());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getApellido1())){interesado.setApellido1(datosInteresadoWs.getApellido1());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getApellido2())){interesado.setApellido2(datosInteresadoWs.getApellido2());}
        
        if(StringUtils.isNotEmpty(datosInteresadoWs.getTipoDocumentoIdentificacion())) {
            interesado.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_BY_CODIGO_NTI.get(datosInteresadoWs.getTipoDocumentoIdentificacion().charAt(0)));
        }
        
        if(StringUtils.isNotEmpty(datosInteresadoWs.getDocumento())){
            interesado.setDocumento(datosInteresadoWs.getDocumento());
            // Si es un Interesado tipo Administración, copiamos el Documento al campo CodigoDir3
            if(datosInteresadoWs.getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){
                interesado.setCodigoDir3(datosInteresadoWs.getDocumento());
            }
        }

        if(StringUtils.isNotEmpty(datosInteresadoWs.getRazonSocial())){interesado.setRazonSocial(datosInteresadoWs.getRazonSocial());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getCodigoDire())){interesado.setCodigoDire(datosInteresadoWs.getCodigoDire());}
        if(datosInteresadoWs.getPais() != null){interesado.setPais(getPais(datosInteresadoWs.getPais(), catPaisEjb));}
        if(datosInteresadoWs.getProvincia() != null){interesado.setProvincia(getProvincia(datosInteresadoWs.getProvincia(), catProvinciaEjb));}
        if(datosInteresadoWs.getLocalidad() != null && datosInteresadoWs.getProvincia() != null){interesado.setLocalidad(getLocalidad(datosInteresadoWs.getLocalidad(), datosInteresadoWs.getProvincia(), catLocalidadEjb));}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getDireccion())){interesado.setDireccion(datosInteresadoWs.getDireccion());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getCp())){interesado.setCp(datosInteresadoWs.getCp());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getEmail())){interesado.setEmail(datosInteresadoWs.getEmail());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getTelefono())){interesado.setTelefono(datosInteresadoWs.getTelefono());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getDireccionElectronica())){interesado.setDireccionElectronica(datosInteresadoWs.getDireccionElectronica());}
        if(datosInteresadoWs.getCanal() != null){interesado.setCanal(datosInteresadoWs.getCanal());}
        if(StringUtils.isNotEmpty(datosInteresadoWs.getObservaciones())){interesado.setObservaciones(datosInteresadoWs.getObservaciones());}

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

        if(StringUtils.isNotEmpty(interesado.getNombre())){datosInteresadoWs.setNombre(interesado.getNombre());}
        if(StringUtils.isNotEmpty(interesado.getApellido1())){datosInteresadoWs.setApellido1(interesado.getApellido1());}
        if(StringUtils.isNotEmpty(interesado.getApellido2())){datosInteresadoWs.setApellido2(interesado.getApellido2());}

        if(interesado.getTipoDocumentoIdentificacion() != null) {
            datosInteresadoWs.setTipoDocumentoIdentificacion(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion()).toString());
        }

        if(StringUtils.isNotEmpty(interesado.getDocumento())){datosInteresadoWs.setDocumento(interesado.getDocumento());}
        if(StringUtils.isNotEmpty(interesado.getRazonSocial())){datosInteresadoWs.setRazonSocial(interesado.getRazonSocial());}
        if(StringUtils.isNotEmpty(interesado.getCodigoDire())){datosInteresadoWs.setCodigoDire(interesado.getCodigoDire());}
        if(interesado.getPais() != null){datosInteresadoWs.setPais(interesado.getPais().getCodigoPais());}
        if(interesado.getProvincia() != null){datosInteresadoWs.setProvincia(interesado.getProvincia().getCodigoProvincia());}
        if(interesado.getLocalidad() != null && interesado.getProvincia() != null){datosInteresadoWs.setLocalidad(interesado.getLocalidad().getCodigoLocalidad());}
        if(StringUtils.isNotEmpty(interesado.getDireccion())){datosInteresadoWs.setDireccion(interesado.getDireccion());}
        if(StringUtils.isNotEmpty(interesado.getCp())){datosInteresadoWs.setCp(interesado.getCp());}
        if(StringUtils.isNotEmpty(interesado.getEmail())){datosInteresadoWs.setEmail(interesado.getEmail());}
        if(StringUtils.isNotEmpty(interesado.getTelefono())){datosInteresadoWs.setTelefono(interesado.getTelefono());}
        if(StringUtils.isNotEmpty(interesado.getDireccionElectronica())){datosInteresadoWs.setDireccionElectronica(interesado.getDireccionElectronica());}
        if(interesado.getCanal() != null){datosInteresadoWs.setCanal(interesado.getCanal());}
        if(StringUtils.isNotEmpty(interesado.getObservaciones())){datosInteresadoWs.setObservaciones(interesado.getObservaciones());}

        return datosInteresadoWs;
    }
}
