$(document).ready(function() {

    // Muestra los errores en los formularios
    $(".help-block").parents(".form-group").each(function(){
        $(this).addClass("has-error");
    });

    // Activa las Pestañas

    $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
        });
    $('#myTab a:first').tab('show');

    $('#myTab2 a').click(function (e) {
          e.preventDefault();
          $(this).tab('show');
          });
    
    $('#myTab2 a:first').tab('show');


    // Menú _Hover bootstrap-dropdown-on-hover-plugin.js 
    $('[data-toggle="dropdown"]').dropdownHover({delay: 50});

    // JS Code to add Submenus in BS 3:
    $("li.dropdown").mouseover(function (e) {
        if ( $(this).find(".dropdown-submenu-left") ) {
            var elm = $(this).find("a");
            var off = elm.offset();
            var l = off.left;
            var w = elm.width();
            var m = 30;
            var docW = $(".container").width();

            var isEntirelyVisible = (l + w + m <= docW);

            if ( ! isEntirelyVisible ) {
                $(this).find(".dropdown-submenu-left").addClass('toggle-left');
                elm.find(".chevron-right").hide();
                elm.find(".chevron-left").show();
            } else {
                elm.find(".chevron-left").hide();
            }
        }
    });

    // Select autocomplete
    $(function () {

        var config = {
            '.chosen-select': {width: "100%", search_contains: true}
        };
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }
    });


    <!-- Activa los popover de mensajes de ampliación de información de los registros -->
    $("[rel='popupAbajo']").popover({ trigger: "hover",placement: "bottom",container:"body", html:true});
    $("[rel='popupArriba']").popover({ trigger: 'hover',placement: 'top',container:"body", html:true});
    $("[rel='popupDerecha']").popover({ trigger: 'hover', container: 'body', html: true, placement: 'right'});


    $.sessionTimeout({
        title:trads_general['sesion.expirar.titulo'],
        message:trads_general['sesion.expirar.mensaje'],
        ignoreUserActivity:true,
        keepAliveUrl: '/regweb3/rest/extenderSesion',
        keepAliveButton: trads_general['sesion.expirar.boton'],
        logoutUrl: 'login.html',
        redirUrl: 'http://www.google.es' ,
        warnAfter: 1740000,
        redirAfter: 1788000,
        countdownBar: true
    });

});


// Muestra el cuadro de confirmación para distribuir un registro
function confirmDistribuir(mensaje) {

    var confirmModal =
        $("<div class=\"modal fade\">" +
            "<div class=\"modal-dialog\">" +
            "<div class=\"modal-content\">" +
            "<div class=\"modal-header\">" +
            "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
            "<h4 class=\"modal-title\">Confirmar</h4>" +
            "</div>" +

            "<div class=\"modal-body\">" +
            "<p>" + mensaje + "</p>" +
            "</div>" +

            "<div class=\"modal-footer\">" +
            "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>" +
            "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>");

    confirmModal.find("#okButton").click(function (event) {
        distribuir();
        confirmModal.modal("hide");
    });

    confirmModal.modal("show");
}

// Muestra un popup con un mensaje informativo
function mensajeInformativo(titulo,mensaje) {

    var confirmModal =
        $("<div class=\"modal fade\">" +
            "<div class=\"modal-dialog\">" +
            "<div class=\"modal-content\">" +
            "<div class=\"modal-header\">" +
            "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
            "<h4 class=\"modal-title\">" + titulo + "</h4>" +
            "</div>" +

            "<div class=\"modal-body\">" +
            "<p>" + mensaje + "</p>" +
            "</div>" +

            "<div class=\"modal-footer\">" +
            "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Ok</button>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>");

    confirmModal.find("#okButton").click(function (event) {
        confirmModal.modal("hide");
    });

    confirmModal.modal("show");
}

function confirmRW(url, mensaje) {
    confirm(url, mensaje);
}

// Muestra el cuadro de confirmación para realizar una acción sobre un registro
function confirm(url, mensaje) {

    var confirmModal = 
      $("<div class=\"modal fade\">" +
          "<div class=\"modal-dialog\">" +
            "<div class=\"modal-content\">"+
              "<div class=\"modal-header\">" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
                "<h4 class=\"modal-title\">Confirmar</h4>" +
              "</div>" +

          "<div class=\"modal-body\">" +
            "<p>"+mensaje+"</p>" +
          "</div>" +

          "<div class=\"modal-footer\">" +
              "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>"+
              "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>"+
          "</div>" +
          "</div>" +
          "</div>" +
        "</div>");

    confirmModal.find("#okButton").click(function(event) {
      document.location.href=url;
	  confirmModal.modal("hide");
    });

    confirmModal.modal("show");
}

