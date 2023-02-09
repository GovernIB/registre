<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="detalleInteresadoSir" class="modal fade">

    <div class="modal-dialog modal-lg" id="formularioInteresado">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        onclick="limpiarInteresadoDetalleSir()">x
                </button>
                <h3 id="interesadoTitulo"></h3>
            </div>

            <div class="modal-body">

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="regweb.nombre"/></label>
                    </div>
                    <div class="col-xs-7" id="nombreInteresado"></div>
                </div>
                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="usuario.apellido1"/></label>
                    </div>
                    <div class="col-xs-7" id="primerApellidoInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="usuario.apellido2"/></label>
                    </div>
                    <div class="col-xs-7" id="segundoApellidoInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.tipoDocumentoIdentificacion"/></label>
                    </div>
                    <div class="col-xs-7" id="tipoDocumentoIdentificacionInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="usuario.documento"/></label>
                    </div>
                    <div class="col-xs-7" id="documentoIdentificacionInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="usuario.email"/></label>
                    </div>
                    <div class="col-xs-7" id="correoElectronicoInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.telefono"/></label>
                    </div>
                    <div class="col-xs-7" id="telefonoInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.movil"/></label>
                    </div>
                    <div class="col-xs-7" id="telefonoMovilInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.canal"/></label>
                    </div>
                    <div class="col-xs-7" id="canalPreferenteComunicacionInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="interesado.pais"/></label>
                    </div>
                    <div class="col-xs-7" id="codigoPaisInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="interesado.provincia"/></label>
                    </div>
                    <div class="col-xs-7" id="codigoProvinciaInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="interesado.localidad"/></label>
                    </div>
                    <div class="col-xs-7" id="codigoMunicipioInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label ><spring:message code="persona.direccion"/></label>
                    </div>
                    <div class="col-xs-7" id="direccionInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.cp"/></label>
                    </div>
                    <div class="col-xs-7" id="codigoPostalInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.razonSocial"/></label>
                    </div>
                    <div class="col-xs-7" id="razonSocialInteresado"></div>
                </div>


                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="interesado.observaciones"/></label>
                    </div>
                    <div class="col-xs-7" id="observaciones"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="interesado.codDirectoriosUnificados"/></label>
                    </div>
                    <div class="col-xs-7" id="codigoDirectorioUnificadosInteresado"></div>
                </div>


                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.notificacion.sms"/></label>
                    </div>
                    <div class="col-xs-7" id="avisoNotificacionSMSInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.notificacion.email"/></label>
                    </div>
                    <div class="col-xs-7" id="avisoCorreoElectronicoInteresado"></div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="persona.receptor.notificaciones"/></label>
                    </div>
                    <div class="col-xs-7" id="receptorNotificacionesInteresado"></div>
                </div>


            </div>
            <div class="clearfix"></div>

            <div id="representante">
                <div class="modal-header">
                    <h3><strong><spring:message code='representante.representante'/></strong></h3>
                </div>

                <div class="modal-body">

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="regweb.nombre"/></label>
                        </div>
                        <div class="col-xs-7" id="nombreRepresentante"></div>
                    </div>
                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label ><spring:message code="usuario.apellido1"/></label>
                        </div>
                        <div class="col-xs-7" id="primerApellidoRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="usuario.apellido2"/></label>
                        </div>
                        <div class="col-xs-7" id="segundoApellidoRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.tipoDocumentoIdentificacion"/></label>
                        </div>
                        <div class="col-xs-7" id="tipoDocumentoIdentificacionRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="usuario.documento"/></label>
                        </div>
                        <div class="col-xs-7" id="documentoIdentificacionRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="usuario.email"/></label>
                        </div>
                        <div class="col-xs-7" id="correoElectronicoRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.telefono"/></label>
                        </div>
                        <div class="col-xs-7" id="telefonoRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.movil"/></label>
                        </div>
                        <div class="col-xs-7" id="telefonoMovilRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.canal"/></label>
                        </div>
                        <div class="col-xs-7" id="canalPreferenteComunicacionRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label ><spring:message code="interesado.pais"/></label>
                        </div>
                        <div class="col-xs-7" id="codigoPaisRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label ><spring:message code="interesado.provincia"/></label>
                        </div>
                        <div class="col-xs-7" id="codigoProvinciaRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label ><spring:message code="interesado.localidad"/></label>
                        </div>
                        <div class="col-xs-7" id="codigoMunicipioRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label ><spring:message code="persona.direccion"/></label>
                        </div>
                        <div class="col-xs-7" id="direccionRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.cp"/></label>
                        </div>
                        <div class="col-xs-7" id="codigoPostalRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.razonSocial"/></label>
                        </div>
                        <div class="col-xs-7" id="razonSocialRepresentante"></div>
                    </div>


                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="interesado.codDirectoriosUnificados"/></label>
                        </div>
                        <div class="col-xs-7" id="codigoDirectorioUnificadosRepresentante"></div>
                    </div>


                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.notificacion.sms"/></label>
                        </div>
                        <div class="col-xs-7" id="avisoNotificacionSMSRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.notificacion.email"/></label>
                        </div>
                        <div class="col-xs-7" id="avisoCorreoElectronicoRepresentante"></div>
                    </div>

                    <div class="form-group col-xs-6">
                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="persona.receptor.notificaciones"/></label>
                        </div>
                        <div class="col-xs-7" id="receptorNotificacionesRepresentante"></div>
                    </div>

                    <div class="clearfix"></div>

                </div>
            </div>
            <%--<div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarInteresadoDetalleSir()">
                    <spring:message code="regweb.cerrar"/></button>
            </div>--%>

        </div>
    </div>
</div>
<script type="text/javascript" src="<c:url value="/js/interesadoSir.js"/>"></script>

<script type="text/javascript">
    var urlCargarInteresadoSir = '<c:url value="/rest/obtenerInteresadoSir"/>';

    var tradsinteresado = new Array();
    tradsinteresado['tipoDocumentoIdentificacion.N'] = "<spring:message code='tipoDocumentoIdentificacion.NIF' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.C'] = "<spring:message code='tipoDocumentoIdentificacion.CIF' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.P'] = "<spring:message code='tipoDocumentoIdentificacion.PASAPORTE' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.E'] = "<spring:message code='tipoDocumentoIdentificacion.NIE' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.X'] = "<spring:message code='tipoDocumentoIdentificacion.OTROS_PERSONA_FISICA' javaScriptEscape='true' />";
    tradsinteresado['tipoDocumentoIdentificacion.O'] = "<spring:message code='tipoDocumentoIdentificacion.CODIGO_ORIGEN_VALUE' javaScriptEscape='true' />";
    tradsinteresado['canalNotificacion.01'] = "<spring:message code='canalNotificacion.DIRECCION_POSTAL' javaScriptEscape='true' />";
    tradsinteresado['canalNotificacion.02'] = "<spring:message code='canalNotificacion.DIRECCION_ELECTRONICA_HABILITADA' javaScriptEscape='true' />";
    tradsinteresado['canalNotificacion.03'] = "<spring:message code='canalNotificacion.COMPARECENCIA_ELECTRONICA' javaScriptEscape='true' />";
    tradsinteresado['true'] = "<spring:message code='regweb.si' javaScriptEscape='true' />";
    tradsinteresado['false'] = "<spring:message code='regweb.no' javaScriptEscape='true' />";

</script>