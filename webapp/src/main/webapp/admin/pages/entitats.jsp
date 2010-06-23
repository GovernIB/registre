<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
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

      function obriEntitats() {
      miRemitentes=open("<c:url value='/admin/popup.do?accion=totesEntitats'/>","Entitats","scrollbars,resizable,width=400,height=400");
      miRemitentes.focus();
      }

      function setEntidad(codEntidad1, codEntidad2, descod) {
      var formulari = document.getElementById("cercaEntitat");
      document.getElementById("entitatGestionar").value=codEntidad1;
      document.getElementById("subentitatGestionar").value=codEntidad2;
      formulari.submit();
      }

      function validaCerca() {
      var formulari = document.getElementById("cercaEntitat");
      if (document.getElementById("entitatGestionar").value=="" || document.getElementById("subentitatGestionar").value=="" ) {
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
      abreError();
      });				
      </c:if>

      </script>
      <script LANGUAGE="JavaScript">


      function confirmaProceso() {
      var respuesta = true;

      codEntidad=document.gestionaOficines.codEntidad.value;
      codEntitat=document.gestionaOficines.codEntitat.value;
      descEntidad=document.gestionaOficines.descEntidad.value;
      descEntitat=document.gestionaOficines.descEntitat.value;
      subcodEntitat=document.gestionaOficines.subcodEntitat.value;

      if ( (codEntidad == null) || (codEntidad.length == 0) || (codEntitat == null) || (codEntitat.length == 0)) {
      alert("<fmt:message key='error_codi_entitat'/>");
      respuesta = false;
      }    
      if ( (descEntidad == null) || (descEntidad.length == 0)||(descEntitat == null)|| (descEntitat.length == 0)) {
      alert("<fmt:message key='error_desc_entitat'/>");
      respuesta = false;
      } 
      if ( (subcodEntitat == null) || (subcodEntitat.length == 0)) {
      alert("<fmt:message key='error_subcodi_entitat'/>");
      respuesta = false;
      } 

      return respuesta;
      }
    </script>
  </head>
  <body>

    <!-- Molla pa --> 
    <ul id="mollaPa">
      <li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
      <li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
      <li><fmt:message key="gestio_entitats"/></li>
    </ul>
    <!-- Fi Molla pa-->
    <p>&nbsp;</p>

    <c:choose>
    <c:when test="${not hayEntidad}">

    <br/>
    <p>&nbsp;</p>
    <p>&nbsp;</p>  
    <table align="center" >
      <tr>
        <td>
          <div id="menuDocAdm" style="width:300px">
            <ul style="margin-right: 5px">
              <li><p><fmt:message key='entitat_a_gestionar_q' /> <a href="javascript:obriEntitats()"><img border="0" src="<c:url value='/imagenes/buscar.gif'/>" align=middle alt="<fmt:message key='cercar'/>"></a></p></li>
              <li>	
                <form id="cercaEntitat" name="cercaEntitat" action="<c:url value='/admin/controller.do?accion=entitats'/>" method="post" onSubmit="return validaCerca();">
                  <input style="width:60px;" type="text" text="codi entitat (catala)" name="entitatGestionar" id="entitatGestionar" size="7" maxlength="7" />
                  <input onKeyPress="return goodchars(event,'0123456789')" style="width:25px;" type="text" text="subcodi entitat" name="subentitatGestionar" id="subentitatGestionar" size="3" maxlength="3" />
                  <input type="submit" value="<fmt:message key='cercar_alta'/>" />
                </form>
              </li>
            </ul>
          </div>
        </td>
      </tr>			
    </table>
    <p>&nbsp;</p>
    <p>&nbsp;</p>


    </c:when>
    <c:otherwise>


    <div class="RecuadreCentrat" style="width:600px;">

      <c:choose>
      <c:when test="${existeEntidad}">
      <p><fmt:message key='entitat_a_gestionar'/> <b><c:out escapeXml="false" value='${entitats.codigoEntidad} - ${entitats.subcodiEnt}'/></b></p>
      </c:when>
      <c:otherwise>
      <p><fmt:message key='entitat_no_existeix'><fmt:param value='${entitatGestionar} - ${subentitatGestionar}'/></fmt:message></p>
      </c:otherwise>
      </c:choose>

      <br/>
      <form name="gestionaOficines" id="gestionaOficines" action="<c:url value='/UtilAdmin'/>" method="post" onsubmit="return confirmaProceso();">	
        <input type="hidden" name="accion" id="accion" value="gestionaEntitat"/>
        <input type="hidden" name="entitatGestionar" id="entitatGestionar" value="<c:out escapeXml="true" value='${entitatGestionar}'/>"/>
        <input type="hidden" name="subentitatGestionar" id="subentitatGestionar" value="<c:out escapeXml="true" value='${subentitatGestionar}'/>"/>
        <table>
          <tr>
            <th><fmt:message key='codigo_entidad_castella'/></th>
            <th><fmt:message key='codi_entitat_catala'/></th>
            <th><fmt:message key='subcodi_entitat'/></th>
            <th><fmt:message key='descripcion_entidad_castella'/></th>
            <th><fmt:message key='descripcio_entitat_catala'/></th>
            <!-- <th><fmt:message key='data_baixa_2'/></th> -->
          </tr>
          <tr>
            <td><input type="text" name="codEntidad" readonly="true" id="codEntidad" size="7" maxlength="7" value="<c:out escapeXml="true" value='${entitats.codigoEntidad}'/>" style="width: 70px;"/></td>
            <td><input type="text" name="codEntitat" id="codEntitat" size="7" maxlength="7" value="<c:out escapeXml="true" value='${entitats.codiEntitat}'/>" style="width: 70px;"/></td>
            <td><input type="text" name="subcodEntitat" id="subcodEntitat" size="3" maxlength="3" value="<c:out escapeXml="true" value='${entitats.subcodiEnt}'/>" style="width: 30px;"/></td>
            <td><input type="text" name="descEntidad" id="descEntidad" value="<c:out escapeXml="true" value='${entitats.descEntidad}'/>" size="30" maxlength="30" style="width: 200px;"/></td>
            <td>
              <input type="text" name="descEntitat" id="descEntitat" value="<c:out escapeXml="true" value='${entitats.descEntitat}'/>" size="30" maxlength="30" style="width: 200px;"/>
              <input type="hidden" name="dataBaixa" id="dataBaixa" value="<c:out escapeXml='true' value='${entitats.dataBaixa}'/>" />
            </td>
            <!-- <td><input type="text" name="dataBaixa" id="dataBaixa" value="<c:out escapeXml="true" value='${entitats.dataBaixa}'/>" size="12" maxlength="10" style="width: 60px;"/></td> -->
          </tr>
          <tr>
            <td align="center" colspan="6"><input type="submit" value="<fmt:message key='desa'/>" /></td>
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