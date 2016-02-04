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
                    <%--<li><a href="<c:url value="/registroEntrada/list"/>" ><i class="fa fa-list"></i> <spring:message code="registroEntrada.listado"/></a></li>--%>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                 </ol>
             </div>
        </div><!-- Fin miga de pan -->

        <div class="row">

            <div class="col-xs-4">

                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <dl class="detalle_registro">
                            <dt><i class="fa fa-globe"></i> <spring:message code="entidad.entidad"/>: </dt> <dd> ${registro.oficina.organismoResponsable.entidad.nombre}</dd>
                            <dt><i class="fa fa-briefcase"></i> <spring:message code="registroEntrada.oficina"/>: </dt> <dd> ${registro.oficina.denominacion}</dd>
                            <dt><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>: </dt> <dd> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            <dt><i class="fa fa-book"></i> <spring:message code="libro.libro"/>: </dt> <dd> ${registro.libro.nombre}</dd>
                            <dt><i class="fa fa-bookmark"></i> <spring:message code="registroEntrada.estado"/>: </dt>
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
                            <c:if test="${not empty registro.registroDetalle.reserva}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.reserva"/>: </dt> <dd> ${registro.registroDetalle.reserva}</dd></c:if>
                            <c:if test="${not empty registro.destino}"> <dt><i class="fa fa-exchange"></i> <spring:message code="registroEntrada.organismoDestino"/>: </dt> <dd>${registro.destino.denominacion}</dd></c:if>
                            <c:if test="${not empty registro.destinoExternoCodigo}"> <dt><i class="fa fa-exchange"></i> <spring:message code="registroEntrada.organismoDestino"/>: </dt> <dd>${registro.destinoExternoDenominacion}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.extracto}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.extracto"/>: </dt> <dd> ${registro.registroDetalle.extracto}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.tipoDocumentacionFisica}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.tipoDocumentacionFisica"/>: </dt> <dd> <spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}" /></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.tipoAsunto}"><dt><i class="fa fa-thumb-tack"></i> <spring:message code="tipoAsunto.tipoAsunto"/>: </dt> <dd> <i:trad value="${registro.registroDetalle.tipoAsunto}" property="nombre"/></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.idioma}">
                                <dt><i class="fa fa-bullhorn"></i> 
                                <spring:message code="registroEntrada.idioma"/>:
                                </dt> <dd> <spring:message code="idioma.${registro.registroDetalle.idioma}"/></dd>
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
                                                <button type="button" class="btn btn-warning btn-sm btn-block" onclick="imprimirRecibo('<c:url value="/modeloRecibo/${registro.id}/RE/imprimir/"/>')"><spring:message code="modeloRecibo.imprimir"/></button>
                                            </div>
                                        </div>
                                    </form:form>
                                </c:if>
                                <c:if test="${fn:length(modelosRecibo) == 1}">
                                    <button type="button" class="btn btn-warning btn-sm btn-block" onclick="goTo('<c:url value="/modeloRecibo/${registro.id}/RE/imprimir/${modelosRecibo[0].id}"/>')"><spring:message code="modeloRecibo.imprimir"/></button>
                                </c:if>

                                <button type="button" data-toggle="modal" data-target="#selloModal" class="btn btn-warning btn-sm btn-block"><spring:message code="sello.imprimir"/></button>
                            </c:if>

                            <%--Si el registro es válido--%>
                            <c:if test="${registro.estado == RegwebConstantes.ESTADO_VALIDO && puedeEditar && registro.destino != null && isOficioRemision == false}">
                                <%-- <button type="button"  onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/tramitar"/>","<spring:message code="regweb.confirmar.tramitar" htmlEscape="true"/>")' class="btn btn-success btn-sm btn-block"><spring:message code="regweb.distribuir"/></button>--%>
                                <button type="button" onclick='javascript:distribuir("<c:url
                                        value="/registroEntrada/${registro.id}/tramitar"/>")'
                                        class="btn btn-success btn-sm btn-block"><spring:message
                                        code="regweb.distribuir"/></button>
                                <%--<button type="button" data-toggle="modal" data-target="#distribuirModal" class="btn btn-success btn-sm btn-block"><spring:message code="regweb.distribuir"/></button>--%>

                            </c:if>

                            <%--Si el registro está anulado--%>
                            <c:if test="${registro.estado == RegwebConstantes.ESTADO_ANULADO && puedeEditar}">
                                <button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' class="btn btn-primary btn-sm btn-block"><spring:message code="regweb.activar"/></button>
                            </c:if>

                            <%--Si el registro está pendiente de visar--%>
                            <c:if test="${registro.estado == RegwebConstantes.ESTADO_PENDIENTE_VISAR && isAdministradorLibro}">
                                <button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/visar"/>","<spring:message code="regweb.confirmar.visar" htmlEscape="true"/>")' class="btn btn-success btn-sm btn-block"><spring:message code="regweb.visar"/></button>
                            </c:if>

                            <%--Si el registro está pendiente--%>
                            <c:if test="${(registro.estado == RegwebConstantes.ESTADO_VALIDO || registro.estado == RegwebConstantes.ESTADO_PENDIENTE || registro.estado == RegwebConstantes.ESTADO_PENDIENTE_VISAR) && puedeEditar}">
                                <button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' class="btn btn-danger btn-sm btn-block"><spring:message code="regweb.anular"/></button>
                            </c:if>


                            <c:if test="${(registro.estado == RegwebConstantes.ESTADO_VALIDO || registro.estado == RegwebConstantes.ESTADO_PENDIENTE) && puedeEditar}">
                                <button type="button" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/edit"/>')" class="btn btn-warning btn-sm btn-block"><spring:message code="registroEntrada.editar"/></button>
                            </c:if>
                        </div>
                    </c:if>


                    <div class="panel-footer">
                        <c:if test="${registro.estado != RegwebConstantes.ESTADO_PENDIENTE}">
                            <button type="button" onclick="goTo('/regweb3/registroEntrada/new')" class="btn btn-info btn-sm btn-block"><spring:message code="registroEntrada.nuevo"/></button>
                        </c:if>

                        <c:if test="${registro.estado == RegwebConstantes.ESTADO_PENDIENTE}">
                            <button type="button" onclick="goTo('/regweb3/registroEntrada/reserva')" class="btn btn-info btn-sm btn-block"><spring:message code="registroEntrada.reserva.nuevo"/></button>
                        </c:if>
                    </div>

                </div>

            </div>

            <div class="col-xs-8 pull-right" >
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <!-- ANEXOS -->
            <c:if test="${(registro.estado == 1 || registro.estado == 3) && registro.registroDetalle.tipoDocumentacionFisica != 4}">
                <c:import url="../registro/anexos.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

             <%--INTERESADOS--%>
            <c:if test="${registro.estado == 1 && oficinaRegistral}">
                <c:import url="../registro/interesados.jsp">
                    <c:param name="tipo" value="detalle"/>
                    <c:param name="tipoRegistro" value="entrada"/>
                    <c:param name="comunidad" value="${comunidad.codigoComunidad}"/>
                </c:import>
            </c:if>

            <%--INTERESADOS SOLO LECTURA--%>
            <c:if test="${(registro.estado != 1 && registro.estado != 2) || !oficinaRegistral}">
                <c:import url="../registro/interesadosLectura.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

            <!-- MODIFICACIONES REGISTRO -->
            <c:if test="${not empty historicos}">
                <c:import url="../registro/modificaciones.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

            <%--Trazabilidad--%>
            <c:if test="${not empty trazabilidades}">
                <c:import url="../registro/trazabilidadEntrada.jsp"/>
            </c:if>

        </div>
        </div><!-- /div.row-->

    <%--SELLO --%>
    <c:import url="../registro/sello.jsp">
        <c:param name="tipoRegistro" value="registroEntrada"/>
    </c:import>

    <%-- MODAL TRAMITAR--%>
    <c:import url="../registro/registroTramitar.jsp">
        <c:param name="tipoRegistro" value="registroEntrada"/>
    </c:import>

    <%-- MODAL DISTRIBUIR--%>
    <c:import url="../modalDistribuir.jsp"/>

