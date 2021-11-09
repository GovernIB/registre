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
                    <%--<li><a href="javascript:void(0);" ><i class="fa fa-globe"></i> <spring:message code="entidad.entidad"/></a></li>--%>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${not empty entidadForm.entidad.id}"><spring:message code="entidad.editar"/> ${entidadForm.entidad.nombre}</c:if>
                        <c:if test="${empty entidadForm.entidad.id}"><spring:message code="entidad.nuevo"/></c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <form:form modelAttribute="entidadForm" method="post" cssClass="form-horizontal" enctype="multipart/form-data">
            <div class="row">
                <div class="col-xs-12">

                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong>
                                    <c:if test="${not empty entidadForm.entidad.id}"><spring:message code="entidad.editar"/></c:if>
                                    <c:if test="${empty entidadForm.entidad.id}"><spring:message code="entidad.nuevo"/></c:if>
                                </strong>
                            </h3>
                        </div>

                        <!-- Formulario -->

                        <div class="panel-body">

                            <form:errors path="entidad.sello" cssClass="has-error help-block" element="span"><span class="help-block-red"><spring:message code="entidad.sello.obligatorio"/></span></form:errors>
                            <form:errors path="entidad.posXsello" cssClass="has-error help-block" element="span"><span class="help-block-red"><spring:message code="entidad.sello.posicionX.obligatorio"/></span></form:errors>
                            <form:errors path="entidad.posYsello" cssClass="has-error help-block" element="span"><span class="help-block-red"><spring:message code="entidad.sello.posicionY.obligatorio"/></span></form:errors>
                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
                                <sec:authorize access="hasRole('RWE_SUPERADMIN')">

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="entidad.nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="entidad.nombre" cssClass="form-control"/> <form:errors path="entidad.nombre" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="entidad.descripcion"><span class="text-danger">*</span> <spring:message code="regweb.descripcion"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:textarea path="entidad.descripcion" rows="3" cssClass="form-control"/> <form:errors path="entidad.descripcion" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="entidad.codigoDir3"><span class="text-danger">*</span> <spring:message code="entidad.codigoDir3"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <c:if test="${tieneOrganismos}">
                                                <form:input path="entidad.codigoDir3" cssClass="form-control" readonly="true"/> <form:errors path="entidad.codigoDir3" cssClass="help-block" element="span"/>
                                            </c:if>
                                            <c:if test="${!tieneOrganismos}">
                                                <form:input path="entidad.codigoDir3" cssClass="form-control"/> <form:errors path="entidad.codigoDir3" cssClass="help-block" element="span"/>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="entidad.propietario.id"><span class="text-danger">*</span> <spring:message code="entidad.propietario"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="entidad.propietario.id" items="${propietarios}" itemValue="id" itemLabel="nombreCompleto" cssClass="chosen-select"/> <form:errors path="entidad.propietario.id" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>

                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="entidad.libro.nombre"><span class="text-danger">*</span> <spring:message code="libro.nombre"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="entidad.libro.nombre" cssClass="form-control"/> <form:errors path="entidad.libro.nombre" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="entidad.libro.codigo"><span class="text-danger">*</span> <spring:message code="libro.codigo.nombre"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="entidad.libro.codigo" cssClass="form-control" maxlength="4"/> <form:errors path="entidad.libro.codigo" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                    <c:if test="${not empty entidadForm.entidad.id}">
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label> <spring:message code="libro.contador.entrada"/></label>
                                            </div>
                                            <div class="col-xs-8">${entidadForm.entidad.libro.contadorEntrada.numero}</div>
                                        </div>
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label> <spring:message code="libro.contador.salida"/></label>
                                            </div>
                                            <div class="col-xs-8">${entidadForm.entidad.libro.contadorSalida.numero}</div>
                                        </div>
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label> <spring:message code="libro.contador.oficio"/></label>
                                            </div>
                                            <div class="col-xs-8">${entidadForm.entidad.libro.contadorOficioRemision.numero}</div>
                                        </div>
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <label> <spring:message code="libro.contador.sir"/></label>
                                            </div>
                                            <div class="col-xs-8">${entidadForm.entidad.libro.contadorSir.numero}</div>
                                        </div>
                                    </c:if>

                                </div>

                                <div class="col-xs-12">
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="entidad.mantenimiento"><spring:message code="entidad.mantenimiento"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:checkbox path="entidad.mantenimiento"/>
                                            </div>
                                        </div>
                                </div>

                                </sec:authorize>
                            </c:if>

                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                                <sec:authorize access="hasRole('RWE_ADMIN')">
                                    <ul class="nav nav-tabs" id="myTab">
                                        <li><a href="#Datos" data-toggle="tab"><spring:message code="entidad.datos"/></a></li>
                                        <li><a href="#Libro" data-toggle="tab"><spring:message code="libro.libro"/></a></li>
                                        <li><a href="#Formatos" data-toggle="tab"><spring:message code="entidad.formatos"/></a></li>
                                        <li><a href="#Configuracion" data-toggle="tab"><spring:message code="entidad.configuracion"/></a></li>
                                    </ul>
                                    <div id='content' class="tab-content">

                                        <div class="tab-pane" id="Datos">

                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:input path="entidad.nombre" cssClass="form-control"/> <form:errors path="entidad.nombre" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.descripcion"><span class="text-danger">*</span> <spring:message code="regweb.descripcion"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:textarea path="entidad.descripcion" rows="3" cssClass="form-control"/> <form:errors path="entidad.descripcion" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.codigoDir3"><span class="text-danger">*</span> <spring:message code="entidad.codigoDir3"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <c:if test="${tieneOrganismos}">
                                                            <form:input path="entidad.codigoDir3" cssClass="form-control" readonly="true"/> <form:errors path="entidad.codigoDir3" cssClass="help-block" element="span"/>
                                                        </c:if>
                                                        <c:if test="${!tieneOrganismos}">
                                                            <form:input path="entidad.codigoDir3" cssClass="form-control"/> <form:errors path="entidad.codigoDir3" cssClass="help-block" element="span"/>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.administradores"><spring:message code="entidad.administradores"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:select path="entidad.administradores" items="${administradoresEntidad}" itemValue="id" itemLabel="nombreCompleto" multiple="true" cssClass="chosen-select"/> <form:errors path="entidad.administradores" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.oficioRemision"><spring:message code="oficioRemision.oficioRemision"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:checkbox path="entidad.oficioRemision"/>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.sir"><spring:message code="regweb.sir"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:checkbox path="entidad.sir"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.mantenimiento"><spring:message code="entidad.mantenimiento"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:checkbox path="entidad.mantenimiento"/>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                        <!-- Fi datos -->

                                        <div class="tab-pane" id="Libro">
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.libro.nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:input path="entidad.libro.nombre" cssClass="form-control"/> <form:errors path="entidad.libro.nombre" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.libro.codigo"><span class="text-danger">*</span> <spring:message code="libro.codigo"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:input path="entidad.libro.codigo" cssClass="form-control" maxlength="4"/> <form:errors path="entidad.libro.codigo" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <label> <spring:message code="libro.contador.entrada"/></label>
                                                    </div>
                                                    <div class="col-xs-8">${entidadForm.entidad.libro.contadorEntrada.numero}</div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <label> <spring:message code="libro.contador.salida"/></label>
                                                    </div>
                                                    <div class="col-xs-8">${entidadForm.entidad.libro.contadorSalida.numero}</div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <label> <spring:message code="libro.contador.oficio"/></label>
                                                    </div>
                                                    <div class="col-xs-8">${entidadForm.entidad.libro.contadorOficioRemision.numero}</div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <label> <spring:message code="libro.contador.sir"/></label>
                                                    </div>
                                                    <div class="col-xs-8">${entidadForm.entidad.libro.contadorSir.numero}</div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.libro.activo"><spring:message code="regweb.activo"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:checkbox path="entidad.libro.activo"/> <form:errors path="entidad.libro.activo" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="tab-pane" id="Formatos">

                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-12 senseMargeLat">
                                                    <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.sello"><span class="text-danger">*</span> <spring:message code="entidad.sello"/></form:label>
                                                    </div>
                                                    <div class="col-xs-10">
                                                        <form:textarea path="entidad.sello" rows="5" cssClass="form-control"/> <form:errors path="entidad.sello" cssClass="help-block" element="span"/>
                                                        <a data-toggle="modal" href="#myModalSello" class="btn btn-warning btn-xs button-right"><spring:message code="regweb.ayuda"/></a>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-12 senseMargeLat">
                                                    <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                                        <label> <spring:message code="entidad.sello.posicion"/></label>
                                                    </div>
                                                    <div class="col-xs-5">
                                                        <div class="col-xs-1 pull-left etiqueta_regweb control-label textEsq">
                                                            <form:label path="entidad.posXsello"> <spring:message code="entidad.sello.posicionX"/></form:label>
                                                        </div>
                                                        <div class="col-xs-2">
                                                            <form:input path="entidad.posXsello" maxlength="3" cssClass="form-control"/> <form:errors path="entidad.posXsello" cssClass="help-block" element="span"/>
                                                        </div>
                                                        <div class="col-xs-1 pull-left etiqueta_regweb control-label textEsq">
                                                            <form:label path="entidad.posYsello"> <spring:message code="entidad.sello.posicionY"/></form:label>
                                                        </div>
                                                        <div class="col-xs-2">
                                                            <form:input path="entidad.posYsello" maxlength="3" cssClass="form-control"/> <form:errors path="entidad.posYsello" cssClass="help-block" element="span"/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- 
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-12 senseMargeLat">
                                                    <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.numRegistro"><span class="text-danger">*</span> <spring:message code="entidad.numRegistro"/></form:label>
                                                    </div>
                                                    <div class="col-xs-10">
                                                        <form:input path="entidad.numRegistro" cssClass="form-control"/> <form:errors path="entidad.numRegistro" cssClass="help-block" element="span"/>
                                                        <a data-toggle="modal" href="#myModalNumRegistro" class="btn btn-warning btn-xs button-right"><spring:message code="regweb.ayuda"/></a>
                                                    </div>
                                                </div>
                                            </div>
                                             -->
                                            <!--  logo sello -->
                                            <div class="col-xs-12">
                                                <div class="col-xs-12 senseMargeLat">
                                                    <div class="form-group col-xs-6">
                                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                            <form:label path="logoSello"><spring:message code="entidad.logoSello"/></form:label>
                                                        </div>
                                                        <div class="col-xs-8">
                                                            <div class="input-group">
                                                            <span class="input-group-btn">
                                                                <span class="btn btn-success btn-sm btn-file">
                                                                    Explorar&hellip; <input id="logoSello" name="logoSello" type="file" multiple>
                                                                </span>
                                                            </span>
                                                                <input type="text" class="form-control" readonly>
                                                            </div>
                                                            <form:errors path="logoSello" cssClass="help-block" element="span"/>
                                                        </div>
                                                    </div>
                                                    <c:if test="${not empty entidadForm.entidad.logoSello}">
                                                        <div class="form-group col-xs-6">
                                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                                <form:label path="logoSello"><spring:message
                                                                        code="entidad.logoSello.existente"/></form:label>
                                                                <form:hidden path="entidad.logoSello.id"/>
                                                            </div>
                                                            <div class="col-xs-8 arxiu_actual">
                                                                <a href="<c:url value="/archivo/${entidadForm.entidad.logoSello.id}"/>" target="_blank">${entidadForm.entidad.logoSello.nombre}</a>  <br>
                                                                <form:checkbox path="borrarLogoSello"></form:checkbox><spring:message code="regweb.eliminar"/>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>

                                        </div>
                                        <!-- Fi formatos -->

                                        <div class="tab-pane" id="Configuracion">

                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.colorMenu"><spring:message code="entidad.colorMenu"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:input path="entidad.colorMenu" cssClass="form-control pick-a-color"/> <form:errors path="entidad.colorMenu" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.configuracionPersona"><spring:message code="entidad.configuracionPersona"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:select path="entidad.configuracionPersona" cssClass="chosen-select">
                                                            <c:forEach items="${configuraciones}" var="configuracion">
                                                                <form:option value="${configuracion}"><spring:message code="configuracionPersona.${configuracion}" /></form:option>
                                                            </c:forEach>
                                                        </form:select>
                                                        <form:errors path="entidad.configuracionPersona" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.textoPie"><spring:message code="entidad.textoPie"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:textarea path="entidad.textoPie" rows="5" cssClass="form-control"/> <form:errors path="entidad.textoPie" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.diasVisado"><span class="text-danger">*</span> <spring:message code="entidad.diasVisado"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:select id="entidad.diasVisado" path="entidad.diasVisado" class="chosen-select">
                                                            <c:forEach begin="1" end="10" var="rangoDiasVisado">
                                                                <c:if test="${entidad.diasVisado == rangoDiasVisado}">
                                                                    <form:option value="${rangoDiasVisado}" selected="true">${rangoDiasVisado}</form:option>
                                                                </c:if>
                                                                <c:if test="${!(entidad.diasVisado == rangoDiasVisado)}">
                                                                    <form:option value="${rangoDiasVisado}">${rangoDiasVisado}</form:option>
                                                                </c:if>
                                                            </c:forEach>
                                                        </form:select>
                                                        <form:errors path="entidad.diasVisado" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <!--  logo menu -->
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="logoMenu"><spring:message code="entidad.logoMenu"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <div class="input-group">
                                                        <span class="input-group-btn">
                                                            <span class="btn btn-success btn-sm btn-file">
                                                                Explorar&hellip; <input id="logoMenu" name="logoMenu" type="file" multiple>
                                                            </span>
                                                        </span>
                                                            <input type="text" class="form-control" readonly>
                                                        </div>
                                                        <spring:message code="entidad.logoMenu.maxHeigh"/><br><spring:message code="entidad.logoMenu.maxWidth"/>
                                                        <form:errors path="logoMenu" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <c:if test="${not empty entidadForm.entidad.logoMenu}">
                                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                            <form:label path="logoMenu"><spring:message code="entidad.logoMenu.existente"/></form:label>
                                                            <form:hidden path="entidad.logoMenu.id"/>
                                                        </div>
                                                        <div class="col-xs-8 arxiu_actual">
                                                            <a href="<c:url value="/archivo/${entidadForm.entidad.logoMenu.id}"/>" target="_blank">${entidadForm.entidad.logoMenu.nombre}</a>  <br>
                                                            <form:checkbox path="borrarLogoMenu"></form:checkbox><spring:message code="regweb.eliminar"/>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                            <!-- Fi logo menu -->
                                            <!--  logo pie -->
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="logoPie"><spring:message code="entidad.logoPie"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <div class="input-group">
                                                        <span class="input-group-btn">
                                                            <span class="btn btn-success btn-sm btn-file">
                                                                Explorar&hellip; <input id="logoPie" name="logoPie" type="file" multiple>
                                                            </span>
                                                        </span>
                                                            <input type="text" class="form-control" readonly>
                                                        </div>
                                                        <form:errors path="logoPie" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <c:if test="${not empty entidadForm.entidad.logoPie}">
                                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                            <form:label path="logoPie"><spring:message code="entidad.logoPie.existente"/></form:label>
                                                            <form:hidden path="entidad.logoPie.id"/>
                                                        </div>
                                                        <div class="col-xs-8 arxiu_actual">
                                                            <a href="<c:url value="/archivo/${entidadForm.entidad.logoPie.id}"/>" target="_blank">${entidadForm.entidad.logoPie.nombre}</a>  <br>
                                                            <form:checkbox path="borrarLogoPie"></form:checkbox><spring:message code="regweb.eliminar"/>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                            <!-- Fi logo pie -->
                                            <div class="col-xs-12">
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                        <form:label path="entidad.perfilCustodia"><spring:message code="entidad.perfilCustodia"/></form:label>
                                                    </div>
                                                    <div class="col-xs-8">
                                                        <form:select path="entidad.perfilCustodia" cssClass="chosen-select">
                                                            <c:forEach items="${perfilesCustodia}" var="perfil">
                                                                <form:option value="${perfil}"><spring:message code="perfilCustodia.${perfil}" /></form:option>
                                                            </c:forEach>
                                                        </form:select>
                                                        <form:errors path="entidad.perfilCustodia" cssClass="help-block" element="span"/>
                                                    </div>
                                                </div>
                                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">

                                                </div>
                                            </div>
                                        </div>
                                        <!-- Fi Configuracion -->

                                    </div>
                                </sec:authorize>
                            </c:if>

                            <sec:authorize access="!hasRole('RWE_ADMIN')">
                                <c:if test="${not empty entidadForm.entidad.configuracionPersona}">
                                    <form:input path="entidad.configuracionPersona" type="hidden"/>
                                </c:if>
                            </sec:authorize>
                            <sec:authorize access="!hasRole('RWE_SUPERADMIN')">
                                <form:input path="entidad.propietario.id" type="hidden"/>
                            </sec:authorize>

                        </div>

                    </div>

                    <!-- Botonera -->

                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>

                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/entidad/list"/>')" class="btn btn-sm">
                    </c:if>
                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/inici"/>')" class="btn btn-sm">
                    </c:if>



                </div>

            </div>
        </form:form>

    </div>
