package es.caib.regweb3.sir.core.utils;

import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.sir.TipoTransporte;
import es.caib.regweb3.model.utils.TipoRegistro;
import es.caib.regweb3.sir.core.schema.De_Internos_Control;
import es.caib.regweb3.sir.core.schema.Fichero_Intercambio_SICRES_3;
import es.caib.regweb3.sir.core.schema.types.Tipo_RegistroType;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Agrupa funcionalidades comunes para interactuar con {@link Fichero_Intercambio_SICRES_3}, la clase que representa
 * el FicheroIntercambio.xsd
 */
public class FicheroIntercambio {

    public final Logger log = Logger.getLogger(getClass());

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    private Fichero_Intercambio_SICRES_3 ficheroIntercambio = null;


    public FicheroIntercambio() {
    }

    public FicheroIntercambio(Fichero_Intercambio_SICRES_3 ficheroIntercambio) {
        this.ficheroIntercambio = ficheroIntercambio;
    }

    public Fichero_Intercambio_SICRES_3 getFicheroIntercambio() {
        return ficheroIntercambio;
    }

    public void setFicheroIntercambio(Fichero_Intercambio_SICRES_3 ficheroIntercambio) {
        this.ficheroIntercambio = ficheroIntercambio;
    }

    public String getCodigoEntidadRegistralOrigen() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getCodigo_Entidad_Registral_Origen();
        }

        return null;
    }

    public String getDecodificacionEntidadRegistralOrigen() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getDecodificacion_Entidad_Registral_Origen();
        }

        return null;
    }

    public String getCodigoUnidadTramitacionOrigen() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getCodigo_Unidad_Tramitacion_Origen();
        }

        return null;
    }

    public String getDecodificacionUnidadTramitacionOrigen() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getDecodificacion_Unidad_Tramitacion_Origen();
        }

        return null;
    }

    public String getCodigoEntidadRegistralDestino() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getCodigo_Entidad_Registral_Destino();
        }

        return null;
    }

    public String getDescripcionEntidadRegistralDestino() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getDecodificacion_Entidad_Registral_Destino();
        }

        return null;
    }

    public String getCodigoUnidadTramitacionDestino() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getCodigo_Unidad_Tramitacion_Destino();
        }

        return null;
    }

    public String getDescripcionUnidadTramitacionDestino() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Destino() != null)) {
            return getFicheroIntercambio().getDe_Destino().getDecodificacion_Unidad_Tramitacion_Destino();
        }

        return null;
    }

    public String getNumeroRegistro() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getNumero_Registro_Entrada();
        }

        return null;
    }

    public String getFechaRegistroXML() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getFecha_Hora_Entrada();
        }

        return null;
    }

    public Date getFechaRegistro() {

        String fechaRegistro = getFechaRegistroXML();
        if (StringUtils.isNotBlank(fechaRegistro)) {
            try {
                return SDF.parse(fechaRegistro);
            } catch (ParseException e) {
                log.error("Error al parsear la fecha de registro: [" + fechaRegistro + "]", e);
            }
        }

        return null;
    }

    public byte[] getTimestampRegistro() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Origen_o_Remitente() != null)) {
            return getFicheroIntercambio().getDe_Origen_o_Remitente().getTimestamp_Entrada();
        }

        return null;
    }

    public String getCodigoAsunto() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getCodigo_Asunto_Segun_Destino();
        }

        return null;
    }

    public String getNumeroExpediente() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getNumero_Expediente();
        }

        return null;
    }

    public String getReferenciaExterna() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getReferencia_Externa();
        }

        return null;
    }

    public String getResumen() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Asunto() != null)) {
            return getFicheroIntercambio().getDe_Asunto().getResumen();
        }

        return null;
    }

    public String getCodigoEntidadRegistralInicio() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getCodigo_Entidad_Registral_Inicio();
        }

        return null;
    }

    public String getDecodificacionEntidadRegistralInicio() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getDecodificacion_Entidad_Registral_Inicio();
        }

        return null;
    }

    public String getNombreUsuario() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getNombre_Usuario();
        }

        return null;
    }

    public String getContactoUsuario() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getContacto_Usuario();
        }

        return null;
    }

    public String getTipoTransporteXML() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getTipo_Transporte_Entrada();
        }

        return null;
    }

    public TipoTransporte getTipoTransporte() {

        String tipoTransporte = getTipoTransporteXML();
        if (StringUtils.isNotBlank(tipoTransporte)) {
            return TipoTransporte.getTipoTransporte(tipoTransporte);
        }

        return null;
    }

    public Long getTipoTransporteEntrada() {

        String tipoTransporte = getFicheroIntercambio().getDe_Internos_Control().getTipo_Transporte_Entrada();
        if (StringUtils.isNotBlank(tipoTransporte)) {
            return RegwebConstantes.TRANSPORTE_BY_CODIGO_SICRES.get(tipoTransporte);
        }

        return null;
    }


    public String getNumeroTransporte() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getNumero_Transporte_Entrada();
        }

        return null;
    }


    public String getIdentificadorIntercambio() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getIdentificador_Intercambio();
        }

        return null;
    }

    public String getAplicacionEmisora() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getAplicacion_Version_Emisora();
        }

        return null;
    }

    public String getTipoAnotacionXML() {

        if (getFicheroIntercambio() != null) {
            De_Internos_Control de_Internos_Control = getFicheroIntercambio().getDe_Internos_Control();
            if (de_Internos_Control != null) {
                return de_Internos_Control.getTipo_Anotacion();
            }
        }

        return null;
    }

    public String getTipoAnotacion() {

        String tipoAnotacion = getTipoAnotacionXML();
        if (StringUtils.isNotBlank(tipoAnotacion)) {
            return TipoAnotacion.getTipoAnotacionValue(tipoAnotacion);
        }

        return null;
    }

    public String getDescripcionTipoAnotacion() {

        if (getFicheroIntercambio() != null) {
            De_Internos_Control de_Internos_Control = getFicheroIntercambio().getDe_Internos_Control();
            if (de_Internos_Control != null) {
                return de_Internos_Control.getDescripcion_Tipo_Anotacion();
            }
        }

        return null;
    }

    public TipoRegistro getTipoRegistro() {

        if ((getFicheroIntercambio() != null)
                && (getFicheroIntercambio().getDe_Internos_Control() != null)){

            Tipo_RegistroType tipoRegistro = getFicheroIntercambio().getDe_Internos_Control().getTipo_Registro();
            if ((tipoRegistro != null) && StringUtils.isNotBlank(tipoRegistro.value())) {
                return TipoRegistro.getTipoRegistro(tipoRegistro.value());
            }
        }

        return null;
    }

    public String getObservacionesApunte() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Internos_Control() != null)) {
            return getFicheroIntercambio().getDe_Internos_Control().getObservaciones_Apunte();
        }

        return null;
    }


    public String getExpone() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Formulario_Generico() != null)) {
            return getFicheroIntercambio().getDe_Formulario_Generico().getExpone();
        }

        return null;
    }

    public String getSolicita() {

        if ((getFicheroIntercambio() != null) && (getFicheroIntercambio().getDe_Formulario_Generico() != null)) {
            return getFicheroIntercambio().getDe_Formulario_Generico().getSolicita();
        }

        return null;
    }


    public String marshallObject() {
        String result;
        JAXBContext jc = null;
        StringWriter sw = new StringWriter();

        try {
            jc = JAXBContext.newInstance(Fichero_Intercambio_SICRES_3.class);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


            m.marshal(ficheroIntercambio, sw);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        result = sw.toString();

        return result;
    }


    /**
     * Obtiene el número de anexos.
     *
     * @return Número de anexos
     */
    public int countAnexos() {

        if (getFicheroIntercambio() != null) {
            return getFicheroIntercambio().getDe_AnexoCount();
        }

        return 0;
    }

    /**
     * Establece el contenido del anexo.
     *
     * @param secuencia Ordinal del anexo
     * @param contenido Contenido del anexo.
     */
    public void setContenidoAnexo(int secuencia, byte[] contenido) {
        if (secuencia < getFicheroIntercambio().getDe_AnexoCount()) {
            getFicheroIntercambio().getDe_Anexo()[secuencia].setAnexo(contenido);
        }
    }

    /**
     * Obtiene el contenido de un anexo.
     *
     * @param secuencia Ordinal del anexo.
     * @return Contenido del anexo.
     */
    public byte[] getContenidoAnexo(int secuencia) {
        if (secuencia < getFicheroIntercambio().getDe_AnexoCount()) {
            return getFicheroIntercambio().getDe_Anexo()[secuencia].getAnexo();
        } else {
            return null;
        }
    }




    @Override
    public String toString() {
        return "FicheroIntercambio{" +
                "ficheroIntercambio=" + ficheroIntercambio +
                '}';
    }
}
