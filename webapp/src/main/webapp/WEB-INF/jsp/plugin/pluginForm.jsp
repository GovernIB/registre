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
                    <li><a href="<c:url value="/plugin/list"/>"><i class="fa fa-list"></i> <spring:message code="plugin.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${not empty plugin.id}"><spring:message code="plugin.editar"/> ${plugin.nombre}</c:if>
                        <c:if test="${empty plugin.id}"><spring:message code="plugin.nuevo"/></c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <form:form modelAttribute="plugin" method="post" cssClass="form-horizontal">
            <div class="row">
                <div class="col-xs-12">

                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong>
                                    <c:if test="${not empty plugin.id}"><spring:message code="plugin.editar"/></c:if>
                                    <c:if test="${empty plugin.id}"><spring:message code="plugin.nuevo"/></c:if>
                                </strong>
                            </h3>
                        </div>

                        <!-- Formulario -->

                        <div class="panel-body">

                            <c:if test="${empty plugin.id}">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="tipo"><spring:message code="plugin.tipo"/></form:label>
                                    </div>
                                    <div class="col-xs-10">
                                        <form:select path="tipo" cssClass="chosen-select">
                                            <c:forEach items="${tiposDisponibles}" var="tipo">
                                                <form:option value="${tipo}"><spring:message code="plugin.tipo.${tipo}" /></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${not empty plugin.id}">
                                <form:hidden path="tipo" value="${plugin.tipo}"/>
                            </c:if>

                            <div class="form-group col-xs-12">
                                <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="nombre"><span class="text-danger">*</span> <spring:message code="plugin.nombre"/></form:label>
                                </div>
                                <div class="col-xs-10">
                                    <form:input path="nombre" cssClass="form-control"/> <form:errors path="nombre"
                                                                                                    cssClass="help-block"
                                                                                                    element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="descripcion"><span class="text-danger">*</span> <spring:message code="plugin.descripcion"/></form:label>
                                </div>
                                <div class="col-xs-10">
                                    <form:textarea path="descripcion" rows="3" cssClass="form-control"/> <form:errors path="descripcion" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="clase"><span class="text-danger">*</span> <spring:message code="plugin.clase"/></form:label>
                                </div>
                                <div class="col-xs-10">
                                    <form:input path="clase" cssClass="form-control"/> <form:errors path="clase"
                                                                                                    cssClass="help-block"
                                                                                                    element="span"/>
                                </div>
                            </div>
                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="propiedadesAdmin"><span class="text-danger">*</span> <spring:message code="plugin.propiedadesAdmin"/></form:label>
                                    </div>
                                    <div class="col-xs-10">
                                        <form:textarea path="propiedadesAdmin" rows="10" cssClass="form-control"/> <form:errors path="descripcion" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="propiedadesEntidad"><span class="text-danger">*</span> <spring:message code="plugin.propiedadesEntidad"/></form:label>
                                    </div>
                                    <div class="col-xs-10">
                                        <form:textarea path="propiedadesEntidad" rows="10" cssClass="form-control"/> <form:errors path="descripcion" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </c:if>


                        </div>

                    </div>

                    <!-- Botonera -->
                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                    <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/plugin/list"/>')" class="btn btn-sm">

                </div>

            </div>
        </form:form>

    </div>
</div> <!-- /container -->


<c:import url="../modulos/pie.jsp"/>

</body>
</html>