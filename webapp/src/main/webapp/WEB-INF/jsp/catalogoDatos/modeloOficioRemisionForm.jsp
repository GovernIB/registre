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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${entidadActiva.nombre}</a></li>
                    <li><a href="<c:url value="/modeloOficioRemision/list"/>" ><i class="fa fa-list"></i> <spring:message code="modeloOficioRemision.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${not empty modeloOficioRemisionForm.modeloOficioRemision.id}"><spring:message code="modeloOficioRemision.editar"/></c:if>
                        <c:if test="${empty modeloOficioRemisionForm.modeloOficioRemision.id}"><spring:message code="modeloOficioRemision.nuevo"/></c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong>
                                <c:if test="${not empty modeloOficioRemisionForm.modeloOficioRemision.id}"><spring:message code="modeloOficioRemision.editar"/></c:if>
                                <c:if test="${empty modeloOficioRemisionForm.modeloOficioRemision.id}"><spring:message code="modeloOficioRemision.nuevo"/></c:if>
                            </strong>
                        </h3>
                    </div>

                    <!-- Formulario -->
                    <div class="panel-body">
                        <form:form modelAttribute="modeloOficioRemisionForm" method="post" cssClass="form-horizontal" enctype="multipart/form-data">
                            <form:hidden path="modeloOficioRemision.id"/>
                            <div class="col-xs-12 no-pad-left">
                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                        <form:label path="modeloOficioRemision.nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="modeloOficioRemision.nombre" cssClass="form-control"/> <form:errors path="modeloOficioRemision.nombre" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12 no-pad-left">
                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                        <form:label path="modelo"><span class="text-danger">*</span> <spring:message code="modeloOficioRemision.archivo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <div class="input-group">
                                                    <span class="input-group-btn">
                                                        <span class="btn btn-warning btn-sm btn-file">
                                                            Explorar&hellip; <input id="modelo" name="modelo" type="file" multiple>
                                                        </span>
                                                    </span>
                                            <input type="text" class="form-control" readonly>
                                        </div>
                                        <form:errors path="modelo" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <c:if test="${not empty modeloOficioRemisionForm.modeloOficioRemision.modelo}">
                                    <div class="form-group col-xs-6">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                            <form:label path="modeloOficioRemision.modelo"><spring:message code="modeloOficioRemision.archivo.existente"/></form:label>
                                            <form:hidden path="modeloOficioRemision.modelo.id"/>
                                        </div>
                                        <div class="col-xs-8 arxiu_actual">
                                            <a href="<c:url value="/archivo/${modeloOficioRemisionForm.modeloOficioRemision.modelo.id}"/>" target="_blank">${modeloOficioRemisionForm.modeloOficioRemision.modelo.nombre}</a>  <br>
                                        </div>
                                    </div>
                                </c:if>
                            </div>

                    </div>

                </div>
                <!-- Botonera -->
                <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/modeloOficioRemision/list"/>')" class="btn btn-sm">
                <c:if test="${not empty modeloRecibo.id}">
                    <input type="button" value="<spring:message code="regweb.eliminar"/>" onclick='javascript:confirm("<c:url value="/modeloOficioRemision/${modeloOficioRemision.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' class="btn btn-danger btn-sm"/>
                </c:if>
                </form:form>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>