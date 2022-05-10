<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-12">

    <div class="panel panel-${color}">

        <div class="panel-heading">
            <h3 class="panel-title">
                <i class="fa fa-user"></i>
                <strong>
                    <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                        <spring:message code="interesado.interesados"/>
                    </c:if>
                    <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                        <spring:message code="registroSalida.destinatarios"/>
                    </c:if>

                </strong>
            </h3>
        </div>

        <div class="panel-body">
            <c:if test="${errorInteresado && param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                <div class="alert alert-danger alert-dismissable">
                    <strong><spring:message code="interesado.interesado"/>.</strong> <spring:message code="interesado.registro.obligatorio"/>
                </div>
            </c:if>

            <c:if test="${errorInteresado && param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                <div class="alert alert-danger alert-dismissable">
                    <strong><spring:message code="registroSalida.destinatario"/>.</strong> <spring:message code="destinatario.registro.obligatorio"/>
                </div>
            </c:if>

            <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                            <label rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoInteresado.entrada"/>" data-toggle="popover"><spring:message code="interesado.tipoInteresado"/></label>
                        </c:if>
                    </div>

                    <div class="col-xs-10">
                        <c:forEach items="${tiposInteresado}" var="tipoInteresado">
                            <label class="radio-inline">
                                <input type="radio" name="tipoInteresado" value="${tipoInteresado}" <c:if test="${tipoInteresado == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">checked</c:if> > <spring:message code="interesado.tipo.${tipoInteresado}"/>
                            </label>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <div class="form-group col-xs-12">
                <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                    <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                        <label id="personaFisicaLabel" for="personaFisica" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.personaFisica"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="persona.fisica"/></label>
                        <label id="personaJuridicaLabel" for="personaJuridica" style="display: none;" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.personaJuridica"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="persona.juridica"/></label>
                    </c:if>
                    <label id="organismoInteresadoLabel" for="organismoInteresado" style="display: none;" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.organoInteresado"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="interesado.organismo"/></label>

                </div>
                <div class="col-xs-6">
                    <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                        <input id="personaFisica" name="personaFisica" type="text" class="form-control" placeholder="<spring:message code="persona.buscar.fisicas"/>" autocomplete="off"/>
                        <input id="personaJuridica" name="personaJuridica" type="text" class="form-control" style="display: none;" placeholder="<spring:message code="persona.buscar.juridicas"/>" autocomplete="off"/>
                    </c:if>

                    <select id="organismoInteresado" name="organismoInteresado" class="chosen-select" style="display: none;">
                        <option value="-1">...</option>
                        <c:forEach items="${ultimosOrganismos}" var="organismoInteresado">
                            <option value="${organismoInteresado.codigo}">${organismoInteresado.denominacion}</option>
                        </c:forEach>
                    </select>
                </div>

                <%--Botones búsqueda--%>
                <div class="col-xs-2 pull-right boto-panel center">
                    <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                        <a id="buscarPersonaFisica" data-toggle="modal" href="#modalBuscadorPersonasFisicas" onclick="limpiarBusquedaPersona('Fisicas')" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                        <a id="buscarPersonaJuridica" data-toggle="modal" href="#modalBuscadorPersonasJuridicas" onclick="limpiarBusquedaPersona('Juridicas')" style="display: none;" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                    </c:if>

                    <a id="buscarOrganismo" data-toggle="modal" href="#modalBuscadorOrganismoInteresado"
                       onclick="inicializarBuscador('#codNivelAdministracionOrganismoInteresado','#codComunidadAutonomaOrganismoInteresado','#provinciaOrganismoInteresado','#localidadOrganismoInteresado',${RegwebConstantes.nivelAdminAutonomica}, ${RegwebConstantes.comunidadBaleares}, 'OrganismoInteresado' );"
                       style="display: none;" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                </div>
                <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                    <%--Botones nueva persona--%>
                    <div class="col-xs-2 pull-right boto-panel center">
                        <a id="nuevaPersonaFisica" data-toggle="modal" role="button" href="#modalInteresado" class="btn btn-warning btn-sm" onclick="nuevoInteresado('<spring:message code="persona.fisica.nueva"/>')"><spring:message code="regweb.nueva"/></a>
                        <a id="nuevaPersonaJuridica" data-toggle="modal" role="button" href="#modalInteresado" style="display: none;" class="btn btn-warning btn-sm" onclick="nuevoInteresado('<spring:message code="persona.juridica.nueva"/>')"><spring:message code="regweb.nueva"/></a>
                    </div>
                </c:if>

            </div>

            <table id="interesados" class="table table-bordered table-hover table-striped" style="display: none;">
                <colgroup>
                    <col>
                    <col>
                    <col>
                    <col width="100">
                </colgroup>
                <thead>
                <tr>
                    <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
                        <th><spring:message code="registroEntrada.interesado"/></th>
                    </c:if>
                    <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
                        <th><spring:message code="registroSalida.destinatario"/></th>
                    </c:if>

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
    <c:param name="idRegistroDetalle" value="${param.idRegistroDetalle}"/>
</c:import>

<%--Nuevo Interesado--%>
<c:import url="../registro/formularioInteresado.jsp">
    <c:param name="registro" value="${param.tipoRegistro}"/>
</c:import>