function confirmEnvioSIR(url, oficinasSirLength, oficinaSIRCodigo, confirmacionTitulo, confirmacionCuerpo, enviando) {
	var confirmEnvioModal  =
        $("<div class=\"modal fade\">" +
            "<div class=\"modal-dialog\">" +
	            "<div class=\"modal-content\">" +
		            "<div class=\"modal-header\">" +
			            "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
			            "<h4 class=\"modal-title\">" + confirmacionTitulo + "</h4>" +
		            "</div>" +
		            "<div class=\"modal-body\">" +
		            	"<p>" + confirmacionCuerpo + "</p>" +
		            "</div>" +
		            "<div class=\"modal-footer\">" +
			            "<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">No seguir</button>" +
			            "<button type=\"button\" id=\"okButton\" class=\"btn btn-success\">Seguir</button>" +
		            "</div>" +
		            "</div>" +
	            "</div>" +
            "</div>");
	// Modal confirmación envío (se quita la página de envioSir)
	confirmEnvioModal.find("#okButton").click(function(event) {
		if (oficinasSirLength > 1) {
			document.location.href=url;
		} else {
			realiarEnvioViaAjax(url, oficinaSIRCodigo, enviando);
		}
		confirmEnvioModal.modal("hide");
	});
	
	confirmEnvioModal.modal("show");
}

// Muestra el cuadro de confirmación para enviar un registro SIR sin anexos
function confirmEnvioSinAnexos(url, oficinasSirLength, oficinaSIRCodigo, confirmacionTitulo, confirmacionCuerpo, enviando, mensaje, mensajeTitulo, mensajeEnviar, mensajeCuerpo, mostrarBoton2) {

	var confirmEnvioModal  =
		        $("<div class=\"modal fade\">" +
		            "<div class=\"modal-dialog\">" +
			            "<div class=\"modal-content\">" +
				            "<div class=\"modal-header\">" +
					            "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
					            "<h4 class=\"modal-title\">" + confirmacionTitulo + "</h4>" +
				            "</div>" +
				            "<div class=\"modal-body\">" +
				            	"<p>" + confirmacionCuerpo + "</p>" +
				            "</div>" +
				            "<div class=\"modal-footer\">" +
					            "<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">No seguir</button>" +
					            "<button type=\"button\" id=\"okButton\" class=\"btn btn-success\">Seguir</button>" +
				            "</div>" +
				            "</div>" +
			            "</div>" +
		            "</div>");
	// Modal confirmación envío (se quita la página de envioSir)
	confirmEnvioModal.find("#okButton").click(function(event) {
	    var confirmModal;
	
	    if(mostrarBoton2) {
	        confirmModal =
	            $("<div class=\"modal fade\">" +
	                "<div class=\"modal-dialog\">" +
	                "<div class=\"modal-content\">" +
	                "<div class=\"modal-header\">" +
	                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
	                "<h4 class=\"modal-title\">" + mensajeTitulo + "</h4>" +
	                "</div>" +
	
	                "<div class=\"modal-body\">" +
	                "<p>" + mensaje + "</p>" +
	                "</div>" +
	
	                "<div class=\"modal-footer\">" +
	                "<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">" + mensajeCuerpo + "</button>" +
	                "<button type=\"button\" id=\"okButton\" class=\"btn btn-success\">" + mensajeEnviar + "</button>" +
	                "</div>" +
	                "</div>" +
	                "</div>" +
	                "</div>");
	    }else{
	        confirmModal =
	            $("<div class=\"modal fade\">" +
	                "<div class=\"modal-dialog\">" +
	                "<div class=\"modal-content\">" +
	                "<div class=\"modal-header\">" +
	                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
	                "<h4 class=\"modal-title\">" + mensajeTitulo + "</h4>" +
	                "</div>" +
	
	                "<div class=\"modal-body\">" +
	                "<p>" + mensaje + "</p>" +
	                "</div>" +
	
	                "<div class=\"modal-footer\">" +
	                "<button type=\"button\" id=\"okButton\" class=\"btn btn-success\">" + mensajeEnviar + "</button>" +
	                "</div>" +
	                "</div>" +
	                "</div>" +
	                "</div>");
	    }
	    // Modal confirmación envío sin documentación
	    confirmModal.find("#okButton").click(function(event) {
	    	if (oficinasSirLength > 1) {
	    		document.location.href=url;
	    	} else {
	    		realiarEnvioViaAjax(url, oficinaSIRCodigo, enviando);
	    	}
	        confirmModal.modal("hide");
	    });
	
	    confirmModal.modal("show");
		
    	confirmEnvioModal.modal("hide");
	});

	confirmEnvioModal.modal("show");
}

