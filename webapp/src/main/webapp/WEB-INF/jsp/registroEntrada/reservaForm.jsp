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
                        <li><a <c:if test="${oficinaActiva.sirEnvio || oficinaActiva.sirRecepcion}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${oficinaActiva.denominacion}</a></li>
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroEntrada.reserva.nuevo"/></li>
                    </ol>
                </div>
           </div><!-- Fin miga de pan -->

            <div class="row">
                <div class="col-xs-12">

                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                <strong><spring:message code="registroEntrada.reserva.nuevo"/></strong>
                            </h3>
                        </div>
                        <div class="panel-body">
                            <div class="col-xs-12"><strong>${entidad.nombre}</strong></div>
                            <div class="col-xs-12"><strong>${registro.oficina.denominacion}</strong></div>
                            <div class="form-group col-xs-12"><strong><c:set var="now" value="<%=new java.util.Date()%>" /> <fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/></strong></div>
                            <div class="col-xs-12"><strong>${usuario.nombreCompleto} (${usuario.email})</strong></div>
                        </div>
                    </div>

                </div>
            </div>


           <div class="row">
               <form:form modelAttribute="registro" method="post" cssClass="form-horizontal">
                   <div class="col-xs-6">

                       <div class="panel panel-info">

                           <div class="panel-heading">
                               <h3 class="panel-title"><i class="fa fa-pencil-square"></i> <strong><spring:message code="registro.datos.obligatorios"/></strong></h3>
                           </div>

                           <div class="panel-body">

                               <div class="form-group col-xs-12">
                                   <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                                       <form:label path="libro.id"><span class="text-danger">*</span> <spring:message code="registroEntrada.libro"/></form:label>
                                   </div>
                                   <div class="col-xs-10">
                                       <form:select path="libro.id" items="${libros}" itemValue="id" itemLabel="nombreCompleto" cssClass="chosen-select"/> <form:errors path="libro.id" cssClass="help-block" element="span"/>
                                   </div>
                               </div>

                               <div class="form-group col-xs-12">
                                   <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                                       <form:label path="registroDetalle.reserva"><span class="text-danger">*</span> <spring:message code="registroEntrada.reserva"/></form:label>
                                   </div>
                                   <div class="col-xs-10">
                                       <form:input path="registroDetalle.reserva" cssClass="form-control" maxlength="200"/> <form:errors path="registroDetalle.reserva" cssClass="help-block" element="span"/>
                                   </div>
                               </div>

                           </div>
                       </div>
                   </div>


                   <!-- Botonera -->
                   <div class="col-xs-12">
                       <input type="submit" value="<spring:message code="regweb.registrar"/>" class="btn btn-warning btn-sm"/>
                       <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/inici"/>')" class="btn btn-sm">
                   </div>

               </form:form>

           </div>


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>