<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>
<div id="modalInteresado" class="modal fade bs-example-modal-lg" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarInteresado()">x</button>
                <h3 id="interesadoTitulo"></h3>
            </div>

            <div class="modal-body">

                <form id="interesadoForm" class="form-horizontal" action="${pageContext.request.contextPath}/interesado/gestionar" method="post">
                    <input type="hidden" id="accion" name="accion" value="nuevo"/>
                    <input type="hidden" id="id" name="id" value=""/>
                    <input type="hidden" id="idRegistroDetalle" name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>

                    <input type="hidden" id="isRepresentante" name="isRepresentante" value="false"/>
                    <input type="hidden" id="representado.id" name="representado.id" value=""/>

                    <div id="tipoInteresadoSelect" class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="tipo"><spring:message code="persona.tipoPersona"/></label>
                        </div>
                        <div class="col-xs-8">
                            <select id="tipo" name="tipo" class="chosen-select" onchange="tipoInteresadoRepresentante()">
                                <c:forEach items="${tiposInteresado}" var="tipoInteresado">
                                    <c:if test="${tipoInteresado != RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">
                                        <option value="${tipoInteresado}"><spring:message code="interesado.tipo.${tipoInteresado}"/></option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="nombre" id="nom"><spring:message code="regweb.nombre"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="nombre" name="nombre" class="form-control" type="text" value="" maxlength="30"/>
                            <span id="nombreError"></span>
                        </div>
                    </div>
                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="apellido1" id="llinatge1"><spring:message code="usuario.apellido1"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="apellido1" name="apellido1" class="form-control" type="text" value="" maxlength="30"/>
                            <span id="apellido1Error"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="apellido2"><spring:message code="usuario.apellido2"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="apellido2" name="apellido2" class="form-control" type="text" value="" maxlength="30"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="tipoDocumentoIdentificacion"><spring:message code="persona.tipoDocumentoIdentificacion"/></label>
                        </div>
                        <div class="col-xs-8">
                            <select id="tipoDocumentoIdentificacion" name="tipoDocumentoIdentificacion" class="chosen-select">
                                <option value="-1">...</option>
                                <c:forEach items="${tiposDocumento}" var="tipoDocumento">
                                    <option value="${tipoDocumento}"><spring:message code="tipoDocumentoIdentificacion.${tipoDocumento}"/></option>
                                </c:forEach>
                            </select>
                            <span id="tipoDocumentoIdentificacionError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="documento"><spring:message code="usuario.documento"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="documento" name="documento" maxlength="17" class="form-control" type="text" value="" disabled="disabled" style="text-transform:uppercase"/>
                            <span id="documentoError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="pais.id"><spring:message code="interesado.pais"/></label>
                        </div>
                        <div class="col-xs-8">
                            <select id="pais.id" name="pais.id" class="chosen-select">
                                <option value="-1">...</option>
                                <c:forEach items="${paises}" var="pais">
                                    <option value="${pais.id}">${pais.descripcionPais}</option>
                                </c:forEach>
                            </select>
                            <span id="pais.idError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="provincia.id"><spring:message code="interesado.provincia"/></label>
                        </div>
                        <div class="col-xs-8">
                            <select id="provincia.id" name="provincia.id" class="chosen-select" onchange="actualizarLocalidad(this)" disabled="disabled">
                                <option value="-1">...</option>
                                <c:forEach items="${provincias}" var="provincia">
                                    <option value="${provincia.id}">${provincia.descripcionProvincia}</option>
                                </c:forEach>
                            </select>
                            <span id="provincia.idError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="localidad.id"><spring:message code="interesado.localidad"/></label>
                        </div>
                        <div class="col-xs-8">
                            <select id="localidad.id" name="localidad.id" class="chosen-select" disabled="disabled"></select>
                            <span id="localidad.idError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="direccion"><spring:message code="persona.direccion"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="direccion" name="direccion" class="form-control" type="text" value="" maxlength="160"/>
                            <span id="direccionError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="cp"><spring:message code="persona.cp"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="cp" name="cp" class="form-control" type="text" value="" maxlength="5"/>
                            <span id="cpError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="email"><spring:message code="usuario.email"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="email" name="email" class="form-control" type="text" value="" maxlength="160"/>
                            <span id="emailError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="telefono"><spring:message code="persona.telefono"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="telefono" name="telefono" class="form-control" type="text" value="" maxlength="20"/>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="razonSocial" id="rao"><spring:message code="persona.razonSocial"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="razonSocial" name="razonSocial" class="form-control" type="text" value="" maxlength="80"/>
                            <span id="razonSocialError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="direccionElectronica"><spring:message code="persona.direccionElectronica"/></label>
                        </div>
                        <div class="col-xs-8">
                            <input id="direccionElectronica" name="direccionElectronica" class="form-control" type="text" value="" maxlength="160"/>
                            <span id="direccionElectronicaError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                        <%--  TODO No està traduit !!!!! --%>
                            <label for="canalNotificacion">Canal not.</label>
                        </div>
                        <div class="col-xs-8">
                            <select id="canalNotificacion" name="canalNotificacion" class="chosen-select">
                                <option value="-1">...</option>
                                <c:forEach items="${canalesNotificacion}" var="canalNotificacion">
                                    <option value="${canalNotificacion}"><spring:message code="canalNotificacion.${canalNotificacion}"/></option>
                                </c:forEach>
                            </select>
                            <span id="canalNotificacionError"></span>
                        </div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="observaciones"><spring:message code="interesado.observaciones"/></label>
                        </div>
                        <div class="col-xs-8">
                            <textarea id="observaciones" name="observaciones" class="form-control" rows="2" maxlength="160"></textarea>
                        </div>
                    </div>

                    <c:if test="${entidadActiva.configuracionPersona == 3}">
                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="guardarInteresado"><spring:message code="interesado.guardar"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input type="checkbox" id="guardarInteresado" value="true" />
                            </div>
                        </div>
                    </c:if>


                    <div class="clearfix"></div>

            </div>
            <div class="modal-footer">
                <input type="button" onclick="procesarInteresado()" title="<spring:message code="regweb.guardar"/>" value="<spring:message code="regweb.guardar"/>" class="btn btn-warning btn-sm" tabindex="1">
                <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarInteresado()"><spring:message code="regweb.cerrar"/></button>
                <%--<button id="eliminarRepresentante" class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="eliminarRepresentante($('#id').val(),$('#representado\\.id').val(),$('#idRegistroDetalle\\.id').val())"><spring:message code="regweb3.eliminar"/></button>--%>
            </div>
             </form>
        </div>
    </div>
</div>