function realiarEnvioViaAjax(url, oficinaCodigo, enviando) {
	jQuery.ajax({
		crossDomain: true,
        url: url,
        data: { oficinaSIRCodigo: oficinaCodigo},
        type: 'POST',
        beforeSend: function(){
            waitingDialog.show(enviando, {dialogSize: 'm', progressType: 'primary'});
        },
        success: function() {
        	location.reload(); 
        },
        error: function() {
        	location.reload(); 
        }
    }) ;
}

// Muestra el cuadro de confirmación para realizar una acción sobre un registro
function cerrar(mensaje) {

    var confirmModal =
        $("<div class=\"modal fade\">" +
        "<div class=\"modal-dialog\">" +
        "<div class=\"modal-content\">"+
        "<div class=\"modal-header\">" +
        "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
        "<h4 class=\"modal-title\">Confirmar</h4>" +
        "</div>" +

        "<div class=\"modal-body\">" +
        "<p>"+mensaje+"</p>" +
        "</div>" +

        "<div class=\"modal-footer\">" +
        "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>"+
        "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Sí</button>"+
        "</div>" +
        "</div>" +
        "</div>" +
        "</div>");

    confirmModal.find("#okButton").click(function(event) {
        confirmModal.modal("hide");
    });

    confirmModal.modal("show");
}


function goTo(url) {
    document.location.href=url;
}

function goToNewPage(url){
    window.open(url, '_blank');
}

function imprimirRecibo(url) {

    var idModelo = $('#idModelo').val();
    var url2=url.concat(idModelo);

    document.location.href=url2;
}


/**
 * Añade un mensaje de error con el texto indicado en el div con el id indicado
 * @param id
 * @param texto
 */
function mensajeError(id,texto){
    $(id).html('');
 var mensaje = "<div class=\"alert alert-danger alert-dismissable\">"+
    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>"+
     "<div class=\"row vertical-align\">" +
     "            <div class=\"col-xs-1 text-center\">" +
     "                <i class=\"fa fa-times-circle fa-2x\"></i>" +
     "            </div>" +
     "            <div class=\"col-xs-11\">" +
     "                <strong>"+texto+"</strong>" +
     "            </div>" +
     "        </div></div>";

    $(id).append(mensaje);

    $("html, body").animate({ scrollTop: 0 }, "slow"); // Movemos el scroll ariba de la página
}

/**
 * Añade un mensaje de info con el texto indicado en el div con el id indicado
 * @param id
 * @param texto
 */
function mensajeSuccess(id,texto){
    $(id).html('');
    var mensaje = "<div class=\"alert alert-success alert-dismissable\">"+
        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>"+
        "<div class=\"row vertical-align\">" +
        "            <div class=\"col-xs-1 text-center\">" +
        "                <i class=\"fa fa-info-circle fa-2x\"></i>" +
        "            </div>" +
        "            <div class=\"col-xs-11\">" +
        "                <c:forEach var=\"info\" items=\"${infos}\">" +
        "                    <strong>"+texto+"</strong><br>" +
        "                </c:forEach>" +
        "            </div>\n" +
        "        </div></div>";

    $(id).append(mensaje);

    $("html, body").animate({ scrollTop: 0 }, "slow"); // Movemos el scroll ariba de la página
}


