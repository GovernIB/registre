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

  
    @EJB(mappedName = CatComunidadAutonomaLocal.JNDI_NAME)
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = CatNivelAdministracionLocal.JNDI_NAME)
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = CatProvinciaLocal.JNDI_NAME)
    private CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = TrazabilidadLocal.JNDI_NAME)
    public TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = LopdLocal.JNDI_NAME)
    public LopdLocal lopdEjb;

    @EJB(mappedName = CatPaisLocal.JNDI_NAME)
    private CatPaisLocal catPaisEjb;
    
    @EJB(mappedName = ScanWebModuleLocal.JNDI_NAME)
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
    public Long[] tiposDocumentacionFisica() {
        return RegwebConstantes.TIPOS_DOCFISICA;
    }


    @ModelAttribute("estados")
    public Long[] estados() throws Exception {
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

}