<script type="text/javascript">
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
    tradsinteresado['persona.codigodire'] = "<spring:message code='persona.codigoDire' javaScriptEscape='true' />";
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
    tradsinteresado['interesado.añadir.error'] = "<spring:message code='interesado.añadir.error' javaScriptEscape='true' />";
    tradsinteresado['interesado.actualizado'] = "<spring:message code='interesado.actualizado' javaScriptEscape='true' />";
    tradsinteresado['interesado.eliminado'] = "<spring:message code='interesado.eliminado' javaScriptEscape='true' />";
    tradsinteresado['interesado.eliminar.ultimo'] = "<spring:message code='interesado.eliminar.ultimo' javaScriptEscape='true' />";
    tradsinteresado['interesado.añadir.organismo'] = "<spring:message code='interesado.añadir.organismo' javaScriptEscape='true' />";
    tradsinteresado['representante.añadir.error'] = "<spring:message code='representante.añadir.error' javaScriptEscape='true' />";
    tradsinteresado['representante.añadido'] = "<spring:message code='representante.añadido' javaScriptEscape='true' />";
    tradsinteresado['representante.eliminado'] = "<spring:message code='representante.eliminado' javaScriptEscape='true' />";
    tradsinteresado['interesado.maxresultados'] = "<spring:message code='interesado.maxresultados' javaScriptEscape='true' />";
    tradsinteresado['interesado.camposBusqueda.vacios'] = "<spring:message code='interesado.camposBusqueda.vacios' javaScriptEscape='true' />";
</script>

<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/interesados.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/representantes.js"/>"></script>
<script src="<c:url value="/js/bootstrap-typeahead.js"/>" type="text/javascript"></script>


<script type="text/javascript">
    var tipoRegistro = '${param.tipoRegistro}';
    var urlAddOrganismoInteresado = '<c:url value="/interesado/${param.tipoRegistro}/addOrganismo"/>';
    var urlEliminarOrganismoInteresado = '<c:url value="/interesado/${param.tipoRegistro}/eliminarOrganismo"/>';
    var urlEliminarInteresados = '<c:url value="/interesado/${param.tipoRegistro}/eliminarInteresados"/>';
    var urlAddPersonaInteresado = '<c:url value="/interesado/${param.tipoRegistro}/addPersona"/>';
    var urlEliminarPersonaInteresado = '<c:url value="/interesado/${param.tipoRegistro}/eliminarPersona"/>';
    var urlAddRepresentante = '<c:url value="/interesado/${param.tipoRegistro}/addRepresentante"/>';
    var urlEliminarRepresentante = '<c:url value="/interesado/${param.tipoRegistro}/eliminarRepresentante"/>';
    var urlObtenerInteresado = '<c:url value="/interesado/${param.tipoRegistro}/obtenerInteresado"/>';
    var urlBusquedaPersonasFisicas = '<c:url value="/rest/busquedaPersonas/2"/>';
    var urlBusquedaPersonasJuridicas = '<c:url value="/rest/busquedaPersonas/3"/>';
    var urlActualizarEventoRegistroSalida = '<c:url value="/registroSalida/actualizarEvento"/>';
    var idRegistroSalida = '${registro.id}';

    <c:import url="../registro/addInteresadosBbdd.jsp"/>

    $(window).load(function() {

        actualizarCanalNotificacion();
        <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
        mostrarPersonaFisica();
        </c:if>
        <c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
        mostrarOrganismos();
        </c:if>

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
        $('#canal').change(function() {actualizarCanalNotificacion();});

        // Gestión de los cambios de persona
        $('#pais\\.id').change(function() {actualizarPais();});

        //Gestión de los cambios de tipo documento
        $('#tipoDocumentoIdentificacion').change(
            function() {
                actualizarTipoDocumentoIdentificacion();
                quitarError('documento');
            });
    });


    $(document).ready(function () {
        $('#organismoInteresado').chosen().change(function () {
            addOrganismoInteresado('<spring:message code="interesado.administracion"/>','${param.idRegistroDetalle}');
        });
    });

    // Búsqueda de personas fisicas
    $('#personaFisica').typeahead({
        items:25,
        scrollBar:true,
        ajax: {
            url: urlBusquedaPersonasFisicas,
            data: 'query=' + JSON.stringify($('#personaFisica').val()),
            dataType: 'json',
            timeout: 200,
            displayField: "nombre",
            valueField: "id",
            triggerLength: 4,
            method: "post",
            loadingClass: "loading-circle"
        },
        onSelect: function(item) {
            if(item.value != -1){
                addPersonaInteresado(item.value,item.text,'<spring:message code="persona.fisica"/>','No',null,'${param.idRegistroDetalle}')
            }
        }
    });

    // Búsqueda de personas juridicas
    $('#personaJuridica').typeahead({
        items:25,
        scrollBar:true,
        ajax: {
            url: urlBusquedaPersonasJuridicas,
            data: 'query=' + JSON.stringify($('#personaJuridica').val()),
            dataType: 'json',
            timeout: 200,
            displayField: "nombre",
            valueField: "id",
            triggerLength: 4,
            method: "post",
            loadingClass: "loading-circle"
        },
        onSelect: function(item) {
            if(item.value != -1){
                addPersonaInteresado(item.value,item.text,'<spring:message code="persona.juridica"/>','No',null,'${param.idRegistroDetalle}')
            }
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
        $('#denominacionOrganismoInteresado').focus();
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