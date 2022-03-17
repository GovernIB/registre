<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!-- Formulario de busqueda compartido. Se usa tanto para buscar organismos como oficinas, en función del parámetro
que se le indica -->

<div id="modalBuscador${param.tipo}" class="modal fade">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">

            <button type="button" class="close margeDret5" data-dismiss="modal" aria-hidden="true"
                    onclick="limpiarFormularioBusqueda('${param.tipo}')">x
            </button>

            <ul class="nav nav-tabs" id="tab${param.tipo}">
                <li class="active"><a href="#buscador${param.tipo}" data-toggle="tab"><h3 id="titulo${param.tipo}"><spring:message
                        code="organismo.buscador.${param.tipo}"/></h3></a></li>
                <c:if test="${param.tipo != 'OficinaOrigen'}">
                    <li><a href="#organigrama${param.tipo}" data-toggle="tab"><h3>Organigrama</h3></a></li>
                </c:if>
            </ul>

            <div class="tab-content" id='content'>

                <div class="tab-pane active" id="buscador${param.tipo}">

                    <div class="modal-body">

                        <form id="organismoREBusquedaForm${param.tipo}" class="form-horizontal" action="" method="post">
                            <input id="nivelAdministracion" type="hidden" value="${RegwebConstantes.nivelAdminAutonomica}"/>
                            <input id="comunidadAutonoma" type="hidden" value="${RegwebConstantes.comunidadBaleares}"/>

                            <div class="form-group col-xs-12 senseMargeLat">
                                <div class="col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="denominacion${param.tipo}" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.denominacion.organismo"/>" data-toggle="popover"><spring:message code="organismo.buscador.denominacion"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <input id="denominacion${param.tipo}" name="denominacion" class="form-control"
                                               type="text" value=""/>
                                    </div>
                                </div>

                                <div class="col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="codigoOrganismo${param.tipo}" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.codigo.organismo"/>" data-toggle="popover"><spring:message code="organismo.buscador.codigo"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <input id="codigoOrganismo${param.tipo}" name="codigoOrganismo" class="form-control"
                                               type="text" value="" />
                                    </div>
                                </div>
                            </div>

                            <!-- Nivel Administracion -->
                            <div class="form-group col-xs-12 senseMargeLat">
                                <div class="col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="codNivelAdministracion${param.tipo}" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.nivel.organismo"/>" data-toggle="popover"><spring:message code="organismo.buscador.nivelAdministracion"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <select id="codNivelAdministracion${param.tipo}" name="codNivelAdministracion"
                                                class="chosen-select">
                                            <option value="-1">...</option>
                                            <c:forEach items="${nivelesAdministracion}" var="nivelAdministracion">
                                                <option value="${nivelAdministracion.codigoNivelAdministracion}">${nivelAdministracion.descripcionNivelAdministracion}</option>
                                            </c:forEach>

                                        </select>
                                    </div>
                                </div>

                                <!-- Comunidad Autonoma -->
                                <div class="col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="codComunidadAutonoma${param.tipo}" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.comunidad.organismo"/>" data-toggle="popover"><spring:message code="organismo.buscador.comunidadAutonoma"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <select id="codComunidadAutonoma${param.tipo}" name="codComunidadAutonoma"
                                                class="chosen-select"
                                                onchange="actualizarProvinciaDestinatarios('${param.tipo}')">
                                            <option value="-1">...</option>
                                            <c:forEach items="${comunidadesAutonomas}" var="codComunidadAutonoma">
                                                <option value="${codComunidadAutonoma.codigoComunidad}">${codComunidadAutonoma.descripcionComunidad}</option>
                                            </c:forEach>

                                        </select>
                                    </div>
                                </div>
                            </div>

                            <!-- Provincias -->
                            <div class="form-group col-xs-12 senseMargeLat">
                                <div class="col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="provincia${param.tipo}" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.provincia.organismo"/>" data-toggle="popover"><spring:message code="interesado.provincia"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <select id="provincia${param.tipo}" name="provincia" class="chosen-select"
                                                onchange="actualizarLocalidadDestinatarios('${param.tipo}')">
                                            <option value="-1">...</option>
                                            <c:forEach items="${provinciasComunidad}" var="codProvincia">
                                                <option value="${codProvincia.codigoProvincia}">${codProvincia.descripcionProvincia}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="col-xs-6">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="localidad${param.tipo}" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.localidad.organismo"/>" data-toggle="popover"><spring:message code="interesado.localidad"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <select id="localidad${param.tipo}" name="localidad" class="chosen-select">
                                            <option value="-1">...</option>
                                            <c:forEach items="${localidadesProvincia}" var="codLocalidad">
                                                <option value="${codLocalidad.codigoLocalidad}">${codLocalidad.nombre}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <c:if test="${param.tipo == 'OrganismoInteresado'}">
                                <!-- Unidad Raiz -->
                                <div class="form-group col-xs-12 senseMargeLat">
                                    <div class="col-xs-6">
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                            <label for="unidadRaiz${param.tipo}" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.raiz.organismo"/>" data-toggle="popover"><spring:message code="organismo.buscador.unidadRaiz"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <input type="checkbox" id="unidadRaiz${param.tipo}"/>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <div class="clearfix"></div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <!-- Imatge de reload -->
                        <div class="col-xs-12 text-center centrat" id="reloadorg${param.tipo}">
                            <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
                        </div>

                        <input type="button" id="buscarorganimos${param.tipo}"
                               onclick="organismoBusqueda('${param.tipo}','${loginInfo.dir3Caib.server}','${param.idRegistroDetalle}')"
                               class="btn btn-warning btn-sm" title="<spring:message code="regweb.buscar"/>"
                               value="<spring:message code="regweb.buscar"/>"/>
                        <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true"
                                onclick="limpiarFormularioBusqueda('${param.tipo}')"><spring:message
                                code="regweb.cerrar"/></button>
                        <br/>
                        <br/>
                        <br/>
                        <!-- Mostrar llistat de la busqueda-->
                        <div class="form-group col-xs-12">
                            <div id="resultadosbusqueda${param.tipo}">
                            </div>
                        </div>
                    </div>


                </div>
                <c:if test="${param.tipo != 'OficinaOrigen'}">
                    <div class="tab-pane" id="organigrama${param.tipo}">
                        <div class="modal-body">
                            <div class="tree" id="arbol${param.tipo}">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true"
                                    onclick="limpiarFormularioBusqueda('${param.tipo}')"><spring:message
                                    code="regweb.cerrar"/></button>
                        </div>
                    </div>
                    <!-- tab pane -->
                </c:if>

            </div>
            <!--tab content-->
        </div>
    </div>
