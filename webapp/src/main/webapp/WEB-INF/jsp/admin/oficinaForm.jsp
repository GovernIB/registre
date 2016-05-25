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
                    <li><a href="<c:url value="/admin/oficina/list"/>"><i class="fa fa-list-ul"></i> <spring:message
                            code="oficina.oficinas"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <strong>
                            <spring:message code="oficina.editar"/>
                        </strong>
                    </li>
                </ol>
            </div>
        </div>
        <!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">
                <form:form modelAttribute="oficina" method="post" cssClass="form-horizontal">

                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong><spring:message code="oficina.editar"/>:</strong> ${oficina.denominacion}
                            </h3>
                        </div>

                        <!-- Formulario -->
                        <div class="panel-body">

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="organismoResponsable.id">
                                        <spring:message code="oficina.organismoResponsable"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="organismoResponsable.id" items="${organismos}" itemValue="id"
                                                 itemLabel="denominacion" cssClass="chosen-select"/>
                                    <form:errors path="organismoResponsable.id" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="oficinaResponsable">
                                        <spring:message code="oficina.oficinaResponsable"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="oficinaResponsable.id" cssClass="chosen-select">
                                        <form:option value="">...</form:option>
                                        <c:forEach var="oficina" items="${oficinas}">
                                            <form:option value="${oficina.id}">${oficina.denominacion}</form:option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="oficinaResponsable.id" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                        </div>

                    </div>

                    <!-- Botonera -->
                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick=""
                           class="btn btn-warning btn-sm"/>
                    <input type="button" value="<spring:message code="regweb.cancelar"/>"
                           onclick="goTo('<c:url value="/admin/oficina/list"/>')" class="btn btn-sm">

                </form:form>
            </div>
        </div>

    </div>
</div>
<!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>