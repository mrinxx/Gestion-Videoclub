
$(document).ready(function(){
	$("#btnenvio").click(function(){
		var nombreusuario=$("#nombreusuario").val();
		var clave=$("#pass").val();
		$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletInicioSesion",
			data: $.param({
				nombreusuario:nombreusuario,
				clave: clave
			}),
			success: function(result){ //Si la funcion va bien
				if(result=="ok"){
					$("#nombreusuario").val(""); //Borro valor
					$("#pass").val("");//Borro valor
					alert("Bienvenido,"+nombreusuario);
					document.location.href="web.html?username="+nombreusuario;
				}else{
					$("#respuesta").text("Error: Credenciales incorrectas");
					$("#nombreusuario").val(""); //Borro valor
					$("#pass").val("");//Borro valor
					
				}
			},
			error: function(){
				$("#respuesta").val("Error en la operación"); //En caso de que haya un error esto es lo que se muestra
		}
		})
	})
})
