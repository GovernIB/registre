<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/> - <spring:message code="usuario.editar"/></title>
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
                    <c:if test="${loginInfo.rolActivo.nombre != 'RWE_USUARI'}"><li><a href="<c:url value="/usuarioEntidad/list"/>" ><i class="fa fa-list-ul"></i> <spring:message code="usuario.usuarios"/></a></li></c:if>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <strong>
                            <spring:message code="usuario.editar"/>
                        </strong>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">
                <form:form modelAttribute="usuarioEntidadForm" method="post" cssClass="form-horizontal" enctype="multipart/form-data">
                    <form:hidden path="usuarioEntidad.usuario.id"/>
                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong><spring:message code="usuario.editar"/> ${usuarioEntidadForm.usuarioEntidad.usuario.nombreCompleto} (${usuarioEntidadForm.usuarioEntidad.usuario.identificador})</strong>
                            </h3>
                        </div>

                        <!-- Formulario -->
                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuarioEntidad.usuario.nombre"><spring:message code="usuario.nombre"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuarioEntidad.usuario.nombre" cssClass="form-control"/> <form:errors path="usuarioEntidad.usuario.nombre" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuarioEntidad.usuario.apellido1"><spring:message code="usuario.apellido1"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuarioEntidad.usuario.apellido1" cssClass="form-control"/> <form:errors path="usuarioEntidad.usuario.apellido1" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuarioEntidad.usuario.apellido2"><spring:message code="usuario.apellido2"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuarioEntidad.usuario.apellido2" cssClass="form-control"/> <form:errors path="usuarioEntidad.usuario.apellido2" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuarioEntidad.usuario.documento"><spring:message code="usuario.documento"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuarioEntidad.usuario.documento" cssClass="form-control mayusculas"/> <form:errors path="usuarioEntidad.usuario.documento" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuarioEntidad.usuario.email"><spring:message code="usuario.email"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuarioEntidad.usuario.email" cssClass="form-control"/> <form:errors path="usuarioEntidad.usuario.email" cssClass="help-block" element="span"/>
                                    </div>
                                </div>

                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuarioEntidad.telefono"><spring:message code="usuario.telefono"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuarioEntidad.telefono" cssClass="form-control"/> <form:errors path="usuarioEntidad.telefono" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>

                            <%--Solo usuario Tipo Persona--%>
                            <c:if test="${usuarioEntidadForm.usuarioEntidad.usuario.tipoUsuario == 1}">
                                <c:set var="rweUsuari" value="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}"/>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.codigoTrabajo"><spring:message code="usuario.codigoTrabajo"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="usuarioEntidad.codigoTrabajo" cssClass="form-control"/> <form:errors path="usuarioEntidad.codigoTrabajo" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.nombreTrabajo"><spring:message code="usuario.nombreTrabajo"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="usuarioEntidad.nombreTrabajo" cssClass="form-control"/> <form:errors path="usuarioEntidad.nombreTrabajo" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.categoria"><spring:message code="usuario.categoria"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="usuarioEntidad.categoria" cssClass="chosen-select" disabled="${rweUsuari}">
                                                <c:forEach var="categoria" items="${categorias}" >
                                                    <form:option value="${categoria}"><spring:message code="usuario.categoria.${categoria}"/></form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>

                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <c:if test="${loginInfo.rolActivo.nombre != 'RWE_USUARI'}">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="usuarioEntidad.funcion"><spring:message code="usuario.funcion"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:select path="usuarioEntidad.funcion" cssClass="chosen-select">
                                                   <c:forEach var="funcion" items="${funciones}">
                                                        <form:option value="${funcion}"><spring:message code="usuario.funcion.${funcion}" /></form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </c:if>
                                    </div>

                                </div>
                                <c:if test="${loginInfo.rolActivo.nombre != 'RWE_USUARI'}">
                                    <div class="col-xs-12">
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="usuarioEntidad.oficinaSolicitada.id"><spring:message code="usuario.oficinaSolicitada"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:select path="usuarioEntidad.oficinaSolicitada.id" cssClass="chosen-select">
                                                    <form:option value="" label="..."/>
                                                    <c:forEach var="oficina" items="${oficinasUsuario}" >
                                                        <form:option value="${oficina.id}">${oficina.denominacion}</form:option>
                                                    </c:forEach>
                                                </form:select>
                                            </div>
                                        </div>
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="usuarioEntidad.cai"><spring:message code="usuario.cai"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:input path="usuarioEntidad.cai" cssClass="form-control" disabled="${rweUsuari}"/>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.clave"><spring:message code="usuario.clave"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="usuarioEntidad.clave" disabled="${rweUsuari}"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.bitcita"><spring:message code="usuario.bitcita"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="usuarioEntidad.bitcita" disabled="${rweUsuari}"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.asistencia"><spring:message code="usuario.asistencia"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="usuarioEntidad.asistencia" disabled="${rweUsuari}"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.apodera"><spring:message code="usuario.apodera"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="usuarioEntidad.apodera" disabled="${rweUsuari}"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="usuarioEntidad.notificacionEspontanea"><spring:message code="usuario.notificacionEspontanea"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="usuarioEntidad.notificacionEspontanea" disabled="${rweUsuari}"/>
                                        </div>
                                    </div>
                                </div>
                                <c:if test="${loginInfo.rolActivo.nombre != 'RWE_USUARI'}">
                                    <div class="col-xs-12">
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="usuarioEntidad.externo"><spring:message code="usuario.externo"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:checkbox path="usuarioEntidad.externo" disabled="${rweUsuari}"/>
                                            </div>
                                        </div>
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="usuarioEntidad.observaciones"><spring:message code="usuario.observaciones"/></form:label>
                                            </div>
                                            <div class="col-xs-8">
                                                <form:textarea path="usuarioEntidad.observaciones" rows="3" cssClass="form-control"/>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <div class="col-xs-12">
                                    <c:if test="${not empty usuarioEntidadForm.usuarioEntidad.certificadoCurso}">
                                        <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                                <form:label path="borrarCertificado"><spring:message code="usuario.certificado.existente"/></form:label>
                                                <form:hidden path="usuarioEntidad.certificadoCurso.id"/>
                                            </div>
                                            <div class="col-xs-8 arxiu_actual">
                                                <a href="<c:url value="/archivo/${usuarioEntidadForm.usuarioEntidad.certificadoCurso.id}"/>" target="_blank">${usuarioEntidadForm.usuarioEntidad.certificadoCurso.nombre}</a>
                                                <c:if test="${usuarioEntidadForm.usuarioEntidad.certificadoCurso.mime == RegwebConstantes.MIME_PDF}">

                                                    <a data-toggle="modal" class="btn btn-info btn-default btn-xs"
                                                       href="#visorAnexo${usuarioEntidadForm.usuarioEntidad.certificadoCurso.id}"
                                                       title="<spring:message code="anexo.visualizar"/>"><span class="fa fa-search"></span></a>

                                                    <div id="visorAnexo${usuarioEntidadForm.usuarioEntidad.certificadoCurso.id}" class="modal fade" role="dialog">
                                                        <div class="modal-dialog modal-lg">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                                                                    <h3 class="modal-title"><spring:message code="anexo.visualizar"/>: ${usuarioEntidadForm.usuarioEntidad.certificadoCurso.nombre}</h3>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <object type="${usuarioEntidadForm.usuarioEntidad.certificadoCurso.mime}" data="<c:url value="/archivo/${usuarioEntidadForm.usuarioEntidad.certificadoCurso.id}/false"/>" width="100%" height="700"></object>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                                <span><fmt:formatDate value="${usuarioEntidadForm.usuarioEntidad.fechaCertificado}" pattern="dd/MM/yyyy"/></span>
                                                <br>
                                                <form:checkbox path="borrarCertificado"></form:checkbox><spring:message code="regweb.eliminar"/>
                                            </div>
                                        </div>
                                    </c:if>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="certificadoCurso"><spring:message code="usuario.certificado"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <div class="input-group">
                                                <span class="input-group-btn">
                                                    <span class="btn btn-success btn-sm btn-file">
                                                        Explorar&hellip; <input id="certificadoCurso" name="certificadoCurso" type="file" multiple>
                                                    </span>
                                                </span>
                                                <input type="text" class="form-control" readonly>
                                            </div>
                                            <form:errors path="certificadoCurso" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <!-- Botonera -->
                    <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/inici"/>')" class="btn btn-sm">
                    </c:if>
                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                        <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/usuarioEntidad/list"/>')" class="btn btn-sm">
                    </c:if>
                 <!-- Fin Botonera -->
                </form:form>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">

    $(document).ready(function() {

        tipoUsuario();
    });

    function tipoUsuario() {

        <c:if test="${usuarioEntidadForm.usuarioEntidad.usuario.tipoUsuario == RegwebConstantes.TIPO_USUARIO_PERSONA}">
            $('#usuario\\.documento').removeAttr("disabled", "disabled");
            $('#usuario\\.apellido1').removeAttr("disabled", "disabled");
            $('#usuario\\.apellido2').removeAttr("disabled", "disabled");
        </c:if>

        <c:if test="${usuarioEntidadForm.usuarioEntidad.usuario.tipoUsuario == RegwebConstantes.TIPO_USUARIO_APLICACION}">
            $('#usuario\\.documento').val("");
            $('#usuario\\.documento').attr("disabled", "disabled");
            $('#usuario\\.apellido1').val("");
            $('#usuario\\.apellido1').attr("disabled", "disabled");
            $('#usuario\\.apellido2').val("");
            $('#usuario\\.apellido2').attr("disabled", "disabled");
        </c:if>
    }

</script>

</body>
</html>