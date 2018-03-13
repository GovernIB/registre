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
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <spring:message code="menu.configuracion"/>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->


        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong>
                                <spring:message code="regweb.apariencia"/>
                            </strong>
                        </h3>
                    </div>

                    <!-- Formulario -->
                    <div class="panel-body">
                        <form:form modelAttribute="configuracionForm" method="post" cssClass="form-horizontal" enctype="multipart/form-data">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <form:label path="configuracion.colorMenu"><spring:message code="configuracion.colorMenu"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="configuracion.colorMenu" cssClass="form-control pick-a-color"/> <form:errors path="configuracion.colorMenu" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                                <div class="col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <form:label path="configuracion.textoPie"><spring:message code="configuracion.textoPie"/></form:label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:textarea path="configuracion.textoPie" rows="5" cssClass="form-control"/> <form:errors path="configuracion.textoPie" cssClass="help-block" element="span"/>
                                    </div>
                                </div>
                            </div>

                            <!--  logo menu -->
                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <form:label path="logoMenu"><spring:message code="configuracion.logoMenu"/></form:label>
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
                                        <form:errors path="logoMenu" cssClass="help-block" element="span"/>
                                    </div>
                                </div>

                                <c:if test="${not empty configuracionForm.configuracion.logoMenu}">
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <form:label path="logoMenu"><spring:message code="configuracion.logoMenu.existente"/></form:label>
                                            <form:hidden path="configuracion.logoMenu.id"/>
                                        </div>
                                        <div class="col-xs-8 arxiu_actual">
                                            <a href="<c:url value="/archivo/${configuracionForm.configuracion.logoMenu.id}"/>" target="_blank">${configuracionForm.configuracion.logoMenu.nombre}</a>  <br>
                                            <form:checkbox path="borrarLogoMenu"></form:checkbox><spring:message code="regweb.eliminar"/>
                                        </div>
                                    </div>
                                </c:if>

                            </div>
                            <!-- Fi logo menu -->

                            <!--  logo pie -->
                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <form:label path="logoPie"><spring:message code="configuracion.logoPie"/></form:label>
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

                                <c:if test="${not empty configuracionForm.configuracion.logoPie}">
                                    <div class="form-group col-xs-6">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <form:label path="logoPie"><spring:message code="configuracion.logoPie.existente"/></form:label>
                                            <form:hidden path="configuracion.logoPie.id"/>
                                        </div>
                                        <div class="col-xs-8 arxiu_actual">
                                            <a href="<c:url value="/archivo/${configuracionForm.configuracion.logoPie.id}"/>" target="_blank">${configuracionForm.configuracion.logoPie.nombre}</a>  <br>
                                            <form:checkbox path="borrarLogoPie"></form:checkbox><spring:message code="regweb.eliminar"/>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            <!-- Fi logo pie -->

                    </div>

                </div>

                <!-- Botonera -->
                <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/inici"/>')" class="btn btn-sm">

              </form:form>

            </div>

        </div>

    </div>
</div> <!-- /container -->

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