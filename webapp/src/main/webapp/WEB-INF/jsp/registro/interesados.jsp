<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == 'salida'}">
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
                    <c:if test="${param.tipoRegistro == 'entrada'}">
                        <spring:message code="interesado.interesados"/>
                    </c:if>
                    <c:if test="${param.tipoRegistro == 'salida'}">
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
                        <c:if test="${param.tipoRegistro == 'entrada'}">
                            <label rel="ayuda" data-content="<spring:message code="registro.ayuda.tipoInteresado.entrada"/>" data-toggle="popover"><spring:message code="interesado.tipoInteresado"/></label>
                        </c:if>
                        <c:if test="${param.tipoRegistro == 'salida'}">
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
                    <label id="personaFisicaLabel" for="personaFisica" style="display: none;" rel="ayuda" data-content="<spring:message code="registro.ayuda.personaFisica"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="persona.fisica"/></label>
                    <label id="personaJuridicaLabel" for="personaJuridica" style="display: none;" rel="ayuda" data-content="<spring:message code="registro.ayuda.personaJuridica"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="persona.juridica"/></label>
                </div>
                <div class="col-xs-6">

                   <select id="organismoInteresado" name="organismoInteresado" class="chosen-select" style="width:100%;">
                       <option value="-1">...</option>
                        <c:forEach items="${organismosOficinaActiva}" var="organismoInteresado">
                            <option value="${organismoInteresado.codigo}">${organismoInteresado.denominacion}</option>
                        </c:forEach>
                    </select>

                    <input id="personaFisica" name="personaFisica" type="text" class="form-control" style="display: none;" placeholder="<spring:message code="persona.buscar.fisicas"/>" autocomplete="off"/>

                    <%--<select id="personaFisica" name="personaFisica" class="chosen-select">
                        <option value="-1">...</option>
                        <c:forEach items="${personasFisicas}" var="fisica">
                            <option value="${fisica.id}">${fisica.nombrePersonaFisica}</option>
                        </c:forEach>
                    </select>--%>
                    <input id="personaJuridica" name="personaJuridica" type="text" class="form-control" style="display: none;" placeholder="<spring:message code="persona.buscar.juridicas"/>" autocomplete="off"/>

                    <%--<select id="personaJuridica" name="personaJuridica" class="chosen-select">
                        <option value="-1">...</option>
                        <c:forEach items="${personasJuridicas}" var="juridica">
                            <option value="${juridica.id}">${juridica.nombrePersonaJuridica}</option>
                        </c:forEach>
                    </select>--%>

                </div>
                <%--Botones búsqueda--%>
                <div class="col-xs-2 pull-right boto-panel">
                    <a id="buscarOrganismo" data-toggle="modal" href="#modalBuscadorOrganismoInteresado"
                       onclick="inicializarBuscador('#codNivelAdministracionOrganismoInteresado','#codComunidadAutonomaOrganismoInteresado','#provinciaOrganismoInteresado','#localidadOrganismoInteresado','${oficina.organismoResponsable.nivelAdministracion.codigoNivelAdministracion}', '${oficina.organismoResponsable.codAmbComunidad.codigoComunidad}', 'OrganismoInteresado' );"
                       class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                    <a id="buscarPersonaFisica" data-toggle="modal" href="#modalBuscadorPersonasFisicas" onclick="limpiarBusquedaPersona('Fisicas')" style="display: none;" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                    <a id="buscarPersonaJuridica" data-toggle="modal" href="#modalBuscadorPersonasJuridicas" onclick="limpiarBusquedaPersona('Juridicas')" style="display: none;" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                </div>
                <%--Botones nueva persona--%>
                <div class="col-xs-2 pull-right boto-panel">
                    <%--<a id="addOrganismo" href="javascript:void(0);" onclick="addOrganismoInteresado('<spring:message code="interesado.administracion"/>','${registro.registroDetalle.id}')" class="btn btn-warning btn-sm"><spring:message code="regweb.añadir"/></a>
                    <a id="addPersonaFisica" href="javascript:void(0);" onclick="addInteresado($('#personaFisica option:selected').val(),$('#personaFisica option:selected').text(),'<spring:message code="persona.fisica"/>','No',null,'${registro.registroDetalle.id}')" class="btn btn-warning btn-sm"><spring:message code="regweb.añadir"/></a>
                    <a id="addPersonaJuridica" href="javascript:void(0);" onclick="addInteresado($('#personaJuridica option:selected').val(),$('#personaJuridica option:selected').text(),'<spring:message code="persona.juridica"/>','No',null,'${registro.registroDetalle.id}')" class="btn btn-warning btn-sm"><spring:message code="regweb.añadir"/></a>--%>
                    <a id="nuevaPersonaFisica" data-toggle="modal" role="button" href="#modalInteresado" style="display: none;" class="btn btn-warning btn-sm" onclick="nuevoInteresado('<spring:message code="persona.fisica.nueva"/>')"><spring:message code="regweb.nueva"/></a>
                    <a id="nuevaPersonaJuridica" data-toggle="modal" role="button" href="#modalInteresado" style="display: none;" class="btn btn-warning btn-sm" onclick="nuevoInteresado('<spring:message code="persona.juridica.nueva"/>')"><spring:message code="regweb.nueva"/></a>
                </div>
            </div>

            <div class="row table-responsive">
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

