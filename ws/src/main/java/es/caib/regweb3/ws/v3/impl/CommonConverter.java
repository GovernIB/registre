package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.converter.AnexoConverter;
import es.caib.regweb3.ws.converter.DatosInteresadoConverter;
import es.caib.regweb3.ws.model.*;
import org.fundaciobit.genapp.common.i18n.I18NException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anadal
 */
public class CommonConverter {

    public static Long getIdiomaRegistro(String codigo) throws Exception {
        if (codigo == null) {
            return null;
        }
        return RegwebConstantes.IDIOMA_ID_BY_CODIGO.get(codigo);
    }


    public static TipoAsunto getTipoAsunto(String codigo, Long idEntidad, TipoAsuntoLocal tipoAsuntoEjb) throws Exception {
        if (codigo == null) {
            return null;
        }
        return tipoAsuntoEjb.findByCodigoEntidad(codigo, idEntidad);
    }


    public static CodigoAsunto getCodigoAsunto(String codigo, CodigoAsuntoLocal codigoAsuntoEjb) throws Exception {
        return codigoAsuntoEjb.findByCodigo(codigo);
    }

    public static Long getPaisDir3ID(CatPais pais) {
        if (pais == null) {
            return null;
        }
        return pais.getCodigoPais();
    }

    public static CatPais getPais(Long paisID, CatPaisLocal catPaisEjb) throws Exception {
        if (paisID == null) {
            return null;
        }
        return catPaisEjb.findByCodigo(paisID);
    }

    public static Long getProvinciaDir3ID(CatProvincia prov) {
        if (prov == null) {
            return null;
        }
        return prov.getCodigoProvincia();
    }

    public static CatProvincia getProvincia(Long provDir3ID, CatProvinciaLocal catProvinciaEjb)
            throws Exception {
        if (provDir3ID == null) {
            return null;
        }
        return catProvinciaEjb.findByCodigo(provDir3ID);
    }

    public static Long getLocalidadDir3ID(CatLocalidad loc) {
        if (loc == null) {
            return null;
        }
        return loc.getCodigoLocalidad();
    }

    public static CatLocalidad getLocalidad(Long locDir3ID, Long provDir3ID,
                                            String codigoEntidadGeograficaDir3ID, CatLocalidadLocal catLocalidadEjb)
            throws I18NException, Exception {
        if (locDir3ID == null) {
            return null;
        }
        if (provDir3ID == null || codigoEntidadGeograficaDir3ID == null) {
            // Si defineix el camp locDir3ID llavors es necessari
            // que els camp provDir3ID i codigoEntidadGeograficaDir3ID no siguin nulls
            throw new I18NException("error.locDir3ID.requiereotroscampos");
        }

        return catLocalidadEjb.findByCodigo(locDir3ID, provDir3ID, codigoEntidadGeograficaDir3ID);
    }

    public static CatLocalidad getLocalidad(Long locDir3ID, Long provDir3ID, CatLocalidadLocal catLocalidadEjb)
            throws I18NException, Exception {
        if (locDir3ID == null) {
            return null;
        }
        if (provDir3ID == null) {
            // Si defineix el camp locDir3ID llavors es necessari
            throw new I18NException("error.locDir3ID.requiereotroscampos");
        }

        return catLocalidadEjb.findByLocalidadProvincia(locDir3ID, provDir3ID);
    }


    public static String getEntidadCodigoDir3(Entidad entidad) {
        if (entidad == null) {
            return null;
        }
        return entidad.getCodigoDir3();
    }

    public static Entidad getEntidad(String entidadCodigoDir3, EntidadLocal entidadEjb) throws Exception {
        if (entidadCodigoDir3 == null) {
            return null;
        }
        return entidadEjb.findByCodigoDir3(entidadCodigoDir3);
    }

    public static String getCodigoEntidadGeograficaDir3ID(CatLocalidad localidad) {
        if (localidad == null || localidad.getEntidadGeografica() == null) {
            return null;
        }
        return localidad.getEntidadGeografica().getCodigoEntidadGeografica();
    }