/**
 * Carga de valores un Select
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param idElemento
 * @param valorSelected Valor seleccionado en el select, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function cargarSelect(url,idElemento,idSelect, valorSelected, todos){
    //alert( "cargarSelect " + idSelect);
    var html = '';
    jQuery.ajax({
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: { idOrganismo: idElemento },
        success: function(result) {
            if(todos){html = '<option value="-1">...</option>';}
            var len = result.length;
            var selected='';
            for ( var i = 0; i < len; i++) {
                selected='';
                if(valorSelected != null && result[i].codigo == valorSelected){
                    selected = 'selected="selected"';
                }
                html += '<option '+selected+' value="' + result[i].id + '">'
                    + result[i].denominacion + '</option>';
            }
            html += '</option>';

            if(len != 0){
                $(idSelect).html(html);
            }else if(len==0){
                $(idSelect).attr("disabled","disabled");
                var html='';
                $(idSelect).html(html);
            }
            $(idSelect).trigger("chosen:updated");
        }
    });


}


/**
 * Carga de valores un Select dependiente de otro
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param seleccion valor seleccionado en el Select principal
 * @param valorSelected Valor seleccionado en el select dependiente, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function actualizarSelect(url, idSelect, seleccion, valorSelected, todos, async){
    var html = '';
    if (seleccion != '-1' && seleccion != null) {
        jQuery.ajax({
            async: async,
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '<option value="-1">...</option>';}
                var len = result.length;
                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(valorSelected != null && result[i].id == valorSelected){
                        selected = 'selected="selected"';
                        }
                    html += '<option '+selected+' value="' + result[i].id + '">'
                        + result[i].nombre + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    var html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }
            }
        });

    }else{
        var html='';
        $(idSelect).html(html);
        $(idSelect).attr("disabled",true).trigger("chosen:updated");
    }


}


/**
 * Carga las oficinas SIR del codigoDestinoSIR que nos indican en idElemento en un select y activa o desactiva el botón indicado.
 *
 * @param url donde hacer la petición ajax
 * @param idElemento código del destino SIR indicado
 * @param idSelect select que queremos cargar
 * @param valorSelected valor que se quiere seleccionar.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 * @param mensaje mensaje de que no se han encontrado oficinas
 * @param mensaje2 mensaje del waiting dialog
 * @param claseselect atributo class del select.
 * @param boton  boton a activar/desactivar.
 */
function cargarOficinasSIR(url,idElemento,idSelect, valorSelected, todos, mensaje,mensaje2,claseselect,boton){
    var html = '';
    jQuery.ajax({
        crossDomain: true,
        url: url,
        type: 'GET',
        dataType: 'json',
        data: { codigoDestinoSIR: idElemento },
        beforeSend: function(objeto){
            waitingDialog.show(mensaje2, {dialogSize: 'm', progressType: 'primary'});
        },
        success: function(result) {

            if(todos){html = '<option value="-1">...</option>';}
            var len = result.length;
            var selected='';
            //Montamos opciones del select
            for ( var i = 0; i < len; i++) {
                selected='';
                if(valorSelected != null && result[i].codigo == valorSelected){
                    selected = 'selected="selected"';
                }
                html += '<option '+selected+' value="' + result[i].codigo + '">'
                    + result[i].denominacion + '</option>';
            }


            if(len !== 0) {
                if (len === 1) { // solo se muestra el nombre de la oficina
                    html = '<p>' + result[len - 1].denominacion + '</p>' + '<input type="hidden" id="oficinaSIRCodigo" name="oficinaSIRCodigo" value="' + result[len - 1].codigo + '"/>';
                    $('#oficinaSIR').html(html);
                    $('#' + idSelect).html('');
                } else { // se monta el select con las oficinas encontradas
                    var html2 = '<div class="col-xs-9">';
                    html2 += '<select id="' + idSelect + '" name="' + idSelect + '" class="' + claseselect + '">';
                    html2 += html;
                    html2 += '</select>';
                    html2 += '</div>';
                    $('#oficinaSIR').html(html2);
                }
                //Se activa el botón indicado (Enviar)
                if (boton != '') {$(boton).removeAttr("disabled", "disabled");}
            }else if(len===0){ // se muestra el mensaje de que no hay oficinas.
                $('#oficinaSIR').html(mensaje);
                // No hay oficinas, se desactiva el botón (Enviar).
                if (boton != '') {$(boton).attr("disabled", "disabled");}
            }
            $('#ofSIR').show();
            $('#'+idSelect).trigger("chosen:updated");
            waitingDialog.hide();
        }
    });

}

/**
 * Carga de valores un Select dependiente de otro
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param seleccion valor seleccionado en el Select principal
 * @param valorSelected Valor seleccionado en el select dependiente, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 */
