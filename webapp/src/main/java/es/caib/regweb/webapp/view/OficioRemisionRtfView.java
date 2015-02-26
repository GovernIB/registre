package es.caib.regweb.webapp.view;

import es.caib.regweb.model.ModeloOficioRemision;
import es.caib.regweb.model.OficioRemision;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.webapp.utils.CombineStream;
import es.caib.regweb.webapp.utils.ConvertirTexto;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jpernia
 * Date: 25/04/14
 */
public class OficioRemisionRtfView extends AbstractView {

    protected final Logger log = Logger.getLogger(getClass());

    public OficioRemisionRtfView() {
        setContentType("text/rtf;charset=UTF-8");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {


        OficioRemision oficioRemision = (OficioRemision) model.get("oficioRemision");
        ModeloOficioRemision modeloOficioRemision = (ModeloOficioRemision) model.get("modeloOficioRemision");

        String idiomaActual = request.getLocale().getLanguage();

        File archivo = es.caib.regweb.persistence.utils.FileSystemManager.getArchivo(modeloOficioRemision.getModelo().getId());

        // Convertimos el archivo a array de bytes
        byte[] datos = FileUtils.readFileToByteArray(archivo);
        InputStream is = new ByteArrayInputStream(datos);
        java.util.Hashtable<String,Object> ht = new java.util.Hashtable<String,Object>();

        // Extraemos año de la fecha del registro
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String anoOficio = formatYear.format(oficioRemision.getFecha());
        //SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        //String fechaOficio = formatDate.format(oficioRemision.getFecha());


        // Fecha según el idioma y mes
        Date dataActual = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMMMM", new Locale("es"));
        SimpleDateFormat sdf4 = new SimpleDateFormat("MMMMM", new Locale("ca"));
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
        String diaRecibo = sdf1.format(dataActual);
        String mesRecibo = "";
        String anoRecibo = sdf3.format(dataActual);
        String fechaActual = "";

        if(idiomaActual.equals("es")){
            mesRecibo = sdf2.format(dataActual);
            fechaActual = diaRecibo + " de " + mesRecibo + " de " + anoRecibo;
        }
        if(idiomaActual.equals("ca")){
            mesRecibo = sdf4.format(dataActual);
            if(mesRecibo.startsWith("a") || mesRecibo.startsWith("o")){
                mesRecibo= " d'" + mesRecibo;
            }else{
                mesRecibo= " de " + mesRecibo;
            }
            fechaActual = diaRecibo + mesRecibo + " de " + anoRecibo;
        }

        // Registros Entrada
        String registrosEntrada = "";

        for(RegistroEntrada registroEntrada:oficioRemision.getRegistrosEntrada()){

            registrosEntrada = registrosEntrada.concat("- "+ registroEntrada.getNumeroRegistroFormateado() +"\\\r\n");

        }


        // Mapeamos los campos del rtf con los del registro
        if(oficioRemision.getOrganismoDestinatario() != null) {
            ht.put("(organismoDestinatario)", ConvertirTexto.toCp1252(oficioRemision.getOrganismoDestinatario().getDenominacion()));
        } else{
            ht.put("(organismoDestinatario)", ConvertirTexto.toCp1252(oficioRemision.getDestinoExternoDenominacion()));
        }
        ht.put("(numeroOficio)", ConvertirTexto.toCp1252(oficioRemision.getNumeroOficio().toString()));
        ht.put("(anoOficio)", ConvertirTexto.toCp1252(anoOficio));
        ht.put("(oficina)", ConvertirTexto.toCp1252(oficioRemision.getOficina().getDenominacion()));
        ht.put("(registrosEntrada)", ConvertirTexto.toCp1252(registrosEntrada));
        ht.put("(data)", ConvertirTexto.toCp1252(fechaActual));

        // Reemplaza el texto completo
        ht.put("(read_only)", ConvertirTexto.getISOBytes("\\annotprot\\readprot\\enforceprot1\\protlevel3\\readonlyrecommended "));

        CombineStream cs = new CombineStream(is, ht);

        try {
        // Retornamos el archivo
        String nombreFichero = "OficioRemision_"+ oficioRemision.getNumeroOficio()+ "/"+anoOficio +".rtf";

        response.setHeader("Content-Type", "text/rtf;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+nombreFichero);

        response.setContentType(getContentType());

        // Flush byte array to servlet output stream.
        ServletOutputStream outstream = response.getOutputStream();
        int l=0;
        while ((l=cs.read())!=-1) {
            outstream.write(l);
        }
        outstream.close();
        outstream.flush();
        
        } finally {
          try { cs.close(); } catch (Exception e) {};
        }

    }
}
