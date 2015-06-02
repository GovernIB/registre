package es.caib.regweb.webapp.controller.registro;

import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.caib.regweb.model.CatComunidadAutonoma;
import es.caib.regweb.model.CatNivelAdministracion;
import es.caib.regweb.model.Entidad;
import es.caib.regweb.persistence.ejb.CatComunidadAutonomaLocal;
import es.caib.regweb.persistence.ejb.CatNivelAdministracionLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.scan.ScannerManager;

/**
 * 
 * @author anadal
 *
 */
@Controller
public abstract class AbstractRegistroCommonListController extends BaseController {

  
  
  @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
  public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

  @EJB(mappedName = "regweb/CatNivelAdministracionEJB/local")
  public CatNivelAdministracionLocal catNivelAdministracionEjb;
  
  @ModelAttribute("comunidadesAutonomas")
  public List<CatComunidadAutonoma> comunidadesAutonomas() throws Exception {
      return catComunidadAutonomaEjb.getAll();
  }

  @ModelAttribute("nivelesAdministracion")
  public List<CatNivelAdministracion> nivelesAdministracion() throws Exception {
      return catNivelAdministracionEjb.getAll();
  }  
  
  
  @ModelAttribute("estados")
  public Long[] estados(HttpServletRequest request) throws Exception {
      if(getEntidadActiva(request).getSir()){
          return RegwebConstantes.ESTADOS_REGISTRO_SIR;
      }else {
          return RegwebConstantes.ESTADOS_REGISTRO;
      }
  }
  
  
  public void initAnexos(Entidad entidad, Model model, HttpServletRequest request, Long registroID) throws Exception {
    Integer tipusScan = 0;
    if (entidad.getTipoScan() != null && !"".equals(entidad.getTipoScan())) {
      tipusScan = Integer.parseInt(entidad.getTipoScan());
    }
    //      Integer tipusScan = 2;
    boolean teScan = ScannerManager.teScan(tipusScan);
    model.addAttribute("teScan", teScan);
    if (teScan) {

      model.addAttribute("iframe_anexos_height", AnexoController.BASE_IFRAME_HEIGHT + ScannerManager.getMinHeight(request, tipusScan, String.valueOf(registroID)));
    } else {
      model.addAttribute("iframe_anexos_height", AnexoController.BASE_IFRAME_HEIGHT + AnexoController.FILE_TAB_HEIGHT);
    }
  }
  

  
  
}
