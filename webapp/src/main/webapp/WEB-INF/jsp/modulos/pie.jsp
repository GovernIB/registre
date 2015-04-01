<%@page import="es.caib.regweb.utils.TimeStamp"%>
<%@page import="es.caib.regweb.utils.Configuracio"%>
<%@ page import="es.caib.regweb.utils.Versio" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="container peu row-fluid">
    <div class="pull-left colophon">REGWEB &copy; | <fmt:message key="regweb.titulo"/> - <fmt:message key="regweb.version"/> <%=Versio.VERSIO + (Configuracio.isCAIB()?"-caib":"") + " " + (Configuracio.showTimeStamp()?TimeStamp.TIMESTAMP : "") %></div>
    <c:if test="${entidadActiva != null}">
        <div class="col-xs-4 centrat-float-left text-center">${entidadActiva.textoPie}</div>
    </c:if>
    <c:if test="${entidadActiva == null}">
        <div class="pull-right govern-footer"> <img src="<c:url value="/img/govern-logo-neg.png"/>" width="129" height="30" alt="Govern de les Illes Balears" /></div>
    </c:if>
    <c:if test="${entidadActiva != null}">
        <div class="pull-right govern-footer">
            <c:if test="${entidadActiva.logoPie != null}">
                <img src="<c:url value="/archivo/${entidadActiva.logoPie.id}"/>" alt="${entidadActiva.nombre}" />
            </c:if>
            <c:if test="${entidadActiva.logoPie == null}">
                <img src="<c:url value="/img/govern-logo-neg.png"/>" width="129" height="30" alt="Govern de les Illes Balears" />
            </c:if>
        </div>
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
<script>
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
</script>
<!-- Fin input file -->



<!-- COLOR MENU -->
<script>
    $(document).ready(function() {
        $(function () {
            if(${entidadActiva != null}){
                $('.navbar-header').css('background-color','#${entidadActiva.colorMenu}');
                $('.navbar-nav > li > a').css('background-color','#${entidadActiva.colorMenu}');
            }
        });
    });

</script>
<!-- Fin COLOR MENU -->
