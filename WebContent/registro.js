/**
 * 
 */

$(document).ready(function(){
	$("#btnregistro").click(function(){	
		var nombreusuario=$("#nombreusuario").val();
		var clave=$("#pass").val();
		var nombre=$("#nombre").val();
		var apellidos=$("#apellidos").val();
		var email=$("#correo").val();
		var saldo=$("#saldo").val();
		
		if($("#eleccion").prop("checked")){
			var premium="true";
		}else{
			var premium="false";
		}
		
		$.ajax({
			type:"POST",
			dataType:"html",
			url: "./ServletRegistro",
			data: $.param({
				nombreusuario:nombreusuario,
				clave:clave,
				nombre:nombre,
				apellidos:apellidos,
				email:email,
				saldo:saldo,
				premium:premium
			}),
			success: function(result){ //Si la funcion va bien
				if(result=="ok"){
					document.location.href="index.html";
				}
				else{
					$("#respuesta").text("Se han producido errores en la validación del formulario. Intentelo de nuevo");
				}
			},
			error: function(){
				$("#respuesta").text("Error en la operación"); //En caso de que haya un error esto es lo que se muestra
		}
	})
})
})