</div>
<c:set var="val"><spring:message code="organismo.raiz"/></c:set>
<input id="organismo_raiz" type="hidden" value="${val}"/>
<c:set var="valor"><spring:message code="organismo.superior"/></c:set>
<input id="organismo_superior" type="hidden" value="${valor}"/>

<script type="text/javascript">
    <%-- traduccions para busquedaorganismo.js--%>
    var tradorganismo = new Array();
    tradorganismo['organismo.denominacion'] = "<spring:message code='organismo.denominacion' javaScriptEscape='true' />";
    tradorganismo['regweb.acciones'] = "<spring:message code='regweb.acciones' javaScriptEscape='true' />";
    tradorganismo['organismo.superior'] = "<spring:message code='organismo.superior' javaScriptEscape='true' />";
    tradorganismo['organismo.superior.vacio'] = "<spring:message code='organismo.superior.vacio' javaScriptEscape='true' />";
    tradorganismo['organismo.raiz'] = "<spring:message code='organismo.raiz' javaScriptEscape='true' />";
    tradorganismo['organismo.localidad'] = "<spring:message code='organismo.localidad' javaScriptEscape='true' />";
    tradorganismo['organismo.oficinaSir'] = "<spring:message code='organismo.oficinaSir' javaScriptEscape='true' />";
    tradorganismo['organismo.oficinaSir.si'] = "<spring:message code='regweb.si' javaScriptEscape='true' />";
    tradorganismo['organismo.oficinaSir.no'] = "<spring:message code='regweb.no' javaScriptEscape='true' />";
    tradorganismo['interesado.resultado'] = "<spring:message code="interesado.resultado" javaScriptEscape='true' />";
    tradorganismo['interesado.resultados'] = "<spring:message code='interesado.resultados' javaScriptEscape='true' />";
    tradorganismo['regweb.noresultados'] = "<spring:message code='regweb.noresultados' javaScriptEscape='true' />";
    tradorganismo['regweb.resultado'] = "<spring:message code='regweb.resultado' javaScriptEscape='true' />";
    tradorganismo['regweb.resultados'] = "<spring:message code='regweb.resultados' javaScriptEscape='true' />";
    tradorganismo['organismo.estado'] = "<spring:message code='organismo.estado' javaScriptEscape='true' />";
    tradorganismo['organismo.estado.vigente'] = "<spring:message code='unidad.estado.V' javaScriptEscape='true' />";
    tradorganismo['organismo.estado.extinguido'] = "<spring:message code='unidad.estado.E' javaScriptEscape='true' />";
    tradorganismo['organismo.estado.anulado'] = "<spring:message code='unidad.estado.A' javaScriptEscape='true' />";
    tradorganismo['organismo.estado.transitorio'] = "<spring:message code='unidad.estado.T' javaScriptEscape='true' />";
    tradorganismo['organismo.buscador.error'] = "<spring:message code='organismo.buscador.error' javaScriptEscape='true' />";
    tradorganismo['interesado.administracion'] = "<spring:message code='interesado.administracion' javaScriptEscape='true' />";

    function actualizarLocalidadDestinatarios(paramTipo) {

        <c:url var="obtenerLocalidades" value="/rest/obtenerLocalidadesProvincia" />
        var provinciaString = '#provincia' + paramTipo + ' option:selected';
        $('#localidad' + paramTipo).empty();
        $('#localidad' + paramTipo).attr("disabled", "disabled").trigger("chosen:updated");

        actualizarSelectLocalidad('${obtenerLocalidades}', '#localidad' + paramTipo, $(provinciaString).val(), null, true, true);
    }

    function actualizarProvinciaDestinatarios(paramTipo) {
        <c:url var="obtenerProvincias" value="/rest/obtenerProvincias" />

        var codautonomaString = '#codComunidadAutonoma' + paramTipo + ' option:selected';

        $('#provincia' + paramTipo).empty();
        $('#provincia' + paramTipo).attr("disabled", "disabled").trigger("chosen:updated");
        $('#localidad' + paramTipo).empty();
        $('#localidad' + paramTipo).attr("disabled", "disabled").trigger("chosen:updated");
        actualizarSelect('${obtenerProvincias}', '#provincia' + paramTipo, $(codautonomaString).val(), null, true, true);
    }

    // Realizamos la búsqueda al presionar la tecla enter
    $("#modalBuscador${param.tipo}").keypress(function(e) {
        if ((e.keyCode == 13)) {
            e.preventDefault();
            organismoBusqueda('${param.tipo}','${loginInfo.dir3Caib.server}','${param.idRegistroDetalle}');
        }
    });




</script>