function actualizarSelectLocalidad(url, idSelect, seleccion, valorSelected, todos, async) {
    var html = '';
    if (seleccion != '-1' && seleccion != null) {
        jQuery.ajax({
            async: async,
            url: url,
            type: 'GET',
            dataType: 'json',
            data: {id: seleccion},
            contentType: 'application/json',
            success: function (result) {
                if (todos) {
                    html = '<option value="-1">...</option>';
                }

                var len = result.length;
                var selected = '';
                for (var i = 0; i < len; i++) {
                    selected = '';
                    if (valorSelected != null && result[i].id == valorSelected) {
                        selected = 'selected="selected"';
                    }
                    html += '<option ' + selected + ' value="' + result[i].id + "-" + result[i].codigoEntidadGeografica + '">'
                        + result[i].nombre + '</option>';
                }
                html += '</option>';


                if (len != 0) {
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled", false).trigger("chosen:updated");
                } else if (len == 0) {
                    var html = '';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled", true).trigger("chosen:updated");
                }

            }
        });

    } else {
        var html = '';
        $(idSelect).html(html);
        $(idSelect).attr("disabled", true).trigger("chosen:updated");
    }

}

/**
 * Carga de valores un Select dependiente de otro
 * @param url donde hacer la petición ajax
 * @param idSelect id del campo select donde cargar los datos obtenidos
 * @param seleccion valor seleccionado en el Select principal
 * @param valorSelected Valor seleccionado en el select dependiente, si es que lo hay. Sirve solo para las modificaciones.
 * @param todos Booleano para definir si incluir la opción de todos (...) en el Select
 * @param idioma
 */
function actualizarSelectTraduccion(url, idSelect, seleccion, valorSelected, todos, idioma) {
    var html = '';
    $(idSelect).attr("disabled",true).trigger("chosen:updated");
    if(seleccion != '-1'){
        jQuery.ajax({
            async: false,
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '<option value="-1">...</option>';}
                var len = result.length;
                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(valorSelected != null && result[i].id == valorSelected){
                        selected = 'selected="selected"';
                    }
                    html += '<option '+selected+' value="' + result[i].id + '">'
                        + result[i].traducciones[idioma].nombre + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    html='<option value="-1">...</option>';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                    //$(idSelect).attr("disabled",true).trigger("chosen:updated");
                }
            }
        });

    }else{
        html='';
        $(idSelect).html(html);
        $(idSelect).attr("disabled",true).trigger("chosen:updated");
    }

}

/**
*Función que añade el externo al select indicado y además lo marca como seleccionado
*(solo se emplea para organismoDestino
* @param idSelect identificador del select al que añadirle la opción
* @param codigo identificador a añadir
* @param denominacion denominación del objeto a añadir
*/
function addExterno(idSelect, codigo, denominacion){
    var html =  $(idSelect).html();
    html += '<option value="'+codigo+'" selected="selected">'+ denominacion+'</option>';
    $(idSelect).html(html);
    $(idSelect).attr("disabled",false).trigger("chosen:updated");

}

/* Función que hace el reset de un formulario con distintos tipos de inputs.*/
function clearForm(form) {
  // iterate over all of the inputs for the given form element
  $(':input', form).each(function() {
    var nombre = this.name;
    var type = this.type;
    var tag = this.tagName.toLowerCase(); // normalize case

      if(nombre != 'idRegistroDetalle'){
        // it's ok to reset the value attr of text inputs,
        // password inputs, and textareas
        if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea' || type=='file')
            this.value = "";
        // checkboxes and radios need to have their checked state cleared
        // but should *not* have their 'value' changed
        else if (type == 'checkbox' || type == 'radio')
            this.checked = false;
        // select elements need to have their 'selectedIndex' property set to -1
        // (this works for both single and multiple select elements)
        else if (tag == 'select')
            this.selectedIndex = 0;
        $(this).trigger("chosen:updated");
    }

  });
}

/**  CALENDAR **/
$(function() {
    $('#fechaInicio').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY',
        pickTime: false,
        useMinutes: false,
        useSeconds: false,
        useCurrent: false,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});

$(function() {
    $('#fechaFin').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY',
        pickTime: false,
        useMinutes: false,
        useSeconds: false,
        useCurrent: false,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});

$(function() {
    $('#fechaOrigen').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY HH:mm:ss',
        pickTime: true,
        useMinutes: true,
        useSeconds: true,
        useCurrent: false,
        use24hours: true,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});

$(function() {
    $('#fechaInicioImportacion').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY HH:mm:ss',
        pickTime: true,
        useMinutes: true,
        useSeconds: true,
        useCurrent: false,
        use24hours: true,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});

$(function() {
    $('#fechaFinImportacion').datetimepicker({
        language: 'ca',
        format: 'DD/MM/YYYY HH:mm:ss',
        pickTime: true,
        useMinutes: true,
        useSeconds: true,
        useCurrent: false,
        use24hours: true,
        icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down'
        }
    });
});


