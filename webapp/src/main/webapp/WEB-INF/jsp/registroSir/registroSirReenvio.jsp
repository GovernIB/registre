<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li><a href="<c:url value="/registroSir/list"/>"><i class="fa fa-list"></i> <spring:message
                            code="registroSir.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message
                            code="registroSir.registroSir"/> ${registroSir.numeroRegistro}</li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong><spring:message code="regweb.reenviar"/> <spring:message
                                    code="registroSir.registroSir"/> ${registroSir.numeroRegistro}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <c:url value="/registroSir/reenviar/${registroSir.id}" var="urlReenviar" scope="request"/>
                        <form:form modelAttribute="reenviarForm" method="post" action="${urlReenviar}" cssClass="form-horizontal" >

                            <form:hidden path="codigoOficina" value=""/>
                            <form:hidden path="denominacionOficina" value=""/>
                            <form:hidden path="codigoOrganismoResponsable" value=""/>
                            <form:hidden path="denominacionOrganismoResponsable" value=""/>

                            <!-- Buscador Oficina Destino -->
                            <div class="col-xs-12">
                                <div class="form-group col-xs-8">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="idDatosOficinaReenvio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficina.reenvio"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroSir.oficinaReenvio"/></label>
                                    </div>
                                    <div class="col-xs-6" id="idDatosOficinaReenvio">
                                        <form:textarea path="datosOficinaReenvio" class="form-control" rows="3" disabled="true"/> <span class="errors"></span>

                                    </div>
                                    <div class="col-xs-3 boto-panel">
                                        <a data-toggle="modal" role="button" href="#modalBuscadorOficinaSir"
                                           onclick="inicializarBuscador('#codNivelAdministracionOficinaSir','#codComunidadAutonomaOficinaSir','#provinciaOficinaSir','#localidadOficinaSir',${RegwebConstantes.nivelAdminAutonomica}, ${RegwebConstantes.comunidadBaleares},'OficinaSir' );"
                                           class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                                    </div>
                                    <!-- Fin de gestión de organismo destino -->
                                </div>
                            </div> <!--./row-->

                            <!--Observaciones -->
                            <div class="col-xs-12">
                                <div class="form-group col-xs-8">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="observaciones" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.reenvio"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.observaciones"/></label>
                                    </div>
                                    <div class="col-xs-9" id="idObservaciones">
                                        <form:textarea path="observaciones" rows="3" class="form-control" maxlength="80"/>
                                        <span id="errorObsv" class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12">
                                        <input type="button" value="<spring:message code="regweb.reenviar"/>" class="btn btn-warning btn-sm" onclick="validarFormReenvio();"/>
                                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/registroSir/${registroSir.id}/detalle"/>')" class="btn btn-sm"/>
                                    </div>
                                </div>
                            </div>
                            <c:set var="errorObligatori"><spring:message code="error.valor.requerido"/></c:set>
                            <input id="error" type="hidden" value="${errorObligatori}"/>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="../modulos/pie.jsp"/>
<!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
<c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
    <c:param name="tipo" value="OficinaSir"/>
</c:import>
<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>


</body>
</html>




