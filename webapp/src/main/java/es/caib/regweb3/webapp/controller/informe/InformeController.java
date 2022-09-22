package es.caib.regweb3.webapp.controller.informe;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.registro.AbstractRegistroCommonFormController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 * Controller que gestiona los informes y estadísticas
 * @author jpernia
 * Date: 2/05/14
 */
@Controller
@RequestMapping(value = "/informe")

public class InformeController extends AbstractRegistroCommonFormController {


    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb3/LopdEJB/local")
    private LopdLocal lopdEjb;
    
    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb3/InformeEJB/local")
    private InformeLocal informeEjb;

    private SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatDateLong = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat formatMes = new SimpleDateFormat("MMMMM", new Locale("ca"));
    private SimpleDateFormat formatMonth = new SimpleDateFormat("MM");


    /**
     * Informe de registros de un organismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registrosOrganismo", method = RequestMethod.GET)
    public String registrosOrganismo(Model model, HttpServletRequest request)throws Exception {

        InformeOrganismoBusquedaForm informeOrganismoBusquedaForm = new InformeOrganismoBusquedaForm();
        informeOrganismoBusquedaForm.setFechaFin(new Date());

        if(getRolActivo(request).getId().equals(RegwebConstantes.RWE_USUARI_ID)) {
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
            model.addAttribute("organosDestino", organismosOficinaActiva);
        }
        if(getRolActivo(request).getId().equals(RegwebConstantes.RWE_ADMIN_ID)) {
            model.addAttribute("organosDestino", organismoEjb.getAllByEntidad(getEntidadActiva(request).getId()));
        }

        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("informeOrganismoBusquedaForm", informeOrganismoBusquedaForm);
        model.addAttribute("organismosConsulta", organismos(request));
        model.addAttribute("oficinasRegistro", oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

        return "informe/registrosOrganismo";
    }

    /**
     * Realiza la busqueda de registros según los parametros del formulario
     */
    @RequestMapping(value = "/registrosOrganismo", method = RequestMethod.POST)
    public ModelAndView registrosOrganismo(@ModelAttribute InformeOrganismoBusquedaForm informeOrganismoBusquedaForm, HttpServletRequest request)throws Exception {

        ModelAndView mav = null;

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        if(informeOrganismoBusquedaForm.getFormato().equals("pdf")){
            mav = new ModelAndView("registrosOrganismoPdf");
        }else if(informeOrganismoBusquedaForm.getFormato().equals("excel")){
            mav = new ModelAndView("registrosOrganismoExcel");
        }

        Set<String> campos = informeOrganismoBusquedaForm.getCampos();


        // Obtener los registros del Organismo
        ArrayList<ArrayList<String>> registrosOrganismo = new ArrayList<ArrayList<String>>();

        Date dataFi = RegistroUtils.ajustarHoraBusqueda(informeOrganismoBusquedaForm.getFechaFin());

        Long idOficina = informeOrganismoBusquedaForm.getIdOficina();
        String nomOficina = "";
        if(idOficina != -1){
            nomOficina = oficinaEjb.findById(idOficina).getDenominacion();
        }

        String codigoOrganDest = "";
        if(!informeOrganismoBusquedaForm.getOrganDestinatari().equals("")){
            codigoOrganDest = informeOrganismoBusquedaForm.getOrganDestinatari();
        }

        String nomOrganismeDest = "";
        if(!codigoOrganDest.equals("")) {
            if(!informeOrganismoBusquedaForm.getOrganDestinatariNom().equals("")) {
                nomOrganismeDest = informeOrganismoBusquedaForm.getOrganDestinatariNom();
            } else{
                nomOrganismeDest = organismoEjb.findByCodigoByEntidadMultiEntidad(codigoOrganDest,usuarioEntidad.getEntidad().getId()).getDenominacion();
            }
        }

        Boolean mostraInteressats = false;
        for (String valorCamp : campos) {
            if (valorCamp.equals("nomIn")) {
                mostraInteressats = true;
                break;
            }
        }

        // REGISTROS DE ENTRADA
        if(informeOrganismoBusquedaForm.getTipo().equals(RegwebConstantes.REGISTRO_ENTRADA)){
            Long start = System.currentTimeMillis();
            List<RegistroEntrada> registrosEntrada = informeEjb.buscaRegistroEntradasOrganismo(informeOrganismoBusquedaForm.getFechaInicio(),
                    dataFi, informeOrganismoBusquedaForm.getNumeroRegistroFormateado(), informeOrganismoBusquedaForm.getInteressatNom(),
                    informeOrganismoBusquedaForm.getInteressatLli1(), informeOrganismoBusquedaForm.getInteressatLli2(), informeOrganismoBusquedaForm.getInteressatDoc(),
                    informeOrganismoBusquedaForm.getAnexos(), informeOrganismoBusquedaForm.getObservaciones(),
                    informeOrganismoBusquedaForm.getExtracto(), informeOrganismoBusquedaForm.getUsuario(), informeOrganismoBusquedaForm.getIdOrganismo(),
                    informeOrganismoBusquedaForm.getEstado(), idOficina, codigoOrganDest, usuarioEntidad.getEntidad().getId(), mostraInteressats);
            Long end = System.currentTimeMillis();
            log.info("Tiempo informeEjb.buscaLibroRegistroEntradas: " + TimeUtils.formatElapsedTime(end - start));

            start = System.currentTimeMillis();
            for (int i = 0; i < registrosEntrada.size(); i++) {
                registrosOrganismo.add(new ArrayList<String>());
                RegistroEntrada registroEntrada = registrosEntrada.get(i);

                for (String valorCamp : campos) {
                    if (valorCamp.equals("codAs")) {
                        if (registroEntrada.getRegistroDetalle().getCodigoAsunto() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getCodigoAsunto().getCodigo());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("anyRe")) {
                        if (registroEntrada.getFecha() != null) {
                            String anoRegistro = formatYear.format(registroEntrada.getFecha());
                            registrosOrganismo.get(i).add(anoRegistro);
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("estat")) {
                        registrosOrganismo.get(i).add(I18NUtils.tradueix("registro.estado." + registroEntrada.getEstado()));
                    } else if (valorCamp.equals("exped")) {
                        if (registroEntrada.getRegistroDetalle().getExpediente() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getExpediente());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("extra")) {
                        if (registroEntrada.getRegistroDetalle().getExtracto() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getExtracto());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("data")) {
                        if (registroEntrada.getFecha() != null) {
                            String fechaRegistro = formatDateLong.format(registroEntrada.getFecha());
                            registrosOrganismo.get(i).add(fechaRegistro);
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("numRe")) {
                        if (registroEntrada.getNumeroRegistroFormateado() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getNumeroRegistroFormateado());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("ofici")) {
                        if (registroEntrada.getOficina().getDenominacion() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getOficina().getDenominacion());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    }  else if (valorCamp.equals("obser")) {
                        if (registroEntrada.getRegistroDetalle().getObservaciones() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getObservaciones());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("llibr")) {
                        if (registroEntrada.getLibro().getNombre() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getLibro().getNombre());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("docFi")) {
                        if (registroEntrada.getRegistroDetalle().getTipoDocumentacionFisica() != null) {
                            registrosOrganismo.get(i).add(I18NUtils.tradueix("tipoDocumentacionFisica." + registroEntrada.getRegistroDetalle().getTipoDocumentacionFisica()));
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("orgDe")) {
                        if (registroEntrada.getDestinoExternoCodigo() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getDestinoExternoDenominacion());
                        } else {
                            if (registroEntrada.getDestino() != null) {
                                registrosOrganismo.get(i).add(registroEntrada.getDestino().getDenominacion());
                            } else {
                                registrosOrganismo.get(i).add("");
                            }
                        }
                    } else if (valorCamp.equals("idiom")) {
                        if (registroEntrada.getRegistroDetalle().getIdioma() != null) {
                            final String nombre = I18NUtils.tradueix("idioma." + registroEntrada.getRegistroDetalle().getIdioma());
                            registrosOrganismo.get(i).add(nombre);
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("refEx")) {
                        if (registroEntrada.getRegistroDetalle().getReferenciaExterna() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getReferenciaExterna());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("trans")) {
                        if (registroEntrada.getRegistroDetalle().getTransporte() != null) {
                            registrosOrganismo.get(i).add(I18NUtils.tradueix("transporte." + registroEntrada.getRegistroDetalle().getTransporte()));
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("numTr")) {
                        if (registroEntrada.getRegistroDetalle().getNumeroTransporte() != null) {
                            final String nombre = I18NUtils.tradueix("idioma." + registroEntrada.getRegistroDetalle().getIdioma());
                            registrosOrganismo.get(i).add(nombre);
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("orgOr")) {
                        if (registroEntrada.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoCodigo());
                        } else {
                            if (registroEntrada.getRegistroDetalle().getOficinaOrigen() != null) {
                                registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                            } else {
                                registrosOrganismo.get(i).add("");
                            }
                        }
                    } else if (valorCamp.equals("numOr")) {
                        if (registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("datOr")) {
                        if (registroEntrada.getRegistroDetalle().getFechaOrigen() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getFechaOrigen().toString());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("nomIn")) {
                        if (registroEntrada.getRegistroDetalle().getInteresados() != null) {
                            StringBuilder interessats = new StringBuilder();

                            for (int k = 0; k < registroEntrada.getRegistroDetalle().getInteresados().size(); k++) {
                                Interesado interesado = registroEntrada.getRegistroDetalle().getInteresados().get(k);
                                if (interesado.getIsRepresentante()) {
                                    interessats.append("(Rep.) ");
                                }

                                // Añadimos el nombre completo del interesado
                                interessats.append(interesado.getNombreCompletoInforme());

                                if (k < registroEntrada.getRegistroDetalle().getInteresados().size() - 1) {
                                    interessats.append(", ");
                                }
                            }
                            registrosOrganismo.get(i).add(interessats.toString());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("intMa")) {
                        if (registroEntrada.getRegistroDetalle().getInteresados() != null) {
                            StringBuilder mailInteressats = new StringBuilder();

                            for (int k = 0; k < registroEntrada.getRegistroDetalle().getInteresados().size(); k++) {
                                Interesado interesado = registroEntrada.getRegistroDetalle().getInteresados().get(k);

                                if(interesado.getEmail()!=null) {
                                    mailInteressats.append(interesado.getEmail());

                                    if (k < registroEntrada.getRegistroDetalle().getInteresados().size() - 1) {
                                        mailInteressats.append(", ");
                                    }
                                }
                            }
                            registrosOrganismo.get(i).add(mailInteressats.toString());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("aplic")) {
                        if (registroEntrada.getRegistroDetalle().getAplicacion() != null) {
                            registrosOrganismo.get(i).add(registroEntrada.getRegistroDetalle().getAplicacion());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    }
                }
            }

            // Alta en tabla LOPD de los registros del Informe
            Paginacion paginacionEntrada = new Paginacion(0, 0);
            paginacionEntrada.setListado(new ArrayList<Object>(registrosEntrada));
            start = System.currentTimeMillis();
            lopdEjb.insertarRegistros(paginacionEntrada, usuarioEntidad, entidadActiva.getLibro(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_LISTADO);

            mav.addObject("tipo", RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA);


        // REGISTROS DE SALIDA
        }else if(informeOrganismoBusquedaForm.getTipo().equals(RegwebConstantes.REGISTRO_SALIDA)){

            List<RegistroSalida> registrosSalida = informeEjb.buscaRegistroSalidasOrganismo(informeOrganismoBusquedaForm.getFechaInicio(),
                    dataFi, informeOrganismoBusquedaForm.getNumeroRegistroFormateado(), informeOrganismoBusquedaForm.getInteressatNom(),
                    informeOrganismoBusquedaForm.getInteressatLli1(), informeOrganismoBusquedaForm.getInteressatLli2(), informeOrganismoBusquedaForm.getInteressatDoc(),
                    informeOrganismoBusquedaForm.getAnexos(), informeOrganismoBusquedaForm.getObservaciones(),
                    informeOrganismoBusquedaForm.getExtracto(), informeOrganismoBusquedaForm.getUsuario(), informeOrganismoBusquedaForm.getIdOrganismo(),
                    informeOrganismoBusquedaForm.getEstado(), idOficina, codigoOrganDest, usuarioEntidad.getEntidad().getId(), mostraInteressats);


            for (int i = 0; i < registrosSalida.size(); i++) {
                registrosOrganismo.add(new ArrayList<String>());
                RegistroSalida registroSalida = registrosSalida.get(i);

                for (String valorCamp : campos) {
                    if (valorCamp.equals("codAs")) {
                        if (registroSalida.getRegistroDetalle().getCodigoAsunto() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getCodigoAsunto().getCodigo());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("anyRe")) {
                        if (registroSalida.getFecha() != null) {
                            String anoRegistro = formatYear.format(registroSalida.getFecha());
                            registrosOrganismo.get(i).add(anoRegistro);
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("estat")) {
                        if (registroSalida.getEstado() != null) {
                            registrosOrganismo.get(i).add(I18NUtils.tradueix("registro.estado." + registroSalida.getEstado()));
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("exped")) {
                        if (registroSalida.getRegistroDetalle().getExpediente() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getExpediente());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("extra")) {
                        if (registroSalida.getRegistroDetalle().getExtracto() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getExtracto());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("data")) {
                        if (registroSalida.getFecha() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getFecha().toString());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("numRe")) {
                        if (registroSalida.getNumeroRegistroFormateado() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getNumeroRegistroFormateado());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("ofici")) {
                        if (registroSalida.getOficina().getDenominacion() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getOficina().getDenominacion());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    }  else if (valorCamp.equals("obser")) {
                        if (registroSalida.getRegistroDetalle().getObservaciones() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getObservaciones());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("llibr")) {
                        if (registroSalida.getLibro().getNombre() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getLibro().getNombre());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("docFi")) {
                        if (registroSalida.getRegistroDetalle().getTipoDocumentacionFisica() != null) {

                            registrosOrganismo.get(i).add(I18NUtils.tradueix("tipoDocumentacionFisica." + registroSalida.getRegistroDetalle().getTipoDocumentacionFisica()));
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("orgDe")) {
                        if (registroSalida.getOrigen() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getOrigen().getDenominacion());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("idiom")) {
                        if (registroSalida.getRegistroDetalle().getIdioma() != null) {
                            final String nombre = I18NUtils.tradueix("idioma." + registroSalida.getRegistroDetalle().getIdioma());
                            registrosOrganismo.get(i).add(nombre);
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("refEx")) {
                        if (registroSalida.getRegistroDetalle().getReferenciaExterna() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getReferenciaExterna());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("trans")) {
                        if (registroSalida.getRegistroDetalle().getTransporte() != null) {
                            registrosOrganismo.get(i).add(I18NUtils.tradueix("transporte." + registroSalida.getRegistroDetalle().getTransporte()));
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("numTr")) {
                        if (registroSalida.getRegistroDetalle().getNumeroTransporte() != null) {
                            final String nombre = I18NUtils.tradueix("idioma." + registroSalida.getRegistroDetalle().getIdioma());
                            registrosOrganismo.get(i).add(nombre);
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("orgOr")) {
                        if (registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo());
                        } else {
                            if (registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null) {
                                registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                            } else {
                                registrosOrganismo.get(i).add("");
                            }
                        }
                    } else if (valorCamp.equals("numOr")) {
                        if (registroSalida.getRegistroDetalle().getNumeroRegistroOrigen() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("datOr")) {
                        if (registroSalida.getRegistroDetalle().getFechaOrigen() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getFechaOrigen().toString());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("nomIn")) {
                        if (registroSalida.getRegistroDetalle().getInteresados() != null) {
                            StringBuilder interessats = new StringBuilder();

                            for (int k = 0; k < registroSalida.getRegistroDetalle().getInteresados().size(); k++) {
                                Interesado interesado = registroSalida.getRegistroDetalle().getInteresados().get(k);
                                if (interesado.getIsRepresentante()) {
                                    interessats.append("(Rep.) ");
                                }

                                // Añadimos el nombre completo del interesado
                                interessats.append(interesado.getNombreCompletoInforme());

                                if (i < registroSalida.getRegistroDetalle().getInteresados().size() - 1) {
                                    interessats.append(", ");
                                }
                            }
                            registrosOrganismo.get(i).add(interessats.toString());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("intMa")) {
                        if (registroSalida.getRegistroDetalle().getInteresados() != null) {
                            StringBuilder mailInteressats = new StringBuilder();

                            for (int k = 0; k < registroSalida.getRegistroDetalle().getInteresados().size(); k++) {
                                Interesado interesado = registroSalida.getRegistroDetalle().getInteresados().get(k);

                                if(interesado.getEmail()!=null) {
                                    mailInteressats.append(interesado.getEmail());

                                    if (k < registroSalida.getRegistroDetalle().getInteresados().size() - 1) {
                                        mailInteressats.append(", ");
                                    }
                                }
                            }
                            registrosOrganismo.get(i).add(mailInteressats.toString());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    } else if (valorCamp.equals("aplic")) {
                        if (registroSalida.getRegistroDetalle().getAplicacion() != null) {
                            registrosOrganismo.get(i).add(registroSalida.getRegistroDetalle().getAplicacion());
                        } else {
                            registrosOrganismo.get(i).add("");
                        }
                    }
                }
            }

            // Alta en tabla LOPD de los registros del Informe
            Paginacion paginacionSalida = new Paginacion(0, 0);
            List<Object> salidasList = new ArrayList<Object>(registrosSalida);
            paginacionSalida.setListado(salidasList);
            lopdEjb.insertarRegistros(paginacionSalida, usuarioEntidad, entidadActiva.getLibro(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_LISTADO);

            mav.addObject("tipo", RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA);
        }

        if(informeOrganismoBusquedaForm.getFechaInicio() != null){
            String fechaInicio = formatDate.format(informeOrganismoBusquedaForm.getFechaInicio());
            mav.addObject("fechaInicio", fechaInicio);
        }

        if(informeOrganismoBusquedaForm.getFechaInicio() != null){
            String fechaFin = formatDate.format(informeOrganismoBusquedaForm.getFechaFin());
            mav.addObject("fechaFin", fechaFin);
        }

        mav.addObject("campos", campos);
        mav.addObject("registrosOrganismo", registrosOrganismo);
        mav.addObject("numRegistro", informeOrganismoBusquedaForm.getNumeroRegistroFormateado());
        mav.addObject("extracto", informeOrganismoBusquedaForm.getExtracto());
        mav.addObject("estado", informeOrganismoBusquedaForm.getEstado());
        mav.addObject("nombreInteresado", informeOrganismoBusquedaForm.getInteressatNom());
        mav.addObject("apell1Interesado", informeOrganismoBusquedaForm.getInteressatLli1());
        mav.addObject("apell2Interesado", informeOrganismoBusquedaForm.getInteressatLli2());
        mav.addObject("docInteresado", informeOrganismoBusquedaForm.getInteressatDoc());
        mav.addObject("oficinaReg", nomOficina);
        mav.addObject("anexos", informeOrganismoBusquedaForm.getAnexos());
        mav.addObject("observaciones", informeOrganismoBusquedaForm.getObservaciones());
        mav.addObject("usuario", informeOrganismoBusquedaForm.getUsuario());
        mav.addObject("organDest", nomOrganismeDest);

        return mav;
    }


    /**
     * Informe de Indicadores
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/indicadores", method = RequestMethod.GET)
    public String indicadores(Model model, HttpServletRequest request)throws Exception {

        InformeIndicadoresBusquedaForm informeIndicadoresBusquedaForm = new InformeIndicadoresBusquedaForm();
        informeIndicadoresBusquedaForm.setFechaFin(new Date());
        model.addAttribute("informeIndicadoresBusquedaForm",informeIndicadoresBusquedaForm);

        return "informe/indicadores";
    }

    /**
     * Realiza la busqueda de registros según los parametros del formulario
     */
    @RequestMapping(value = "/indicadores", method = RequestMethod.POST)
    public ModelAndView indicadores(@ModelAttribute InformeIndicadoresBusquedaForm informeIndicadoresBusquedaForm, HttpServletRequest request)throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        String formato = informeIndicadoresBusquedaForm.getFormato();
        Long calendario = informeIndicadoresBusquedaForm.getCampoCalendario();
        Integer tipoLibro = informeIndicadoresBusquedaForm.getTipo().intValue();
        List<Oficina> oficinas = oficinaEjb.findByEntidadReduce(entidadActiva.getId());
        List<Organismo> organismos = organismoEjb.findByEntidadReduce(entidadActiva.getId());

        ModelAndView mav = new ModelAndView();

        if(formato.equals("pdf")){
            mav.setViewName("indicadoresPdf");
        }else if(formato.equals("excel")){
            mav.setViewName("indicadoresExcel");
        }

        // Intervalo de fechas seleccionado
        mav.addObject("fechaInicio", formatDate.format(informeIndicadoresBusquedaForm.getFechaInicio()));
        mav.addObject("fechaFin", formatDate.format(informeIndicadoresBusquedaForm.getFechaFin()));

        // Calendario seleccionado
        mav.addObject("campoCalendario", calendario);


        Date dataFi = RegistroUtils.ajustarHoraBusqueda(informeIndicadoresBusquedaForm.getFechaFin());
        Date dataInici = informeIndicadoresBusquedaForm.getFechaInicio();


        // Según el tipo de informe seleccionado
        switch (tipoLibro){

            case 0: // Entrada y Salida
                mav.addObject("tipo", RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA);

                // Busca los registros Totales de Entrada y Salida entre las fechas
                mav.addObject("registrosEntrada", informeEjb.buscaIndicadoresEntradaTotal(dataInici, dataFi, entidadActiva.getId()).intValue());
                mav.addObject("registrosSalida", informeEjb.buscaIndicadoresSalidaTotal(dataInici, dataFi, entidadActiva.getId()).intValue());

                // Busca los registros totales según el calendario seleccionado de Entrada y Salida entre las fechas
                if(calendario.equals((long) 0)){ // Años y meses

                    totalRegistresEntradaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);
                    totalRegistresSalidaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);
                    totalRegistresEntradaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);
                    totalRegistresSalidaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);

                }else if(calendario.equals((long) 1)){ // Años

                    totalRegistresEntradaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);
                    totalRegistresSalidaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);

                }else if(calendario.equals((long) 2)){ // Meses
                    totalRegistresEntradaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);
                    totalRegistresSalidaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);
                }

                // Busca los registros totales por Organismo de Entrada y Salida entre las fechas
                totalRegistresEntradaOrganismo(mav,dataInici,dataFi,organismos);
                totalRegistresSalidaOrganismo(mav,dataInici,dataFi,organismos);

                // Busca los registros totales por Libro de Entrada y Salida entre las fechas
                totalRegistresEntradaLibro(mav,dataInici,dataFi,organismos);
                totalRegistresSalidaLibro(mav,dataInici,dataFi,organismos);

                // Busca los registros totales por Oficina de Registro de Entrada y Salida entre las fechas
                totalRegistresEntradaOficina(mav,dataInici,dataFi,oficinas);
                totalRegistresSalidaOficina(mav,dataInici,dataFi,oficinas);

                // Busca los registros totales por Idiomas de Entrada y Salida entre las fechas
                totalRegistresEntradaIdioma(mav,dataInici,dataFi,entidadActiva.getId(),null);
                totalRegistresSalidaIdioma(mav,dataInici,dataFi,entidadActiva.getId(),null);


                break;

            case 1: // Entrada
                mav.addObject("tipo", RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA);

                // Busca los registros Totales de Entrada entre las fechas
                mav.addObject("registrosEntrada", informeEjb.buscaIndicadoresEntradaTotal(dataInici, dataFi, entidadActiva.getId()).intValue());

                // Busca los registros totales según el calendario seleccionado de Entrada entre las fechas
                if(calendario.equals((long) 0)){ // Años y meses

                    totalRegistresEntradaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);
                    totalRegistresEntradaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);

                }else if(calendario.equals((long) 1)){ // Años

                    totalRegistresEntradaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);

                }else if(calendario.equals((long) 2)){ // Meses
                    totalRegistresEntradaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);
                }

                // Busca los registros totales por Organismo de Entrada entre las fechas
                totalRegistresEntradaOrganismo(mav,dataInici,dataFi,organismos);

                // Busca los registros totales por Libro de Entrada entre las fechas
                totalRegistresEntradaLibro(mav,dataInici,dataFi,organismos);

                // Busca los registros totales por Oficina de Registro de Entrada entre las fechas
                totalRegistresEntradaOficina(mav,dataInici,dataFi,oficinas);

                // Busca los registros totales por Idiomas de Entrada entre las fechas
                totalRegistresEntradaIdioma(mav,dataInici,dataFi,entidadActiva.getId(),null);



            break;

            case 2: //  Salida
                mav.addObject("tipo", RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA);

                // Busca los registros Totales de Salida entre las fechas
                mav.addObject("registrosSalida", informeEjb.buscaIndicadoresSalidaTotal(dataInici, dataFi, entidadActiva.getId()).intValue());

                // Busca los registros totales según el calendario seleccionado de Salida entre las fechas
                if(calendario.equals((long) 0)){ // Años y meses

                    totalRegistresSalidaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);
                    totalRegistresSalidaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);

                }else if(calendario.equals((long) 1)){ // Años

                    totalRegistresSalidaAny(mav,dataInici,dataFi,entidadActiva.getId(),null);

                }else if(calendario.equals((long) 2)){ // Meses

                    totalRegistresSalidaMes(mav,dataInici,dataFi,entidadActiva.getId(),null);
                }

                // Busca los registros totales por Organismo de Salida entre las fechas
                totalRegistresSalidaOrganismo(mav,dataInici,dataFi,organismos);

                // Busca los registros totales por Libro de Salida entre las fechas
                totalRegistresSalidaLibro(mav,dataInici,dataFi,organismos);

                //Busca los registros totales por Oficina de Registro Salida entre las fechas
                totalRegistresSalidaOficina(mav,dataInici,dataFi,oficinas);

                // Busca los registros totales por Idiomas de  Salida entre las fechas
                totalRegistresSalidaIdioma(mav,dataInici,dataFi,entidadActiva.getId(),null);


            break;
        }


        return mav;
    }

    /**
     * Informe de usuario LOPD
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/usuarioLopd", method = RequestMethod.GET)
    public String usuarioLopd(Model model, HttpServletRequest request)throws Exception {

        UsuarioLopdBusqueda usuarioLopdBusqueda = new UsuarioLopdBusqueda(1);

        usuarioLopdBusqueda.setFechaFin(new Date());

        model.addAttribute("usuarios", usuarios(request));
        model.addAttribute("libros", libroEjb.getLibrosEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("usuarios", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("usuarioLopdBusqueda",usuarioLopdBusqueda);

        return "informe/usuarioLopd";
    }

    /**
     * Realiza la busqueda de acciones de un usuario sobre registros entre dos fechas
     */
    @RequestMapping(value = "/usuarioLopd", method = RequestMethod.POST)
    public ModelAndView usuarioLopd(@ModelAttribute UsuarioLopdBusqueda busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("informe/usuarioLopd");
        Entidad entidadActiva = getEntidadActiva(request);
        Paginacion paginacion = null;

        // Obtener los registros del usuario
        Date dataFi = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());
        Date dataInici = busqueda.getFechaInicio();
        Long tipo = busqueda.getTipo();
        Long accion = busqueda.getAccion();

        // Mira si ha elegido un libro en la búsqueda
        if(busqueda.getLibro() != -1){

            Libro libro = libroEjb.findById(busqueda.getLibro());
            mav.addObject("libro", libro);

            if(tipo.equals(RegwebConstantes.REGISTRO_ENTRADA)){
                // Busca los registros Creados por Usuario de Registro de Entrada entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_CREACION)) {
                   paginacion = lopdEjb.buscaEntradaPorUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), busqueda.getLibro());
                }
                // Busca los registros Modificados por Usuario de Registro de Entrada entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_MODIFICACION)) {
                    paginacion = lopdEjb.entradaModificadaPorUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), busqueda.getLibro());
                }
                // Registros Listados
                if(accion.equals(RegwebConstantes.LOPD_LISTADO)) {
                    paginacion = lopdEjb.getByFechasUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libro.getId(), RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_ENTRADA);
                }
                // Registros Consultados
                if(accion.equals(RegwebConstantes.LOPD_CONSULTA)) {
                    paginacion = lopdEjb.getByFechasUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libro.getId(), RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_ENTRADA);
                }
                // Justificantes Consultados
                if(accion.equals(RegwebConstantes.LOPD_JUSTIFICANTE)) {
                    paginacion = lopdEjb.getByFechasUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libro.getId(), RegwebConstantes.LOPD_JUSTIFICANTE, RegwebConstantes.REGISTRO_ENTRADA);
                }
            }

            if(tipo.equals(RegwebConstantes.REGISTRO_SALIDA)){
                // Busca los registros Creados por Usuario de Registro de Salida entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_CREACION)) {
                    paginacion = lopdEjb.buscaSalidaPorUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), busqueda.getLibro());
                }
                // Busca los registros Modificados por Usuario de Registro de Salida entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_MODIFICACION)) {
                    paginacion = lopdEjb.salidaModificadaPorUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), busqueda.getLibro());
                }
                // Registros Listados
                if(accion.equals(RegwebConstantes.LOPD_LISTADO)) {
                    paginacion = lopdEjb.getByFechasUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libro.getId(), RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_SALIDA);
                }
                // Registros Consultados
                if(accion.equals(RegwebConstantes.LOPD_CONSULTA)) {
                    paginacion = lopdEjb.getByFechasUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libro.getId(), RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_SALIDA);
                }
                // Justificantes Consultados
                if(accion.equals(RegwebConstantes.LOPD_JUSTIFICANTE)) {
                    paginacion = lopdEjb.getByFechasUsuarioLibro(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libro.getId(), RegwebConstantes.LOPD_JUSTIFICANTE, RegwebConstantes.REGISTRO_SALIDA);
                }
            }

        // Si no ha elegido ningún libro en la búsqueda también tendrá en cuenta los Registros Migrados
        } else {

            List<Libro> libros = null;

            // Es Administrador de Entidad
            if (isAdminEntidad(request)) {
                libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());
            }

            mav.addObject("libros", libros);

            if(tipo.equals(RegwebConstantes.REGISTRO_ENTRADA)){
                // Busca los registros Creados por Usuario de Registro de Entrada entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_CREACION)) {
                    paginacion = lopdEjb.buscaEntradaPorUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros);
                }
                // Busca los registros Modificador por Usuario de Registro de Entrada y Salida entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_MODIFICACION)) {
                    paginacion = lopdEjb.entradaModificadaPorUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros);
                }
                // Registros Listados
                if(accion.equals(RegwebConstantes.LOPD_LISTADO)) {
                    paginacion = lopdEjb.getByFechasUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_ENTRADA);
                }
                // Registros Consultados
                if(accion.equals(RegwebConstantes.LOPD_CONSULTA)) {
                    paginacion = lopdEjb.getByFechasUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_ENTRADA);
                }
                // Justificantes Consultados
                if(accion.equals(RegwebConstantes.LOPD_JUSTIFICANTE)) {
                    paginacion = lopdEjb.getByFechasUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros, RegwebConstantes.LOPD_JUSTIFICANTE, RegwebConstantes.REGISTRO_ENTRADA);
                }
            }

            if(tipo.equals(RegwebConstantes.REGISTRO_SALIDA)){
                // Busca los registros Creados por Usuario de Registro de Salida entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_CREACION)) {
                    paginacion = lopdEjb.buscaSalidaPorUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros);
                }
                // Busca los registros Modificador por Usuario de Registro de Salida entre las fechas
                if(accion.equals(RegwebConstantes.LOPD_MODIFICACION)) {
                    paginacion = lopdEjb.salidaModificadaPorUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros);
                }
                // Registros Listados
                if(accion.equals(RegwebConstantes.LOPD_LISTADO)) {
                    paginacion = lopdEjb.getByFechasUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_SALIDA);
                }
                // Registros Consultados
                if(accion.equals(RegwebConstantes.LOPD_CONSULTA)) {
                    paginacion = lopdEjb.getByFechasUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_SALIDA);
                }
                // Justificantes Consultados
                if(accion.equals(RegwebConstantes.LOPD_JUSTIFICANTE)) {
                    paginacion = lopdEjb.getByFechasUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getUsuario(), libros, RegwebConstantes.LOPD_JUSTIFICANTE, RegwebConstantes.REGISTRO_SALIDA);
                }
            }

            // Registros Migrados
            if(tipo.equals(RegwebConstantes.LOPD_TIPO_MIGRADO)) {
                UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(busqueda.getUsuario());
                Usuario usuarioBusqueda = usuarioEntidad.getUsuario();
                // Registros Consultados
                if(accion.equals(RegwebConstantes.LOPD_CONSULTA)) {
                    paginacion = lopdEjb.getByUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, usuarioBusqueda.getIdentificador(), RegwebConstantes.LOPDMIGRADO_CONSULTA);
                }
                // Registros Listados
                if(accion.equals(RegwebConstantes.LOPD_LISTADO)) {
                    paginacion = lopdEjb.getByUsuario(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, usuarioBusqueda.getIdentificador(), RegwebConstantes.LOPDMIGRADO_LISTADO);
                }
            }

        }
        busqueda.setPageNumber(1);
        mav.addObject("usuarioLopdBusqueda",busqueda);
        mav.addObject("paginacion", paginacion);
        mav.addObject("usuarios", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));
        mav.addObject("libros", libroEjb.getLibrosEntidad(getEntidadActiva(request).getId()));
        mav.addObject("tipo", tipo);
        mav.addObject("accion", accion);
        mav.addObject("usuario", usuarioEntidadEjb.findById(busqueda.getUsuario()).getUsuario());

        return mav;
    }


    /**
     * Informe de registro LOPD
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registroLopd", method = RequestMethod.GET)
    public String registroLopd(Model model, HttpServletRequest request)throws Exception {

        RegistroLopdBusqueda registroLopdBusqueda = new RegistroLopdBusqueda(1);
        registroLopdBusqueda.setFechaFin(new Date());

        model.addAttribute("libros", libroEjb.getLibrosEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("registroLopdBusqueda",registroLopdBusqueda);

        return "informe/registroLopd";
    }

    /**
     * Realiza la busqueda de acciones sobre un registro entre dos fechas
     */
    @RequestMapping(value = "/registroLopd", method = RequestMethod.POST)
    public ModelAndView registroLopd(@ModelAttribute RegistroLopdBusqueda busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("informe/registroLopd");
        Entidad entidadActiva = getEntidadActiva(request);

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        // Obtener los registros del usuario
        Date dataFi = busqueda.getFechaFin();
        if(dataFi != null){
            dataFi = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());
        }
        Date dataInici = busqueda.getFechaInicio();

        Libro libro = libroEjb.findById(busqueda.getLibro());
        mav.addObject("libro", libro);

        // Busca los registros según parámetros de búsqueda
        if((dataInici != null) && (dataFi != null)) {
            if (busqueda.getTipoRegistro().equals(RegwebConstantes.REGISTRO_ENTRADA)) {
                Paginacion paginacion = lopdEjb.buscaEntradasPorLibroTipoNumero(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getLibro(), busqueda.getNumeroRegistro());
                busqueda.setPageNumber(1);
                mav.addObject("paginacion", paginacion);
                // Alta en tabla LOPD de las entradas del listado
                lopdEjb.insertarRegistros(paginacion, usuarioEntidad, entidadActiva.getLibro(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_LISTADO);
                mav.addObject("entradas", true);
                mav.addObject("salidas", false);
            }
            if (busqueda.getTipoRegistro().equals(RegwebConstantes.REGISTRO_SALIDA)) {
                Paginacion paginacion = lopdEjb.buscaSalidasPorLibroTipoNumero(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageLopd(entidadActiva.getId()), dataInici, dataFi, busqueda.getLibro(), busqueda.getNumeroRegistro());
                busqueda.setPageNumber(1);
                mav.addObject("paginacion", paginacion);
                // Alta en tabla LOPD de las salidas del listado
                lopdEjb.insertarRegistros(paginacion, usuarioEntidad, entidadActiva.getLibro(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_LISTADO);
                mav.addObject("entradas", false);
                mav.addObject("salidas", true);
            }
        }

        mav.addObject("idTipoRegistro", busqueda.getTipoRegistro());
        mav.addObject("libros", libroEjb.getLibrosEntidad(getEntidadActiva(request).getId()));


        return mav;
    }


    /**
     * Realiza el informe Lopd del Registro seleccionado
     */
    @RequestMapping(value = "/{idRegistro}/{idTipoRegistro}/informeRegistroLopd", method = RequestMethod.GET)
    public String informeRegistroLopd(Model model,@PathVariable Long idRegistro, @PathVariable Long idTipoRegistro, HttpServletRequest request)throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);

        Integer numRegistro = null;
        String anyoRegistro = "";
        Libro libro = null;

        if(idTipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)){

            // Añade la información del Registro creado
            RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);

            // Comprueba que el Registro existe
            if(registro == null) {
                log.info("No existe el Registro Entrada");
                Mensaje.saveMessageError(request, getMessage("aviso.registroEntrada.noExiste"));
                return "redirect:/informe/registroLopd";
            }
            // Comprueba que el registro es de la Entidad Activa
            if(!Objects.equals(registro.getUsuario().getEntidad().getId(), entidadActiva.getId())) {
                log.info("No es administrador de este registro");
                Mensaje.saveMessageError(request, getMessage("aviso.registro.noAdministrador"));
                return "redirect:/informe/registroLopd";
            }

            model.addAttribute("registro", registro);

            // Añade la información de las modificaciones del Registro
            List<HistoricoRegistroEntrada> modificaciones = historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro);
            // Eliminamos el primer registro porque es el de creación
            if(modificaciones.size()>0) {
                modificaciones.remove(modificaciones.size()-1);
            }
            model.addAttribute("modificaciones", modificaciones);

            // Registros Listados y Consultas
            anyoRegistro = formatYear.format(registro.getFecha());
            libro = registro.getLibro();
            Long idLibro = registro.getLibro().getId();
            numRegistro = registro.getNumeroRegistro();

            List<Lopd> consultas = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_ENTRADA);
            model.addAttribute("consultas", consultas);

            List<Lopd> listados = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_ENTRADA);
            model.addAttribute("listados", listados);

            List<Lopd> consultasJustificante = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_JUSTIFICANTE, RegwebConstantes.REGISTRO_ENTRADA);
            model.addAttribute("consultasJustificante", consultasJustificante);
        }

        if(idTipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)){

            // Añade la información del Registro creado
            RegistroSalida registro = registroSalidaEjb.findById(idRegistro);

            // Comprueba que el Registro existe
            if(registro == null) {
                log.info("No existe el Registro Salida");
                Mensaje.saveMessageError(request, getMessage("aviso.registroSalida.noExiste"));
                return  "redirect:/informe/registroLopd";
            }
            // Comprueba que el registro es de la Entidad Activa
            if(!Objects.equals(registro.getUsuario().getEntidad().getId(), entidadActiva.getId())){
                log.info("No es administrador de este registro");
                Mensaje.saveMessageError(request, getMessage("aviso.registro.noAdministrador"));
                return  "redirect:/informe/registroLopd";
            }

            model.addAttribute("registro", registro);

            // Añade la información de las modificaciones del Registro
            List<HistoricoRegistroSalida> modificaciones = historicoRegistroSalidaEjb.getByRegistroSalida(idRegistro);
            // Eliminamos el primer registro porque es el de creación
            if(modificaciones.size()>0) {
                modificaciones.remove(modificaciones.size()-1);
            }
            model.addAttribute("modificaciones", modificaciones);

            // Registros Listados y Consultas
            anyoRegistro = formatYear.format(registro.getFecha());
            libro = registro.getLibro();
            Long idLibro = registro.getLibro().getId();
            numRegistro = registro.getNumeroRegistro();

            List<Lopd> consultas = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_CONSULTA, RegwebConstantes.REGISTRO_SALIDA);
            model.addAttribute("consultas", consultas);

            List<Lopd> listados = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_LISTADO, RegwebConstantes.REGISTRO_SALIDA);
            model.addAttribute("listados", listados);

            List<Lopd> consultasJustificante = lopdEjb.getByRegistro(anyoRegistro, numRegistro, idLibro, RegwebConstantes.LOPD_JUSTIFICANTE, RegwebConstantes.REGISTRO_SALIDA);
            model.addAttribute("consultasJustificante", consultasJustificante);
        }

        model.addAttribute("idTipoRegistro", idTipoRegistro);
        model.addAttribute("numRegistro", numRegistro);
        model.addAttribute("anyoRegistro", anyoRegistro);
        model.addAttribute("libro", libro);

        return "informe/informeRegistroLopd";
    }

    /**
     * Realiza el informe en pdf o excel de con las acciones realizadas sobre un registro
     */
    @RequestMapping(value = "/{formato}/{tipoRegistro}/{idRegistro}/registroLopdCrear", method = RequestMethod.GET)
    public ModelAndView registroLopdCrear(@PathVariable Long idRegistro, @PathVariable String formato, @PathVariable String tipoRegistro, HttpServletRequest request)throws Exception {

        ModelAndView mav = null;

        if(formato.equals("pdf")){
            mav = new ModelAndView("registroLopdPdf");
        }else if(formato.equals("excel")){
            mav = new ModelAndView("registroLopdExcel");
        }

        mav.addObject("tipoRegistro", tipoRegistro);

        // RESGISTRO DE ENTRADA
        if(tipoRegistro.equals("entrada")) {

            RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);

            mav.addObject("numeroRegistro", registro.getNumeroRegistro().toString());

            // Obtiene los datos del registro de entrada
            ArrayList<String> valorRegistro = new ArrayList<String>();
            valorRegistro.add(formatYear.format(registro.getFecha()) + " / " + registro.getNumeroRegistro().toString());
            valorRegistro.add(formatDateLong.format(registro.getFecha()));
            valorRegistro.add(registro.getLibro().getNombreCompleto());
            valorRegistro.add(registro.getUsuario().getUsuario().getIdentificador());
            valorRegistro.add(registro.getOficina().getDenominacion());

            if(registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {
                valorRegistro.add("VÀLID");
            } else if(registro.getEstado().equals(RegwebConstantes.REGISTRO_DISTRIBUIDO)) {
                valorRegistro.add("TRAMITAT");
            } else if(registro.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {
                valorRegistro.add("ANUL·LAT");
            } else if(registro.getEstado().equals(RegwebConstantes.REGISTRO_OFICIO_EXTERNO)) {
                valorRegistro.add("OFICI EXTERN");
            } else if(registro.getEstado().equals(RegwebConstantes.REGISTRO_OFICIO_INTERNO)) {
                valorRegistro.add("OFICI INTERN");
            } else if(registro.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)) {
                valorRegistro.add("PENDENT");
            } else if(registro.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {
                valorRegistro.add("PENDENT VISAR");
            }else if(registro.getEstado().equals(RegwebConstantes.REGISTRO_OFICIO_SIR)) {
                valorRegistro.add("OFICI SIR");
            }

            valorRegistro.add(registro.getDestino().getDenominacion());
            valorRegistro.add(registro.getRegistroDetalle().getExtracto());

            mav.addObject("valorRegistro", valorRegistro);

            // Obtiene los datos del histórico del registro de entrada
            ArrayList<ArrayList<String>> registros = new ArrayList<ArrayList<String>>();

            List<HistoricoRegistroEntrada> historicos = historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro);

            for (int i = 0; i < historicos.size(); i++) {
                registros.add(new ArrayList<String>());
                HistoricoRegistroEntrada historicoRegistroEntrada = historicos.get(i);
                registros.get(i).add(formatYear.format(registro.getFecha()) + " / " + registro.getNumeroRegistro().toString());
                registros.get(i).add(formatDateLong.format(historicoRegistroEntrada.getFecha()));
                registros.get(i).add(historicoRegistroEntrada.getModificacion());
                registros.get(i).add(historicoRegistroEntrada.getUsuario().getUsuario().getIdentificador());

                if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {
                    registros.get(i).add("VÀLID");
                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_DISTRIBUIDO)) {
                    registros.get(i).add("TRAMITAT");
                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {
                registros.get(i).add("ANUL·LAT");
                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_OFICIO_EXTERNO)) {
                    registros.get(i).add("OFICI EXTERN");
                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_OFICIO_INTERNO)) {
                    registros.get(i).add("OFICI INTERN");
                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)) {
                    registros.get(i).add("PENDENT");
                } else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {
                    registros.get(i).add("PENDENT VISAR");
                }else if(historicoRegistroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_OFICIO_SIR)) {
                    registros.get(i).add("OFICI SIR");
                }
            }

            mav.addObject("registros", registros);


        //  REGISTRO DE SALIDA
        } else if(tipoRegistro.equals("salida")){
            //todo Joan Pernia, faltan las modificaciones?

            RegistroSalida registro = registroSalidaEjb.findById(idRegistro);

            mav.addObject("numeroRegistro", registro.getNumeroRegistro());

            // Obtiene los datos del registro de salida
            ArrayList<String> valorRegistro = new ArrayList<String>();
            valorRegistro.add(registro.getNumeroRegistro().toString());
            valorRegistro.add(formatDateLong.format(registro.getFecha()));
            valorRegistro.add(registro.getLibro().getNombreCompleto());
            valorRegistro.add(registro.getOficina().getDenominacion());

            mav.addObject("valorRegistro", valorRegistro);

        }

        return mav;
    }


    /**
     * Informe de Indicadores por Oficina
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/indicadoresOficina", method = RequestMethod.GET)
    public String indicadoresOficina(Model model, HttpServletRequest request)throws Exception {

        InformeIndicadoresOficinaBusquedaForm informeIndicadoresOficinaBusquedaForm = new InformeIndicadoresOficinaBusquedaForm();
        informeIndicadoresOficinaBusquedaForm.setFechaFin(new Date());
        model.addAttribute("oficinasInforme", oficinasInformeOficina(request));
        model.addAttribute("informeIndicadoresOficinaBusquedaForm",informeIndicadoresOficinaBusquedaForm);

        return "informe/indicadoresOficina";
    }

    /**
     * Realiza la busqueda de registros según los parametros del formulario de Indicadores por Oficina
     */
    @RequestMapping(value = "/indicadoresOficina", method = RequestMethod.POST)
    public ModelAndView indicadoresOficina(@ModelAttribute InformeIndicadoresOficinaBusquedaForm informeIndicadoresOficinaBusquedaForm, HttpServletRequest request)throws Exception {

        String formato = informeIndicadoresOficinaBusquedaForm.getFormato();

        ModelAndView mav = null;

        if(formato.equals("pdf")){
            mav = new ModelAndView("indicadoresOficinaPdf");
        }else if(formato.equals("excel")){
            mav = new ModelAndView("indicadoresOficinaExcel");
        }

        //Nombre de la Oficina seleccionada
        Oficina oficina = oficinaEjb.findById(informeIndicadoresOficinaBusquedaForm.getOficina());
        if(oficina != null) {
            mav.addObject("nombreOficina", oficina.getDenominacion());
            mav.addObject("codigoOficina", oficina.getCodigo());
        }

        // Intervalo de fechas seleccionado
        mav.addObject("fechaInicio", formatDate.format(informeIndicadoresOficinaBusquedaForm.getFechaInicio()));
        mav.addObject("fechaFin", formatDate.format(informeIndicadoresOficinaBusquedaForm.getFechaFin()));

        Date dataFi = RegistroUtils.ajustarHoraBusqueda(informeIndicadoresOficinaBusquedaForm.getFechaFin());
        Date dataInici = informeIndicadoresOficinaBusquedaForm.getFechaInicio();

        // Busca los registros Totales de Entrada y Salida entre las fechas
        mav.addObject("registrosEntrada", informeEjb.buscaIndicadoresOficinaTotalEntrada(dataInici, dataFi, oficina.getId()).intValue());
        mav.addObject("registrosSalida", informeEjb.buscaIndicadoresOficinaTotalSalida(dataInici, dataFi, oficina.getId()).intValue());

        // Busca los registros totales según el calendario seleccionado de Entrada y Salida entre las fechas
        // Años y meses
        totalRegistresEntradaAny(mav,dataInici,dataFi,null,oficina.getId());
        totalRegistresSalidaAny(mav, dataInici, dataFi,null,oficina.getId());
        totalRegistresEntradaMes(mav, dataInici, dataFi,null,oficina.getId());
        totalRegistresSalidaMes(mav, dataInici, dataFi,null,oficina.getId());

        // Busca los registros totales por Idiomas de Entrada y Salida entre las fechas
        totalRegistresEntradaIdioma(mav,dataInici,dataFi,null,oficina.getId());
        totalRegistresSalidaIdioma(mav,dataInici,dataFi,null,oficina.getId());

        return mav;
    }



    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    private List<Organismo> organismos(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<Organismo> organismos = null;

        // Es operador
        if(isOperador(request)){

            // Obtenemos los Libros de los cuales el Usuario es administrador
            organismos = permisoOrganismoUsuarioEjb.getOrganismosAdministrados(usuarioEntidad.getId());

            if(organismos.size()==0) {
                organismos = getOrganismosConsultaEntrada(request);
            }
        }

        // Es Administrador de Entidad
        if(isAdminEntidad(request)){
            organismos = organismoEjb.getPermitirUsuarios(getEntidadActiva(request).getId());
        }

        return organismos;
    }

    /**
     * Obtiene las {@link es.caib.regweb3.model.Oficina} del Usuario actual
     */
    private List<Oficina> oficinasInformeOficina(HttpServletRequest request) throws Exception {

        List<Oficina> oficinas = new ArrayList<Oficina>();

        // Es operador
        if(isOperador(request)){
            LinkedHashSet<Oficina> llistaOficines = getOficinasAcceso(request);
            oficinas.addAll(llistaOficines);
        }

        // Es Administrador de Entidad
        if(isAdminEntidad(request)){
            oficinas = oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        }

        return oficinas;
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    private List<UsuarioEntidad> usuarios(HttpServletRequest request) throws Exception {

        getRolActivo(request);

        List<UsuarioEntidad> usuarios = null;

        // Es Administrador de Entidad
        if(isAdminEntidad(request)){
            usuarios = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        }

        // Es operador
        if(isOperador(request)){
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            List<Organismo> organismos = permisoOrganismoUsuarioEjb.getOrganismosAdministrados(usuarioEntidad.getId());

            // Obtenemos los usuarios de los libros de los cuales el Usuario es administrador
            usuarios = permisoOrganismoUsuarioEjb.getUsuariosEntidadByOrganismos(organismos);
        }

        return usuarios;
    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.UsuarioEntidad} del Organismo Seleccionado
     */
    @RequestMapping(value = "/obtenerUsuarios", method = RequestMethod.GET)
    public @ResponseBody
    List<UsuarioEntidad> obtenerUsuarios(@RequestParam Long id,HttpServletRequest request) throws Exception {

        if(id != -1) {
            return permisoOrganismoUsuarioEjb.getUsuariosEntidadByOrganismo(id);
        }else{
            return usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        }

    }

    /**
     * Obtiene los {@link es.caib.regweb3.model.Organismo} del Usuario actual y Tipo Registro seleccionado
     */
    @RequestMapping(value = "/obtenerOrganismos", method = RequestMethod.GET)
    public @ResponseBody
    List<Organismo> obtenerOrganismos(HttpServletRequest request, @RequestParam Long id) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<Organismo> organismos = null;

        // Es operador
        if(isOperador(request)){

            // Obtenemos los ORganismos de los cuales el Usuario es administrador
            organismos = getOrganismosResponsable(request);

            if(organismos.size()>0) {
                return organismos ;
            }else if(id.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
                organismos = getOrganismosConsultaEntrada(request);
            }else{
                organismos = getOrganismosConsultaSalida(request);
            }
        }

        // Es Administrador de Entidad
        if(isAdminEntidad(request)){
            organismos = organismoEjb.getPermitirUsuarios(getEntidadActiva(request).getId());
        }

        return organismos;
    }

    /**
     * Busca los registros de entrada totales por Años entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param idEntidad
     * @param idOficina
     * @throws Exception
     */
    private void totalRegistresEntradaAny(ModelAndView mav, Date dataInici, Date dataFi, Long idEntidad, Long idOficina) throws Exception{

        List<String> entradaAnosValor = new ArrayList<String>();
        List<String> entradaAnosNombre = new ArrayList<String>();

        while (dataInici.compareTo(dataFi) < 0) {
            String anyActual = formatYear.format(dataInici);
            entradaAnosNombre.add(anyActual);
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DATE, 31);
            cal.set(Calendar.YEAR, Integer.parseInt(anyActual));
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            if((idEntidad!=null)&&(idOficina==null)) {
                if (cal.getTime().compareTo(dataFi) < 0) {
                    entradaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresEntradaTotal(dataInici, cal.getTime(), idEntidad)));
                } else {
                    entradaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresEntradaTotal(dataInici, dataFi, idEntidad)));
                    break;
                }
            }
            if((idEntidad==null)&&(idOficina!=null)) {
                if (cal.getTime().compareTo(dataFi) < 0) {
                    entradaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalEntrada(dataInici, cal.getTime(), idOficina)));
                } else {
                    entradaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalEntrada(dataInici, dataFi, idOficina)));
                    break;
                }
            }
            cal.add(Calendar.DATE, 1);
            dataInici = cal.getTime();
        }
        mav.addObject("entradaAnosValor", entradaAnosValor);
        mav.addObject("entradaAnosNombre", entradaAnosNombre);

    }

    /**
     * Busca los registros de salida totales por Años entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param idEntidad
     * @param idOficina
     * @throws Exception
     */
    private void totalRegistresSalidaAny(ModelAndView mav, Date dataInici, Date dataFi, Long idEntidad, Long idOficina) throws Exception{

        List<String> salidaAnosValor = new ArrayList<String>();
        List<String> salidaAnosNombre = new ArrayList<String>();

        while (dataInici.compareTo(dataFi) < 0) {
            String anyActual = formatYear.format(dataInici);
            salidaAnosNombre.add(anyActual);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DATE, 31);
            cal.set(Calendar.YEAR, Integer.parseInt(anyActual));
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            if((idEntidad!=null)&&(idOficina==null)) {
                if (cal.getTime().compareTo(dataFi) < 0) {
                    salidaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresSalidaTotal(dataInici, cal.getTime(), idEntidad)));
                } else {
                    salidaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresSalidaTotal(dataInici, dataFi, idEntidad)));
                    break;
                }
            }
            if((idEntidad==null)&&(idOficina!=null)) {
                if (cal.getTime().compareTo(dataFi) < 0) {
                    salidaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalSalida(dataInici, cal.getTime(), idOficina)));
                } else {
                    salidaAnosValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalSalida(dataInici, dataFi, idOficina)));
                    break;
                }
            }
            cal.add(Calendar.DATE, 1);
            dataInici = cal.getTime();
        }
        mav.addObject("salidaAnosValor", salidaAnosValor);
        mav.addObject("salidaAnosNombre", salidaAnosNombre);
    }



    /**
     * Busca los registros de entrada totales por Meses entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param idEntidad
     * @param idOficina
     * @throws Exception
     */
    private void totalRegistresEntradaMes(ModelAndView mav, Date dataInici, Date dataFi, Long idEntidad, Long idOficina) throws Exception{

        List<String> entradaMesesValor = new ArrayList<String>();
        List<String> entradaMesesNombre = new ArrayList<String>();

        // Mientras no hemos llegado a la fecha final
        while (dataInici.compareTo(dataFi) < 0) {
            String anyActual = formatYear.format(dataInici);
            String mesActualNom = formatMes.format(dataInici);
            String mesActual = formatMonth.format(dataInici);
            String mesActualCompost = mesActualNom + "/" + anyActual;

            // Añade el mes a la tabla
            entradaMesesNombre.add(mesActualCompost);
            // Empezamos a montar una fecha del mes que se está tratando en este momento
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Integer.parseInt(mesActual) - 1);
            int mesAra = Integer.parseInt(mesActual);
            int anyAra = Integer.parseInt(anyActual);
            // Borramos ponemos a 0 la hora de la fecha que estamos montando
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            // Calculamos el último día del mes para la fecha que montamos
            if((mesAra == 1)|| (mesAra == 3)|| (mesAra == 5)|| (mesAra == 7)|| (mesAra == 8)|| (mesAra == 10)|| (mesAra == 12)){
                cal.set(Calendar.DAY_OF_MONTH, 31);
            } else if((mesAra == 4)|| (mesAra == 6)|| (mesAra == 9)|| (mesAra == 11)){
                cal.set(Calendar.DAY_OF_MONTH, 30);
            } else if((anyAra % 4 == 0) && ((anyAra % 100 != 0) || (anyAra % 400 == 0))){
                cal.set(Calendar.DAY_OF_MONTH, 29);
            } else{
                cal.set(Calendar.DAY_OF_MONTH, 28);
            }
            // Añadimos las 23:59:59 a la fecha que montamos, que será la fechaFin para la búsqueda (para calcular cada mes por separado)
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.YEAR, Integer.parseInt(anyActual));

            // Miramos si es informe por entidad o por oficina
            if((idEntidad!=null)&&(idOficina==null)) {
                // Es informe por entidad
                if (cal.getTime().compareTo(dataFi) < 0) {
                    // Si no estamos en el último mes de la búsqueda, coje la fecha montada como fechaFin
                    entradaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresEntradaTotal(dataInici, cal.getTime(), idEntidad)));
                } else {
                    // Si estamos en el último mes de la búsqueda, utiliza la dataFi
                    entradaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresEntradaTotal(dataInici, dataFi, idEntidad)));
                    break;
                }
            }
            // Miramos si es informe por entidad o por oficina
            if((idEntidad==null)&&(idOficina!=null)) {
                // Es informe por oficina
                if (cal.getTime().compareTo(dataFi) < 0) {
                    // Si no estamos en el último mes de la búsqueda, coje la fecha montada como fechaFin
                    entradaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalEntrada(dataInici, cal.getTime(), idOficina)));
                } else {
                    // Si estamos en el último mes de la búsqueda, utiliza la dataFi
                    entradaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalEntrada(dataInici, dataFi, idOficina)));
                    break;
                }
            }
            // Montamos la fechaInicio para la búsqueda del próximo mes
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            dataInici = cal.getTime();
        }
        mav.addObject("entradaMesesValor", entradaMesesValor);
        mav.addObject("entradaMesesNombre", entradaMesesNombre);

    }

    /**
     * Busca los registros de salida totales por Meses entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param idEntidad
     * @param idOficina
     * @throws Exception
     */
    private void totalRegistresSalidaMes(ModelAndView mav, Date dataInici, Date dataFi, Long idEntidad, Long idOficina) throws Exception{

        List<String> salidaMesesValor = new ArrayList<String>();
        List<String> salidaMesesNombre = new ArrayList<String>();

        // Mientras no hemos llegado a la fecha final
        while (dataInici.compareTo(dataFi) < 0) {
            String anyActual = formatYear.format(dataInici);
            String mesActualNom = formatMes.format(dataInici);
            String mesActual = formatMonth.format(dataInici);
            String mesActualCompost = mesActualNom + "/" + anyActual;

            // Añade el mes a la tabla
            salidaMesesNombre.add(mesActualCompost);
            // Empezamos a montar una fecha del mes que se está tratando en este momento
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Integer.parseInt(mesActual) - 1);
            int mesAra = Integer.parseInt(mesActual);
            int anyAra = Integer.parseInt(anyActual);
            // Borramos ponemos a 0 la hora de la fecha que estamos montando
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            // Calculamos el último día del mes para la fecha que montamos
            if((mesAra == 1)|| (mesAra == 3)|| (mesAra == 5)|| (mesAra == 7)|| (mesAra == 8)|| (mesAra == 10)|| (mesAra == 12)){
                cal.set(Calendar.DAY_OF_MONTH, 31);
            } else if((mesAra == 4)|| (mesAra == 6)|| (mesAra == 9)|| (mesAra == 11)){
                cal.set(Calendar.DAY_OF_MONTH, 30);
            } else if((anyAra % 4 == 0) && ((anyAra % 100 != 0) || (anyAra % 400 == 0))){
                cal.set(Calendar.DAY_OF_MONTH, 29);
            } else{
                cal.set(Calendar.DAY_OF_MONTH, 28);
            }
            // Añadimos las 23:59:59 a la fecha que montamos, que será la fechaFin para la búsqueda (para calcular cada mes por separado)
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.YEAR, Integer.parseInt(anyActual));

            // Miramos si es informe por entidad o por oficina
            if((idEntidad!=null)&&(idOficina==null)) {
                // Es informe por entidad
                if (cal.getTime().compareTo(dataFi) < 0) {
                    // Si no estamos en el último mes de la búsqueda, coje la fecha montada como fechaFin
                    salidaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresSalidaTotal(dataInici, cal.getTime(), idEntidad)));
                } else {
                    // Si estamos en el último mes de la búsqueda, utiliza la dataFi
                    salidaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresSalidaTotal(dataInici, dataFi, idEntidad)));
                    break;
                }
            }
            // Miramos si es informe por entidad o por oficina
            if((idEntidad==null)&&(idOficina!=null)) {
                // Es informe por oficina
                if (cal.getTime().compareTo(dataFi) < 0) {
                    // Si no estamos en el último mes de la búsqueda, coje la fecha montada como fechaFin
                    salidaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalSalida(dataInici, cal.getTime(), idOficina)));
                } else {
                    // Si estamos en el último mes de la búsqueda, utiliza la dataFi
                    salidaMesesValor.add(String.valueOf(informeEjb.buscaIndicadoresOficinaTotalSalida(dataInici, dataFi, idOficina)));
                    break;
                }
            }
            // Montamos la fechaInicio para la búsqueda del próximo mes
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            dataInici = cal.getTime();
        }

        mav.addObject("salidaMesesValor", salidaMesesValor);
        mav.addObject("salidaMesesNombre", salidaMesesNombre);

    }

    /**
     * Busca los registros de entrada totales por Organismo entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param organismos
     * @throws Exception
     */
    private void totalRegistresEntradaOrganismo(ModelAndView mav,Date dataInici, Date dataFi, List<Organismo> organismos) throws Exception{

        List<String> entradaConselleriaValor = new ArrayList<String>();
        List<String> entradaConselleriaNombre = new ArrayList<String>();

        for (Organismo organismo : organismos) {
            Long total = informeEjb.buscaEntradaPorConselleria(dataInici, dataFi, organismo.getId());
            if (total > 0) { // Solo lo añadimos al informe si tiene algún registro
                entradaConselleriaNombre.add(organismo.getDenominacion());
                entradaConselleriaValor.add(String.valueOf(total));
            }

        }
        mav.addObject("entradaConselleriaValor", entradaConselleriaValor);
        mav.addObject("entradaConselleriaNombre", entradaConselleriaNombre);

    }

    /**
     * Busca los registros de salida totales por Organismo entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param organismos
     * @throws Exception
     */
    private void totalRegistresSalidaOrganismo(ModelAndView mav,Date dataInici, Date dataFi, List<Organismo> organismos) throws Exception{

        List<String> salidaConselleriaValor = new ArrayList<String>();
        List<String> salidaConselleriaNombre = new ArrayList<String>();

        for (Organismo organismo : organismos) {
            Long total = informeEjb.buscaSalidaPorConselleria(dataInici, dataFi, organismo.getId());
            if (total > 0) { // Solo lo añadimos al informe si tiene algún registro
                salidaConselleriaNombre.add(organismo.getDenominacion());
                salidaConselleriaValor.add(String.valueOf(total));
            }

        }

        mav.addObject("salidaConselleriaValor", salidaConselleriaValor);
        mav.addObject("salidaConselleriaNombre", salidaConselleriaNombre);
    }

    /**
     * Busca los registros de entrada totales por Tipos de Asunto entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param tiposAsunto
     * @param idEntidad
     * @throws Exception
     */
    private void totalRegistresEntradaTipoAsunto(ModelAndView mav,Date dataInici, Date dataFi,List<TipoAsunto> tiposAsunto, Long idEntidad) throws Exception{

        List<String> entradaAsuntoValor = new ArrayList<String>();
        List<String> entradaAsuntoNombre = new ArrayList<String>();

        for (TipoAsunto tipoAsunto : tiposAsunto) {
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) tipoAsunto.getTraduccion();
            entradaAsuntoNombre.add(traduccionTipoAsunto.getNombre());
            entradaAsuntoValor.add(String.valueOf(informeEjb.buscaEntradaPorAsunto(dataInici, dataFi, tipoAsunto.getId(), idEntidad)));
        }
        mav.addObject("entradaAsuntoValor", entradaAsuntoValor);
        mav.addObject("entradaAsuntoNombre", entradaAsuntoNombre);
    }

    /**
     * Busca los registros de salida totales por Tipos de Asunto entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param tiposAsunto
     * @param idEntidad
     * @throws Exception
     */
    private void totalRegistresSalidaTipoAsunto(ModelAndView mav,Date dataInici, Date dataFi,List<TipoAsunto> tiposAsunto, Long idEntidad) throws Exception{

          List<String> salidaAsuntoValor = new ArrayList<String>();
        List<String> salidaAsuntoNombre = new ArrayList<String>();

        for (TipoAsunto tipoAsunto : tiposAsunto) {
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) tipoAsunto.getTraduccion();

            salidaAsuntoNombre.add(traduccionTipoAsunto.getNombre());

            salidaAsuntoValor.add(String.valueOf(informeEjb.buscaSalidaPorAsunto(dataInici, dataFi, tipoAsunto.getId(), idEntidad)));
        }

        mav.addObject("salidaAsuntoValor", salidaAsuntoValor);
        mav.addObject("salidaAsuntoNombre", salidaAsuntoNombre);
    }

    /**
     *  Busca los registros totales por Libro de Entrada entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param organismos
     * @throws Exception
     */
    private void totalRegistresEntradaLibro(ModelAndView mav,Date dataInici, Date dataFi,List<Organismo> organismos) throws Exception{

        List<String> entradaLibroValor = new ArrayList<String>();
        List<String> entradaLibroNombre = new ArrayList<String>();

        for (Organismo organismo : organismos) {
            List<Libro> libros = libroEjb.getLibrosOrganismo(organismo.getId());
            for (Libro libro : libros) {
                Long total = informeEjb.buscaEntradaPorLibro(dataInici, dataFi, libro.getId());
                if (total > 0) { // Solo lo añadimos al informe si tiene algún registro
                    entradaLibroNombre.add(libro.getNombre());
                    entradaLibroValor.add(String.valueOf(total));
                }

            }
        }
        mav.addObject("entradaLibroValor", entradaLibroValor);
        mav.addObject("entradaLibroNombre", entradaLibroNombre);
    }

    /**
     *  Busca los registros totales por Libro de Salida entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param organismos
     * @throws Exception
     */
    private void totalRegistresSalidaLibro(ModelAndView mav,Date dataInici, Date dataFi,List<Organismo> organismos) throws Exception{

        List<String> salidaLibroValor = new ArrayList<String>();
        List<String> salidaLibroNombre = new ArrayList<String>();

        for (Organismo organismo : organismos) {
            List<Libro> libros = libroEjb.getLibrosOrganismo(organismo.getId());
            for (Libro libro : libros) {
                Long total = informeEjb.buscaSalidaPorLibro(dataInici, dataFi, libro.getId());
                if (total > 0) { // Solo lo añadimos al informe si tiene algún registro
                    salidaLibroNombre.add(libro.getNombre());
                    salidaLibroValor.add(String.valueOf(total));
                }

            }
        }
        mav.addObject("salidaLibroValor", salidaLibroValor);
        mav.addObject("salidaLibroNombre", salidaLibroNombre);
    }

    /**
     * Busca los registros totales por Oficina de Entrada entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param oficinas
     * @throws Exception
     */
    private void totalRegistresEntradaOficina(ModelAndView mav,Date dataInici, Date dataFi,List<Oficina> oficinas) throws Exception{
        List<String> entradaOficinaValor = new ArrayList<String>();
        List<String> entradaOficinaNombre = new ArrayList<String>();

        for (Oficina oficina : oficinas) {
            Long total = informeEjb.buscaEntradaPorOficina(dataInici, dataFi, oficina.getId());
            if (total > 0) { // Solo lo añadimos al informe si tiene algún registro
                entradaOficinaNombre.add(oficina.getDenominacion());
                entradaOficinaValor.add(String.valueOf(total));
            }

        }
        mav.addObject("entradaOficinaValor", entradaOficinaValor);
        mav.addObject("entradaOficinaNombre", entradaOficinaNombre);
    }

    /**
     * Busca los registros totales por Oficina de Salida entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param oficinas
     * @throws Exception
     */
    private void totalRegistresSalidaOficina(ModelAndView mav,Date dataInici, Date dataFi,List<Oficina> oficinas) throws Exception{

        List<String> salidaOficinaValor = new ArrayList<String>();
        List<String> salidaOficinaNombre = new ArrayList<String>();


        for (Oficina oficina : oficinas) {
            Long total = informeEjb.buscaSalidaPorOficina(dataInici, dataFi, oficina.getId());
            if (total > 0) { // Solo lo añadimos al informe si tiene algún registro
                salidaOficinaNombre.add(oficina.getDenominacion());
                salidaOficinaValor.add(String.valueOf(total));
            }

        }
        mav.addObject("salidaOficinaValor", salidaOficinaValor);
        mav.addObject("salidaOficinaNombre", salidaOficinaNombre);
    }

    /**
     * Busca los registros totales por Idiomas de Entrada entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param idEntidad
     * @param idOficina
     * @throws Exception
     */
    private void totalRegistresEntradaIdioma(ModelAndView mav,Date dataInici, Date dataFi, Long idEntidad, Long idOficina) throws Exception{

        List<String> entradaIdiomaValor = new ArrayList<String>();
        List<String> entradaIdiomaNombre = new ArrayList<String>();

        for(Long idioma : RegwebConstantes.IDIOMAS_REGISTRO){
            final String nombre = I18NUtils.tradueix("idioma." + idioma);
            entradaIdiomaNombre.add(nombre);
            if((idEntidad!=null)&&(idOficina==null)) {
                entradaIdiomaValor.add(String.valueOf(informeEjb.buscaEntradaPorIdioma(dataInici, dataFi, idioma, idEntidad)));
            }
            if((idEntidad==null)&&(idOficina!=null)) {
                entradaIdiomaValor.add(String.valueOf(informeEjb.buscaEntradaPorIdiomaOficina(dataInici, dataFi, idioma, idOficina)));
            }
        }
        mav.addObject("entradaIdiomaValor", entradaIdiomaValor);
        mav.addObject("entradaIdiomaNombre", entradaIdiomaNombre);
    }

    /**
     * Busca los registros totales por Idiomas de Salida entre las fechas
     * @param mav
     * @param dataInici
     * @param dataFi
     * @param idEntidad
     * @param idOficina
     * @throws Exception
     */
    private void totalRegistresSalidaIdioma(ModelAndView mav,Date dataInici, Date dataFi, Long idEntidad, Long idOficina) throws Exception{

        List<String> salidaIdiomaValor = new ArrayList<String>();
        List<String> salidaIdiomaNombre = new ArrayList<String>();

        for(Long idioma : RegwebConstantes.IDIOMAS_REGISTRO){
            final String nombre = I18NUtils.tradueix("idioma." + idioma);
            salidaIdiomaNombre.add(nombre);
            if((idEntidad!=null)&&(idOficina==null)) {
                salidaIdiomaValor.add(String.valueOf(informeEjb.buscaSalidaPorIdioma(dataInici, dataFi, idioma, idEntidad)));
            }
            if((idEntidad==null)&&(idOficina!=null)) {
                salidaIdiomaValor.add(String.valueOf(informeEjb.buscaSalidaPorIdiomaOficina(dataInici, dataFi, idioma, idOficina)));
            }
        }

        mav.addObject("salidaIdiomaValor", salidaIdiomaValor);
        mav.addObject("salidaIdiomaNombre", salidaIdiomaNombre);
    }

    @ModelAttribute("estados")
    public Long[] estados() throws Exception {
        return RegwebConstantes.ESTADOS_REGISTRO;
    }

    @InitBinder("informeOrganismoBusquedaForm")
    public void initBinderOrganismoRegistro(WebDataBinder binder) {
        CustomDateEditor dateEditor = new CustomDateEditor(formatDate, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
        //binder.registerCustomEditor(Organismo.class, "organismos",new OrganismoEditor());
    }

    @InitBinder("informeIndicadoresBusquedaForm")
    public void initBinderIndicadores(WebDataBinder binder) {
        CustomDateEditor dateEditor = new CustomDateEditor(formatDate, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

    @InitBinder("usuarioLopdBusqueda")
    public void initBinderUsuarioLopd(WebDataBinder binder) {
        CustomDateEditor dateEditor = new CustomDateEditor(formatDate, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

    @InitBinder("registroLopdBusqueda")
    public void initBinderRegistroLopd(WebDataBinder binder) {
        CustomDateEditor dateEditor = new CustomDateEditor(formatDate, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

    @InitBinder("informeIndicadoresOficinaBusquedaForm")
    public void initBinderIndicadoresOficina(WebDataBinder binder) {
        CustomDateEditor dateEditor = new CustomDateEditor(formatDate, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }
}