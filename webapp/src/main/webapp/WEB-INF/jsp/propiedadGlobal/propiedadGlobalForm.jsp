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
                    <li><a href="<c:url value="/propiedadGlobal/list"/>"><i class="fa fa-list"></i> <spring:message
                            code="propiedadGlobal.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${not empty propiedadGlobal.id}"><spring:message
                                code="propiedadGlobal.editar"/> ${propiedadGlobal.clave}</c:if>
                        <c:if test="${empty propiedadGlobal.id}"><spring:message code="propiedadGlobal.nuevo"/></c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <form:form modelAttribute="propiedadGlobal" method="post" cssClass="form-horizontal">
            <div class="row">
                <div class="col-xs-12">

                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong>
                                    <c:if test="${not empty propiedadGlobal.id}"><spring:message
                                            code="propiedadGlobal.editar"/></c:if>
                                    <c:if test="${empty propiedadGlobal.id}"><spring:message
                                            code="propiedadGlobal.nuevo"/></c:if>
                                </strong>
                            </h3>
                        </div>

                        <!-- Formulario -->

                        <div class="panel-body">

                            <div class="form-group col-xs-12">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="tipo"><spring:message code="propiedadGlobal.tipo"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="tipo" cssClass="chosen-select">
                                        <c:forEach items="${tipos}" var="tipo">
                                            <form:option value="${tipo}"><spring:message code="propiedadGlobal.tipo.${tipo}" /></form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="clave"><span class="text-danger">*</span> <spring:message
                                            code="propiedadGlobal.clave"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="clave" cssClass="form-control"/> <form:errors path="clave"
                                                                                                    cssClass="help-block"
                                                                                                    element="span"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="valor"><spring:message code="propiedadGlobal.valor"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="valor" cssClass="form-control"/> <form:errors path="valor"
                                                                                                    cssClass="help-block"
                                                                                                    element="span"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="descripcion"><spring:message code="propiedadGlobal.descripcion"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:textarea path="descripcion" rows="3" cssClass="form-control"/> <form:errors
                                        path="descripcion" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                        </div>

                    </div>

                    <!-- Botonera -->
                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick=""
                           class="btn btn-warning btn-sm"/>
                    <input type="button" value="<spring:message code="regweb.cancelar"/>"
                           onclick="goTo('<c:url value="/propiedadGlobal/list"/>')" class="btn btn-sm">

                </div>

            </div>
        </form:form>

    </div>
</div> <!-- /container -->


<c:import url="../modulos/pie.jsp"/>

</body>
</html>