</div>


<c:import url="../modulos/pie.jsp"/>


<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
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

    /*
     * Función que permite distribuir el registro a los destinatarios que se le indiquen.
     * Realiza una llamada ajax para obtener los destinatarios
     * si son modificables muestra el pop up para poder modificarlos
     * si no lo son redirecciona directamente a los destinatarios devueltos.
     * si no hay destinatarios se marca el registro como tramitado y listo.
     */
    function distribuir(url) {
        var html = '';
        jQuery.ajax({
            async: true,
            url: url,
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if (result.propuestos != null) { // Si hay destinatarios
                    //Si no es modificable se distribuye directamente a la lista de propuestos
                    if (!result.modificable) {
                        //enviar destinatarios directamente sin popup

                        //Pintamos los destinatarios
                        var htmlul = "";

                        // $('#modalDistribDestinatarios').modal('show');
                        var destinatarios = [];
                        var destinatariosarray = "";
                        var lenpropuestos = result.propuestos.length;
                        for (var j = 0; j < lenpropuestos; j++) {
                            destinatariosarray = "[";
                            destinatarios[j] = '{"id":"' + result.propuestos[j].id + '","name":"' + result.propuestos[j].name + '"}';

                            htmlul += "<li>" + result.propuestos[j].name + "</li>";
                            // Colocamos la coma de separación
                            if (j != 0 && j < lenpropuestos) {
                                destinatariosarray += ",";
                            }
                            destinatariosarray += destinatarios[j];
                        }
                        $('#listadestin').html(htmlul);
                        $('#modalDistribDestinatarios').modal('show');
                        destinatariosarray += "]";
                        var json = '{"destinatarios":' + destinatariosarray + ',"observaciones":""}';

                        jQuery.ajax({
                            url: '<c:url value="/registroEntrada/${registro.id}/enviardestinatarios"/>',
                            type: 'POST',
                            dataType: 'json',
                            data: json,
                            contentType: 'application/json',
                            success: function (resultado) {
                                // $('#modalDistribDestinatarios').modal('hide');
                                goTo('<c:url value="/registroEntrada/${registro.id}/detalle"/>');

                            }
                        });
                    } else { // Si es modificable mostramos el pop up para que cambien la lista de propuestos

                        //Rellenamos el select de posibles
                        var lenposibles = result.posibles.length;
                        for (var i = 0; i < lenposibles; i++)
                            html += '<option value="' + result.posibles[i].id + '">'
                                    + result.posibles[i].name + '</option>';
                        $('#posibles').trigger("chosen:updated");
                        $('#posibles').html(html);


                        //Rellenamos el select de propuestos
                        html = '';
                        var lenpropuestos = result.propuestos.length;
                        for (var j = 0; j < lenpropuestos; j++)
                            html += '<option value="' + result.propuestos[j].id + '">'
                                    + result.propuestos[j].name + '</option>';
                        $('#propuestos').html(html);
                        $('#distribuirModal').modal('show');
                    }
                } else { // No hay destinatarios, se marca como tramitado
                    $('#divlistdestinatarios').hide()
                    $('#modalDistribDestinatarios').modal('show');
                    goTo('<c:url value="/registroEntrada/${registro.id}/detalle"/>');
                }
            }
        });
    }

    /**
     * Función que recoge la lista de destinatarios propuestos y modificados por el usuario y los distribuye.
     */
    function enviarDestinatarios(url) {

        $('#distribuirModal').modal('hide');
        var html = "";
        var destinatarios = [];
        var destinatariosarray = "";

        //Seleccionamos todos por defecto y así el se enviaran todos, que es el comportamiento normal.
        $('#propuestos option').prop('selected', true);
        // Coegemos los destinatarios que han seleccionado en el combo "propuestos"
        if ($('#propuestos :selected').length > 0) {
            //build an array of selected values
            destinatariosarray = "[";
            $('#propuestos :selected').each(function (i, selected) {
                html += "<li>" + $(selected).text() + "</li>";
                destinatarios[i] = '{"id":"' + $(selected).val() + '","name":"' + $(selected).text() + '"}';
                // Colocamos la coma de separación
                if (i != 0 && i < $('#propuestos :selected').length) {
                    destinatariosarray += ",";
                }
                destinatariosarray += destinatarios[i];
            });
        }
        destinatariosarray += "]";

        //Pintamos los destinatarios escogidos
        $('#listadestin').html(html);
        $('#modalDistribDestinatarios').modal('show');

        // var destinatarios = [{"id":"a","name":"shail1"}, {"id":"b","name":"shail2"}];
        //var destinatario = {"id":"a","name":"shail1"};

        var observ = $('#observtramit').val();


        /* HAY que montar el string manual(no se porque), pero funciona */
        var json = '{"destinatarios":' + destinatariosarray + ',"observaciones":"' + observ + '"}';


        jQuery.ajax({
            url: url,
            type: 'POST',
            dataType: 'json',
            data: json,
            contentType: 'application/json',
            success: function (result) {
                goTo('<c:url value="/registroEntrada/${registro.id}/detalle"/>');

            }
        });
    }

</script>


</body>
</html>