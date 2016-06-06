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
                    <li><a href="<c:url value="/admin/organismo/list"/>"><i class="fa fa-list-ul"></i> <spring:message
                            code="organismo.organismos"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <strong>
                            <spring:message code="organismo.editar"/>
                        </strong>
                    </li>
                </ol>
            </div>
        </div>
        <!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">
                <form:form modelAttribute="organismo" method="post" cssClass="form-horizontal">
                    <form:hidden path="id"/>
                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong><spring:message code="organismo.editar"/>:</strong> ${organismo.denominacion}
                            </h3>
                        </div>

                        <!-- Formulario -->
                        <div class="panel-body">

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="organismoSuperior.id">
                                        <spring:message code="organismo.organismoSuperior"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="organismoSuperior.id" items="${organismos}" itemValue="id"
                                                 itemLabel="denominacion" cssClass="chosen-select"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="organismoRaiz.id">
                                        <spring:message code="organismo.organismoRaiz"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="organismoRaiz.id" items="${organismos}" itemValue="id"
                                                 itemLabel="denominacion" cssClass="chosen-select"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="edp"><spring:message code="organismo.edp"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:checkbox path="edp"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="edpPrincipal.id">
                                        <spring:message code="organismo.edpPrincipal"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="edpPrincipal.id" cssClass="chosen-select">
                                        <form:option value="">...</form:option>
                                        <form:options items="${organismos}" itemValue="id" itemLabel="denominacion"/>
                                    </form:select>
                                </div>
                            </div>

                        </div>

                    </div>

                    <!-- Botonera -->
                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick=""
                           class="btn btn-warning btn-sm"/>
                    <input type="button" value="<spring:message code="regweb.cancelar"/>"
                           onclick="goTo('<c:url value="/admin/organismo/list"/>')" class="btn btn-sm">

                </form:form>
            </div>
        </div>

    </div>
</div>
<!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>