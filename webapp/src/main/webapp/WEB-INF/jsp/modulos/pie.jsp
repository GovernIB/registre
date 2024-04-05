<%@page import="es.caib.regweb3.utils.Configuracio"%>
<%@page import="es.caib.regweb3.utils.TimeStamp"%>
<%@ page import="es.caib.regweb3.utils.Versio" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="container peu row-fluid">
    <div class="pull-left colophon">REGWEB3 &copy; | <fmt:message key="regweb.titulo"/> - <fmt:message key="regweb.version"/> <%=Versio.VERSIO + (Configuracio.isCAIB()?"-caib":"") + " " + (Configuracio.showTimeStamp()?TimeStamp.TIMESTAMP : "") %></div>
    <%--Si el usuario es SuperAdministrador--%>
    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
        <div class="col-xs-4 centrat-float-left text-center">${loginInfo.configuracion.textoPie}</div>
        <c:if test="${loginInfo.configuracion.logoPie != null}">
            <div class="pull-right govern-footer">
                <img src="<c:url value="/archivo/${loginInfo.configuracion.logoPie.id}"/>" alt="${loginInfo.configuracion.logoPie.nombre}" />
            </div>
        </c:if>
    </c:if>
    <%--Si el usuario no es SuperAdministrador--%>
    <c:if test="${loginInfo.rolActivo.nombre != 'RWE_SUPERADMIN'}">
        <c:if test="${loginInfo.entidadActiva != null}">
            <div class="col-xs-4 centrat-float-left text-center">${loginInfo.entidadActiva.textoPie}</div>
        </c:if>
        <c:if test="${loginInfo.entidadActiva == null}">
            <div class="pull-right govern-footer"> <img src="<c:url value="/img/govern-logo-neg.png"/>" width="129" height="30" alt="Govern de les Illes Balears" /></div>
        </c:if>
        <c:if test="${loginInfo.entidadActiva != null}">
            <div class="pull-right govern-footer">
                <c:if test="${loginInfo.entidadActiva.logoPie != null}">
                    <img src="<c:url value="/archivo/${loginInfo.entidadActiva.logoPie.id}"/>" alt="${loginInfo.entidadActiva.nombre}" />
                </c:if>
                <c:if test="${loginInfo.entidadActiva.logoPie == null}">
                    <img src="<c:url value="/img/govern-logo-nou-peu.png"/>" width="123" height="48" alt="Govern de les Illes Balears" />
                </c:if>
            </div>
        </c:if>
    </c:if>
</div>

<!-- JavaScript -->

<script type="text/javascript" src="<c:url value="/js/bootstrap.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/bootstrap-dropdown-on-hover-plugin.js"/>"></script>

<!-- DateTimePicker -->
<script type="text/javascript" src="<c:url value="/js/datepicker/moment.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/datepicker/bootstrap-datetimepicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/datepicker/bootstrap-datetimepicker.ca.js"/>"></script>

<!-- Selects multiple -->
<script type="text/javascript" src="<c:url value="/js/chosen.jquery.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/regweb.js"/>"></script>

<!-- Upload file jquery -->
<script type="text/javascript" src="<c:url value="/js/jquery.form.js"/>"></script>


<!-- Input File -->
<script type="text/javascript">
    $(document)
        .on('change', '.btn-file :file', function() {
            var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            input.trigger('fileselect', [numFiles, label]);
    });

    $(document).ready( function() {
        $('.btn-file :file').on('fileselect', function(event, numFiles, label) {

            var input = $(this).parents('.input-group').find(':text'),
                log = numFiles > 1 ? numFiles + ' files selected' : label;

            if( input.length ) {
                input.val(log);
            } else {
                if( log ) alert(log);
            }

        });
    });
<!-- Fin input file -->

<!-- COLOR MENU -->
    $(document).ready(function() {
        $(function () {
            <!--Si es SuperAdministrador-->
            if(${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}){
                $('.navbar-header').css('background-color','#${loginInfo.configuracion.colorMenu}');
                $('.navbar-nav > li > a').css('background-color','#${loginInfo.configuracion.colorMenu}');
            }else{ <!--Si No es SuperAdministrador-->
                if(${loginInfo.entidadActiva != null}){
                    $('.navbar-header').css('background-color','#${loginInfo.entidadActiva.colorMenu}');
                    $('.navbar-nav > li > a').css('background-color','#${loginInfo.entidadActiva.colorMenu}');
                }
            }
        });
    });
<!-- Fin COLOR MENU -->

    <%-- Traducciones para generales --%>
    var trads_general = new Array();
    trads_general['sesion.expirar.titulo'] = "<spring:message code='regweb.sesion.expirar.titulo' javaScriptEscape='true' />";
    trads_general['sesion.expirar.mensaje'] = "<spring:message code='regweb.sesion.expirar.mensaje' javaScriptEscape='true' />";
    trads_general['sesion.expirar.boton'] = "<spring:message code='regweb.sesion.expirar.boton' javaScriptEscape='true' />";
    trads_general['error.valor.requerido'] = "<spring:message code='error.valor.requerido' javaScriptEscape='true' />";
    trads_general['error.formato.incorrecto'] = "<spring:message code='error.formato.incorrecto' javaScriptEscape='true' />";
    trads_general['error.fechaInicio.posterior'] = "<spring:message code='error.fechaInicio.posterior' javaScriptEscape='true' />";
    trads_general['error.fechaFin.posterior'] = "<spring:message code='error.fechaFin.posterior' javaScriptEscape='true' />";
    trads_general['error.fechaInicioFin.posterior'] = "<spring:message code='error.fechaInicioFin.posterior' javaScriptEscape='true' />";
    trads_general['error.numeroRegistro.noNumerico'] = "<spring:message code='error.numeroRegistro.noNumerico' javaScriptEscape='true' />";
    trads_general['error.libro.seleccionado'] = "<spring:message code='error.libro.seleccionado' javaScriptEscape='true' />";

</script>


