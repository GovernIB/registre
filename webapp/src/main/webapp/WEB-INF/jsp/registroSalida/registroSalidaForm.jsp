<%@ page import="es.caib.regweb3.utils.RegwebConstantes" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>


<!DOCTYPE html>
<html lang="ca">
<head>
    <title>
        <c:if test="${not empty registroSalida.id}"><spring:message code="registroSalida.editar"/> ${registroSalida.numeroRegistroFormateado}</c:if>
        <c:if test="${empty registroSalida.id}"><spring:message code="registroSalida.nuevo"/></c:if>
    </title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body onbeforeunload="return cerrar('<spring:message code="regweb.cerrar.pagina"/>')">

<c:import url="../modulos/menu.jsp"/>

    <div class="row-fluid container main">

        <div class="well well-white">

            <!-- Miga de pan -->
            <div class="row">
                <div class="col-xs-12">
                    <ol class="breadcrumb">
                        <c:import url="../modulos/migadepan.jsp"/>
                        <li class="active"><i class="fa fa-pencil-square-o"></i>
                            <c:if test="${not empty registroSalida.id}"><spring:message code="registroSalida.editar"/> ${registroSalida.numeroRegistroFormateado}</c:if>
                            <c:if test="${empty registroSalida.id}"><spring:message code="registroSalida.nuevo"/></c:if>
                        </li>
                    </ol>
                </div>
            </div><!-- Fin miga de pan -->

            <div id="mensajes"></div>

            <div class="row">
                <div class="col-xs-12">

                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <c:if test="${empty registroSalida.id}"><a data-toggle="modal" role="button" href="#modalSelectPlantilla" class="btn btn-danger btn-xs pull-right margin-left10" onclick="cargarPlantillas('<c:url value="/rest/obtenerPlantillas"/>','${loginInfo.usuarioEntidadActivo.id}','${RegwebConstantes.REGISTRO_SALIDA}')"><span class="fa fa-refresh"></span> <spring:message code="plantilla.select"/></a></c:if>
                            <a data-toggle="modal" role="button" href="#modalNewPlantilla" class="btn btn-danger btn-xs pull-right" onclick="preparaFormularioPlantilla('${RegwebConstantes.REGISTRO_SALIDA}')"><span class="fa fa-plus"></span> <spring:message code="plantilla.nuevo"/></a>
                            <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                <strong>
                                    <c:if test="${not empty registroSalida.id}"><spring:message code="registroSalida.editar"/> ${registroSalida.numeroRegistroFormateado}</c:if>
                                    <c:if test="${empty registroSalida.id}"><spring:message code="registroSalida.nuevo"/></c:if>
                                </strong>
                            </h3>
                        </div>
                        <div class="panel-body">
                            <div class="col-xs-12"><strong>${entidad.nombre} - ${registroSalida.oficina.denominacion}</strong></div>
                            <div class="form-group col-xs-12">
                                <strong>
                                    <c:if test="${not empty registroSalida.id}"> <fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></c:if>
                                    <c:if test="${empty registroSalida.id}"><c:set var="now" value="<%=new java.util.Date()%>" /> <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm:ss"/></c:if>
                                </strong>
                            </div>
                            <div class="col-xs-12"><strong>${usuario.nombreCompleto} (${usuario.email})</strong></div>
                        </div>
                    </div>

                </div>
            </div>


        <div class="row">

            <c:if test="${origenPlantilla}">
                <c:url value="/registroSalida/new" var="urlRegistroSalida"/>
            </c:if>
            <c:if test="${!origenPlantilla}">
                <c:url value="" var="urlRegistroSalida"/>
            </c:if>

            <form:form modelAttribute="registroSalida" method="post" cssClass="form-horizontal" action="${urlRegistroSalida}">

            <div class="col-xs-6">

                <div class="panel panel-danger">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square"></i> <strong><spring:message code="registro.datos.obligatorios"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.extracto" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.extracto.salida"/>" data-toggle="popover"><spring:message code="registroEntrada.extracto"/></label>
                            </div>
                            <div class="col-xs-10">
                                <form:textarea path="registroDetalle.extracto" rows="3" cssClass="form-control" maxlength="240"/> <form:errors path="registroDetalle.extracto" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-12">

                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="origen.codigo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.origen"/>" data-toggle="popover"><spring:message code="registroSalida.organismoOrigen"/></label>
                            </div>
                            <div class="col-xs-10">
                                <form:select path="origen.codigo" cssClass="chosen-select">
                                    <c:forEach items="${organismosOficinaActiva}" var="organismo">
                                        <c:if test="${registroSalida.origen.codigo != null}">
                                            <option value="${organismo.codigo}" <c:if test="${registroSalida.origen.codigo == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                        </c:if>
                                        <c:if test="${registroSalida.origen.codigo == null}">
                                            <option value="${organismo.codigo}" <c:if test="${registroSalida.origenExternoCodigo == organismo.codigo}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                        </c:if>

                                    </c:forEach>
                                </form:select>
                                <form:errors path="origen.codigo" cssClass="help-block" element="span"/>
                            </div>

                            <form:hidden path="origen.denominacion"/>

                            <!-- Fin de gestión de organismo origen -->
                        </div>

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.idioma" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.idioma"/>" data-toggle="popover"><spring:message code="registroEntrada.idioma"/></label>
                            </div>
                            <div class="col-xs-10">
                                <form:select path="registroDetalle.idioma" cssClass="chosen-select">
                                <c:forEach items="${idiomas}" var="idioma">
                                   <form:option value="${idioma}"><spring:message code="idioma.${idioma}"/></form:option>
                                </c:forEach>
                               </form:select>
                                <form:errors path="registroDetalle.idioma" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.tipoDocumentacionFisica" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.docFisica"/>" data-toggle="popover"><spring:message code="registroEntrada.documentacionFisica"/></label>
                            </div>
                            <div class="col-xs-10 radioButton">
                                <c:forEach items="${tiposDocumentacionFisica}" var="tipoDocFisica">
                                    <label class="radio">
                                        <c:if test="${tipoDocFisica == RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                            <form:radiobutton  path="registroDetalle.tipoDocumentacionFisica"  value="${tipoDocFisica}" /><span class="text-verd text12"> <spring:message code="tipoDocumentacionFisica.${tipoDocFisica}" /></span>
                                        </c:if>
                                        <c:if test="${tipoDocFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                                            <form:radiobutton  path="registroDetalle.tipoDocumentacionFisica" value="${tipoDocFisica}"/><span class="text-vermell text12"> <spring:message code="tipoDocumentacionFisica.${tipoDocFisica}" /></span>
                                        </c:if>
                                        <c:if test="${tipoDocFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                                            <form:radiobutton  path="registroDetalle.tipoDocumentacionFisica"  value="${tipoDocFisica}"/><span class="text-taronja text12"> <spring:message code="tipoDocumentacionFisica.${tipoDocFisica}" /></span>
                                        </c:if>
                                    </label>
                                </c:forEach>
                                <form:errors path="registroDetalle.tipoDocumentacionFisica" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                    </div>
                </div>
            </div>


            <!-- Datos Opcionales -->
            <div class="col-xs-6">


                <div class="panel panel-danger">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="registro.datos.opcionales"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.codigoAsunto.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.codigoAsunto"/>" data-toggle="popover"><spring:message code="registroEntrada.codigoAsunto"/></label>
                            </div>
                            <div class="col-xs-10">

                                <form:select path="registroDetalle.codigoAsunto.id"  cssClass="chosen-select" >
                                    <form:option value="-1">...</form:option>
                                    <form:options items="${codigosAsunto}" itemValue="id"
                                                  itemLabel="traducciones['${pageContext.response.locale}'].nombre"/>
                                </form:select>
                            </div>
                        </div>

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.referenciaExterna" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.refExterna"/>" data-toggle="popover"><spring:message code="registroEntrada.referenciaExterna"/></label>
                            </div>
                            <div class="col-xs-4">
                                <form:input path="registroDetalle.referenciaExterna" maxlength="16" cssClass="form-control"/> <form:errors path="registroDetalle.referenciaExterna" cssClass="help-block" element="span"/>
                            </div>

                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.expediente" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.expediente"/>" data-toggle="popover"><spring:message code="registroEntrada.expediente"/></label>
                            </div>
                            <div class="col-xs-4">
                                <form:input path="registroDetalle.expediente" maxlength="80" cssClass="form-control"/> <form:errors path="registroDetalle.expediente" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.transporte" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.transporte"/>" data-toggle="popover"><spring:message code="registroEntrada.transporte"/></label>
                            </div>
                            <div class="col-xs-5">
                               <form:select path="registroDetalle.transporte" cssClass="chosen-select">
                                  <form:option value="-1">...</form:option>
                                  <c:forEach items="${transportes}" var="transporte">
                                     <form:option value="${transporte}"><spring:message code="transporte.0${transporte}" /></form:option>
                                  </c:forEach>
                               </form:select>

                                <form:errors path="registroDetalle.transporte" cssClass="help-block" element="span"/>

                            </div>
                            <div class="col-xs-1 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.numeroTransporte" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numeroTransporte"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroTransporte"/></label>
                            </div>
                            <div class="col-xs-4">
                                <form:input path="registroDetalle.numeroTransporte" maxlength="20" cssClass="form-control"/> <form:errors path="registroDetalle.numeroTransporte" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb12 control-label textEsq">
                                <label for="registroDetalle.observaciones" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.observaciones"/>" data-toggle="popover"><spring:message code="registroEntrada.observaciones"/></label>
                            </div>
                            <div class="col-xs-10">
                                <form:input path="registroDetalle.observaciones" cssClass="form-control" maxlength="50"/> <form:errors path="registroDetalle.observaciones" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                        <div class="form-group col-xs-12">

                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.oficinaOrigen.codigo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.origen"/>" data-toggle="popover"><spring:message code="registroEntrada.oficinaOrigen"/></label>
                            </div>
                            <div class="col-xs-8">
                                <form:select path="registroDetalle.oficinaOrigen.codigo" cssClass="chosen-select">
                                    <option value="-1">...</option>
                                    <c:forEach items="${oficinasOrigen}" var="oficina">
                                        <c:if test="${registroSalida.registroDetalle.oficinaOrigen.codigo != null}">
                                            <option value="${oficina.codigo}" <c:if test="${registroSalida.registroDetalle.oficinaOrigen.codigo == oficina.codigo}">selected="selected"</c:if>>${oficina.denominacion}</option>
                                        </c:if>
                                        <c:if test="${registroSalida.registroDetalle.oficinaOrigen.codigo == null}">
                                            <option value="${oficina.codigo}" <c:if test="${registroSalida.registroDetalle.oficinaOrigenExternoCodigo == oficina.codigo}">selected="selected"</c:if>>${oficina.denominacion}</option>
                                        </c:if>

                                    </c:forEach>
                                </form:select>
                                <form:errors path="registroDetalle.oficinaOrigen.codigo" cssClass="help-block" element="span"/>
                            </div>

                            <div class="col-xs-2 boto-panel">
                                <a data-toggle="modal" role="button" href="#modalBuscadorOficinaOrigen"
                                   onclick="inicializarBuscador('#codNivelAdministracionOficinaOrigen','#codComunidadAutonomaOficinaOrigen','#provinciaOficinaOrigen','#localidadOficinaOrigen',${RegwebConstantes.nivelAdminAutonomica}, ${RegwebConstantes.comunidadBaleares}, 'OficinaOrigen' );"
                                   class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></a>
                            </div>

                            <form:hidden path="registroDetalle.oficinaOrigen.denominacion"/>
                            <form:hidden path="registroDetalle.oficinaOrigenExternoDenominacion"/>

                            <!-- Fin gestión de oficina origen(se prepara en función de si es interno o externo )-->
                        </div>

                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.numeroRegistroOrigen" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numRegOrigen"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroRegistroOrigen"/></label>
                            </div>
                            <div class="col-xs-4">
                                <form:input path="registroDetalle.numeroRegistroOrigen" maxlength="20" cssClass="form-control"/> <form:errors path="registroDetalle.numeroRegistroOrigen" cssClass="help-block" element="span"/>
                            </div>

                            <div class="col-xs-1 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.fechaOrigen" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fecha"/>" data-toggle="popover"><spring:message code="registroEntrada.fecha"/></label>
                            </div>
                            <div class="col-xs-5 pad-right-15" id="fechaOrigen">
                                <div class="input-group date no-pad-right">
                                    <form:input type="text" cssClass="form-control" path="registroDetalle.fechaOrigen" maxlength="19" placeholder="dd/mm/yyyy HH:mm:ss" name="fechaOrigen"/>
                                    <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                </div>
                                <form:errors path="registroDetalle.fechaOrigen" cssClass="help-block" element="span"/>
                            </div>
                        </div>
                        <div class="form-group col-xs-12">
                            <div class="col-xs-2 pull-left etiqueta_regweb control-label textEsq">
                                <label for="registroDetalle.codigoSia" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.codigoSIA"/>" data-toggle="popover"><spring:message code="registroEntrada.codigoSIA"/></label>
                            </div>
                            <div class="col-xs-4">
                                <form:input path="registroDetalle.codigoSia" maxlength="20" cssClass="form-control"/> <form:errors path="registroDetalle.codigoSia" cssClass="help-block" element="span"/>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </form:form>

        <!-- INTERESADOS -->
        <c:if test="${empty registroSalida.id}">
            <c:set var="registro" value="${registroSalida}"/>
            <c:import url="../registro/interesados.jsp">
                <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_SALIDA}"/>
                <%--<c:param name="comunidad" value="${comunidad.codigoComunidad}"/>--%>
            </c:import>
        </c:if>

        <!-- Botonera -->
        <div class="col-xs-12">
            <button type="button" class="btn btn-warning btn-sm" onclick="doForm('#registroSalida')">
                <c:if test="${empty registroSalida.id}">
                    <spring:message code="regweb.registrar"/>
                </c:if>
                <c:if test="${not empty registroSalida.id}">
                    <spring:message code="regweb.actualizar"/>
                </c:if>
            </button>

            <button type="button" onclick="goTo('<c:url value="/registroSalida/list"/>')" class="btn btn-sm"><spring:message code="regweb.cancelar"/></button>
        </div>

        </div>

        <!-- Importamos el codigo jsp del modal del formulario para realizar la búsqueda de organismos Destino
             Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
            <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
            <c:param name="tipo" value="OrganismoOrigen"/>
        </c:import>

        <!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
             Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
            <c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
            <c:param name="tipo" value="OficinaOrigen"/>
        </c:import>

        <%--Nueva Plantilla--%>
            <c:import url="../registro/formularioPlantilla.jsp"/>

        </div>
    </div> <!-- /container -->

    <c:import url="../modulos/pie.jsp"/>

    <script type="text/javascript">
        var urlObtenerAnexo = '<c:url value="/anexo/obtenerAnexo"/>';
        var urlObtenerPlantilla = '<c:url value="/plantilla/obtenerPlantilla"/>';
    </script>

    <script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>

    <script type="text/javascript" >

        // Posicionamos el ratón en el campo indicado al cargar el modal
        $('#registroDetalle\\.extracto').focus();

        $(window).load(function() {

            // CARGA DE INTERESADOS REGISTRO ENTRADA DESDE LA SESION
            <c:if test="${empty registroSalida.id}">
            <c:import url="../registro/addInteresadosSesion.jsp">
            <c:param name="variable" value="${RegwebConstantes.SESSION_INTERESADOS_SALIDA}"/>
            </c:import>
            </c:if>

            actualizarColorTipoDocumentacion();
        });

        function actualizarLocalidad(){
            <c:url var="obtenerLocalidades" value="/rest/obtenerLocalidades" />
            actualizarSelect('${obtenerLocalidades}', '#localidad\\.id', $('#provincia\\.id option:selected').val(), $('#localidad\\.id option:selected').val(), false, false);
        }

        <!-- Pone el color que corresponde con el el Tipo de documentacion elegido -->
        function actualizarColorTipoDocumentacion(){
            var valorDocSelected = $('#registroDetalle\\.tipoDocumentacionFisica option:selected').val();
            if (valorDocSelected == ${RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}) {
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find("span").removeClass("text-verd");
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find("span").removeClass("text-taronja");
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find("span").addClass("text-vermell");
            }
            if (valorDocSelected == ${RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}) {
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find('span').removeClass("text-vermell");
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find('span').removeClass("text-verd");
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find('span').addClass("text-taronja");
            }
            if (valorDocSelected == ${RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}) {
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find('span').removeClass("text-vermell");
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find('span').removeClass("text-taronja");
                $("#registroDetalle_tipoDocumentacionFisica_chosen").find('span').addClass("text-verd");
            }
        }

        window.onbeforeunload = OnBeforeUnLoad;
        function OnBeforeUnLoad () {
            return "<spring:message code="regweb.cerrar.pagina"/>";
        }

        // Posicionamos el ratón en el campo indicado al cargar el modal
        $('#modalBuscadorOrganismoDestino').on('shown.bs.modal', function () {
            $('#denominacionOrganismoDestino').focus();
        });
        $('#modalBuscadorOficinaOrigen').on('shown.bs.modal', function () {
            $('#denominacionOficinaOrigen').focus();
        });

    </script>

    <script type="text/javascript" src="<c:url value="/js/plantilla.js"/>"></script>

</body>
</html>