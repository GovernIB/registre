<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>
<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">
    
        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <%--<li><a href="<c:url value="/registroSalida/list"/>" ><i class="fa fa-list"></i> <spring:message code="registroSalida.listado"/></a></li>--%>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}</li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->
    
        <div class="row">
        
            <div class="col-xs-4">
            
                <div class="panel panel-danger">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">
            
                        <dl class="detalle_registro">
                            <dt><i class="fa fa-globe"></i> <spring:message code="entidad.entidad"/>: </dt> <dd> ${registro.oficina.organismoResponsable.entidad.nombre}</dd>
                            <dt><i class="fa fa-briefcase"></i> <spring:message code="registroSalida.oficina"/>: </dt> <dd> ${registro.oficina.denominacion}</dd>
                            <dt><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>: </dt> <dd> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            <dt><i class="fa fa-book"></i> <spring:message code="libro.libro"/>: </dt> <dd> ${registro.libro.nombre}</dd>
                            <dt><i class="fa fa-bookmark"></i> <spring:message code="registroSalida.estado"/>: </dt>
                            <dd>
                                <c:choose>
                                    <c:when test="${registro.estado == RegwebConstantes.ESTADO_VALIDO}">
                                        <span class="label label-success"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.ESTADO_PENDIENTE}">
                                        <span class="label label-warning"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.ESTADO_PENDIENTE_VISAR}">
                                        <span class="label label-info"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.ESTADO_OFICIO_EXTERNO || registro.estado == RegwebConstantes.ESTADO_OFICIO_INTERNO}">
                                        <span class="label label-default"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.ESTADO_ENVIADO}">
                                        <span class="label label-primary"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.ESTADO_TRAMITADO}">
                                        <span class="label label-primary"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.ESTADO_ANULADO}">
                                        <span class="label label-danger"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
            
                                </c:choose>
                            </dd>
                            <c:if test="${not empty registro.origen}"> <dt><i class="fa fa-exchange"></i> <spring:message code="registroSalida.origen"/>: </dt> <dd>${registro.origen.denominacion}</dd></c:if>
                            <c:if test="${not empty registro.origenExternoCodigo}"> <dt><i class="fa fa-exchange"></i> <spring:message code="registroSalida.origen"/>: </dt> <dd>${registro.origenExternoDenominacion}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.extracto}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.extracto"/>: </dt> <dd> ${registro.registroDetalle.extracto}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.tipoDocumentacionFisica}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.tipoDocumentacionFisica"/>: </dt> <dd> <spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}" /></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.tipoAsunto}"><dt><i class="fa fa-thumb-tack"></i> <spring:message code="tipoAsunto.tipoAsunto"/>: </dt> <dd> <i:trad value="${registro.registroDetalle.tipoAsunto}" property="nombre"/></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.idioma}">
                                 <dt><i class="fa fa-bullhorn"></i>
                                  <spring:message code="registroEntrada.idioma"/>:
                                  </dt>
                                  <dd> <spring:message code="idioma.${registro.registroDetalle.idioma}" /></dd>
                            </c:if>
                            <c:if test="${not empty registro.registroDetalle.codigoAsunto}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="codigoAsunto.codigoAsunto"/>: </dt> <dd> <i:trad value="${registro.registroDetalle.codigoAsunto}" property="nombre"/></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.referenciaExterna}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="registroEntrada.referenciaExterna"/>: </dt> <dd> ${registro.registroDetalle.referenciaExterna}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.expediente}"> <dt><i class="fa fa-newspaper-o"></i> <spring:message code="registroEntrada.expediente"/>: </dt> <dd> ${registro.registroDetalle.expediente}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.transporte}"> <dt><i class="fa fa-bus"></i> <spring:message code="registroEntrada.transporte"/>: </dt> <dd> <spring:message code="transporte.${registro.registroDetalle.transporte}"/> ${registro.registroDetalle.numeroTransporte}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.oficinaOrigen}"> <dt><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficinaOrigen"/>: </dt> <dd> ${registro.registroDetalle.oficinaOrigen.denominacion} </dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.oficinaOrigenExternoCodigo}"> <dt><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficinaOrigen"/>: </dt> <dd> ${registro.registroDetalle.oficinaOrigenExternoDenominacion}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.numeroRegistroOrigen}"> <dt><i class="fa fa-barcode"></i> <spring:message code="registroEntrada.numeroRegistroOrigen"/>: </dt> <dd> ${registro.registroDetalle.numeroRegistroOrigen}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.fechaOrigen}"> <dt><i class="fa fa-clock-o"></i> <spring:message code="registroEntrada.fechaOrigen"/>: </dt> <dd> <fmt:formatDate value="${registro.registroDetalle.fechaOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.observaciones}"> <dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.observaciones"/>: </dt> <dd> ${registro.registroDetalle.observaciones}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.expone}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.expone"/>: </dt> <dd> ${registro.registroDetalle.expone}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.solicita}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.solicita"/>: </dt> <dd> ${registro.registroDetalle.solicita}</dd></c:if>
                            <dt><i class="fa fa-gears"></i> <spring:message code="registroEntrada.aplicacion"/>: </dt> <dd> ${registro.registroDetalle.aplicacion} ${registro.registroDetalle.version}</dd>
            
                        </dl>
            
                    </div>

                    <%--Si no nos encontramos en la misma Oficia en la que se creó el Registro o en su Oficina Responsable, no podemos hacer nada con el--%>
                    <c:if test="${oficinaRegistral}">

                        <div class="panel-footer">  <%--Botonera--%>
                            <%--Si el resgistro no está pendiente de visar o anulado--%>
                            <c:if test="${registro.estado != 3 && registro.estado != 8}">
                                <c:if test="${fn:length(modelosRecibo) > 1}">
                                    <form:form modelAttribute="modeloRecibo" method="post" cssClass="form-horizontal">
                                        <div class="col-xs-12 btn-block">
                                            <div class="col-xs-6 no-pad-lateral list-group-item-heading">
                                                <form:select path="id" cssClass="chosen-select">
                                                    <form:options items="${modelosRecibo}" itemValue="id" itemLabel="nombre"/>
                                                </form:select>
                                            </div>
                                            <div class="col-xs-6 no-pad-right list-group-item-heading">
                                                <button type="button" class="btn btn-warning btn-sm btn-block" onclick="imprimirRecibo('<c:url value="/modeloRecibo/${registro.id}/RS/imprimir/"/>')"><spring:message code="modeloRecibo.imprimir"/></button>
                                            </div>
                                        </div>
                                    </form:form>
                                </c:if>
                                <c:if test="${fn:length(modelosRecibo) == 1}">
                                    <button type="button" class="btn btn-warning btn-sm btn-block" onclick="goTo('<c:url value="/modeloRecibo/${registro.id}/RS/imprimir/${modelosRecibo[0].id}"/>')"><spring:message code="modeloRecibo.imprimir"/></button>
                                </c:if>

                                <button type="button" data-toggle="modal" data-target="#selloModal" class="btn btn-warning btn-sm btn-block"><spring:message code="sello.imprimir"/></button>
                            </c:if>

                            <%--Si el registro está anulado--%>
                            <c:if test="${registro.estado == RegwebConstantes.ESTADO_ANULADO && puedeEditar}">
                                <button type="button" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' class="btn btn-primary btn-sm btn-block"><spring:message code="regweb.activar"/></button>
                            </c:if>

                            <%--Si el registro está pendiente de visar--%>
                            <c:if test="${registro.estado == RegwebConstantes.ESTADO_PENDIENTE_VISAR && isAdministradorLibro}">
                                <button type="button" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/visar"/>","<spring:message code="regweb.confirmar.visar" htmlEscape="true"/>")' class="btn btn-success btn-sm btn-block"><spring:message code="regweb.visar"/></button>
                            </c:if>

                            <%--Si el registro está pendiente--%>
                            <c:if test="${(registro.estado == RegwebConstantes.ESTADO_VALIDO || registro.estado == RegwebConstantes.ESTADO_PENDIENTE || registro.estado == RegwebConstantes.ESTADO_PENDIENTE_VISAR) && puedeEditar}">
                                <button type="button" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' class="btn btn-danger btn-sm btn-block"><spring:message code="regweb.anular"/></button>
                            </c:if>


                            <%--Si el resgistro no está pendiente de visar o anulado o tramitado--%>
                            <c:if test="${(registro.estado == RegwebConstantes.ESTADO_VALIDO || registro.estado == RegwebConstantes.ESTADO_PENDIENTE) && puedeEditar}">
                                <button type="button" onclick="goTo('<c:url value="/registroSalida/${registro.id}/edit"/>')" class="btn btn-warning btn-sm btn-block"><spring:message code="registroSalida.editar"/></button>
                            </c:if>
                        </div>
                    </c:if>


                    <div class="panel-footer">
                        <button type="button" onclick="goTo('/regweb3/registroSalida/new')" class="btn btn-danger btn-sm btn-block"><spring:message code="registroSalida.nuevo"/></button>
                    </div>
            
                </div>
            
            </div>
            
            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <!-- ANEXOS -->
            <c:if test="${registro.registroDetalle.tipoDocumentacionFisica != 4}">
                <c:import url="../registro/anexos.jsp">
                    <c:param name="tipoRegistro" value="salida"/>
                </c:import>
            </c:if>

            <%--INTERESADOS--%>
            <c:if test="${registro.estado == 1 && oficinaRegistral}">
                <c:import url="../registro/interesados.jsp">
                    <c:param name="tipo" value="detalle"/>
                    <c:param name="tipoRegistro" value="salida"/>
                    <c:param name="comunidad" value="${comunidad.codigoComunidad}"/>
                </c:import>
            </c:if>

            <%--INTERESADOS SOLO LECTURA--%>
            <c:if test="${(registro.estado != 1 && registro.estado != 2) || !oficinaRegistral}">
                <c:import url="../registro/interesadosLectura.jsp">
                    <c:param name="tipoRegistro" value="salida"/>
                </c:import>
            </c:if>

            <!-- MODIFICACIONES REGISTRO -->
            <c:if test="${not empty historicos}">

                <div class="col-xs-8 pull-right">


                    <div class="panel panel-danger">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="regweb.modificaciones"/></strong></h3>
                        </div>

                        <div class="panel-body">
                            <div class="col-xs-12">
                                <div class="table-responsive">

                                    <table id="historicos" class="table table-bordered table-hover table-striped">
                                        <colgroup>
                                            <col>
                                            <c:if test="${isAdministradorLibro}"> <col> </c:if>
                                            <col>
                                            <col width="100">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="historicoEntrada.fecha"/></th>
                                            <c:if test="${isAdministradorLibro}"> <th><spring:message code="historicoEntrada.usuario"/></th> </c:if>
                                            <th><spring:message code="historicoEntrada.modificacion"/></th>
                                            <th><spring:message code="historicoEntrada.estado"/></th>
                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="historico" items="${historicos}">
                                            <tr>
                                                <td><fmt:formatDate value="${historico.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                <c:if test="${isAdministradorLibro}"> <td>${historico.usuario.nombreCompleto}</td> </c:if>
                                                <td>${historico.modificacion}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${historico.estado == 1}">
                                                            <span class="label label-success"><spring:message code="registro.estado.${historico.estado}" /></span>
                                                        </c:when>
                                                        <c:when test="${historico.estado == 2}">
                                                            <span class="label label-warning"><spring:message code="registro.estado.${historico.estado}" /></span>
                                                        </c:when>
                                                        <c:when test="${historico.estado == 3}">
                                                            <span class="label label-info"><spring:message code="registro.estado.${historico.estado}" /></span>
                                                        </c:when>
                                                        <c:when test="${historico.estado == 4 || registro.estado == 5}">
                                                            <span class="label label-default"><spring:message code="registro.estado.${historico.estado}" /></span>
                                                        </c:when>
                                                        <c:when test="${historico.estado == 6}">
                                                            <span class="label label-primary"><spring:message code="registro.estado.${historico.estado}" /></span>
                                                        </c:when>
                                                        <c:when test="${historico.estado == 7}">
                                                            <span class="label label-primary"><spring:message code="registro.estado.${historico.estado}" /></span>
                                                        </c:when>
                                                        <c:when test="${historico.estado == 8}">
                                                            <span class="label label-danger"><spring:message code="registro.estado.${historico.estado}" /></span>
                                                        </c:when>
                                                    </c:choose>

                                                </td>
                                                <td class="center">
                                                    <c:if test="${not empty historico.registroSalidaOriginal}">
                                                        <a data-toggle="modal" role="button" href="#modalCompararRegistros" onclick="comparaRegistros('${historico.id}')" class="btn btn-warning btn-sm">Comparar</a>
                                                    </c:if>
                                                    <c:if test="${empty historico.registroSalidaOriginal}">
                                                        <a href="javascript:void(0);" class="btn btn-warning disabled btn-sm">Comparar</a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <c:import url="comparaRegistros.jsp"/>

            </c:if>
            <!-- Fin modificaciones -->

        
            <%--Trazabilidad--%>
            <c:if test="${not empty trazabilidades}">
            
                <div class="col-xs-8 col-xs-offset pull-right">
                
                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-clock-o fa-fw"></i> <strong><spring:message code="registroEntrada.trazabilidad"/></strong></h3>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <ul class="timeline">
                
                                <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="loopStatus">
                
                                    <li> <%--REGISTRO ENTRADA ORIGEN--%>
                                        <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
                
                                        <div class="timeline-panel">
                                            <div class="timeline-heading">
                                                <h4 class="timeline-title">
                                                    <a href="<c:url value="/registroEntrada/${trazabilidad.registroEntradaOrigen.id}/detalle"/>">
                                                        <spring:message code="registroEntrada.registroEntrada"/> ${trazabilidad.registroEntradaOrigen.numeroRegistroFormateado}
                                                    </a>
                                                </h4>
                                                <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.registroEntradaOrigen.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                            </div>
                                            <div class="timeline-body">
                                                <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${trazabilidad.registroEntradaOrigen.oficina.denominacion}</small></p>
                                            </div>
                                        </div>
                                    </li>
                
                                    <li> <%--OFICIO REMISION--%>
                                        <div class="timeline-badge success"><i class="fa fa-envelope-o"></i></div>
                
                                        <div class="timeline-panel">
                                            <div class="timeline-heading">
                                                <h4 class="timeline-title">
                                                    <a href="<c:url value="/oficioRemision/${trazabilidad.oficioRemision.id}/detalle"/>"><spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${trazabilidad.oficioRemision.fecha}" pattern="yyyy"/> / ${trazabilidad.oficioRemision.numeroOficio}</a>
                                                </h4>
                                                <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                            </div>
                                            <div class="timeline-body">
                
                                                <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.organismoDestino"/>:</strong> ${trazabilidad.oficioRemision.organismoDestinatario}</small></p>
                
                                                <p>
                                                    <small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong>
                                                          <span class="label ${(trazabilidad.oficioRemision.estado == 2)?'label-success':'label-danger'}">
                                                          <spring:message code="oficioRemision.estado.${trazabilidad.oficioRemision.estado}"/>
                                                          <c:if test="${not empty trazabilidad.oficioRemision.fechaEstado}">
                                                              - <fmt:formatDate value="${trazabilidad.oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                          </c:if>
                                                        </span>
                                                    </small>
                                                </p>
                
                                            </div>
                                        </div>
                                    </li>
                
                                    <li> <%--REGISTRO SALIDA--%>
                                        <div class="timeline-badge danger"><i class="fa fa-external-link"></i></div>
                                        <div class="timeline-panel timeline-panel-activo-rs">
                                            <div class="timeline-heading">
                                                <h4 class="timeline-title">
                                                    <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}
                                                </h4>
                                                <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                            </div>
                                            <div class="timeline-body">
                                                <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroSalida.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                                            </div>
                                        </div>
                                    </li>
                
                                </c:forEach>
                
                            </ul>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
            
            </div>
        </c:if>
    
    </div><!-- /div.row-->

    <%--SELLO --%>
    <c:import url="../registro/sello.jsp">
        <c:param name="tipoRegistro" value="registroSalida"/>
    </c:import>

</div>

<c:import url="../modulos/pie.jsp"/>

<%--<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>--%>
<script type="text/javascript" src="<c:url value="/js/sello.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/repro.js"/>"></script>

<script type="text/javascript">

    // Muestra los datos del hitórico seleccionado y oculta el resto
    function comparaRegistros(idHistorico){

        <c:forEach var="historico" items="${historicos}">
        $('#'+${historico.id}).hide();
        </c:forEach>
        var elemento = '#'+idHistorico;
        $(elemento).show();
    }

    function actualizarLocalidad(){
        <c:url var="obtenerLocalidades" value="/registroEntrada/obtenerLocalidades" />
        actualizarSelect('${obtenerLocalidades}','#localidad\\.id',$('#provincia\\.id option:selected').val(),$('#localidad\\.id option:selected').val(),false,false);
    }

</script>

</body>
</html>