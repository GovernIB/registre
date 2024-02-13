<%@page import="es.caib.regweb3.utils.RegwebConstantes"%>
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
                    <li><a href="<c:url value="/usuarioEntidad/list"/>" ><i class="fa fa-list-ul"></i> <spring:message code="usuario.usuarios"/></a></li>
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
                <form:form modelAttribute="usuarioEntidad" method="post" cssClass="form-horizontal">
                    <form:hidden path="usuario.id"/>
                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                <strong><spring:message code="usuario.editar"/> ${usuarioEntidad.usuario.nombreCompleto} (${usuarioEntidad.usuario.identificador})</strong>
                            </h3>
                        </div>

                        <!-- Formulario -->
                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuario.nombre"><spring:message code="usuario.nombre"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuario.nombre" cssClass="form-control"/> <form:errors path="usuario.nombre" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuario.apellido1"><spring:message code="usuario.apellido1"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuario.apellido1" cssClass="form-control"/> <form:errors path="usuario.apellido1" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuario.apellido2"><spring:message code="usuario.apellido2"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuario.apellido2" cssClass="form-control"/> <form:errors path="usuario.apellido2" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuario.documento"><spring:message code="usuario.documento"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuario.documento" cssClass="form-control mayusculas"/> <form:errors path="usuario.documento" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="usuario.email"><spring:message code="usuario.email"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="usuario.email" cssClass="form-control"/> <form:errors path="usuario.email" cssClass="help-block" element="span"/>
                                    </div>
                                </div>

                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <form:label path="telefono"><spring:message code="usuario.telefono"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="telefono" cssClass="form-control"/> <form:errors path="telefono" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>

                            <%--Solo usuario Tipo Persona--%>
                            <c:if test="${usuarioEntidad.usuario.tipoUsuario == 1 && loginInfo.rolActivo.nombre != 'RWE_USUARI'}">
                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="codigoTrabajo"><spring:message code="usuario.codigoTrabajo"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="codigoTrabajo" cssClass="form-control"/> <form:errors path="codigoTrabajo" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="nombreTrabajo"><spring:message code="usuario.nombreTrabajo"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="nombreTrabajo" cssClass="form-control"/> <form:errors path="nombreTrabajo" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="categoria"><spring:message code="usuario.categoria"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="categoria" cssClass="chosen-select">
                                                <c:forEach var="categoria" items="${categorias}">
                                                    <form:option value="${categoria}"><spring:message code="usuario.categoria.${categoria}"/></form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>

                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="funcion"><spring:message code="usuario.funcion"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="funcion" cssClass="chosen-select">
                                               <c:forEach var="funcion" items="${funciones}">
                                                    <form:option value="${funcion}"><spring:message code="usuario.funcion.${funcion}"/></form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="clave"><spring:message code="usuario.clave"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="clave"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="bitcita"><spring:message code="usuario.bitcita"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="bitcita"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="asistencia"><spring:message code="usuario.asistencia"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="asistencia"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="apodera"><spring:message code="usuario.apodera"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="apodera"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="notificacionEspontanea"><spring:message code="usuario.notificacionEspontanea"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:checkbox path="notificacionEspontanea"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="cai"><spring:message code="usuario.cai"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="cai" cssClass="form-control"/> <form:errors path="cai" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <form:label path="observaciones"><spring:message code="usuario.observaciones"/></form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:textarea path="observaciones" rows="3" cssClass="form-control"/>
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
        var tipoUsuario = ${usuarioEntidad.usuario.tipoUsuario};

        if (tipoUsuario == <%=RegwebConstantes.TIPO_USUARIO_PERSONA%>) { //Persona
            $('#usuario\\.documento').removeAttr("disabled", "disabled");
            $('#usuario\\.apellido1').removeAttr("disabled", "disabled");
            $('#usuario\\.apellido2').removeAttr("disabled", "disabled");
        }

        if (tipoUsuario == <%=RegwebConstantes.TIPO_USUARIO_APLICACION%>) { //Aplicación
            $('#usuario\\.documento').val("");
            $('#usuario\\.documento').attr("disabled", "disabled");
            $('#usuario\\.apellido1').val("");
            $('#usuario\\.apellido1').attr("disabled", "disabled");
            $('#usuario\\.apellido2').val("");
            $('#usuario\\.apellido2').attr("disabled", "disabled");
        }
    }

</script>

</body>
</html>