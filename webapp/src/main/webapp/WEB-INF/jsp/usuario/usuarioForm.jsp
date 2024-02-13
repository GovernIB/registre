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
                    <li><a href="<c:url value="/usuario/list"/>" ><i class="fa fa-list-ul"></i> <spring:message code="usuario.usuarios"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <strong>
                            <c:if test="${not empty usuario.id}"><spring:message code="usuario.editar"/></c:if>
                            <c:if test="${empty usuario.id}"><spring:message code="usuario.nuevo"/></c:if>
                        </strong>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">
                <form:form modelAttribute="usuario" method="post" cssClass="form-horizontal">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong><c:if test="${not empty usuario.id}"><spring:message code="usuario.editar"/></c:if>
                            <c:if test="${empty usuario.id}"><spring:message code="usuario.nuevo"/></c:if></strong>
                        </h3>
                    </div>

                    <!-- Formulario -->
                    <div class="panel-body">

                        <div class="col-xs-12">
                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="tipoUsuario"><span class="text-danger">*</span> <spring:message code="usuario.tipoUsuario"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="tipoUsuario" cssClass="chosen-select">
                                        <c:forEach var="tipoUsuario" items="${tiposUsuario}">
                                            <form:option value="${tipoUsuario}"><spring:message code="usuario.tipo.${tipoUsuario}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="tipoUsuario" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="idioma"><span class="text-danger">*</span> <spring:message code="usuario.idioma"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="idioma" cssClass="chosen-select">
                                       <c:forEach var="idioma_var" items="${idiomas}">
                                            <form:option value="${idioma_var}"><spring:message code="idioma.${idioma_var}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                    
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="nombre"><span class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="nombre" cssClass="form-control"/> <form:errors path="nombre" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="documento"><span class="text-danger">*</span> <spring:message code="usuario.documento"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="documento" cssClass="form-control mayusculas"/> <form:errors path="documento" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="apellido1"><span class="text-danger">*</span> <spring:message code="usuario.apellido1"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="apellido1" cssClass="form-control"/> <form:errors path="apellido1" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="apellido2"><spring:message code="usuario.apellido2"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="apellido2" cssClass="form-control"/> <form:errors path="apellido2" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                        </div>

                        <div class="col-xs-12">
                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="identificador"><span class="text-danger">*</span> <spring:message code="usuario.identificador"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="identificador" cssClass="form-control"/> <form:errors path="identificador" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <form:label path="email"><span class="text-danger">*</span> <spring:message code="usuario.email"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="email" cssClass="form-control"/> <form:errors path="email" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                        </div>

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
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
                    <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/usuario/list"/>')" class="btn btn-sm">
                    <c:if test="${not empty usuario.id}">
                        <input type="button" value="<spring:message code="regweb.eliminar"/>" onclick='javascript:confirm("<c:url value="/usuario/${usuario.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' class="btn btn-danger btn-sm"/>
                    </c:if>
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

        $('#tipoUsuario').change(
                function() {
                    tipoUsuario();
                });
    });

    function tipoUsuario() {
        var tipoUsuario = $('#tipoUsuario option:selected').val();

        if (tipoUsuario == <%=RegwebConstantes.TIPO_USUARIO_PERSONA%>) { //Persona
            $('#documento').removeAttr("disabled", "disabled");
            $('#apellido1').removeAttr("disabled", "disabled");
            $('#apellido2').removeAttr("disabled", "disabled");
        }

        if (tipoUsuario == <%=RegwebConstantes.TIPO_USUARIO_APLICACION%>) { //Aplicación
            $('#documento').val("");
            $('#documento').attr("disabled", "disabled");
            $('#apellido1').val("");
            $('#apellido1').attr("disabled", "disabled");
            $('#apellido2').val("");
            $('#apellido2').attr("disabled", "disabled");
        }
    }

</script>

</body>
</html>