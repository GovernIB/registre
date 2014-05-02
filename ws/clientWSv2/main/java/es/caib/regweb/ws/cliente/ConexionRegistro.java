package es.caib.regweb.ws.cliente;


import es.caib.regweb.ws.cliente.model.*;
import es.caib.regweb.ws.model.ParametrosRegistroEntradaWS;
import es.caib.regweb.ws.model.ParametrosRegistroSalidaWS;
import es.caib.regweb.ws.services.regwebfacade.RegwebFacade_PortType;

public class ConexionRegistro {

    public ConexionRegistro() {
        super();
    }

    public static IdentificadorRegistroEntrada registrarEntrada(ParametrosUsuarioConexion usuConexion, ParametrosRegistroEntrada param, RegwebFacade_PortType rf) throws Exception {
        IdentificadorRegistroEntrada rtdo = new IdentificadorRegistroEntrada();

        ParametrosRegistroEntradaWS paramEntrada = new ParametrosRegistroEntradaWS();
        //Datos para la conexion
        paramEntrada.setUsuarioConexion(usuConexion.getIdUsuario());
        paramEntrada.setPassword(usuConexion.getPassword());

        //Datos del asiento registral
        paramEntrada.setUsuarioRegistro(param.getUsuarioRegistro());
        paramEntrada.setDataentrada(param.getFechaEntrada());
        paramEntrada.setHora(param.getHoraEntrada());
        paramEntrada.setOficina(param.getNumOficinaRegistro());
        paramEntrada.setOficinafisica(param.getNumOficinaFisica());

        //---Remitente
        paramEntrada.setEntidad1(param.getRemitenteCodigo1Entidad());
        paramEntrada.setEntidadCastellano(param.getRemitenteCodigo1EntidadCastellano());
        paramEntrada.setEntidad2(param.getRemitenteCodigo2Entidad());
        paramEntrada.setAltres(param.getRemitenteOtros());

        //---Organismo destinatario
        paramEntrada.setDestinatari(param.getOrganismoDestinatario());

        //---Procedencia Geografica
        paramEntrada.setFora(param.getProcedenciaFueraBaleares());
        paramEntrada.setBalears(param.getProcedenciaBaleares());

        //---Extracto (Resumen asiento registral)
        paramEntrada.setComentario(param.getExtractoRegistro());
        paramEntrada.setIdioex(param.getIdiomaExtracto());

        //Datos del documento a registrar
        paramEntrada.setData(param.getFechaDocumento());
        paramEntrada.setIdioma(param.getIdiomaDocumento());
        paramEntrada.setTipo(param.getTipoDocumento());

        //Llamamos al WS
        paramEntrada = rf.grabarEntrada(paramEntrada);

        //Copiamos los datos de respuesta que nos interesan
        rtdo.setAnyoRegistro(paramEntrada.getAnoEntrada());
        rtdo.setNumeroRegistro(paramEntrada.getNumeroEntrada());
        rtdo.setNumOficinaRegistro(paramEntrada.getOficina());

        return rtdo;
    }

    public static IdentificadorRegistroSalida registrarSalida(ParametrosUsuarioConexion usuConexion, ParametrosRegistroSalida param, RegwebFacade_PortType rf) throws Exception {
        IdentificadorRegistroSalida rtdo = new IdentificadorRegistroSalida();
        ParametrosRegistroSalidaWS paramSalida = new ParametrosRegistroSalidaWS();

        //Datos para la conexion
        paramSalida.setUsuarioConexion(usuConexion.getIdUsuario());
        paramSalida.setPassword(usuConexion.getPassword());

        //Datos del asiento registral
        paramSalida.setUsuarioRegistro(param.getUsuarioRegistro());
        paramSalida.setDatasalida(param.getFechaSalida());
        paramSalida.setHora(param.getHoraSalida());
        paramSalida.setOficina(param.getNumOficinaRegistro());
        paramSalida.setOficinafisica(param.getNumOficinaFisica());
        //---Destintaios
        paramSalida.setEntidad1(param.getDestinatarioCodigo1Entidad());
        paramSalida.setEntidadCastellano(param.getDestinatarioCodigo1EntidadCastellano());
        paramSalida.setEntidad2(param.getDestinatarioCodigo2Entidad());
        paramSalida.setAltres(param.getDestinatarioOtros());

        //---Organismo emisor
        paramSalida.setRemitent(param.getOrganismoEmisor());

        //---Procedencia Geografica
        paramSalida.setFora(param.getDestinoFueraBaleares());
        paramSalida.setBalears(param.getDestinoBaleares());
        //---Extracto (Resumen asiento registral)
        paramSalida.setComentario(param.getExtractoRegistro());
        paramSalida.setIdioex(param.getIdiomaExtracto());

        //Datos del documento a registrar
        paramSalida.setData(param.getFechaDocumento());
        paramSalida.setIdioma(param.getIdiomaDocumento());
        paramSalida.setTipo(param.getTipoDocumento());

        //Llamamos al WS
        paramSalida = rf.grabarSalida(paramSalida);

        //Copiamos los datos de respuesta que nos interesan
        rtdo.setAnyoRegistro(paramSalida.getAnoSalida());
        rtdo.setNumeroRegistro(paramSalida.getNumeroSalida());
        rtdo.setNumOficinaRegistro(paramSalida.getOficina());

        return rtdo;
    }

