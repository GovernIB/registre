package es.caib.regweb3.webapp.view;

import es.caib.regweb3.model.ModeloOficioRemision;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.persistence.ejb.OficioRemisionLocal;
import es.caib.regweb3.utils.CombineStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import javax.ejb.EJB;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jpernia
 * Date: 25/04/14
 */
public class OficioRemisionRtfView extends AbstractView {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = OficioRemisionLocal.JNDI_NAME)
    private OficioRemisionLocal oficioRemisionEjb;

    public OficioRemisionRtfView() {
        setContentType("text/rtf;charset=UTF-8");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {


        OficioRemision oficioRemision = (OficioRemision) model.get("oficioRemision");

        ModeloOficioRemision modeloOficioRemision = (ModeloOficioRemision) model.get("modeloOficioRemision");
        List<String> entradas = (List<String>) model.get("registrosEntrada");
        List<String> salidas = (List<String>) model.get("registrosSalida");
        CombineStream cs = oficioRemisionEjb.generarOficioRemisionRtf(oficioRemision,modeloOficioRemision, entradas,salidas);

        try {
        // Retornamos el archivo
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String anoOficio = formatYear.format(oficioRemision.getFecha());
        String nombreFichero = "OficioRemision_"+ oficioRemision.getNumeroOficio()+ "/"+anoOficio +".rtf";

        response.setHeader("Content-Type", "text/rtf;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+nombreFichero);

        response.setContentType(getContentType());

        // Flush byte array to servlet output stream.
        ServletOutputStream outstream = response.getOutputStream();
        int l;
        while ((l=cs.read())!=-1) {
            outstream.write(l);
        }
        outstream.close();
        outstream.flush();
        
        } finally {
          try { cs.close(); } catch (Exception e) {}
        }

    }
}