<!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de Personas  -->
<c:import url="../registro/buscadorPersonas.jsp">
    <c:param name="tipoPersona" value="Fisicas"/>
    <c:param name="idRegistroDetalle" value="${param.idRegistroDetalle}"/>
</c:import>
<c:import url="../registro/buscadorPersonas.jsp">
    <c:param name="tipoPersona" value="Juridicas"/>
    <c:param name="idRegistroDetalle" value="${param.idRegistroDetalle}"/>
</c:import>
<c:import url="../registro/buscadorPersonas.jsp">
    <c:param name="tipoPersona" value="Todas"/>
    <c:param name="idRegistroDetalle" value="${param.idRegistroDetalle}"/>
</c:import>

<!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
<c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
    <c:param name="tipo" value="OrganismoInteresado"/>
    <c:param name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>
</c:import>

<%--Nuevo Interesado--%>
<c:import url="../registro/formularioInteresado.jsp">
    <c:param name="registro" value="${param.tipoRegistro}"/>
</c:import>

<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/interesados.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/representantes.js"/>"></script>
<script src="<c:url value="/js/bootstrap-typeahead.min.js"/>" type="text/javascript"></script>


<script type="text/javascript">
    var urlAddOrganismoInteresado = '<c:url value="/interesado/${param.tipoRegistro}/addOrganismo"/>';
    var urlEliminarOrganismoInteresado = '<c:url value="/interesado/${param.tipoRegistro}/eliminarOrganismo"/>';
    var urlAddPersonaInteresado = '<c:url value="/interesado/${param.tipoRegistro}/addPersona"/>';
    var urlEliminarPersonaInteresado = '<c:url value="/interesado/${param.tipoRegistro}/eliminarPersona"/>';
    var urlAddRepresentante = '<c:url value="/interesado/${param.tipoRegistro}/addRepresentante"/>';
    var urlEliminarRepresentante = '<c:url value="/interesado/${param.tipoRegistro}/eliminarRepresentante"/>';
    var urlObtenerInteresado = '<c:url value="/interesado/${param.tipoRegistro}/obtenerInteresado"/>';
    var urlBusquedaPersonasFisicas = '<c:url value="/rest/busquedaPersonas/2"/>';
    var urlBusquedaPersonasJuridicas = '<c:url value="/rest/busquedaPersonas/3"/>';

    <%-- Traducciones para interesados.js --%>
    var tradsinteresado = new Array();
    tradsinteresado['interesado.representante.anadir'] = "<spring:message code='interesado.representante.anadir' javaScriptEscape='true' />";
    tradsinteresado['interesado.representante.buscar'] = "<spring:message code='interesado.representante.buscar' javaScriptEscape='true' />";
    tradsinteresado['interesado.representante.editar'] = "<spring:message code='interesado.representante.editar' javaScriptEscape='true' />";
    tradsinteresado['interesado.representante.eliminar'] = "<spring:message code='interesado.representante.eliminar' javaScriptEscape='true' />";
    tradsinteresado['interesado.representante.nuevo'] = "<spring:message code='interesado.representante.nuevo' javaScriptEscape='true' />";
    tradsinteresado['interesado.personafisica.editar'] = "<spring:message code='interesado.personafisica.editar' javaScriptEscape='true' />";
    tradsinteresado['interesado.personajuridica.editar'] = "<spring:message code='interesado.personajuridica.editar' javaScriptEscape='true' />";
    tradsinteresado['interesado.noresultados.escoge'] = "<spring:message code='interesado.noresultados.escoge' javaScriptEscape='true' />";
    tradsinteresado['interesado.noresultados'] = "<spring:message code='interesado.noresultados' javaScriptEscape='true' />";
    tradsinteresado['interesado.hay'] = "<spring:message code='interesado.hay' javaScriptEscape='true' />";
    tradsinteresado['regweb3.editar'] = "<spring:message code='regweb.editar' javaScriptEscape='true' />";
    tradsinteresado['regweb3.nombre'] = "<spring:message code='regweb.nombre' javaScriptEscape='true' />";
    tradsinteresado['persona.documento'] = "<spring:message code='persona.documento' javaScriptEscape='true' />";
    tradsinteresado['persona.tipoPersona'] = "<spring:message code='persona.tipoPersona' javaScriptEscape='true' />";
    tradsinteresado['persona.fisica'] = "<spring:message code='persona.fisica' javaScriptEscape='true' />";
    tradsinteresado['persona.juridica'] = "<spring:message code='persona.juridica' javaScriptEscape='true' />";
    tradsinteresado['persona.razonSocial'] = "<spring:message code='persona.razonSocial' javaScriptEscape='true' />";
    tradsinteresado['persona.persona'] = "<spring:message code='persona.persona' javaScriptEscape='true' />";
    tradsinteresado['persona.razonSocial'] = "<spring:message code='persona.razonSocial' javaScriptEscape='true' />";
    tradsinteresado['interesado.resultados'] = "<spring:message code='interesado.resultados' javaScriptEscape='true' />";
    tradsinteresado['representante.eliminar'] = "<spring:message code='representante.eliminar' javaScriptEscape='true' />";
    tradsinteresado['regweb.confirmar'] = "<spring:message code='regweb.confirmar' javaScriptEscape='true' />";
    tradsinteresado['regweb.acciones'] = "<spring:message code='regweb.acciones' javaScriptEscape='true' />";
    tradsinteresado['usuario.apellido1'] = "<spring:message code='usuario.apellido1' javaScriptEscape='true' />";
    tradsinteresado['regweb3.confirmar'] = "<spring:message code='regweb.confirmar' javaScriptEscape='true' />";
    tradsinteresado['regweb3.anadir'] = "<spring:message code='regweb.añadir' javaScriptEscape='true' />";
    tradsinteresado['persona.fisica'] = "<spring:message code='persona.fisica' javaScriptEscape='true' />";
    tradsinteresado['persona.juridica'] = "<spring:message code='persona.juridica' javaScriptEscape='true' />";
    tradsinteresado['interesado.añadido'] = "<spring:message code='interesado.añadido' javaScriptEscape='true' />";
    tradsinteresado['interesado.actualizado'] = "<spring:message code='interesado.actualizado' javaScriptEscape='true' />";
    tradsinteresado['interesado.eliminado'] = "<spring:message code='interesado.eliminado' javaScriptEscape='true' />";
    tradsinteresado['interesado.eliminar.ultimo'] = "<spring:message code='interesado.eliminar.ultimo' javaScriptEscape='true' />";



