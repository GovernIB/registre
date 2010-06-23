<!-- importa la hoja de estilos -->
<link rel="stylesheet" type="text/css" media="all" href="/regweb/jscripts/jscalendar/calendar-blue.css" title="winter" />

<!-- importa el script calendar -->
<script type="text/javascript" src="/regweb/jscripts/jscalendar/calendar_stripped.js"></script>

<!-- importa el módulo de idioma -->
<script type="text/javascript" src="/regweb/jscripts/jscalendar/lang/calendar-ca.js"></script>

<!-- importa el script calendar -->
<script type="text/javascript" src="/regweb/jscripts/jscalendar/calendar-setup_stripped.js"></script>
<script>
<!--
	// Se llama a esta función cuando el usuario presiona sobre una fecha.
	function selected(cal, date) {
		cal.sel.value = date; // Actualizamos el valor
  		if (calendar.dateClicked) {
		    calendar.callCloseHandler(); // Cerramos el calendario
		}
	};

	function closeHandler(cal) {
		cal.hide();
	}

	function calendario(id) { 
		calendario_con_formato(id, "%d/%m/%Y ");
	}
	
	function calendario_con_formato(id, formato) {
		var el = document.getElementById(id);
		if (calendar != null) { 
			calendar.hide(); // Ocultamos el calendario previo (si est� visible)
		} 
		else {
			// Creamos el calendario.
			var cal = new Calendar(true, null, selected, closeHandler);
			calendar = cal;  	// Lo almacenamos en la variable global
			cal.setRange(2000, 2070);        // A�os m�nimo y m�ximo.
			cal.create();
		}
		calendar.setDateFormat(formato);    // Fijamos el formato
		calendar.parseDate(el.value);      // Ponemos el calendario a la fecha del valor actual
		calendar.sel = el;                 // fijamos el campo a modificar
		calendar.showAtElement(el);        // Mostramos el calendario

		return false;
	}			
-->
</script>
	

