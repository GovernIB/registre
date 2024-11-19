<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

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

            <div class="row">
                <div class="col-xs-12">
                    <ol class="breadcrumb">
                        <c:import url="../modulos/migadepan.jsp"/>
                        <li class="active"><i class="fa fa-envelope"></i> <strong><spring:message code="remesa.buscador"/></strong></li>
                    </ol>
                </div>
            </div><!-- /.row -->

            <c:import url="../modulos/mensajes.jsp"/>
            <div id="mensajes"></div>

			<!-- LISTADO -->
	        <div class="row">
	
	            <div class="col-xs-12">
	
					<!-- BUSCADOR -->
	                <div class="panel panel-info">
	
	                    <c:url value="/remesa/busqueda" var="urlBusqueda" scope="request"/>
	                    <!--  con esta opcion tambien funciona  pero depende de  javascript onsubmit="document.charset = 'ISO-8859-1'"-->
	                     <form:form modelAttribute="remesaBusqueda" action="${urlBusqueda}"  method="get" cssClass="form-horizontal">
	
	                        <form:hidden path="pageNumber"/>
	
	                        <div class="panel-body">
	                        
		                        <div class="col-xs-12">
		                            <div class="col-xs-6 espaiLinies">
		                                <div class="col-xs-4 pull-left etiqueta_regweb">
		                                    <label for="remesa.concepto" rel="popupAbajo" data-content="<spring:message code="remesa.concepto"/>" data-toggle="popover"><spring:message code="remesa.concepto"/></label>
		                                </div>
		                                <div class="col-xs-8">
		                                    <form:input path="remesa.concepto" cssClass="form-control"/>
		                                </div>
		                            </div>
		                            
		                         	<div class="col-xs-6 espaiLinies">
		                                <div class="col-xs-4 pull-left etiqueta_regweb">
		                                    <label for="emisor" rel="popupAbajo" data-content="<spring:message code="remesa.emisor"/>" data-toggle="popover"><spring:message code="remesa.emisor"/></label>
		                                </div>
		                                <div class="col-xs-8">
		                                    <form:input path="emisor" cssClass="form-control"/>
		                                </div>
		                            </div>
								</div>
								
								<div class="col-xs-12">
		                            
		                            <div class="col-xs-6 espaiLinies">
		                                <div class="col-xs-4 pull-left etiqueta_regweb">
		                                    <label for="fechaPuestaDisposicionDesde" rel="popupAbajo" data-content="<spring:message code="remesa.fecha.disposicion.desde"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="remesa.fecha.disposicion.desde"/></label>
		                                </div>
		                                <div class="col-xs-8" id="fechaPuestaDisposicionDesde">
		                                    <div class="input-group date no-pad-right">
		                                        <form:input path="fechaPuestaDisposicionDesde" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaPuestaDisposicionDesde"/>
		                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
		                                    </div>	
		                                </div>
		                            </div>
		                            
		                            <div class="col-xs-6 espaiLinies">
		                                <div class="col-xs-4 pull-left etiqueta_regweb">
		                                    <label for="fechaPuestaDisposicionHasta" rel="popupAbajo" data-content="<spring:message code="remesa.fecha.disposicion.hasta"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="remesa.fecha.disposicion.hasta"/></label>
		                                </div>
		                                <div class="col-xs-8" id="fechaPuestaDisposicionHasta">
		                                    <div class="input-group date no-pad-right">
		                                        <form:input path="fechaPuestaDisposicionHasta" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaPuestaDisposicionHasta"/>
		                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
		                                    </div>	
		                                </div>
		                            </div>
		                            
		                         </div>
		                         
		                         <div class="col-xs-12">
			                         <div class="col-xs-6 espaiLinies">
		                                <div class="col-xs-4 pull-left etiqueta_regweb">
		                                    <label for="remesa.estado" rel="popupAbajo" data-content="<spring:message code="remesa.estado"/>" data-toggle="popover"><spring:message code="remesa.estado"/></label>
		                                </div>
		                                <div class="col-xs-8">
		                                    <form:select path="remesa.estado" cssClass="chosen-select">
		                                        <form:option value="" label="..."/>
		                                        <c:forEach var="estado" items="${estados}">
		                                            <form:option value="${estado}">
		                                            	<spring:message code="remesa.list.estado.${estado}"/>
		                                            </form:option>
		                                        </c:forEach>
		                                    </form:select>
		                                </div>
		                            </div>
		                        </div>
	                        
						 	<div class="row">
	
	                            <div class="form-group col-xs-12">
	                                <div class="col-xs-1 boto-panel center">
	                                    <button type="submit" id="btnFiltrar" class="btn btn-warning btn-sm" style="margin-left: 15px;">
	                                        <spring:message code="regweb.buscar"/>
	                                    </button>
	                                </div>
	                            </div>
	
							</div>
							
							
						
						 <c:if test="${paginacion != null}">

                                <div class="form-group col-xs-12">

                                        <c:if test="${empty paginacion.listado}">
                                            <div class="alert alert-grey alert-dismissable">
                                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroEntrada.registroEntrada"/></strong>
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty paginacion.listado}">

                                            <div class="alert-grey">
                                                <c:if test="${paginacion.totalResults == 1}">
                                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                </c:if>
                                                <c:if test="${paginacion.totalResults > 1}">
                                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                </c:if>

                                                <%--Select de "Ir a página"--%>
                                                <c:import url="../modulos/paginas.jsp"/>
                                            </div>


                                        <div class="table-responsive">

                                                <table class="table table-bordered table-hover table-striped tablesorter">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                        <col>
                                                        <col width="125">
                                                    </colgroup>
                                                    <thead>
                                                        <tr>
                                                            <th class="center" width="8%"><spring:message code="remesa.list.tipo"/></th>
                                                            <th class="center"><spring:message code="remesa.list.extracto"/></th>
                                                            <th class="center" width="17%"><spring:message code="remesa.list.fecha.disposicion"/></th>
                                                            <th class="center" width="15%"><spring:message code="remesa.list.emisor"/></th>
                                                            <th class="center" width="10%"><spring:message code="remesa.list.estado"/></th>
                                                            <th class="center" width="10%"><spring:message code="remesa.list.usuario"/></th>
                                                            <th class="center" width="12%"><spring:message code="remesa.list.estado.notifica"/></th>
                                                            <th class="center" width="5%"><spring:message code="regweb.acciones"/></th>
                                                        </tr>
                                                    </thead>

                                                    <tbody>
                                                        <c:forEach var="remesa" items="${paginacion.listado}" varStatus="status">
                                                            <tr>
                                                            	<td class="hidden">${remesa.identificador}</td>
                                                                <td class="center"><spring:message code="remesa.list.tipo.${remesa.tipo}"/></td>
                                                                <td class="center">${remesa.concepto}</td>
                                                                <td class="center">
                                                                	<fmt:formatDate value="${remesa.fechaPuestaDisposicion}" pattern="dd/MM/yyyy"/>
                                                                </td>
                                                                <td class="center">${remesa.organoEmisorCodigo} - ${remesa.organoEmisorNombre}</td>
                                                                <td class="center"><spring:message code="remesa.list.estado.${remesa.estado}"/></td>
                                                                <td class="center">${remesa.usuario.nombreCompleto}</td>
                                                                <td class="center"><spring:message code="remesa.list.estado.notifica.${remesa.estadoNotifica}"/></td>
                                                                <td class="center">
                                                                	<c:choose>
                                                                		<c:when test="${remesa.estado == 'PENDIENTE'}">
                                                                			<span class="fa ${remesa.reintentosLectura == 1 ? 'fa-warning text-danger' : 'fa-envelope text-warning'}"
																					title="<spring:message code="${remesa.reintentosLectura > 0 ? 'remesa.list.reintentos.comentario' : 'remesa.list.reintentos.agotados.comentario'}"/>"> ${remesa.reintentosLectura}
																			</span>
		                                                                	 <a data-toggle="modal" role="button" href="#modalConfirmacionNotificacion"
		                                      									onclick="inicializarModalNotificacion('${remesa.identificador}', false);"
		                                      									class="btn btn-warning btn-sm ${remesa.reintentosLectura == 0 ? 'href-disabled' : ''}"
		                                      									title="<spring:message code="remesa.list.comparecer.title"/>">
		                                      									<span class="fa fa-envelope"></span>
		                                      								</a>	
                                                                		</c:when>
                                                                		<c:when test="${remesa.estado == 'REGISTRADA'}">
																			<a class="btn btn-info btn-sm"
																				href="<c:url value="/registroEntrada/${remesa.registro.id}/detalle"/>"
																				title="<spring:message code="remesa.list.registro.title"/>">
																				<span class="fa fa-eye"></span>
																			</a>
																		</c:when>
                                                                		<c:otherwise>
	                                                                		<a data-toggle="modal" role="button" href="#modalConfirmacionNotificacion"
		                                      									onclick="inicializarModalNotificacion('${remesa.identificador}', true);"
		                                      									class="btn btn-success btn-sm"
		                                      									title="<spring:message code="remesa.list.registrar.title"/>">
		                                      									<span class="fa fa-file"></span>
		                                      								</a>
                                                                		</c:otherwise>
                                                                	</c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
									        <c:import url="../remesa/modalConfirmacionNotificacion.jsp">
								               
								            </c:import>
                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="remesa"/>
                                            </c:import>

                                    </div>

                                    </c:if>

                                </div>

                            </c:if>
                            
	                        </div>
	                        
						</form:form>
					</div>
				</div>
	        </div>
        </div>
    </div> <!-- /container -->

    <c:import url="../modulos/pie.jsp"/>
    
    <script type="text/javascript" src="<c:url value="/js/notificacionProcesar.js"/>"></script>
    
    <script type="text/javascript">

		var tradRemesas = new Array();
		tradRemesas['remesa.detalle'] = "<spring:message code='remesa.modal.acceso' javaScriptEscape='true' />";
		tradRemesas['remesa.resumen'] = "<spring:message code='remesa.modal.resumen' javaScriptEscape='true' />";
		const wrapper = document.querySelector('.remesa-iframe-wrapper');
		const prevButton = document.getElementById('prev');
		const nextButton = document.getElementById('next');
		let totalDocumentos = 0;
		let currentIndex = 0;
		
	    $(document).ready(function() {
		    $('#btnLeerNotificacion').click(function() {
		    	localStorage.setItem("isLoadingRemesa", true);
		    	$('#loading').show();
		    	$('#remesa-btn-lectura').hide();
		    	
		    	var identificador = $('#identificadorNotificacion').val();

		    	$.ajax({
		            url: '<c:url value="/remesa/' + identificador + '/lectura"/>',
		            method: 'POST',
		            success: function(response) {
		            	$('#loading').hide();
			    		refrescar();
		            	
		            	cargarDetalleNotificacion(response);
		            },
		            error: function(XMLHttpRequest, textStatus, errorThrown) {
		            	$('#loading').hide();
		            	$('#remesa-btn-lectura').show();
		                $('#errorNotificacion').text('Ocurrió un error al intentar leer la notificación.').show();
		            },
		            complete: function() {
		            	localStorage.removeItem("isLoadingRemesa");
		            }
		        });
		    });

	    });
	    
	    function refrescar() {
	    	$('#modalConfirmacionNotificacion').on('hidden.bs.modal', function () {
	    		location.reload();
		    });
	    }
	    
	    function consultarRealizada() {
	    	$('#loading').show();
	    	
	    	var identificador = $('#identificadorNotificacion').val();
	    	if (identificador) {
		    	$.ajax({
		            url: '<c:url value="/remesa/' + identificador + '/consulta"/>',
		            method: 'POST',
		            success: function(response) {
		            	$('#loading').hide();
		            	
		            	cargarDetalleNotificacion(response);
		            },
		            error: function(XMLHttpRequest, textStatus, errorThrown) {
		            	$('#loading').hide();
		                $('#errorNotificacion').text('Ocurrió un error al intentar leer la notificación').show();
		            }
		        });
	    	}
	    }
	    
	    function cargarDetalleNotificacion(response) {
	    	if (response) {
	    		
                $('#remesa-detalle').html('<div class="panel-heading"><spring:message code="remesa.modal.detalle"/></div><div class="panel-body">\
                    <p><strong><spring:message code="remesa.modal.extracto"/>:</strong> ' + response.concepto + '</p>\
                    <p><strong><spring:message code="remesa.modal.fecha.disposicion"/>:</strong> ' + formatTimestamp(response.fechaPuestaDisposicion) + '</p>\
                	<p><strong><spring:message code="remesa.modal.emisor"/>:</strong> ' + response.organoEmisorCodigo + ' - ' + response.organoEmisorNombre + '</p>\
                    <p><strong><spring:message code="remesa.modal.titular"/>:</strong> ' + response.titularNif + ' - ' + response.titularNombre + '</p></div>\
                ');
                
                let documentosRecibidos = response.documentosRecibidos;
                if (documentosRecibidos) {
                	$('.badge').text(documentosRecibidos.length);
                	totalDocumentos = (documentosRecibidos.length - 1);
                	
                	documentosRecibidos.forEach(function(documento, idx) {
                		var base64 = documento.contenido;
                		
    	                // Convierte el contenido base64 a un objeto Blob
    	                const binary = atob(base64);
    	                const len = binary.length;
    	                const buffer = new Uint8Array(len);
    	                for (let i = 0; i < len; i++) {
    	                  buffer[i] = binary.charCodeAt(i);
    	                }
    	                const blob = new Blob([buffer], { type: documento.mimeType });
    	
    	                // Crea una URL para el blob y carga el PDF en el iframe
    	                const url = URL.createObjectURL(blob);
    	                
    	                const iframe = document.createElement("iframe");
    	                iframe.id = "pdfPreview_" + idx;
    	                iframe.height = "400";
    	                iframe.width = "100%";
    	                iframe.src = url;
    	                
    	                let resumenDocumento = $('<div>', { class: 'resumen-documento' });
    	                
    	                resumenDocumento.append('\
    	                			<p><strong><spring:message code="remesa.modal.documento.nombre"/>:</strong> ' + documento.nombre + '</p>\
    	                   			<p><strong><spring:message code="remesa.modal.documento.mimetype"/>:</strong> ' + documento.mimeType + '</p>');
    	                resumenDocumento.append(iframe);
    	                
    	                $('.remesa-iframe-wrapper').append(resumenDocumento);
                	})
                }
                
            	var urlRegistrar = '<c:url value="/remesa/' + response.identificador + '/registrar"/>'
            	document.getElementById("btnRegistrarNotificacion").href = urlRegistrar;
            	
            	// Mostrar detalle y documentos remesa
            	$('#remesa-resumen').show();
            	$('#remesa-btn-registrar').show();
            	$('#modalTitle').text(tradRemesas['remesa.resumen']);	
            } else {
                $('#errorNotificacion').text('Error al leer la notificación').show();
            }
	    	
	    }
	    
		function formatTimestamp(timestamp) {
			const date = new Date(timestamp);
	        const day = ('0' + date.getDate()).slice(-2);
	        const month = ('0' + (date.getMonth() + 1)).slice(-2);
	        const year = date.getFullYear();

	        return day + '/' + month + '/' + year;
		}
		
		if (prevButton && nextButton) {
			prevButton.addEventListener('click', () => {
			  if (currentIndex > 0) {
			    currentIndex--;
			    updateCarousel();
			  } else {
				  currentIndex = totalDocumentos;
				  updateCarousel();
			  }
			});
	
			nextButton.addEventListener('click', () => {
			  if (currentIndex < totalDocumentos) {
			    currentIndex++;
			    updateCarousel();
			  } else {
				  currentIndex = 0;
				  updateCarousel();
			  }
			});
		}
		
		function updateCarousel() {
		  wrapper.style.transform = 'translateX(-' + currentIndex * 100 + '%)';
		}
		
    </script>
</body>
</html>