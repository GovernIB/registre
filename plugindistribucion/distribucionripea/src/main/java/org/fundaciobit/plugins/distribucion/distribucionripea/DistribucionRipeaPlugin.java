package org.fundaciobit.plugins.distribucion.distribucionripea;


import es.caib.regweb3.model.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.ripea.ws.v1.bustia.BustiaV1;
import es.caib.ripea.ws.v1.bustia.RegistreAnnex;
import es.caib.ripea.ws.v1.bustia.RegistreAnotacio;
import es.caib.ripea.ws.v1.bustia.RegistreInteressat;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.distribucion.ConfiguracionDistribucion;
import org.fundaciobit.plugins.distribucion.Destinatario;
import org.fundaciobit.plugins.distribucion.Destinatarios;
import org.fundaciobit.plugins.distribucion.IDistribucionPlugin;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

import java.util.List;
import java.util.Properties;

/**
 * @author mgonzalez
 */
public class DistribucionRipeaPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = Logger.getLogger(getClass());


    String basePluginRipea = ".plugins.distribucion.distribucionripea";
    String PROPERTY_USUARIO = basePluginRipea + ".usuario";
    String PROPERTY_PASSWORD = basePluginRipea + ".password";
    String PROPERTY_ENDPOINT = basePluginRipea + ".endpoint";


    public String getPropertyUsuario() throws Exception {
        //return getProperty(PROPERTY_EXEMPLE);
        log.info("PROPERTY_USUARIO : " + PROPERTY_USUARIO);

        return getPropertyRequired(PROPERTY_USUARIO);
    }

    public String getPropertyPassword() throws Exception {
        //return getProperty(PROPERTY_EXEMPLE);

        return getPropertyRequired(PROPERTY_PASSWORD);
    }

    public String getPropertyEndPoint() throws Exception {
        //return getProperty(PROPERTY_EXEMPLE);

        return getPropertyRequired(PROPERTY_ENDPOINT);
    }

    /**
     *
     */
    public DistribucionRipeaPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionRipeaPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionRipeaPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    @Override
    public Destinatarios distribuir(RegistroEntrada registro) throws Exception {

        //nothing to do
        return null;
    }

    @Override
    public Boolean enviarDestinatarios(RegistroEntrada registro, List<Destinatario> destinatariosDefinitivos, String observaciones) throws Exception {
        //Invocar ws de ripea

        String endpoint = getPropertyEndPoint();
        String usuario = getPropertyUsuario();
        String password = getPropertyPassword();


        BustiaV1 client = es.caib.ripea.ws.client.BustiaV1WsClientFactory.getWsClient(
                endpoint,
                usuario,
                password);


        //Transformamos a registreAnotació. TODO VER QUE CAMPOS DE REGISTRO ENVIAMOS A RIPEA
        RegistreAnotacio registreAnotacio = transformarARegistreAnotacio(registro);


        //Parte de interesados
        List<RegistreInteressat> registresInteressats = registreAnotacio.getInteressats();
        for (Interesado interesado : registro.getRegistroDetalle().getInteresados()) {
            RegistreInteressat registreInteressat = transformarARegistreInteressat(interesado);
            RegistreInteressat representant = transformarARegistreInteressat(interesado.getRepresentante());
            registreInteressat.setRepresentant(representant);
            registresInteressats.add(registreInteressat);
        }

        List<RegistreAnnex> registreAnnexes = registreAnotacio.getAnnexos();
        for (Anexo anexo : registro.getRegistroDetalle().getAnexos()) {
            //Transformar a RegistreAnnex
            RegistreAnnex registreAnnex = new RegistreAnnex();
            registreAnnex.setFitxerGestioDocumentalId(anexo.getCustodiaID());
            registreAnnexes.add(registreAnnex);
        }


        //Demanar a n'en Victor
        String entidadCodigo = "A04003003";
       // String unidadAdministrativaCodigo = "A04003003";
        // String entidadCodigo = registro.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3();
        // String unidadAdministrativaCodigo= registro.getOficina().getOrganismoResponsable().getCodigo();????
         String unidadAdministrativaCodigo= registro.getDestino().getCodigo();


        client.enviarAnotacioRegistreEntrada(entidadCodigo, unidadAdministrativaCodigo, registreAnotacio);


        return true;

    }

    @Override
    public ConfiguracionDistribucion configurarDistribucion() throws Exception {
        //especifica que información se enviará en el segmento de anexo del registro de entrada.
        /* 1 = custodiaId + metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
        *  2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
        *  3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
        * */
        ConfiguracionDistribucion cd = new ConfiguracionDistribucion(false, 2);
        return cd;

    }


    /**
     * Método que transforma de RegistroEntrada a RegistreAnotacio de Ripea.
     *
     * @param re
     * @return
     * @throws Exception
     */
    private RegistreAnotacio transformarARegistreAnotacio(RegistroEntrada re) throws Exception {
        RegistreAnotacio registreAnotacio = new RegistreAnotacio();

        registreAnotacio.setIdentificador(re.getId().toString());
        registreAnotacio.setAplicacioCodi(re.getRegistroDetalle().getAplicacion());
        registreAnotacio.setAplicacioVersio(re.getRegistroDetalle().getVersion());
        registreAnotacio.setAssumpteCodi(re.getRegistroDetalle().getTipoAsunto().getCodigo());

        TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) re.getRegistroDetalle().getTipoAsunto().getTraduccion();
        registreAnotacio.setAssumpteDescripcio(traduccionTipoAsunto.getNombre());
        if (re.getRegistroDetalle().getCodigoAsunto() != null) {
            registreAnotacio.setAssumpteTipusCodi(re.getRegistroDetalle().getCodigoAsunto().getCodigo());
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) re.getRegistroDetalle().getCodigoAsunto().getTraduccion();
            registreAnotacio.setAssumpteTipusDescripcio(traduccionCodigoAsunto.getNombre());
        }

        registreAnotacio.setData(null); //TODO XMLGregorianCalendar
        String codiDF = re.getRegistroDetalle().getTipoDocumentacionFisica().toString();
        registreAnotacio.setDocumentacioFisicaCodi(codiDF);
        //TODO Es crea dependencia al genapp.
        String descripcionTipoDocFisica = I18NUtils.tradueix("tipoDocumentacionFisica." + codiDF);
        log.info(descripcionTipoDocFisica);
        registreAnotacio.setDocumentacioFisicaDescripcio(descripcionTipoDocFisica);
        registreAnotacio.setEntitatCodi(re.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3());
        registreAnotacio.setEntitatDescripcio(re.getOficina().getOrganismoResponsable().getEntidad().getNombre());
        registreAnotacio.setExpedientNumero(re.getRegistroDetalle().getExpediente());
        registreAnotacio.setExposa(re.getRegistroDetalle().getExpone());
        registreAnotacio.setExtracte(re.getRegistroDetalle().getExtracto());
        registreAnotacio.setIdiomaCodi(re.getRegistroDetalle().getIdioma().toString());
        registreAnotacio.setIdiomaDescripcio(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(re.getRegistroDetalle().getIdioma()));
        registreAnotacio.setLlibreCodi(re.getLibro().getCodigo());
        registreAnotacio.setLlibreDescripcio(re.getLibro().getNombre());
        registreAnotacio.setNumero(re.getNumeroRegistro());
        registreAnotacio.setReferencia(re.getRegistroDetalle().getReferenciaExterna());
        registreAnotacio.setSolicita(re.getRegistroDetalle().getSolicita());
        registreAnotacio.setTransportNumero(re.getRegistroDetalle().getNumeroTransporte());
        String codigoTransporte = re.getRegistroDetalle().getTransporte().toString();
        registreAnotacio.setTransportTipusCodi(codigoTransporte);
        String transporte = I18NUtils.tradueix("transporte." + codigoTransporte);
        registreAnotacio.setTransportTipusDescripcio(transporte);
        registreAnotacio.setUsuariCodi(re.getUsuario().getId().toString());
        registreAnotacio.setUsuariContacte(re.getUsuario().getUsuario().getEmail());
        registreAnotacio.setUsuariNom(re.getUsuario().getNombreCompleto());

        return registreAnotacio;

    }

    private RegistreInteressat transformarARegistreInteressat(Interesado interesado) throws Exception {
        RegistreInteressat registreInteressat = new RegistreInteressat();

        registreInteressat.setAdresa(interesado.getDireccion());
        String canalNotificacion = I18NUtils.tradueix("canalNotificacion." + interesado.getCanal());
        registreInteressat.setCanalPreferent(canalNotificacion);
        registreInteressat.setCodiPostal(interesado.getCp());
        registreInteressat.setDocumentNum(interesado.getDocumento());

        String tipoDocumentoIdentificacion = I18NUtils.tradueix("tipoDocumentoIdentificacion." + interesado.getTipoDocumentoIdentificacion());
        registreInteressat.setDocumentTipus(tipoDocumentoIdentificacion);
        registreInteressat.setEmail(interesado.getEmail());
        registreInteressat.setEmailHabilitat(interesado.getDireccionElectronica());
        registreInteressat.setNom(interesado.getNombre());
        registreInteressat.setLlinatge1(interesado.getApellido1());
        registreInteressat.setLlinatge2(interesado.getApellido2());
        CatLocalidad localidad = interesado.getLocalidad();
        if (localidad != null) {
            registreInteressat.setMunicipi(localidad.getNombre());
        }
        registreInteressat.setObservacions(interesado.getObservaciones());
        CatPais pais = interesado.getPais();
        if (pais != null) {
            registreInteressat.setPais(pais.getDescripcionPais());
        }
        CatProvincia provincia = interesado.getProvincia();
        if (provincia != null) {
            registreInteressat.setProvincia(provincia.getDescripcionProvincia());
        }
        registreInteressat.setRaoSocial(interesado.getRazonSocial());
        registreInteressat.setTelefon(interesado.getTelefono());
        String tipo = I18NUtils.tradueix("interesado.tipo." + interesado.getTipo());
        registreInteressat.setTipus(tipo);

        return registreInteressat;


    }


}