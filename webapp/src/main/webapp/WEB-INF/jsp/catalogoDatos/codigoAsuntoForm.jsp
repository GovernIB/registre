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
                        <li><a href="<c:url value="/codigoAsunto/list"/>" ><i class="fa fa-institution"></i> <spring:message code="codigoAsunto.codigoAsunto"/></a></li>
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="codigoAsunto.editar"/> ${codigoAsunto.traduccion.nombre}</li>
                    </ol>
                </div>
           </div><!-- Fin miga de pan -->

            <div class="row">
                <div class="col-xs-12">
                    <form:form modelAttribute="codigoAsunto" method="post" cssClass="form-horizontal">
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                           <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                              <strong>
                                  <c:if test="${not empty codigoAsunto.id}"><spring:message code="codigoAsunto.editar"/></c:if>
                                  <c:if test="${empty codigoAsunto.id}"><spring:message code="codigoAsunto.nuevo"/></c:if>
                              </strong>
                           </h3>
                        </div>

                        <!-- Formulario -->
                        <div class="panel-body">
                            
                            <%--<form:hidden path="entidad.id"/>--%>
                            <form:errors path="traducciones['es'].nombre" cssClass="has-error help-block" element="span"><span class="help-block-red"><spring:message code="regweb.traduccion.obligatorio"/></span></form:errors>

                            <div class="form-group col-xs-12 senseMargeLat">
                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="codigo"><span class="text-danger">*</span> <spring:message code="codigoAsunto.codigo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="codigo" cssClass="form-control"/> <form:errors path="codigo" cssClass="help-block" element="span"/>
                                    </div>
                                </div>

                                <div class="form-group col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb_left control-label">
                                     <form:label path="activo"><spring:message code="regweb.activo"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                     <form:checkbox path="activo"/>
                                    </div>
                                </div>
                            </div>


                            <ul class="nav nav-tabs" id="myTab">
                                <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                    <li><a href="#${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" data-toggle="tab"><spring:message code="idioma.${idioma}"/></a></li>
                                </c:forEach>
                            </ul>
                            <div id='content' class="tab-content">
                                <c:forEach items="${idiomas}" var="idioma" varStatus="index">
                                    <c:set var="idioma_lang" value="${RegwebConstantes.CODIGO_BY_IDIOMA_ID[idioma]}" />
                                    <div class="tab-pane" id="${idioma_lang}">

                                        <div class="form-group col-xs-12">
                                            <div class="form-group col-xs-6 senseMargeLat">
                                                <div class="col-xs-4 pull-lef etiqueta_regweb control-label textEsq">
                                                    <form:label path="traducciones['${idioma_lang}'].nombre"><span class="text-danger">*</span>
                                                     <spring:message code="regweb.nombre"/></form:label>
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
                    <!-- Botonera -->

                        <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>

                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/codigoAsunto/list"/>')" class="btn btn-sm">
                        <c:if test="${not empty codigoAsunto.id}">
                            <input type="button" value="<spring:message code="regweb.eliminar"/>" onclick='javascript:confirm("<c:url value="/codigoAsunto/${codigoAsunto.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' class="btn btn-danger btn-sm">
                        </c:if>

                    </form:form>
                </div>
            </div>



    </div>
</div>

<c:import url="../modulos/pie.jsp"/>


</body>
</html>