/**
 * Elimina la etiqueta de Error al campo pasado por parámetro
 * @param campo
 */
function quitarError(campo){
    var variable = "#"+campo+"Error";

    $(variable).parents(".form-group").removeClass("has-error");
    var htmlNormal = "<span id=\""+campo+"Error\"></span>";
    $(variable).html(htmlNormal);
}

/**
 * Realiza el submit del formulario pasado por parámetro.
 * @param formulario
 */

function doForm(formulario){

    window.onbeforeunload = null;
    $(formulario).submit();
}

/**
 * Obtiene el valor traducido de un elemento, para comprar las modificaciones de un Registro
 * @param url donde hacer la petición ajax
 * @param id
 * @param elemento
 * @return Texto con la traducción del elmento solicitado
 */
function obtenerElementoTraducido(url, id,elemento){
    jQuery.ajax({
        url: url,
        data: { id: id },
        type: 'GET',
        success: function(result) {
            // Escribimos el resultado en el elmento indicado
            $('#'+elemento).html(result);

        }
    }) ;
}

/* Función que obtiene el contextpath en js */
function getContextPath() {
    var ctx = window.location.pathname,
        path = '/' !== ctx ? ctx.substring(0, ctx.indexOf('/', 1) + 1) : ctx;
    return path + (/\/$/.test(path) ? '' : '/');
}


//Valida las fechas (fecha, nombre del campo)
function validaFecha(inputText, camp){
    var formatoFecha = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
    var variable = "#" + camp + " span.errors";
    var formatoHtml = "<span id='"+ camp +".errors' class='help-block'>El format no és correcte</span>";

    // Comprueba que la fecha tiene el formato adecuado (el día como máximo 31 y el mes 12)
    if(inputText.value.match(formatoFecha)){
        //Comprueba que los campos esten separados por una barra
        var opera1 = inputText.value.split('/');
        lopera1 = opera1.length;
        // Separa la fecha en día, mes y año
        if (lopera1>1){
            var pdate = inputText.value.split('/');
        }
        var dd  = parseInt(pdate[0]);
        var mm = parseInt(pdate[1]);
        var yy = parseInt(pdate[2]);
        // Crea lista de días máximos por cada mes
        var ListofDays = [31,28,31,30,31,30,31,31,30,31,30,31];
        // Comprueba si el mes es 2 (febrero)
        if (mm==1 || mm>2){
            // Si el valor del dia es mayor que el que marca la tabla de días, devuelve error
            if (dd>ListofDays[mm-1]){
                $(variable).html(formatoHtml);
                $(variable).parents(".form-group").addClass("has-error");
                return false;
            }
        }
        // Entra si el mes es febrero
        if (mm==2){
            var lyear = false;
            // Comprueba si el año es bisiesto
            if ( (!(yy % 4) && yy % 100) || !(yy % 400)){
                lyear = true;
            }
            // Retorna error si el año no es bisiesto y el día es mayor que 28
            if ((lyear==false) && (dd>=29)){
                $(variable).html(formatoHtml);
                $(variable).parents(".form-group").addClass("has-error");
                return false;
            }
            // Retorna error si el año es bisiesto y el día es mayor que 29
            if ((lyear==true) && (dd>29)){
                $(variable).html(formatoHtml);
                $(variable).parents(".form-group").addClass("has-error");
                return false;
            }
        }
        // Si el formato es correcto y el día concuerda con el mes elegido y el año, elimina campo de error
        var variable = "#" + camp + " span.errors";
        var htmlNormal = "<span id='"+ camp +".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        return true;
    } // Si el formato de la fecha no ese correcto, retorna error
    else{
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        return false;
    }
}


// Valida si una fecha es anterior a otra
function esAnterior(fechaInicio, fechaFin){
    var anoInicio = parseInt(fechaInicio.substring(6,10));
    var mesInicio = fechaInicio.substring(3,5);
    var diaInicio = fechaInicio.substring(0,2);
    var anoFin = parseInt(fechaFin.substring(6,10));
    var mesFin = fechaFin.substring(3,5);
    var diaFin = fechaFin.substring(0,2);

    if(anoFin > anoInicio){
        return true;
    }else{
        if (anoFin == anoInicio){
            if(mesFin > mesInicio)
                return true;
            if(mesFin == mesInicio)
                return diaFin >= diaInicio;
            else
                return false;
        }else
            return false;
    }
}