    public static ParametrosRegistroSalida leerSalida(ParametrosUsuarioConexion usuConexion, IdentificadorRegistroSalida idRegistro, String usuariolectura, RegwebFacade_PortType rf) throws Exception {
        ParametrosRegistroSalida registro = new ParametrosRegistroSalida();
        ParametrosRegistroSalidaWS paramSalida = new ParametrosRegistroSalidaWS();

        //Datos para la conexion
        paramSalida.setUsuarioConexion(usuConexion.getIdUsuario());
        paramSalida.setPassword(usuConexion.getPassword());

        //Id registro
        paramSalida.setNumeroSalida(idRegistro.getNumeroRegistro());
        paramSalida.setOficina(idRegistro.getNumOficinaRegistro());
        paramSalida.setAnoSalida(idRegistro.getAnyoRegistro());
        paramSalida.setUsuarioRegistro(usuariolectura); // Dato obligatorio al WS pero no usado

        //Llamamos al WS
        paramSalida = rf.leerSalida(paramSalida);

        //Datos del asiento registral
        registro.setUsuarioRegistro(paramSalida.getUsuarioRegistro());
        registro.setFechaSalida(paramSalida.getDatasalida());

        registro.setHoraSalida(paramSalida.getHora());
        registro.setNumOficinaRegistro(paramSalida.getOficina());
        registro.setNumOficinaFisica(paramSalida.getOficinafisica());

        //---Destinatarios
        registro.setDestinatarioCodigo1Entidad(paramSalida.getEntidad1());
        registro.setDestinatarioCodigo1EntidadCastellano(paramSalida.getEntidadCastellano());
        registro.setDestinatarioCodigo2Entidad(paramSalida.getEntidad2());
        registro.setDestinatarioOtros(paramSalida.getAltres());

        //---Organismo emisor
        registro.setOrganismoEmisor(paramSalida.getRemitent());

        //---Procedencia Geografica
        registro.setDestinoFueraBaleares(paramSalida.getFora());
        registro.setDestinoBaleares(paramSalida.getBalears());
        //---Extracto (Resumen asiento registral)
        registro.setExtractoRegistro(paramSalida.getComentario());
        registro.setIdiomaExtracto(paramSalida.getIdioex());

        //Datos del documento a registrar
        registro.setFechaDocumento(paramSalida.getData());
        registro.setIdiomaDocumento(paramSalida.getIdioma());
        registro.setTipoDocumento(paramSalida.getTipo());

        return registro;
    }

    public static ParametrosRegistroEntrada leerEntrada(ParametrosUsuarioConexion usuConexion, IdentificadorRegistroEntrada idRegistro, String usuariolectura, RegwebFacade_PortType rf) throws Exception {
        ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();
        ParametrosRegistroEntradaWS paramEntrada = new ParametrosRegistroEntradaWS();

        //Datos para la conexion
        paramEntrada.setUsuarioConexion(usuConexion.getIdUsuario());
        paramEntrada.setPassword(usuConexion.getPassword());

        //Id registro
        paramEntrada.setNumeroEntrada(idRegistro.getNumeroRegistro());
        paramEntrada.setOficina(idRegistro.getNumOficinaRegistro());
        paramEntrada.setAnoEntrada(idRegistro.getAnyoRegistro());
        paramEntrada.setUsuarioRegistro(usuariolectura); // Dato obligatorio al WS pero no usado

        //Llamamos al WS
        paramEntrada = rf.leerEntrada(paramEntrada);

        //Datos del asiento registral
        registro.setUsuarioRegistro(paramEntrada.getUsuarioRegistro());
        registro.setFechaEntrada(paramEntrada.getDataentrada());

        registro.setHoraEntrada(paramEntrada.getHora());
        registro.setNumOficinaRegistro(paramEntrada.getOficina());
        registro.setNumOficinaFisica(paramEntrada.getOficinafisica());

        //---REmitentes
        registro.setRemitenteCodigo1Entidad(paramEntrada.getEntidad1());
        registro.setRemitenteCodigo1EntidadCastellano(paramEntrada.getEntidadCastellano());
        registro.setRemitenteCodigo2Entidad(paramEntrada.getEntidad2());
        registro.setRemitenteOtros(paramEntrada.getAltres());

        //---Organismo emisor
        registro.setOrganismoDestinatario(paramEntrada.getDestinatari());

        //---Procedencia Geografica
        registro.setProcedenciaFueraBaleares(paramEntrada.getFora());
        registro.setProcedenciaBaleares(paramEntrada.getBalears());
        //---Extracto (Resumen asiento registral)
        registro.setExtractoRegistro(paramEntrada.getComentario());
        registro.setIdiomaExtracto(paramEntrada.getIdioex());

        //Datos del documento a registrar
        registro.setFechaDocumento(paramEntrada.getData());
        registro.setIdiomaDocumento(paramEntrada.getIdioma());
        registro.setTipoDocumento(paramEntrada.getTipo());

        return registro;
    }
}