    public static LibroWs getLibroWs(Libro libro) {
        if (libro == null) {
            return null;
        }
        LibroWs libroWs = new LibroWs();

        libroWs.setCodigoLibro(libro.getCodigo());
        libroWs.setCodigoOrganismo(libro.getOrganismo().getCodigo());
        libroWs.setNombreCorto(libro.getNombre());
        libroWs.setNombreLargo(libro.getNombreCompleto());

        return libroWs;

    }

    public static TipoDocumentalWs getTipoDocumentalWs(TipoDocumental tipoDocumental) {
        if (tipoDocumental == null) {
            return null;
        }

        TipoDocumentalWs tipoDocumentalWs = new TipoDocumentalWs();
        tipoDocumentalWs.setCodigoNTI(tipoDocumental.getCodigoNTI());

        return tipoDocumentalWs;
    }

    public static CodigoAsuntoWs getCodigoAsuntoWs(CodigoAsunto codigoAsunto, String idioma) {
        if (codigoAsunto == null) {
            return null;
        }

        CodigoAsuntoWs caWs = new CodigoAsuntoWs();


        caWs.setCodigo(codigoAsunto.getCodigo());
        caWs.setNombre(((TraduccionCodigoAsunto) codigoAsunto.getTraduccion(idioma)).getNombre());

        return caWs;
    }


    public static OrganismoWs getOrganismoWs(Organismo organismo) {
        if (organismo == null) {
            return null;
        }

        OrganismoWs taWs = new OrganismoWs();

        taWs.setCodigo(organismo.getCodigo());
        taWs.setNombre(organismo.getDenominacion());

        return taWs;
    }

    public static OficinaWs getOficinaWs(Oficina oficina) {
        if (oficina == null) {
            return null;
        }

        OficinaWs oficinaWs = new OficinaWs();

        oficinaWs.setCodigo(oficina.getCodigo());
        oficinaWs.setNombre(oficina.getDenominacion());

        return oficinaWs;
    }

    public static TipoDocumental getTipoDocumental(String tipoDocCodigoNTI, Long idEntidad,
                                                   TipoDocumentalLocal tipoDocumentalEjb) throws Exception {
        if (tipoDocCodigoNTI == null) {
            return null;
        }

        return tipoDocumentalEjb.findByCodigoEntidad(tipoDocCodigoNTI, idEntidad);

    }

    public static Long getTipoValidezDocumento(String tValDocCodigoSicres) throws Exception {

        return (tValDocCodigoSicres == null) ? null : RegwebConstantes.TIPOVALIDEZDOCUMENTO_BY_CODIGO_SICRES.get(tValDocCodigoSicres);

    }

    /**
     * @param registroDetalle
     * @return
     * @throws Exception
     */
    public static List<AnexoWs> transformarAnexosWs(RegistroDetalle registroDetalle) throws Exception {

        List<AnexoWs> anexosWs = new ArrayList<AnexoWs>();

        // Si se trata de AnexoFull
        if(registroDetalle.getAnexosFull() != null){

            for (AnexoFull anexoFull : registroDetalle.getAnexosFull()) {
                AnexoWs anexoWs = AnexoConverter.transformarAnexoWs(anexoFull);
                anexosWs.add(anexoWs);
            }

        }else{ // Si se trata de Anexo

            for (Anexo anexo : registroDetalle.getAnexos()) {
                AnexoWs anexoWs = AnexoConverter.transformarAnexoWs(anexo);
                anexosWs.add(anexoWs);
            }
        }

        return anexosWs;
    }


    /**
     * @param interesados
     * @return
     * @throws Exception
     */
    public static List<InteresadoWs> procesarInteresadosWs(List<Interesado> interesados) throws Exception {

        List<InteresadoWs> interesadosWs = new ArrayList<InteresadoWs>();

        for (Interesado interesado : interesados) {

            if(!interesado.getIsRepresentante()){ // Solo si es Interesado
                InteresadoWs interesadoWs = DatosInteresadoConverter.getInteresadoWs(interesado);

                interesadosWs.add(interesadoWs);
            }

        }

        return interesadosWs;
    }


}