<c:import url="../registro/addInteresadosBbdd.jsp"/>

    $(window).load(function() {

        //mostrarOrganismos();
        actualizarCanalNotificacion();

        // Muestra u Oculta en función del tipoInteresado seleccionado
        $('input[name=tipoInteresado]:radio').click(function () {
            var tipoInteresado = $('input[name=tipoInteresado]:radio:checked').val();

            if(tipoInteresado == 1){

                mostrarOrganismos();

            }else if(tipoInteresado == 2){

                mostrarPersonaFisica();

            }else if(tipoInteresado == 3){

                mostrarPersonaJuridica();
            }

        });

        // Gestión de los cambios del Canal de Notificación
        $('#canal').change(
                function() {actualizarCanalNotificacion();});

        // Gestión de los cambios de persona
        $('#pais\\.id').change(
                function() {actualizarPais();});

        //Gestión de los cambios de tipo documento
        $('#tipoDocumentoIdentificacion').change(
                function() {actualizarTipoDocumentoIdentificacion();});

    });


    $(document).ready(function () {
        $('#organismoInteresado').chosen().change(function () {
            addOrganismoInteresado('<spring:message code="interesado.administracion"/>','${registro.registroDetalle.id}');
        });
    });

    // Búsqueda de personas fisicas
    $('#personaFisica').typeahead({
        items:25,
        scrollBar:true,
        ajax: {
            url: urlBusquedaPersonasFisicas,
            data: 'query=' + $('#personaFisica').val(),
            dataType: 'json',
            timeout: 200,
            displayField: "nombre",
            valueField: "id",
            triggerLength: 3,
            method: "get",
            loadingClass: "loading-circle"
        },
        onSelect: function(item) {
            addInteresado(item.value,item.text,'<spring:message code="persona.fisica"/>','No',null,'${registro.registroDetalle.id}')
        }
    });

    // Búsqueda de personas juridicas
    $('#personaJuridica').typeahead({
        items:25,
        scrollBar:true,
        ajax: {
            url: urlBusquedaPersonasJuridicas,
            data: 'query=' + $('#personaJuridica').val(),
            dataType: 'json',
            timeout: 200,
            displayField: "nombre",
            valueField: "id",
            triggerLength: 3,
            method: "get",
            loadingClass: "loading-circle"
        },
        onSelect: function(item) {
            addInteresado(item.value,item.text,'<spring:message code="persona.juridica"/>','No',null,'${registro.registroDetalle.id}')
        }
    });
    // Posicionamos el ratón en el campo indicado al cargar el modal
    $('#modalBuscadorPersonasFisicas').on('shown.bs.modal', function () {
        $('#nombreFisicas').focus();
    });
    $('#modalBuscadorPersonasJuridicas').on('shown.bs.modal', function () {
        $('#razonSocialJuridicas').focus();
    });
    $('#modalBuscadorPersonasTodas').on('shown.bs.modal', function () {
        $('#nombreTodas').focus();
    });
    $('#modalBuscadorOrganismoInteresado').on('shown.bs.modal', function () {
        $('#codigoOrganismoOrganismoInteresado').focus();
    });

    // Posicionamos el ratón en el campo indicado al cargar el modal
    $('#modalInteresado').on('shown.bs.modal', function () {
        if($('#tipo').val() == 2){
            $('#nombre').focus();
        }else if($('#tipo').val() == 3){
            $('#razonSocial').focus();
        }
    });

</script>