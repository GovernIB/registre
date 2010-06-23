<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
    <script>
	    function addLoadEvent(func) {
    	  var oldonload = window.onload;
	      if (typeof window.onload != 'function') {
	        window.onload = func;
	      } else {
	        window.onload = function() {
	          if (oldonload) {
	            oldonload();
	          }
	          func();
	        }
	      }
	    }

		  function MarcaTot(evnt) {
			  /* Marcam tots els checkboxs de la columna */
			  var valor = false;

			  var elementClickat = null;
			  if (window.event) {
				  elementClickat = window.event.srcElement;
			  } else {
				  elementClickat = evnt.target;
			  }
			  if ( elementClickat.checked == true) {
				  valor=true;
			  } else {
				  valor=false;
			  }
			  //alert('Valor='+valor + ' elementClickat.name='+elementClickat.name+ ' elementClickat.value='+elementClickat.value);
			  var llista = document.getElementById("altaAutorUsu").getElementsByTagName(elementClickat.value);
			  var llistaElementsFormAU = document.getElementById("altaAutorUsu").elements;

			  for (var i=0; i<llistaElementsFormAU.length ;i++) {
				  //window.alert('Element '+i+' '+llistaElementsFormAU[i].name); 
				  if ( llistaElementsFormAU[i].name == elementClickat.value ){
					  llistaElementsFormAU[i].checked=valor;
				  }
			  }
		  }

		  function obriAgruGeo() {
      	miAgruGeo=open("<c:url value='/admin/popup.do?accion=totesAgruGeo'/>","AgrupacionsGeogràfiques","scrollbars,resizable,width=400,height=400");
      	miAgruGeo.focus();
      }

      function setEntidad(codEntidad1, codEntidad2, descod) {
      	var formulari = document.getElementById("cercaAgruGeo");
      	document.getElementById("tipusAgruGeoGestionar").value=codEntidad1;
      	document.getElementById("codiAgruGeoGestionar").value=codEntidad2;
      	formulari.submit();
      }

      function validaCerca() {
    	  var formulari = document.getElementById("cercaAgruGeo");
      	if (document.getElementById("tipusAgruGeoGestionar").value=="" || document.getElementById("codiAgruGeoGestionar").value=="" ) {
      		alert("<fmt:message key='error_camps_alta_agru_geo'/>");
      		return false;
      	} else {
      		return true;
      	}
      }

      function validaGestio() {
        var formulari = document.getElementById("gestionaAgrupacionsGeografiques");

        if (document.getElementById("codTipuAgruGeo").value=="" ) {
					document.getElementById("codTipuAgruGeo").value="0";
				}

            	
        if (document.getElementById("codTipuAgruGeo").value=="" || document.getElementById("codAgruGeo").value==""
					|| document.getElementById("descAgruGeo").value=="" || document.getElementById("codTipusAgruGeoSuperior").value==""
					|| document.getElementById("codAgruGeo").value=="" || document.getElementById("codAgruGeo").value==""
					|| document.getElementById("codAgruGeoSuperior").value=="" || document.getElementById("codiPostal").value=="" ) 
				{
          alert("<fmt:message key='error_camps_alta_entitat'/>");
          return false;
        } else {
          return true;
        }
      }

      function abreError() {
        miError=open("<c:url value='/error.jsp'/>","Error","scrollbars,resizable,width=500,height=380");
        miError.focus();
      }

      function getdescMissatge() {
        return '<c:out escapeXml="false" value="${descMissatge}"/>';
      }

      function getmesInfoMissatge() {
        return '<c:out escapeXml="false" value="${mesInfoMissatge}"/>';
      }

      <c:if test="${not empty missatge}"> 
      addLoadEvent(function() {
        abreError();;
      });				
      </c:if>
        
    </script>
