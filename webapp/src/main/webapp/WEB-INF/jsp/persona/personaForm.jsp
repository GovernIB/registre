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
                    <li><a href="<c:url value="/persona/list"/>"><i class="fa fa-list-ul"></i> <spring:message code="persona.personas"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i>
                        <c:if test="${not empty persona.id}"><spring:message code="persona.editar"/></c:if>
                        <c:if test="${empty persona.id}"><spring:message code="persona.nuevo"/></c:if>
                    </li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong><c:if test="${not empty persona.id}"><spring:message code="persona.editar"/></c:if>
                            <c:if test="${empty persona.id}"><spring:message code="persona.nuevo"/></c:if></strong>
                        </h3>
                    </div>

                    <!-- Formulario -->
                    <div class="panel-body">

                        <form:form modelAttribute="persona" method="post" cssClass="form-horizontal">
                            <form:hidden path="entidad.id"/>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="tipo"><span class="text-danger">*</span> <spring:message code="persona.tipoPersona"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                  <form:select path="tipo" cssClass="chosen-select">
                                    <c:forEach items="${tiposPersona}" var="tmp">
                                      <form:option value="${tmp}" > <spring:message code="persona.tipo.${tmp}"/></form:option>
                                    </c:forEach>
                                  </form:select>
                                  <form:errors path="tipo" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="nombre"><span id="nombreLabel" class="text-danger">*</span> <spring:message code="regweb.nombre"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="nombre" cssClass="form-control" disabled="true" maxlength="30"/> <form:errors path="nombre" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="apellido1"><span id="apellido1Label" class="text-danger">*</span> <spring:message code="persona.apellido1"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="apellido1" cssClass="form-control" disabled="true" maxlength="30"/> <form:errors path="apellido1" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="apellido2"><spring:message code="persona.apellido2"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="apellido2" cssClass="form-control" disabled="true" maxlength="30"/> <form:errors path="apellido2" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="tipoDocumentoIdentificacion"><spring:message code="persona.tipoDocumentoIdentificacion"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="tipoDocumentoIdentificacion" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="tipoDocumento" items="${tiposDocumento}">
                                            <form:option value="${tipoDocumento}"><spring:message code="tipoDocumentoIdentificacion.${tipoDocumento}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="tipoDocumentoIdentificacion" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="documento"><spring:message code="persona.documento"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="documento" cssClass="form-control" disabled="true" maxlength="17" cssStyle="text-transform:uppercase"/> <form:errors path="documento" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-lef etiqueta_regweb control-label">
                                    <form:label path="pais.id">Pais</form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="pais.id" class="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="pais" items="${paises}">
                                            <form:option value="${pais.id}" label="${pais.descripcionPais}" />
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="pais.id" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-lef etiqueta_regweb control-label">
                                    <form:label path="provincia.id">Provincia</form:label>
                                </div>
                                <div class="col-xs-8">

                                    <form:select path="provincia.id" class="chosen-select" onchange="actualizarLocalidad(this)">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="provincia" items="${provincias}">
                                            <form:option value="${provincia.id}" label="${provincia.descripcionProvincia}" />
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="provincia.id" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-lef etiqueta_regweb control-label">
                                    <form:label path="localidad.id">Localidad</form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="localidad.id" cssClass="chosen-select"/>
                                    <form:errors path="localidad.id" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="direccion"><spring:message code="persona.direccion"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:textarea path="direccion" cssClass="form-control" cols="3" maxlength="160"/> <form:errors path="direccion" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="cp"><spring:message code="persona.cp"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="cp" cssClass="form-control" maxlength="5"/> <form:errors path="cp" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="email"><spring:message code="persona.email"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="email" cssClass="form-control" maxlength="160"/> <form:errors path="email" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="telefono"><spring:message code="persona.telefono"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:input path="telefono" cssClass="form-control" maxlength="20"/> <form:errors path="telefono" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="razonSocial"><span id="razonSocialLabel" class="text-danger">*</span> <spring:message code="persona.razonSocial"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:textarea path="razonSocial" cssClass="form-control" cols="3" disabled="true" maxlength="80"/> <form:errors path="razonSocial" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="direccionElectronica"><spring:message code="persona.direccionElectronica"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:textarea path="direccionElectronica" cssClass="form-control" cols="3" maxlength="160"/> <form:errors path="direccionElectronica" cssClass="help-block" element="span"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                    <form:label path="canal"><spring:message code="persona.canal"/></form:label>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="canal" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="canal" items="${canales}">
                                            <form:option value="${canal}"><spring:message code="canalNotificacion.${canal}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="canal" cssClass="help-block" element="span"/>
                                </div>
                            </div>



                    </div>

                </div>

                <!-- Botonera -->
                <input type="submit" value="<spring:message code="regweb.guardar"/>" onclick="" class="btn btn-warning btn-sm"/>
                <input type="button" value="<spring:message code="regweb.cancelar"/>" onclick="goTo('<c:url value="/persona/list"/>')" class="btn btn-sm">
                <c:if test="${not empty persona.id}">
                    <input type="button" value="<spring:message code="regweb.eliminar"/>" onclick='javascript:confirm("<c:url value="/persona/${persona.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' class="btn btn-danger btn-sm"/>
                </c:if>


                 <!-- Fin Botonera -->
                </form:form>
            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript" src="<c:url value="/js/interesados.js"/>"></script>

<script type="text/javascript">


    $(document).ready(function() {

        actualizarLocalidad();
        actualizartipoPersona();



        $('#tipo').change(
            function() {
                actualizartipoPersona();
          });
    });


    function actualizarLocalidad(){
        <c:url var="obtenerLocalidades" value="/persona/obtenerLocalidades" />
        actualizarSelect('${obtenerLocalidades}','#localidad\\.id',$('#provincia\\.id option:selected').val(),'${persona.localidad.id}',false);
    }

    function actualizartipoPersona(){
        var tipoPersona = $('#tipo option:selected').val();

        if (tipoPersona == 2) { //Persona fisica
            $('#razonSocial').val('');
            $('#razonSocial').attr("disabled", "disabled");
            $('#razonSocialLabel').hide();
            $('#nombre').removeAttr("disabled", "disabled");
            $('#apellido1').removeAttr("disabled", "disabled");
            $('#apellido2').removeAttr("disabled", "disabled");
            $('#nombreLabel').show();
            $('#apellido1Label').show();


            // Habilita/Deshabilita los tipos correspondientes
            tiposDocumentoPersonaFisica();
        }

        if (tipoPersona == 3) { //Persona juridica
            $('#razonSocial').removeAttr("disabled", "disabled");
            $('#razonSocialLabel').show();
            $('#nombre').val('');
            $('#apellido1').val('');
            $('#apellido2').val('');
            $('#nombre').attr("disabled", "disabled");
            $('#apellido1').attr("disabled", "disabled");
            $('#apellido2').attr("disabled", "disabled");
            $('#nombreLabel').hide();
            $('#apellido1Label').hide();

            // Habilita/Deshabilita los tipos correspondientes
            tiposDocumentoPersonaJuridica();

        }
    }

</script>


</body>
</html>