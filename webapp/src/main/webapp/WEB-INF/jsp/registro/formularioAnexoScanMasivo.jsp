<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca" >
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
    
    <c:if test="${teScan}">
      ${headerScan}
    </c:if>
</head>

<body style="background-image:none !important;">
<c:if test="${not empty closeAndReload}">

<script type="text/javascript" >

parent.closeAndReload();

</script>


</c:if>

<c:if test="${empty closeAndReload}">

    <c:import url="../modulos/mensajes.jsp"/>

    <div class="form-group col-xs-12">
        <p><spring:message code="anexo.escaneomasivo.bienvenida"/></p>

        <ul>
            <li><spring:message code="anexo.escaneomasivo.separador.obligatorio"/></li>
            <li><spring:message code="anexo.escaneomasivo.numeroseparadores"/></li>
            <li><spring:message code="anexo.escaneomasivo.numeromaximo.ficheros"/></li>
            <ul>
                <li><spring:message code="anexo.escaneomasivo.nummaxenviossir"/></li>
                <li><spring:message code="anexo.escaneomasivo.sinlimite"/></li>
            </ul>
        </ul>
        <p><spring:message code="anexo.escaneomasivo.ejemplo"/></p>
        <div class="center">
            <img src="<c:url value="/img/separador.png"/>" width="200" height="160"/>
        </div>
    </div>
    <div class="form-group col-xs-12 center">
        <div class="btn-group"><button type="button" class="btn btn-success btn-sm" onclick="goTo('<c:url value="/anexo/descargarSeparador/"/>')"><span class="fa fa-download"></span> <spring:message code="anexo.descargar.separador"/></button></div>
        <div class="btn-group"><button type="submit" data-toggle="modal" data-target="#modalAnexos" class="btn btn-warning btn-sm" onclick="parent.nuevoAnexoScan()"><spring:message code="anexo.scan.masivo.iniciar"/></button></div>
    </div>


</c:if>

<script type="text/javascript" src="<c:url value="/js/regweb.js"/>"></script>


</body>

</html>