</div> <!-- /container -->

<!-- ************* <spring:message code="regweb.inicio"/> Modal Oficina ************************** -->
<div class="modal fade" id="myModalSello">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title"><spring:message code="entidad.ayuda.sello"/></h4>
            </div>
            <div class="modal-body modal-maxh-600">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="panel panel-info">
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12 pull-left"><spring:message code="entidad.ayuda.contenido.sello"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" data-dismiss="modal" class="btn btn-warning  btn-sm"><spring:message code="regweb.cerrar"/></a>
            </div>
        </div>
    </div>
</div>
<!-- *************Fi Modal Segell************************** -->

<!-- ************* <spring:message code="regweb.inicio"/> Modal NumRegistre ************************** -->
<div class="modal fade" id="myModalNumRegistro">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title"><spring:message code="entidad.ayuda.numRegistro"/></h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="panel panel-info">
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12 pull-left"><spring:message code="entidad.ayuda.contenido.numRegistro"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" data-dismiss="modal" class="btn btn-warning  btn-sm"><spring:message code="regweb.cerrar"/></a>
            </div>
        </div>
    </div>
</div>
<!-- *************Fi Modal NumRegistre************************** -->

<c:import url="../modulos/pie.jsp"/>
<!-- ColorPicker -->
<script type="text/javascript" src="<c:url value="/js/colorpicker/tinycolor-0.9.14.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/colorpicker/pick-a-color-1.2.3.min.js"/>"></script>

<script>
    $(document).ready(
            function() {
                $(function () {
                    $(".pick-a-color").pickAColor({
                    });
                });
            }
    );

</script>


</body>
</html>