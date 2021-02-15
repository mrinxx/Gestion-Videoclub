

$(document).ready(function(){
	//Cuando se pulse el botón para registrar al usuario, se van a coger todos los valores que están en los inputs
	$("#btnregistro").click(function(){	
		var nombreusuario=$("#nombreusuario").val();
		var clave=$("#pass").val();
		var nombre=$("#nombre").val();
		var apellidos=$("#apellidos").val();
		var email=$("#correo").val();
		var saldo=$("#saldo").val();
		
		//Como la elección de si el usuario quiere o no ser premium es mediante el marcado de una casilla, 
		//dependiendo de si esta o no marcada, el usuario será premium o no
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
			success: function(result){ 
				//Si la funcion va bien y devuelve "ok", significará que el usuario se ha registrado sin problema y por lo tanto se redirigirá al index para que 
				//este se loguee
				if(result=="ok"){
					document.location.href="index.html";
				}
				//Si se ha producido algún error significa que alguno de esos datos ya se encontraban en la base de datos, por lo tanto no se trata de un 
				//usuario válido y así se mostrará en un mensaje
				else{
					//se mostrará el mensaje de error durante 5 segundos y luego se esconderá
					$("#respuesta").show(); 
					$("#respuesta").text("Se han producido errores en la validación del formulario. Intentelo de nuevo");
					setTimeout(() => {
      					$("#respuesta").hide();
    				}, 5000);
				}
			},
			error: function(){
				$("#respuesta").show();
				$("#respuesta").text("Error en la operación"); //En caso de que haya un error esto es lo que se muestra
				setTimeout(() => {
      					$("#respuesta").hide();
    				}, 5000);
		}
	})
})
})