<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb.utils.RegwebConstantes"/>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.registro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.registro == 'salida'}">
    <c:set var="color" value="danger"/>
</c:if>

<c:if test="${param.tipo == 'nuevo'}">
    <div class="col-xs-12">
</c:if>
<c:if test="${param.tipo == 'detalle'}">
    <div class="col-xs-8 col-xs-offset pull-right">
</c:if>

    <div class="panel panel-${color}">

        <div class="panel-heading">
            <h3 class="panel-title">
                <i class="fa fa-pencil-square-o"></i>
                <strong>
                    <c:if test="${param.registro == 'entrada'}">
                        <spring:message code="interesado.interesados"/>
                    </c:if>
                    <c:if test="${param.registro == 'salida'}">
                        <spring:message code="registroSalida.destinatarios"/>
                    </c:if>

                </strong>
            </h3>
        </div>

        <div class="panel-body">
            <c:if test="${errorInteresado}">
                <div class="alert alert-danger alert-dismissable">
                    <strong><spring:message code="interesado.interesado"/>.</strong> <spring:message code="interesado.registro.obligatorio"/>
                </div>
            </c:if>

            <div class="form-group col-xs-12">
                <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <c:if test="${param.registro == 'entrada'}">
                            <label rel="ayuda" data-content="<spring:message code="registro.ayuda.tipoInteresado.entrada"/>" data-toggle="popover"><spring:message code="interesado.tipoInteresado"/></label>
                        </c:if>
                        <c:if test="${param.registro == 'salida'}">
                    <label rel="ayuda" data-content="<spring:message code="registro.ayuda.tipoInteresado.salida"/>" data-toggle="popover"><spring:message code="interesado.tipoDestinatario"/></label>
                        </c:if>
                </div>
                <div class="col-xs-10">
                    <c:forEach items="${tiposInteresado}" var="tipoInteresado">
                        <label class="radio-inline">
                            <input type="radio" name="tipoInteresado" value="${tipoInteresado}" <c:if test="${tipoInteresado == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">checked</c:if> > <spring:message code="interesado.tipo.${tipoInteresado}"/>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="form-group col-xs-12">
                <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                    <label id="organismoInteresadoLabel" for="organismoInteresado" rel="ayuda" data-content="<spring:message code="registro.ayuda.organoInteresado"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="interesado.organismo"/></label>
                    <label id="personaFisicaLabel" for="personaFisica" rel="ayuda" data-content="<spring:message code="registro.ayuda.personaFisica"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="persona.fisica"/></label>
                    <label id="personaJuridicaLabel" for="personaJuridica" rel="ayuda" data-content="<spring:message code="registro.ayuda.personaJuridica"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="persona.juridica"/></label>
                </div>
                <div class="col-xs-4">

                    <select id="organismoInteresado" name="organismoInteresado" class="chosen-select">
                        <c:forEach var="organismo" items="${organismosOficinaActiva}">
                            <option value="${organismo.codigo}">${organismo.denominacion}</option>
                        </c:forEach>
                    </select>

                    <select id="personaFisica" name="personaFisica" class="chosen-select">
                        <option value="-1">...</option>
                        <c:forEach items="${personasFisicas}" var="fisica">
                            <option value="${fisica.id}">${fisica.nombrePersonaFisica}</option>
                        </c:forEach>
                    </select>

                    <select id="personaJuridica" name="personaJuridica" class="chosen-select">
                        <option value="-1">...</option>
                        <c:forEach items="${personasJuridicas}" var="juridica">
                            <option value="${juridica.id}">${juridica.nombrePersonaJuridica}</option>
                        </c:forEach>
                    </select>

                </div>
                <%--Botones añadir interesado--%>
                <div class="col-xs-2 boto-panel">
                    <a id="addOrganismo" href="javascript:void(0);" onclick="addOrganismoInteresado('<spring:message code="interesado.administracion"/>','${registro.registroDetalle.id}')" class="btn btn-warning btn-sm"><spring:message code="regweb.añadir"/></a>
                    <a id="addPersonaFisica" href="javascript:void(0);" onclick="addInteresado($('#personaFisica option:selected').val(),$('#personaFisica option:selected').text(),'<spring:message code="persona.fisica"/>','No',null,'${registro.registroDetalle.id}')" class="btn btn-warning btn-sm"><spring:message code="regweb.añadir"/></a>
                    <a id="addPersonaJuridica" href="javascript:void(0);" onclick="addInteresado($('#personaJuridica option:selected').val(),$('#personaJuridica option:selected').text(),'<spring:message code="persona.juridica"/>','No',null,'${registro.registroDetalle.id}')" class="btn btn-warning btn-sm"><spring:message code="regweb.añadir"/></a>
                </div>
                <%--Botones búsqueda--%>
                <div class="col-xs-2 boto-panel">
                    <a id="buscarOrganismo" data-toggle="modal" href="#modalBuscadorOrganismoInteresado" onclick="inicializarBuscador('#codNivelAdministracionOrganismoInteresado','#codComunidadAutonomaOrganismoInteresado','${oficina.organismoResponsable.nivelAdministracion.codigoNivelAdministracion}', '${oficina.organismoResponsable.codAmbComunidad.codigoComunidad}', 'OrganismoInteresado' );" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                    <a id="buscarPersonaFisica" data-toggle="modal" href="#modalBuscadorPersonasFisicas" onclick="limpiarBusquedaPersona('Fisicas')" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                    <a id="buscarPersonaJuridica" data-toggle="modal" href="#modalBuscadorPersonasJuridicas" onclick="limpiarBusquedaPersona('Juridicas')" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                </div>
                <%--Botones nueva persona--%>
                <div class="col-xs-2 boto-panel">
                    <a id="nuevaPersonaFisica" data-toggle="modal" role="button" href="#modalInteresado" class="btn btn-warning btn-sm" onclick="nuevoInteresado('<spring:message code="persona.fisica.nueva"/>')"><spring:message code="regweb.nueva"/></a>
                    <a id="nuevaPersonaJuridica" data-toggle="modal" role="button" href="#modalInteresado" class="btn btn-warning btn-sm" onclick="nuevoInteresado('<spring:message code="persona.juridica.nueva"/>')"><spring:message code="regweb.nueva"/></a>
                </div>
            </div>

            <div class="col-xs-12">

                <div class="table-responsive">
                    <table id="interesados" class="table table-bordered table-hover table-striped">
                        <colgroup>
                            <col>
                            <col>
                            <col>
                            <col width="100">
                        </colgroup>
                        <thead>
                        <tr>
                            <th><spring:message code="registroEntrada.interesado"/></th>
                            <th><spring:message code="interesado.tipoInteresado"/></th>
                            <th><spring:message code="representante.representante"/></th>
                            <th class="center"><spring:message code="regweb.acciones"/></th>
                        </tr>
                        </thead>

                        <tbody>

                        </tbody>
                    </table>

                </div>
            </div>

        </div>
    </div>
