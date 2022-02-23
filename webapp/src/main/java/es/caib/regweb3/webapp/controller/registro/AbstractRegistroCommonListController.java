package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.CatComunidadAutonoma;
import es.caib.regweb3.model.CatNivelAdministracion;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 
 * @author anadal
 *
 */
@Controller
public abstract class AbstractRegistroCommonListController extends BaseController {

  
    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    private CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    public TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/LopdEJB/local")
    public LopdLocal lopdEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    private CatPaisLocal catPaisEjb;
    
    @EJB(mappedName = "regweb3/ScanWebModuleEJB/local")
    private ScanWebModuleLocal scanWebModuleEjb;

  
    @ModelAttribute("comunidadesAutonomas")
    public List<CatComunidadAutonoma> comunidadesAutonomas() throws Exception {
      return catComunidadAutonomaEjb.getAll();
    }

    @ModelAttribute("nivelesAdministracion")
    public List<CatNivelAdministracion> nivelesAdministracion() throws Exception {
      return catNivelAdministracionEjb.getAll();
    }

    @ModelAttribute("tiposDocumentacionFisica")
    public Long[] tiposDocumentacionFisica() throws Exception {
        return RegwebConstantes.TIPOS_DOCFISICA;
    }


    @ModelAttribute("estados")
    public Long[] estados(HttpServletRequest request) throws Exception {

        return RegwebConstantes.ESTADOS_REGISTRO;

    }

    /**
     *  Inicializa los atributos para escanear anexos
     * @param entidad
     * @param model
     * @throws Exception
     */
    public void initScanAnexos(Entidad entidad, Model model) throws I18NException {

        boolean teScan = scanWebModuleEjb.entitatTeScan(entidad.getId());
        model.addAttribute("teScan", teScan);

        boolean permiteDigitMasiva = scanWebModuleEjb.entitatPermetScanMasiu(entidad.getId());
        model.addAttribute("permiteDigitMasiva", permiteDigitMasiva);
    }

    //Montamos la nota informativa de las limitaciones de los anexos cogiendo los valores de las propiedades configuradas
    public void initMensajeNotaInformativaAnexos(Entidad entidad, Model model) throws Exception{
        Integer numeroMaxAnexosSir = PropiedadGlobalUtil.getNumeroMaxAnexosSir();
        Long tamanoMaximoAnexoSir= new Long(0);
        Long maxUploadSizeTotal= new Long(0);
        if(PropiedadGlobalUtil.getTamanoMaximoPorAnexoSir()!= null){
            tamanoMaximoAnexoSir = PropiedadGlobalUtil.getTamanoMaximoPorAnexoSir()/(1024*1024);
        }
        if(PropiedadGlobalUtil.getTamanoMaxTotalAnexosSir()!=null) {
            maxUploadSizeTotal = PropiedadGlobalUtil.getTamanoMaxTotalAnexosSir() / (1024 * 1024);
        }
        model.addAttribute("numeromaxanexossir", numeroMaxAnexosSir);
        model.addAttribute("notainformativa", I18NUtils.tradueix("anexo.notainformativa",numeroMaxAnexosSir.toString(),tamanoMaximoAnexoSir.toString(),maxUploadSizeTotal.toString(),Arrays.toString(RegwebConstantes.ANEXO_EXTENSIONES_SIR) ));
    }

    /**
     * Carga en el Model todos los datos necesarios para gestioanr Interesados
     * @param model
     * @param organismosOficinaActiva
     * @throws Exception
     */
    public void initDatosInteresados(Model model, LinkedHashSet<Organismo> organismosOficinaActiva) throws Exception{

        model.addAttribute("tiposInteresado", RegwebConstantes.TIPOS_INTERESADO);
        model.addAttribute("tiposPersona",RegwebConstantes.TIPOS_PERSONA);
        model.addAttribute("paises",catPaisEjb.getAll());
        model.addAttribute("provincias",catProvinciaEjb.getAll());
        model.addAttribute("canalesNotificacion",RegwebConstantes.CANALES_NOTIFICACION);
        model.addAttribute("tiposDocumento", RegwebConstantes.TIPOS_DOCUMENTOID);
        model.addAttribute("organismosOficinaActiva",organismosOficinaActiva);
    }

    /**
     * Función que elimina acentos y caracteres especiales de una cadena de texto.
     *
     * @param input
     * @return cadena de texto limpia de acentos y c, sustituidos por "_" para facilitar busquedas.
     */
    public static String limpiarCaracteresEspeciales(String input) {

        String output = null;

        try {
            output = new String(input.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "__________________________________";
        for (int i = 0; i < original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }

        return output;
    }

}
