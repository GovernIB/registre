<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>

<%String usuario=request.getRemoteUser();

ValoresFacade valores = ValoresFacadeUtil.getHome().create();

ParametrosListadoRegistrosSalida parametros;
parametros=(ParametrosListadoRegistrosSalida)session.getAttribute("listadoSalida");

Vector oficinasfisicas = valores.buscarOficinasFisicas(usuario, "CS");

%>
<%! 
void escribeSelect(javax.servlet.jsp.JspWriter out, String tipo, Vector valores, String referencia) throws java.io.IOException {
    
    for (int i=0;i<valores.size();i=i+2){
        String codigo=valores.get(i).toString();
        String descripcion=valores.get(i+1).toString();
        if (!codigo.trim().equals("")) {
            out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) ? "selected" : "")+">");
            if (tipo.equals("N")) {
                out.write(descripcion);
            } else {
                out.write(codigo+" - "+descripcion);
            }
            out.write("</option>\n");
        }
    }
}


void escribeSelect2(javax.servlet.jsp.JspWriter out, String tipo, Vector valores, String referencia, String referencia2) throws java.io.IOException {

    for (int i=0;i<valores.size();i=i+3){
    	String ofi=valores.get(i).toString();
    	String codigo=valores.get(i+1).toString();
        String descripcion=valores.get(i+2).toString();
        out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) && ofi.equals(referencia2) ? "selected" : "")+">");
        if (tipo.equals("N")) {
            out.write(descripcion);
        } else {
            out.write(codigo+" - "+descripcion);
        }
        out.write("</option>\n");
    }
}

%>