</head>
<body>
     	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="gestio_agrupacions_geografiques"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>


    <c:choose>
    <c:when test="${not hayAgrupacion}">
	
      <br/>
    		<p>&nbsp;</p>
    		<p>&nbsp;</p>  
        <table align="center" >
            <tr>
            <td>
              <div id="menuDocAdm" style="width:340px">
                <form id="cercaAgruGeo" name="cercaAgruGeo" action="<c:url value='/admin/controller.do?accion=agrupacionsgeografiques'/>" method="post" onSubmit="return validaCerca();">
                <ul style="margin-right: 5px">
					        <li><p><fmt:message key='agrupacio_a_gestionar_q'/> <a href="javascript:obriAgruGeo()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align=middle alt="<fmt:message key='cercar'/>"></a></p></li>
                	<li>	
								    <input onKeyPress="return goodchars(event,'0123456789')" type="text" text="codi entitat (català)" name="tipusAgruGeoGestionar" id="tipusAgruGeoGestionar" size="2" maxlength="2" style="width: 20px;"/>
								    <input onKeyPress="return goodchars(event,'0123456789')" type="text" text="subcodi entitat" name="codiAgruGeoGestionar" id="codiAgruGeoGestionar" size="3" maxlength="3" style="width: 30px;"/>
								    <input type="submit" value="<fmt:message key='cercar_alta'/>">
					        </li>
            	  </ul>
						    </form>
            	</div>
            </td>
			    </tr>			
        </table>
    		<p>&nbsp;</p>
    		<p>&nbsp;</p>
	

    </c:when>
    <c:otherwise>

	  <div class="RecuadreCentrat" style="width:500px;">
	    <c:choose>
	    <c:when test="${existeAgrupacion}">
  		  <p><fmt:message key='agrupacio_a_gestionar'/> 
  		    <b><fmt:message key='agrupacio_a_gestionar_2'>
  		      <fmt:param value='${agruGeografiques.codiTipusAgruGeo}'/>
  		      <fmt:param value='${agruGeografiques.codiAgruGeo}'/>
  		      <fmt:param value='${agruGeografiques.descAgruGeo}'/>
  		      <fmt:param value='${agruGeografiques.descTipusAgruGeo}'/>
          </fmt:message></b></p>
  		</c:when>
  		<c:otherwise>
  		  <p><fmt:message key="agrupacio_no_existeix"><fmt:param value="${tipusAgruGeoGestionar} - ${codiAgruGeoGestionar}"/></fmt:message></p>
  		</c:otherwise>
  		</c:choose>

      	<br/>
      <form name="gestionaAgrupacionsGeografiques" id="gestionaAgrupacionsGeografiques" action="<c:url value='/UtilAdmin'/>" method="post" onSubmit="return validaGestio();">	
      <input type="hidden" name="accion" id="accion" value="<c:out escapeXml="true" value='${valorAccio}'/>"/>
      <input type="hidden" name="tipusAgruGeoGestionar" id="tipusAgruGeoGestionar" value="<c:out escapeXml="true" value='${tipusAgruGeoGestionar}'/>"/>
      <input type="hidden" name="codiAgruGeoGestionar" id="codiAgruGeoGestionar" value="<c:out escapeXml="true" value='${codiAgruGeoGestionar}'/>"/>
      <table>
      <tr>
      	<th><fmt:message key='codi_tipus_agrupacio'/></th>
      	<th><fmt:message key='codi_agrupacio_geografica'/></th>
      	<th><fmt:message key='descripcio'/></th>
      	<th><fmt:message key='data_baixa_2'/></th>
      	<th><fmt:message key='codi_tipus_agrupacio_superior'/></th>
      	<th><fmt:message key='codi_agrupacio_geografica_superior'/></th>
      	<th><fmt:message key='codi_postal'/></th>
      </tr>
      <tr>
      	<td><input type="text" name="codTipuAgruGeo" id="codTipuAgruGeo" readonly="true" id="codEntidad" size="2" maxlength="2" value="<c:out escapeXml="true" value='${agruGeografiques.codiTipusAgruGeo}'/>" style="width: 22px;"/></td>
      	<td><input type="text" name="codAgruGeo" id="codAgruGeo" readonly="true" size="3" maxlength="3" value="<c:out escapeXml="true" value='${agruGeografiques.codiAgruGeo}'/>" style="width: 30px;"/></td>
      	<td><input type="text" name="descAgruGeo" id="descAgruGeo" value="<c:out escapeXml="true" value='${agruGeografiques.descAgruGeo}'/>" size="30" maxlength="30" style="width: 200px;"/></td>
      	<td><input onKeyPress="return goodchars(event,'0123456789/')" type="text" name="dataBaixa" id="dataBaixa" value="<c:out escapeXml="true" value='${agruGeografiques.dataBaixa}'/>" size="8" maxlength="8" style="width: 60px;"/></td>
      	<td><input type="text" name="codTipusAgruGeoSuperior" id="codTipusAgruGeoSuperior" size="2"  maxlength="2" value="<c:out escapeXml="true" value='${agruGeografiques.codiTipusAgruGeoSuperior}'/>" style="width: 23px;"/></td>
      	<td><input type="text" name="codAgruGeoSuperior" id="codAgruGeoSuperior" size="3"  maxlength="3" value="<c:out escapeXml="true" value='${agruGeografiques.codiAgruGeoSuperior}'/>" style="width: 30px;"/></td>
      	<td><input type="text" name="codiPostal" id="codiPostal" size="5" maxlength="5" value="<c:out escapeXml="true" value='${agruGeografiques.codiPostal}'/>" style="width: 40px;"/></td>
      </tr>
      <c:if test="${existeAgrupacion and not empty agruGeografiques.descAgruGeoSuperior}">
      <tr>
      	<td align="center" colspan="4"></td>
      	<td align="center" colspan="2">(<c:out escapeXml="true" value='${agruGeografiques.descAgruGeoSuperior}'/>)</td>
      	<td align="center" ></td>
      </tr>
      </c:if>
      <tr>
      	<td align="center" colspan="6"><input type="submit" value="<fmt:message key='desa'/>"/></td>
      </tr>

      </table>
      </form>
    </div>
    
    </c:otherwise>
    </c:choose>


<script type="text/javascript">
	 var elFocus = document.getElementById('<c:out escapeXml="true" value='${elementFocus}'/>');
	 if (elFocus!=null) elFocus.focus();
</script>
		<p>&nbsp;</p> 		
		<p>&nbsp;</p> 
	</body>
</html>   