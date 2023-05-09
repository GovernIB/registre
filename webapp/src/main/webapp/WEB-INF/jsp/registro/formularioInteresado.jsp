<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="modalInteresado" class="modal fade" >

    <div class="modal-dialog modal-lg" id="formularioInteresado">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarInteresado()">x</button>
                <h3 id="interesadoTitulo"></h3>
            </div>

            <div class="modal-body">

                <form id="interesadoForm" class="form-horizontal" action="${pageContext.request.contextPath}/interesado/gestionar/${param.registro}" method="post">
                    <input type="hidden" id="accion" name="accion" value="nuevo"/>
                    <input type="hidden" id="id" name="id" value=""/>
                    <input type="hidden" id="idRegistroDetalle" name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>

                    <input type="hidden" id="isRepresentante" name="isRepresentante" value="false"/>
                    <input type="hidden" id="representado.id" name="representado.id" value=""/>

                    <div class="col-xs-12">
                        <div id="tipoInteresadoSelect" class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
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
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="nombre" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.nombre.interesado"/>" data-toggle="popover"><spring:message code="regweb.nombre"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="nombre" name="nombre" class="form-control" tabindex="1" type="text" value="" maxlength="30"/>
                                <span id="nombreError"></span>
                            </div>
                        </div>
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="apellido1" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido1.interesado"/>" data-toggle="popover"><spring:message code="usuario.apellido1"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="apellido1" name="apellido1" class="form-control" tabindex="2" type="text" value="" maxlength="30"/>
                                <span id="apellido1Error"></span>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="apellido2" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.apellido2.interesado"/>" data-toggle="popover"><spring:message code="usuario.apellido2"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="apellido2" name="apellido2" class="form-control" tabindex="3" type="text" value="" maxlength="30"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="tipoDocumentoIdentificacion" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoDoc.interesado"/>" data-toggle="popover"><spring:message code="persona.tipoDocumentoIdentificacion"/></label>
                            </div>
                            <div class="col-xs-8">
                                <select id="tipoDocumentoIdentificacion" name="tipoDocumentoIdentificacion" class="chosen-select" tabindex="4">
                                    <option value="">...</option>
                                    <c:forEach items="${tiposDocumento}" var="tipoDocumento">
                                        <option value="${tipoDocumento}"><spring:message code="tipoDocumentoIdentificacion.${tipoDocumento}"/></option>
                                    </c:forEach>
                                </select>
                                <span id="tipoDocumentoIdentificacionError"></span>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="documento" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.documento.interesado"/>" data-toggle="popover"><spring:message code="usuario.documento"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="documento" name="documento" maxlength="17" class="form-control" tabindex="5" type="text" value="" disabled="disabled" style="text-transform:uppercase"/>
                                <span id="documentoError"></span>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="email" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.mail.interesado"/>" data-toggle="popover"><spring:message code="usuario.email"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="email" name="email" class="form-control" tabindex="6" type="text" value="" maxlength="160"/>
                                <span id="emailError"></span>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="telefono" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.telefono.interesado"/>" data-toggle="popover"><spring:message code="persona.telefono"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="telefono" name="telefono" class="form-control" tabindex="7" type="text" value="" maxlength="20"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="telefono" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.telefono.movil.interesado"/>" data-toggle="popover"><spring:message code="persona.movil"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="telefonoMovil" name="telefonoMovil" class="form-control" tabindex="8" type="text" value="" maxlength="20"/>
                                <span id="telefonoMovilError"></span>
                            </div>
                        </div>

                       <%-- <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="canal" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.canal.interesado"/>" data-toggle="popover"><spring:message code="persona.canal"/></label>
                            </div>
                            <div class="col-xs-8">
                                <select id="canal" name="canal" class="chosen-select" tabindex="8">
                                    <option value="-1">...</option>
                                    <c:forEach items="${canalesNotificacion}" var="canalNotificacion">
                                        <option value="${canalNotificacion}"><spring:message code="canalNotificacion.${canalNotificacion}"/></option>
                                    </c:forEach>
                                </select>
                                <span id="canalNotificacionError"></span>
                            </div>
                        </div>--%>
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="pais.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.pais.interesado"/>" data-toggle="popover"><spring:message code="interesado.pais"/></label>
                            </div>
                            <div class="col-xs-8">
                                <select id="pais.id" name="pais.id" class="chosen-select" tabindex="9" disabled="disabled">
                                    <option value="-1">...</option>
                                    <c:forEach items="${paises}" var="pais">
                                        <option value="${pais.id}">${pais.descripcionPais}</option>
                                    </c:forEach>
                                </select>
                                <span id="pais.idError"></span>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="provincia.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.provincia.interesado"/>" data-toggle="popover"><spring:message code="interesado.provincia"/></label>
                            </div>
                            <div class="col-xs-8">
                                <select id="provincia.id" name="provincia.id" class="chosen-select" tabindex="10" onchange="actualizarLocalidad(this)" disabled="disabled">
                                    <option value="-1">...</option>
                                    <c:forEach items="${provincias}" var="provincia">
                                        <option value="${provincia.id}">${provincia.descripcionProvincia}</option>
                                    </c:forEach>
                                </select>
                                <span id="provincia.idError"></span>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="localidad.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.localidad.interesado"/>" data-toggle="popover"><spring:message code="interesado.localidad"/></label>
                            </div>
                            <div class="col-xs-8">
                                <select id="localidad.id" name="localidad.id" class="chosen-select" tabindex="11" disabled="disabled"></select>
                                <span id="localidad.idError"></span>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="direccion" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.direccion.interesado"/>" data-toggle="popover"><spring:message code="persona.direccion"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="direccion" name="direccion" class="form-control" tabindex="12" type="text" value="" maxlength="160" disabled="disabled"/>
                                <span id="direccionError"></span>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="cp" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.cp.interesado"/>" data-toggle="popover"><spring:message code="persona.cp"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="cp" name="cp" class="form-control" type="text" tabindex="13" value="" maxlength="5" disabled="disabled"/>
                                <span id="cpError"></span>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="canal" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.canal.interesado"/>" data-toggle="popover"><spring:message code="persona.canal"/></label>
                            </div>
                            <div class="col-xs-8">
                                <select id="canal" name="canal" class="chosen-select" tabindex="14">
                                    <option value="-1">...</option>
                                    <c:forEach items="${canalesNotificacion}" var="canalNotificacion">
                                        <option value="${canalNotificacion}"><spring:message code="canalNotificacion.${canalNotificacion}"/></option>
                                    </c:forEach>
                                </select>
                                <span id="canalError"></span>
                            </div>
                        </div>


                       <%-- <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="direccionElectronica" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.direccionElectronica.interesado"/>" data-toggle="popover"><spring:message code="persona.direccionElectronica"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="direccionElectronica" name="direccionElectronica" class="form-control" tabindex="15" type="text" value="" maxlength="160" disabled="disabled"/>
                                <span id="direccionElectronicaError"></span>
                            </div>
                        </div>--%>
                    </div>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="razonSocial" rel="popupAbajo"
                                       data-content="<spring:message code="registro.ayuda.razon.interesado"/>"
                                       data-toggle="popover"><spring:message code="persona.razonSocial"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="razonSocial" name="razonSocial" class="form-control" tabindex="14"
                                       type="text" value="" maxlength="80" disabled="disabled"/>
                                <span id="razonSocialError"></span>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="codDirectoriosUnificados" rel="popupAbajo"
                                       data-content="<spring:message code="registro.ayuda.codDirectoriosUnificados.interesado"/>"
                                       data-toggle="popover"><spring:message
                                        code="persona.codDirectoriosUnificados"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input id="codDirectoriosUnificados" name="codDirectoriosUnificados"
                                       class="form-control" tabindex="14" type="text" value="" maxlength="15"
                                       disabled="disabled"/>
                                <span id="codDirectoriosUnificadosError"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="observaciones" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones.interesado"/>" data-toggle="popover"><spring:message code="interesado.observaciones"/></label>
                            </div>
                            <div class="col-xs-8">
                                <textarea id="observaciones" name="observaciones" class="form-control" tabindex="16" rows="2" maxlength="160"></textarea>
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="receptorNotificaciones" data-content="<spring:message code="registro.ayuda.interesado.receptor"/>"><spring:message code="persona.receptor.notificaciones"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input type="checkbox" id="receptorNotificaciones"/>
                                <span id="receptorNotificacionesError"></span>
                            </div>
                        </div>
                    </div>

                    <c:if test="${loginInfo.entidadActiva.configuracionPersona == 3}">
                        <div class="col-xs-12">
                            <div class="form-group col-xs-6 senseMargeLat">
                                <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                    <label for="guardarInteresado"><spring:message code="interesado.guardar"/></label>
                                </div>
                                <div class="col-xs-8">
                                    <input type="checkbox" id="guardarInteresado" value="true" />
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <div class="col-xs-12">
                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="avisoNotificacionSMS" data-content="<spring:message code="registro.ayuda.interesado.sms"/>"><spring:message code="persona.notificacion.sms"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input type="checkbox" id="avisoNotificacionSMS"  />
                            </div>
                        </div>

                        <div class="form-group col-xs-6 senseMargeLat">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                <label for="avisoCorreoElectronico" data-content="<spring:message code="registro.ayuda.interesado.email"/>"><spring:message code="persona.notificacion.email"/></label>
                            </div>
                            <div class="col-xs-8">
                                <input type="checkbox" id="avisoCorreoElectronico"  />
                            </div>
                        </div>
                    </div>



                    <div class="clearfix"></div>

            </div>
            <div class="modal-footer">
                <input type="button" onclick="procesarInteresado()" title="<spring:message code="regweb.guardar"/>" tabindex="17" value="<spring:message code="regweb.guardar"/>" class="btn btn-warning btn-sm">
                <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarInteresado()"><spring:message code="regweb.cerrar"/></button>
            </div>
             </form>
        </div>
    </div>
</div>