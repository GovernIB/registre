<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div id="modalNuevoAnexo" class="modal fade bs-example-modal-lg" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarAnexo()">×</button>
                <h3 id="anexoTitulo"></h3>
            </div>

            <div class="modal-body" >


                <%-- TODO refactorizar en un futuro. Esto está en dos formularios separados porque tenemos contentTypes, datas diferentes.--%>

                <%-- Formulario que contiene solo el input file del anexo. --%>
               <%-- <form id="archivoAnexoForm" class="form-horizontal" action="${pageContext.request.contextPath}/anexo/guardarArchivo" method="post" enctype="multipart/form-data" >
                    <input type="hidden" id="nombreFicheroAnexado" name="nombreFicheroAnexado" value=""/>
                    <input type="hidden" id="nombreFirmaAnexada" name="nombreFirmaAnexada" value=""/>
                    <!--ANEXO-->
                    <div class="form-group col-xs-6">
                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="archivo"><spring:message code="anexo.archivo"/></label>
                       </div>
                       <div class="col-xs-8">
                           <div class="input-group">
                               <span class="input-group-btn">
                                   <span class="btn btn-success btn-sm btn-file">
                                       Explorar&hellip; <input id="archivo" name="archivo" type="file" multiple  maxlength="80">
                                   </span>
                               </span>
                               <span id="archivoError"></span>
                               <input type="text" class="form-control">
                           </div>

                       </div>
                    </div>

                    <div id="divArchivoActual" class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="archivo"><spring:message code="anexo.archivo.existente"/></label>
                        </div>
                        <div class="col-xs-8 arxiu_actual">
                            <a id="linkFichero" href="" target="_blank"></a>
                                <label for="borrar"><spring:message code="anexo.archivo.borrar"/></label>&nbsp;<input type="checkbox" id="borrar" name="borrar" value="false" />
                        </div>
                    </div>
                    <!--FIN ANEXO-->

                    <!--FIRMA -->
                    <div class="form-group col-xs-6" id="divInputFirma">
                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="firma"><spring:message code="anexo.firma"/></label>
                       </div>
                       <div class="col-xs-8">
                           <div class="input-group">
                               <span class="input-group-btn">
                                   <span class="btn btn-success btn-sm btn-file">
                                       Explorar&hellip; <input id="firma" name="firma" type="file" multiple  maxlength="80">
                                   </span>
                               </span>
                               <span id="firmaError"></span>
                               <input  id="textefirma" type="text" class="form-control" >
                           </div>

                       </div>
                    </div>

                    <div id="divFirmaActual" class="form-group col-xs-6">
                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                            <label for="firma"><spring:message code="anexo.firma.existente"/></label>
                        </div>
                        <div class="col-xs-8 arxiu_actual">
                            <a id="linkFirma" href="" target="_blank"></a>
                            <label for="borrarfirma" id="labelborrarfirma"><spring:message code="anexo.archivo.borrar"/></label>&nbsp;<input type="checkbox" id="borrarfirma" name="borrarfirma" value="false" />
                        </div>
                    </div>
                    <!--FIN FIRMA-->

                </form>--%>

                <%-- Formulario que contiene el resto de campos del anexo. --%>
                <form id="anexoForm" class="form-horizontal" action="${pageContext.request.contextPath}/anexo" method="post" enctype="multipart/form-data">
                        <input type="hidden" id="accion" name="accion" value="nuevo"/>
                        <input type="hidden" id="id" name="id" value=""/>
                        <input type="hidden" id="idRegistro" name="idRegistro" value="${registro.id}"/>
                        <input type="hidden" id="idRegistroDetalle" name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>
                        <input type="hidden" id="tipoRegistro" name="tipoRegistro" value="${param.registro}"/>
                        <input type="hidden" id="custodiaID" name="custodiaID" value=""/>

                        <%--<div class="form-group col-xs-6" id="divautofirma">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                  <label><spring:message code="anexo.autofirma"/></label>
                            </div>
                            <div class="col-xs-8" >
                                <label class="radio-inline">
                                    <input type="radio" id="autofirmasi" name="autofirma" value="1" checked><spring:message code="regweb.si"/>
                                </label>
                                 <label class="radio-inline">
                                    <input type="radio" id="autofirmano" name="autofirma" value="2" ><spring:message code="regweb.no"/>
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" id="sinfirma" name="autofirma" value="0" checked><spring:message code="anexo.sinfirma"/>
                                </label>
                            </div>

                        </div>--%>

                        <div class="form-group col-xs-6">
                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="titulo"><span class="text-danger">*</span>
                                <spring:message code="anexo.titulo"/></label>
                           </div>
                           <div class="col-xs-8">
                               <input id="titulo"  class="form-control"  maxlength="200"/>
                               <span id="tituloError"></span>
                           </div>
                        </div>


                        <div class="form-group col-xs-6">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="validezDocumento"><spring:message code="anexo.validezDocumento"/></label>
                            </div>
                            <div class="col-xs-8">
                                <select id="validezDocumento" name="validezDocumento" class="chosen-select" onchange="bloquearFirma('${registro.id}','${registro.registroDetalle.id}','${param.registro}')">
                                  <option value="-1">...</option>
                                    <c:forEach items="${tiposValidezDocumento}" var="validezDocumento">
                                        <option value="${validezDocumento}"><spring:message code="tipoValidezDocumento.${validezDocumento}"/></option>
                                    </c:forEach>
                                </select>
                                <span id="validezDocumentoError"></span>
                            </div>
                        </div>

                        <div class="form-group col-xs-6">
                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                               <label for="tipoDocumento"><span class="text-danger">*</span> <spring:message code="anexo.tipoDocumento"/></label>
                           </div>
                           <div class="col-xs-8">
                               <select id="tipoDocumento" name="tipoDocumento" class="chosen-select">
                                   <c:forEach items="${tiposDocumentoAnexo}" var="tipoDocumento">
                                       <option value="${tipoDocumento}"><spring:message code="tipoDocumento.${tipoDocumento}"/></option>
                                   </c:forEach>
                               </select>
                               <span id="tipoDocumentoError"></span>
                           </div>
                        </div>


                        <div class="form-group col-xs-6">
                           <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                <label for="observacionesAnexo"><spring:message code="anexo.observaciones"/></label>
                           </div>
                           <div class="col-xs-8">
                               <textarea id="observacionesAnexo" name="observaciones" class="form-control" rows="2"  maxlength="50"></textarea>
                               <span id="observacionesAnexoError"></span>
                           </div>
                        </div>


                       <div class="form-group col-xs-6">
                         <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                             <label for="origenCiudadanoAdmin"><span class="text-danger">*</span> <spring:message code="anexo.origen"/></label>
                         </div>
                         <div class="col-xs-8">
                             <select id="origenCiudadanoAdmin" name="origenCiudadanoAdmin" class="chosen-select">
                                 <option value="0"><spring:message code="anexo.origen.ciudadano"/></option>
                                 <option value="1"><spring:message code="anexo.origen.administracion"/></option>
                             </select>
                             <span id="origenCiudadanoAdminError"></span>
                         </div>
                       </div>

                       <div class="form-group col-xs-6">
                          <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                              <label for="tipoDocumental"><span class="text-danger">*</span> <spring:message code="anexo.tipoDocumental"/></label>
                          </div>
                          <div class="col-xs-8">
                              <select id="tipoDocumental"  name="tipoDocumental" class="chosen-select">
                                 <c:forEach items="${tiposDocumental}" var="tipoDocumental">
                                     <option value="${tipoDocumental.id}"><i:trad value="${tipoDocumental}" property="nombre"/></option>
                                 </c:forEach>
                              </select>
                              <span id="tipoDocumentalError"></span>
                          </div>
                       </div>

                       <div class="form-group col-xs-6" id="divautofirma">
                            <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                  <label><spring:message code="anexo.autofirma"/></label>
                            </div>
                            <div class="col-xs-8" >
                                <label class="radio-inline">
                                    <input type="radio" id="autofirmasi" name="autofirma" value="1" checked><spring:message code="regweb.si"/>
                                </label>
                                 <label class="radio-inline">
                                    <input type="radio" id="autofirmano" name="autofirma" value="2" ><spring:message code="regweb.no"/>
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" id="sinfirma" name="autofirma" value="0" checked><spring:message code="anexo.sinfirma"/>
                                </label>
                            </div>

                        </div>

                        <div class="clearfix"></div>
                </form>
            </div>
            	 <%-- TODO refactorizar en un futuro. Esto está en dos formularios separados porque tenemos contentTypes, datas diferentes.--%>

			<c:if test="${teScan}">
			<ul class="nav nav-tabs" id="pestanyes">
				<li class="active"><a href="#fitxer" data-toggle="tab">Fitxer</a></li>
				<li><a href="#scan" data-toggle="tab">Scan</a></li>
			</ul>
						
			<div class="tab-content">
				<div class="tab-pane active" id="fitxer">
			</c:if>
				
	                <%-- Formulario que contiene solo el input file del anexo. --%>
	                <form id="archivoAnexoForm" class="form-horizontal" action="${pageContext.request.contextPath}/anexo/guardarArchivo" method="post" enctype="multipart/form-data" >
	                    <input type="hidden" id="nombreFicheroAnexado" name="nombreFicheroAnexado" value=""/>
	                    <input type="hidden" id="nombreFirmaAnexada" name="nombreFirmaAnexada" value=""/>
	                    <!--ANEXO-->
	                    <div class="form-group col-xs-6">
	                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
	                            <label for="archivo"><spring:message code="anexo.archivo"/></label>
	                       </div>
	                       <div class="col-xs-8">
	                           <div class="input-group">
	                               <span class="input-group-btn">
	                                   <span class="btn btn-success btn-sm btn-file">
	                                       Explorar&hellip; <input id="archivo" name="archivo" type="file" multiple  maxlength="80">
	                                   </span>
	                               </span>
	                               <span id="archivoError"></span>
	                               <input type="text" class="form-control">
	                           </div>
	
	                       </div>
	                    </div>
	
	                    <div id="divArchivoActual" class="form-group col-xs-6">
	                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
	                            <label for="archivo"><spring:message code="anexo.archivo.existente"/></label>
	                        </div>
	                        <div class="col-xs-8 arxiu_actual">
	                            <a id="linkFichero" href="" target="_blank"></a>
	                                <label for="borrar"><spring:message code="anexo.archivo.borrar"/></label>&nbsp;<input type="checkbox" id="borrar" name="borrar" value="false" />
	                        </div>
	                    </div>
	                    <!--FIN ANEXO-->
	
	                    <!--FIRMA -->
	                    <div class="form-group col-xs-6" id="divInputFirma">
	                       <div class="col-xs-4 pull-left etiqueta_regweb control-label">
	                            <label for="firma"><spring:message code="anexo.firma"/></label>
	                       </div>
	                       <div class="col-xs-8">
	                           <div class="input-group">
	                               <span class="input-group-btn">
	                                   <span class="btn btn-success btn-sm btn-file">
	                                       Explorar&hellip; <input id="firma" name="firma" type="file" multiple  maxlength="80">
	                                   </span>
	                               </span>
	                               <span id="firmaError"></span>
	                               <input  id="textefirma" type="text" class="form-control" >
	                           </div>
	
	                       </div>
	                    </div>
	
	                    <div id="divFirmaActual" class="form-group col-xs-6">
	                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
	                            <label for="firma"><spring:message code="anexo.firma.existente"/></label>
	                        </div>
	                        <div class="col-xs-8 arxiu_actual">
	                            <a id="linkFirma" href="" target="_blank"></a>
	                            <label for="borrarfirma" id="labelborrarfirma"><spring:message code="anexo.archivo.borrar"/></label>&nbsp;<input type="checkbox" id="borrarfirma" name="borrarfirma" value="false" />
	                        </div>
	                    </div>
	
	                    <div class="clearfix"></div>
	                    <!--FIN FIRMA-->
	
	
		                <div class="col-xs-12 text-center centrat" id="reload">
		                        <img src="<c:url value="/img/712.GIF"/>" width="20" height="20"/>
		                </div>
		            </form>
       		<c:if test="${teScan}">
       			</div>
				<div class="tab-pane" id="scan">
					${coreScan}
				</div>
			</div>
			</c:if>
		    <div class="modal-footer">
		    	<input type="button" id="desaAnnex" onclick="procesarAnexo('${pageContext.response.locale}')" title="<spring:message code="regweb.guardar"/>" value="<spring:message code="regweb.guardar"/>" class="btn btn-warning btn-sm">
	        	<button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarAnexo()"><spring:message code="regweb.cerrar"/></button>
			</div>
		</div>
	</div>
</div>