//Valida las fechas combinadas de un formulario (fechaInicio, fechaFin, nombre del campo Inicio, nombre del campo Fin)
function validaFechasConjuntas(fechaInicio, fechaFin, campInicio, campFin){
    var posterior = false;
    var inicioCorrecta = false;
    var finCorrecta = false;

    // Dia actual
    var d = new Date();
    var diaActual = d.getDate();
    var mesActual = d.getMonth();
    var anoActual = d.getFullYear();
    mesActual = mesActual + 1;
    if(mesActual < 10){
        mesActual = "0" + mesActual;
    }
    if(diaActual< 10){
        diaActual = "0" + diaActual;
    }
    var fechaActual = diaActual + "/" + mesActual + "/" + anoActual;

    // Mira si la fecha Fin es posterior a la actual
    if(!esAnterior(fechaFin.value,fechaActual)){
        var variable = "#" + campFin + " span.errors";
        var formatoHtml = "<span id='"+ campFin +".errors' class='help-block'>" + $('#error2').val() + "</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        finCorrecta = false;
    }else{
        var variable = "#" + campFin + " span.errors";
        var htmlNormal = "<span id='"+ campFin +".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        finCorrecta = true;
    }

    // Mira si la fecha Inicio es posterior a la actual
    if(!esAnterior(fechaInicio.value,fechaActual)){
        var variable = "#" + campInicio + " span.errors";
        var formatoHtml = "<span id='"+ campInicio +".errors' class='help-block'>" + $('#error1').val() + "</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        inicioCorrecta = false;
    }else{
        var variable = "#" + campInicio + " span.errors";
        var htmlNormal = "<span id='"+ campInicio +".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        inicioCorrecta = true;
    }

    // Comprueba si la fecha Inicio es anterior a la de Fin
    if(inicioCorrecta && finCorrecta){
        if(esAnterior(fechaInicio.value, fechaFin.value)){
            var variable = "#" + campInicio + " span.errors";
            var htmlNormal = "<span id='"+ campInicio +".errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");
            var variable2 = "#" + campFin + " span.errors";
            var htmlNormal2 = "<span id='"+ campFin +".errors'></span>";
            $(variable2).html(htmlNormal2);
            $(variable2).parents(".form-group").removeClass("has-error");
            posterior = true;
        }else{
            var variable = "#" + campInicio + " span.errors";
            var formatoHtml = "<span id='"+ campInicio +".errors' class='help-block'>" + $('#error3').val() + "</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            var variable2 = "#" + campFin + " span.errors";
            var formatoHtml2 = "<span id='"+ campFin +".errors' class='help-block'>" + $('#error3').val() + "</span>";
            $(variable2).html(formatoHtml2);
            $(variable2).parents(".form-group").addClass("has-error");
            posterior = false;
        }
    }

    // Si las fechas son anteriores o iguales a hoy, y la Inicio es menor o igual a la de Fin, retiorna true
    return !!((posterior) && (inicioCorrecta) && (finCorrecta));
}

// Valida que el valor introducido en el campo numeroRegistro sea un número entero
function validaEntero(numeroRegistro, campNumeroRegistro){

    //Compruebo si es un valor numérico
    if(numeroRegistro.value.length>0) {

        var contienePunto = numeroRegistro.value.indexOf('.');

        if ((!isNaN(numeroRegistro.value))&&(contienePunto==-1)) {
            var variable = "#" + campNumeroRegistro + " span.errors";
            var htmlNormal = "<span id='" + campNumeroRegistro + ".errors'></span>";
            $(variable).html(htmlNormal);
            $(variable).parents(".form-group").removeClass("has-error");
        } else {
            var variable = "#" + campNumeroRegistro + " span.errors";
            var formatoHtml = "<span id='" + campNumeroRegistro + ".errors' class='help-block'>" + $('#error4').val() + "</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
        }
    }

    return !isNaN(numeroRegistro.value);
}

// Valida que esté selecionado un libro
function validaLibro(libro, campoLibro){
    //Comprueba si hay un libro seleccionado
    if (libro.value!='') {
        var variable = "#" + campoLibro + " span.errors";
        var htmlNormal = "<span id='" + campoLibro + ".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
    } else {
        var variable = "#" + campoLibro + " span.errors";
        var formatoHtml = "<span id='" + campoLibro + ".errors' class='help-block'>" + $('#error5').val() + "</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
    }

    return libro.value!='';
}

