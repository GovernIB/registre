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
                       <li><a href="<c:url value="/libro/${organismo.id}/libros"/>"><i
                               class="fa fa-globe"></i> ${organismo.denominacion}</a></li>
                       <li><i class="fa fa-list-ul"></i> <spring:message code="organismo.libros"/></li>
                   </ol>
               </div>
          </div><!-- Fin miga de pan -->
          <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <c:if test="${not empty libro.id}"><strong><spring:message code="libro.editar"/> ${libro.nombre}</strong></c:if>
                            <c:if test="${empty libro.id}"><strong><spring:message code="libro.nuevo"/></strong></c:if>
                        </h3>
                    </div>
                    <!-- Formulario -->
                    <div class="panel-body">
                        <form:form modelAttribute="libro" method="post" cssClass="form-horizontal">
                            <div class="form-group col-xs-6">
                               <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                  <form:label path="nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                               </div>
                               <div class="col-xs-8">
                                  <form:input path="nombre" cssClass="form-control"/> <form:errors path="nombre" cssClass="help-block" element="span"/>
                               </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="codigo"><span class="text-danger">*</span> <spring:message code="libro.codigo"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="codigo" cssClass="form-control" maxlength="4"/> <form:errors path="codigo" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="activo"><spring:message code="regweb.activo"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:checkbox path="activo"/> <form:errors path="activo" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                    </div>
                </div>

                <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/libro/${organismo.id}/libros"/>')" class="btn btn-sm">

                </form:form>

            </div>
          </div>

    </div>
</div>

<c:import url="../modulos/pie.jsp"/>




</body>
</html>