</div>

<!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de Personas  -->
<c:import url="../registro/buscadorPersonas.jsp">
    <c:param name="tipoPersona" value="Fisicas"/>
</c:import>
<c:import url="../registro/buscadorPersonas.jsp">
    <c:param name="tipoPersona" value="Juridicas"/>
</c:import>
<c:import url="../registro/buscadorPersonas.jsp">
    <c:param name="tipoPersona" value="Todas"/>
</c:import>

<!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
<c:import url="../registro/buscadorOrganismosOficinasRE.jsp">
    <c:param name="tipo" value="OrganismoInteresado"/>
    <c:param name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>
</c:import>

<%--Nuevo Interesado--%>
<c:import url="../registro/formularioInteresado.jsp"/>

<script type="text/javascript">
    var urlObtenerPersona = '<c:url value="/persona/obtenerPersona"/>';
    var urlAddPersona = '<c:url value="/persona/addPersonaSesion"/>';
    var urlAddOrganismoInteresado = '<c:url value="/interesado/addOrganismo"/>';
    var urlEliminarOrganismoInteresado = '<c:url value="/interesado/eliminarOrganismo"/>';
    var urlAddPersonaInteresado = '<c:url value="/interesado/addPersona"/>';
    var urlEliminarPersonaInteresado = '<c:url value="/interesado/eliminarPersona"/>';
    var urlAddRepresentante = '<c:url value="/interesado/addRepresentante"/>';
    var urlEliminarRepresentante = '<c:url value="/interesado/eliminarRepresentante"/>';
    var urlObtenerInteresado = '<c:url value="/interesado/obtenerInteresado"/>';

</script>