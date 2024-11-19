function inicializarModalNotificacion(identificador, registrar) {
    $('#identificadorNotificacion').val(identificador);

	// Vaciar datos remesa anterior
    $('#remesa-detalle').empty();
	$('#remesa-resumen').hide();
	$('.remesa-iframe-wrapper').empty();
	
	// Ocultar boton registro
	$('#remesa-btn-registrar').hide();
	
	// Vaciar errores consulta anterior
	$('#errorNotificacion').empty();
	
	if (registrar)
		limpiarModalParaRegistro(identificador);
	else
		limpiarModalParaLectura();
}

function limpiarModalParaLectura() {
	var pdfPreview = document.getElementById('pdfPreview');
	$(pdfPreview).hide();
	// Mostrar boton lectura
    $('#remesa-btn-lectura').show();

	// Ocultar loading y mostrar titulo modal
	$('#loading').hide();
	
	$('#modalTitle').text(tradRemesas['remesa.detalle']);
}

function limpiarModalParaRegistro() {
	// Ocultar boton lectura
	$('#remesa-btn-lectura').hide();
  
	// Consultar y mostrar remesa
	consultarRealizada();
}