// Valida que el campo tenga un valor, sin no tiene marca el error
function validaCampo(valorCampo,campo){
    if (valorCampo!='') {
        var variable = "#" + campo + " span.errors";
        var htmlNormal = "<span id='" + campo + ".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
    } else {
        var variable = "#" + campo + " span.errors";
        var formatoHtml = "<span id='" + campo + ".errors' class='help-block'>" + $('#error').val() + "</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
    }
    return valorCampo!='';
}

// Valida que el campo tenga un valor diferente de -1 (select), si no tiene marca el error
function validaSelect(valorCampo,campo){
    if (valorCampo!='-1') {
        var variable = "#" + campo + " span.errors";
        var htmlNormal = "<span id='" + campo + ".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
    } else {
        var variable = "#" + campo + " span.errors";
        var formatoHtml = "<span id='" + campo + ".errors' class='help-block'>" + $('#error').val() + "</span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
    }
    return valorCampo!='-1';
}

/**
 * Sustituimos los apostrofes para evitar problemas con las comillas
 * @param texto
 */
function normalizarTexto(texto) {
    var normalizado = texto.replace(/\"/g, '&quot;');
    normalizado = normalizado.replace(/'/g, "\\'");

    return normalizado;
}


function quitarErrorAnexo(campo){
    $(campo).parents(".form-group").removeClass("has-error");
    $(campo).remove();
}

function limpiarAnexoForm(){
    //$('#reload').show();
    quitarErrorAnexo('#documentoFile\\.errors');
    quitarErrorAnexo('#firmaFile\\.errors');
}


/**
 * Indica si una ofiicina està integrada amb SIR
 * @param id
 * @param url
 */
function habilitarSir(sir, url) {
	$('.checkOficina').attr('disabled', true);
    $.ajax({
        url: url,
        data: { sir: sir },
        type: "GET",
        dataType: 'json',
        async: false,
        contentType: 'application/json',
        success: function(result) {
            if(result === true){
                mensajeSuccess("#mensajes", tradsModSir['registroSir.modificar.ok']);
            }else{
                mensajeError("#mensajes", tradsModSir['registroSir.modificar.ko']);
            }
        	$('#mensajes').fadeIn('fast');
        }
    });
    
    setTimeout(function() {
        $('#mensajes').fadeOut('fast');
        $('.checkOficina').removeAttr('disabled');
    }, 1000);
}

/**
 * Funció per mostrar missatge i barra de "Processar" amb missatge i color propi
 */
var waitingDialog = waitingDialog || (function ($) {
    'use strict';

    // Creating modal dialog's DOM
    var $dialog = $(
        '<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true" style="padding-top:15%; overflow-y:visible;">' +
        '<div class="modal-dialog modal-m">' +
        '<div class="modal-content">' +
        '<div class="modal-header"><h3 style="margin:0;text-align:center;"></h3></div>' +
        '<div class="modal-body">' +
        '<div class="progress progress-striped active" style="margin-bottom:0;"><div class="progress-bar" style="width: 100%"></div></div>' +
        '</div>' +
        '</div></div></div>');

    return {
        /**
         * Opens our dialog
         * @param message Custom message
         * @param options Custom options:
         * 				  options.dialogSize - bootstrap postfix for dialog size, e.g. "sm", "m";
         * 				  options.progressType - bootstrap postfix for progress bar type, e.g. "success", "warning".
         */
        show: function (message, options) {
            // Assigning defaults
            if (typeof options === 'undefined') {
                options = {};
            }
            if (typeof message === 'undefined') {
                message = 'Loading';
            }
            var settings = $.extend({
                dialogSize: 'm',
                progressType: '',
                onHide: null // This callback runs after the dialog was hidden
            }, options);

            // Configuring dialog
            $dialog.find('.modal-dialog').attr('class', 'modal-dialog').addClass('modal-' + settings.dialogSize);
            $dialog.find('.progress-bar').attr('class', 'progress-bar');
            if (settings.progressType) {
                $dialog.find('.progress-bar').addClass('progress-bar-' + settings.progressType);
            }
            $dialog.find('h3').text(message);
            // Adding callbacks
            if (typeof settings.onHide === 'function') {
                $dialog.off('hidden.bs.modal').on('hidden.bs.modal', function (e) {
                    settings.onHide.call($dialog);
                });
            }
            // Opening dialog
            $dialog.modal();
        },
        /**
         * Closes dialog
         */
        hide: function () {
            $dialog.modal('hide');
        }
    };

})(jQuery);