<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>
        
        
        <jsp:include page="/jscripts/jscalendar/calendario.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <script>
        var oficinasfisicasarray = new Array();
        <% for (int ii=0; oficinasfisicas!=null && ii<oficinasfisicas.size(); ii=ii+3) { %>
        	oficinasfisicasarray.push([<%= oficinasfisicas.get(ii).toString() %>, <%= oficinasfisicas.get(ii+1).toString() %>,"<%= oficinasfisicas.get(ii+2).toString() %>"]);
        <% } %>

        
        function refrescaFisica(){
           	fis=document.getElementById('oficinafisica').value;
           	oficinaDesde=document.getElementById('oficinaDesde').value;
           	oficinaHasta=document.getElementById('oficinaHasta').value;
           	options=document.getElementById('oficinafisica').options;
           	ofifis=document.getElementById('oficinafisica');

    	   	if (oficinaDesde != oficinaHasta) {
        	   	ofifis.selectedIndex = 0;
        	   	ofifis.disabled = true;
        	   	return;
    	   	}

    	   	ofifis.disabled = false;
    	   	oficina = oficinaDesde;
        	var seleccionado = ofifis.selectedIndex;

           	while(ofifis.hasChildNodes()){
           		ofifis.removeChild(ofifis.childNodes[0]);
           	}

       			opcion= new Option('00 - <fmt:message key='registro.todas_autorizadas'/>','');
       			opcion.value='';
       			opcion.selected=false;
       			options[options.length]=opcion;	
        	
            for(i=0; i<oficinasfisicasarray.length; i++) {
           		of = oficinasfisicasarray[i];
           		if(of[0] == oficina){
           			opcion= new Option(of[2],of[1]);
           			opcion.value=of[1];
           			opcion.selected=false;
           			if (fis==of[1]) seleccionado = options.length;
           			options[options.length]=opcion;	
           		}
           	}
           	document.getElementById('oficinafisica').selectedIndex=0;
           }
        </script>
    </head>
    <body bgcolor="#FFFFFF">
    
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="busquedaSalidasIndex.jsp"><fmt:message key='consulta_de_sortides'/></a></li>
		<li><fmt:message key='consulta_per_oficines_i_dates'/></li>
		</ul>
		<!-- Fi Molla pa-->
   <p>&nbsp;</p>

        <center><p><font class="titulo"><fmt:message key='consulta_de_sortides'/></font></center></p>
        <!-- Mostramos Errores si los hubiera -->
        <% Hashtable errores = (parametros==null)? new Hashtable(): parametros.getErrores();
        if (errores.size() > 0) {%>
        <table class="recuadroErrors" width="591" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/></p>
                    <ul>
                        <%      for (Enumeration e=errores.elements();e.hasMoreElements();) { %>
                        <li><%= e.nextElement()%></li>
                        <%}%>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
        <%
        	/* Un cop mostrats els errors, eliminam el llistat.*/
        	session.removeAttribute("listadoSalida");
            }
        %>
        <table align="center" width="70%" class="recuadroSalidas" cols="3">
        <form name="busquedaForm" action="listadoSalida.jsp" method="post">
        <tr>
        	<td style="border:0">&nbsp;<br>&nbsp;</td>
	        <td style="border:0" align="center"><b><fmt:message key='des_de'/></b></td>
 	       <td style="border:0" align="center">
        	    <b><fmt:message key='fins'/></b>
    	    </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='oficina'/>..............................:
            </td>
            <td style="border:0">
                <select name="oficinaDesde" id="oficinaDesde"  onchange="refrescaFisica()">
                    <% escribeSelect(out, "", valores.buscarOficinas(usuario,"CS"), (parametros==null)? "": parametros.getOficinaDesde());%>
                </select>
            </td>
            <td style="border:0">
                <select name="oficinaHasta" id="oficinaHasta"  onchange="refrescaFisica()">
                    <% escribeSelect(out, "", valores.buscarOficinas(usuario,"CS"), (parametros==null)? "": parametros.getOficinaHasta());%>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key="oficina_fisica"/>..................:
            </td>
            <td style="border:0" colspan="2">
                <select name="oficinafisica" id="oficinafisica">
                    <option value="">00 - <fmt:message key='registro.todas_autorizadas'/> </option>
                    <%  escribeSelect2(out, "N", valores.buscarOficinasFisicas(usuario, "CS"), "", ""); %>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
	        <td style="border:0">
    	        &nbsp;<br>&nbsp;<fmt:message key='data_sortida'/>..................:
        	</td>
        	<td style="border:0">
            	<%String anteriorFechaDesde=(parametros==null)? "": parametros.getFechaDesde();%>
            	<input type="text" readonly="true" name="fechaDesde" id="fechaDesde" size="10" maxlength="10" value="<%=anteriorFechaDesde.equals("") ? "" : anteriorFechaDesde%>">&nbsp;<a href="#" ><img src="imagenes/enl_calendario_hab.gif" border="0" id="lanza_calendario1"></a>
        	</td>
        	<td style="border:0">
            	<%String anteriorFechaHasta=(parametros==null)? "": parametros.getFechaHasta();%>
            	<input type="text" readonly="true" name="fechaHasta" id="fechaHasta" size="10" maxlength="10" value="<%=anteriorFechaHasta.equals("") ? "" : anteriorFechaHasta%>">&nbsp;<a href="#" ><img src="imagenes/enl_calendario_hab.gif" border="0" id="lanza_calendario2"></a>
        	</td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
            <td style="border:0">
            &nbsp;<fmt:message key='extracte'/>.............................:
            </td>
            <td style="border:0" colspan="2">
            <%String anteriorExtracto=(parametros==null)? "": parametros.getExtracto();%>
            <input name="extracto" type="text" size="40" value="<%=anteriorExtracto.equals("") ? "" : anteriorExtracto%>">
            </td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='tipus_document'/>..................:
            </td>
            <td colspan="2" style="border:0">
                <select name="tipo" size="1">
                    <option value="  "  selected ><fmt:message key='tots_els_documents'/></option>
                    <% escribeSelect(out, "N", valores.buscarDocumentos(), (parametros==null)? "": parametros.getTipo()); %>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='destinatari'/>.......................:
            </td>
            <td style="border:0" colspan="2">
                <%String anteriorDestinatario=(parametros==null)? "": parametros.getDestinatario();%>
                <input name="destinatario" type="text" size="40" value="<%=anteriorDestinatario.equals("") ? "" : anteriorDestinatario%>">
            </td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='registro.destino_geografico'/>.................:
            </td>
            <td style="border:0" colspan="2">
                <%String anteriorDestino=(parametros==null)? "": parametros.getDestino();%>
                <input name="destino" type="text" size="40" value="<%=anteriorDestino.equals("") ? "" : anteriorDestino%>">
            </td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='remitent'/>............................:
            </td>
            <td style="border:0">
                <%String anteriorRemitente=(parametros==null)? "": parametros.getRemitente();%>
                <input name="remitente" type="text" size="40" value="<%=anteriorRemitente.equals("") ? "" : anteriorRemitente%>">
            </td>
            </td>
			<td style="border:0">
                <%String anteriorCodiRemitent=(parametros==null)? "": parametros.getCodiRemitent();%>
                <fmt:message key='codi_remitent'/>:&nbsp;<input name="codiremitent" type="text" size="4" maxlength="4" value="<%=anteriorCodiRemitent.equals("") ? "" : anteriorCodiRemitent%>"/>
            </td>       
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='mostrar_nombre_total_registres_trobats'/>:<input type="checkbox" name="veureNombreTotalRegistres" id="veureNombreTotalRegistres"/>
            </td>
            <td style="border:0">&nbsp;</td>
			<td style="border:0">&nbsp;</td>            
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
        <td style="border:0" colspan="3">
            <p align="center">
            <input type=submit value="<fmt:message key='cercar'/>">&nbsp;&nbsp;&nbsp;
            </p>
        </td>
        </tr>
        </form>
        </table>
        &nbsp;<p>
        <div align="center">
            [&nbsp;<a href="busquedaSalidasIndex.jsp"><fmt:message key='tornar'/></a>&nbsp;]
        </div>
        <script>
            Calendar.setup(
            {
            inputField  : "fechaDesde",      // ID of the input field
            ifFormat    : "%d/%m/%Y",    // the date format
            button      : "lanza_calendario1"    // ID of the button
            }
            );
            Calendar.setup(
            {
            inputField  : "fechaHasta",      // ID of the input field
            ifFormat    : "%d/%m/%Y",    // the date format
            button      : "lanza_calendario2"    // ID of the button
            }
            );
        </script>
        <script language="JavaScript">
			document.busquedaForm.oficinaDesde.focus();
		</script>
		<script type="text/javascript">
		refrescaFisica();
		</script>
    </body>
</html>
