package es.caib.regweb3.sir.ws.api.manager;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.sir.core.model.AsientoRegistral;
import es.caib.regweb3.sir.ws.api.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.utils.Mensaje;

/**
 *
 */
public interface SicresXMLManager {

    /**
     * Valida el contenido de un fichero de intercambio conforme a la normativa
     * SICRES 3.0.
     *
     * @param ficheroIntercambio Fichero de intercambio.
     */
    public void validarFicheroIntercambio(FicheroIntercambio ficheroIntercambio);

    /**
     * Valida el contenido de un asiento registral conforme a la normativa
     * SICRES 3.0.
     *
     * @param asiento Información del asiento registral.
     */
    public void validarAsientoRegistral(AsientoRegistral asiento);

    /**
     * Valida la información del fichero de datos de control.
     *
     * @param mensaje Información de mensaje SICRES 3.0.
     */
    public void validarMensaje(Mensaje mensaje);

    /**
     * Devuelve un XML con el fichero de datos de intercambio.
     *
     * @param registroEntrada Información del asiento registral.
     * @return XML de fichero de intercambio
     */
    public String crearXMLFicheroIntercambioSICRES3(RegistroEntrada registroEntrada);

    /**
     * Devuelve un XML con el mensaje de propósito general con el objetivo de
     * realizar tareas de avisos y gestión de flujo del intercambio.
     *
     * @param mensaje Información del mensaje.
     * @return XML de mensaje.
     */
    public String createXMLMensaje(Mensaje mensaje);

    /**
     * Carga la información del fichero de datos de intercambio.
     *
     * @param xml XML con la información del fichero de datos de intercambio.
     * @return Información del fichero de datos de intercambio.
     */
    public FicheroIntercambio parseXMLFicheroIntercambio(String xml);

    /**
     * Carga la información del mensaje de datos de control.
     *
     * @param xml XML con la información del fichero de datos de control.
     * @return Información del mensaje.
     */
    public Mensaje parseXMLMensaje(String xml);

}
