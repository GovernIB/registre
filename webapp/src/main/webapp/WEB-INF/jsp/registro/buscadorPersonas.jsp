<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- Formulario de búsqueda de Personas Físicas y Jurídicas -->

<div id="modalBuscadorPersonas${param.tipoPersona}" class="modal fade bs-example-modal-lg">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarBusquedaPersona('${param.tipoPersona}')">x</button>
                <h3>
                    <c:if test="${param.tipoPersona eq 'Fisicas'}"><spring:message code="persona.buscar.fisicas"/></c:if>
                    <c:if test="${param.tipoPersona eq 'Juridicas'}"><spring:message code="persona.buscar.juridicas"/></c:if>
                    <c:if test="${param.tipoPersona eq 'Todas'}"><spring:message code="representante.buscador"/></c:if>
                </h3>
            </div>
            <div class="modal-body" >
                <form id="buscadorPersonasForm${param.tipoPersona}" class="form-horizontal" action="${pageContext.request.contextPath}/interesado/busquedaPersona.json" method="post">
                    <input type="hidden" id="representado" name="representado" value=""/>
                    <input type="hidden" id="idRegistroDetalle" name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>
                    <c:if test="${param.tipoPersona eq 'Fisicas' || param.tipoPersona eq 'Todas'}">
                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="nombre${param.tipoPersona}"><spring:message code="regweb.nombre"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="nombre${param.tipoPersona}" name="nombre${param.tipoPersona}" autofocus="autofocus" class="form-control" type="text" value=""/>
                            </div>
                        </div>
                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="apellido1${param.tipoPersona}"><spring:message code="persona.apellido1"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="apellido1${param.tipoPersona}" name="apellido1${param.tipoPersona}" class="form-control" type="text" value=""/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="apellido2${param.tipoPersona}"><spring:message code="persona.apellido2"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="apellido2${param.tipoPersona}" name="apellido2${param.tipoPersona}" class="form-control" type="text" value=""/>
                            </div>
                        </div>

                    </c:if>

                    <c:if test="${param.tipoPersona eq 'Juridicas' || param.tipoPersona eq 'Todas'}">
                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb">
                                <label for="razonSocial${param.tipoPersona}"><spring:message code="persona.razonSocial"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="razonSocial${param.tipoPersona}" name="razonSocial${param.tipoPersona}" autofocus="autofocus" class="form-control" type="text" value=""/>
                            </div>
                        </div>
                    </c:if>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb">
                            <label for="documento${param.tipoPersona}"><spring:message code="persona.documento"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="documento${param.tipoPersona}" name="documento${param.tipoPersona}" class="form-control" type="text" value=""/>
                        </div>
                    </div>

                    <div class="clearfix"></div>

                </form>
                <input type="button" id="buscarPersonaForm"
                       onclick="buscarPersonas('${param.tipoPersona}','${param.idRegistroDetalle}')"
                       class="btn btn-warning btn-sm" title="<spring:message code="regweb.buscar"/>"
                       value="<spring:message code="regweb.buscar"/>"/>
                    <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarBusquedaPersona('${param.tipoPersona}')"><spring:message code="regweb.cerrar"/></button>

            </div>
            <div class="modal-footer">
                <!-- Mostrar llistat de la busqueda-->
                <div class="form-group col-xs-12">
                    <div id="resultadosBusquedaPersonas${param.tipoPersona}"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    // Realizamos la búsqueda al presionar la techa enter
    $("#modalBuscadorPersonas${param.tipoPersona}").keypress(function(e) {
        if ((e.keyCode == 13)) {
            e.preventDefault();
            buscarPersonas('${param.tipoPersona}','${param.idRegistroDetalle}');
        }
    });

</script>