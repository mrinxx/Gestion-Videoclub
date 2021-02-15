
$(document).ready(function(){
	//Cuando se haga click en el botón para enviar los datos del inicio de sesión
	$("#btnenvio").click(function(){
		var nombreusuario=$("#nombreusuario").val(); //se coge el valor del nombre
		var clave=$("#pass").val(); //valor de la contraseña
		$.ajax({
			type:"GET", 
			dataType:"html",
			url: "./ServletInicioSesion", //se llama al servlet
			data: $.param({
				nombreusuario:nombreusuario, 
				clave: clave
			}),
			success: function(result){ //Si la funcion va bien
				//en caso de que la respuesta obtenida desde el servlet sea la cadena "ok", significará que los datos del usuario son correctos y por lo tanto
				//el inicio de sesión será correcto
				if(result=="ok"){
					limpiar();
					alert("Bienvenido,"+nombreusuario); 
					document.location.href="web.html?username="+nombreusuario; //cambio la página pasandole el nombre de usuario que se ha logueado
				}else{
					//en caso de que la respuesta obtenida desde el servlet no sea el ok que se ha visto anteriormente, significará que alguno o ambos datos
					//son incorrectos y por lo tanto no se realizará el login, se llamará a una función que muestra la alerta de error
					muestraerror("Error: Credenciales incorrectas");
					limpiar();
					
				}
			},
			error: function(){
				muestraerror("Error en la operación"); //llama a funcion para mostrar error
				limpiar();
		}
		})
	})
})

//esta función lo que hace es limpiar los campos del formulario. Lo separo ya que este código lo repetia dos veces
function limpiar(){
	$("#nombreusuario").val(""); //Borro valor
	$("#pass").val("");//Borro valor
}

//esta función recibe una cadena de texto en caso de que se produzca algún error a la hora del loguin y lo muestra en el lugar correspondiente para ello
//durante 5 segundos, tras lo cual lo esconde
function muestraerror(textoerror){
	$("#respuesta").show(); //primero muestro el 
	$("#respuesta").text(textoerror);
					
	setTimeout(() => {
      $("#respuesta").hide();
    }, 5000);
}
