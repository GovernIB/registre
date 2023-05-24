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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li><a href="<c:url value="/tipoDocumental/list"/>" ><i class="fa fa-globe"></i> <spring:message code="tipoDocumental.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${not empty tipoDocumental.id}"><spring:message code="tipoDocumental.editar"/></c:if>
                        <c:if test="${empty tipoDocumental.id}"><spring:message code="tipoDocumental.nuevo"/></c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">
                <form:form modelAttribute="tipoDocumental" method="post" cssClass="form-horizontal">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong>
                                <c:if test="${not empty tipoDocumental.id}"><spring:message code="tipoDocumental.editar"/></c:if>
                                <c:if test="${empty tipoDocumental.id}"><spring:message code="tipoDocumental.nuevo"/></c:if>
                            </strong>
                        </h3>
                    </div>

                    <!-- Formulario -->
                    <div class="panel-body">
                            <%--<form:hidden path="entidad.id"/>--%>
                            <form:errors path="traducciones['es'].nombre" cssClass="has-error help-block" element="span"><span class="help-block-red"><spring:message code="regweb.traduccion.obligatorio"/></span></form:errors>

                            <div class="form-group col-xs-12 senseMargeLat">
                                <div class="form-group col-xs-6">
                                   <div class="col-xs-4 pull-left etiqueta_regweb_left control-label textEsq">
                                       <form:label path="codigoNTI"><span class="text-danger">*</span> <spring:message code="tipoDocumental.codigoNTI"/></form:label>
                                   </div>
                                   <div class="col-xs-8">
                                       <form:input path="codigoNTI" cssClass="form-control"/> <form:errors path="codigoNTI" cssClass="help-block" element="span"/>
                                   </div>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <ul class="nav nav-tabs" id="myTab">
                                    <c:forEach var="idioma" items="${idiomas}" varStatus="index">
                                        <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                        <li><a href="#${idioma_lang}" data-toggle="tab"><spring:message code="idioma.${idioma}"/></a></li>
                                    </c:forEach>
                                </ul>

                                <div id='content' class="tab-content">
                                    <c:forEach var="idioma" items="${idiomas}" varStatus="index">
                                        <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                        <div class="tab-pane" id="${idioma_lang}">

                                            <div class="form-group col-xs-12">
                                                <div class="form-group col-xs-6 senseMargeLat">
                                                    <div class="col-xs-4 pull-lef etiqueta_regweb control-label textEsq">
                                                        <form:label path="traducciones['${idioma_lang}'].nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:input path="traducciones['${idioma_lang}'].nombre" cssClass="form-control"/>
                                                        <form:errors path="traducciones['${idioma_lang}'].nombre" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                     </div>
                </div>
                    <!-- Botonera -->
                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                    <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/tipoDocumental/list"/>')" class="btn btn-sm">
                    <c:if test="${not empty tipoDocumental.id}">
                        <input type="button" value="<spring:message code="regweb.eliminar"/>" onclick='javascript:confirm("<c:url value="/tipoDocumental/${tipoDocumental.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' class="btn btn-danger btn-sm"/>
                    </c:if>
                </form:form>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>