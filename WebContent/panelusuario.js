/**
 * 
 */

var url=window.location;
var parametros=url.toString().split('?');
var usuario=parametros[1].split('=');
let usuariologueado=usuario[1]; //usuario que se ha logueado. Pasado desde el index

//esta variable va a almacenar el saldo más alto que tiene un usuario. Con ello se consigue que al modificar no se pueda
//meter menos saldo del que ya se tiene.
let auxsaldo=0; 

$(document).ready(function(){
	$("#nombreusuario").val(usuariologueado);
	//Esta peticion sirve para coger el saldo del usuario y colocarlo en el lugar que le corresponde nada más cargar la página
	$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletConsultaSaldo",
			data:$.param({
				usuario:usuariologueado //mando el usuario al que pertenece el panel y del cual busco el saldo
			}),
			success: function(result){
				auxsaldo=parseFloat(result); //modifico auxsaldo para guardar lo que tiene en un principio
				$("#saldodisponible").html("Saldo Disponible:"+parseFloat(result)+" euros"); //si la operación es exitosa se muestra donde corresponde
			},
			error: function(){
				alert("Problemas al cargar la página"); //problemas -> error en la carga
			}
			})
	
	//Esta petición sirve para cargar todas las reservas pertenecientes a un usuario al cargar la página directamente
	$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletReservasUsuario",
			data:$.param({
				usuario:usuariologueado //paso el usuario del que quiero saber las reservas, en este caso el que esta logueado
			}),
			success: function(result){ 
				let reservas= JSON.parse(result); //convierto el JSON devuelto a una lista
				//html = variable que almacena una cadena correspondiente a la construccion de la tabla que se va a mostrar en el panel
				let html="<table ><thead><tr><td>Titulo</td><td>Genero</td><td>Estreno</td><td>Fecha del alquiler</td><td></td></tr><thead><tbody>";
				
				//Añado una fila a la cadena correspondiente con la película
				for (let i=0; i<reservas.length; i++){
					let id = reservas[i]["id"];
					let titulo = reservas[i]["titulo"];
					let genero = reservas[i]["genero"];
					let estreno = reservas[i]["estreno"];
					let fecha = reservas[i]["fecha"];
			
					html+="<tr><td>"+titulo+"</td><td>"+genero+"</td><td >"+estreno+"</td><td>"+fecha+"</td><td><button id="+id+" onclick=devolver(this.id) class=\"btn btn-warning\">Devolver Película</button></td></tr>";
				}
					
				$("#reservas").html(html); //muestro la tabla
			},
			error: function(){
				alert("Error durante la carga de la página"); 
		}
		})
		// + -> El usuario puede modificar su saldo siempre y cuando sea mayor de 0
		$("#btnmodificarsaldo").click(function(){
			var saldo=$("#addsaldo").val();
			var saldoactual=auxsaldo;
			if(parseFloat(saldo)<0.50){ //En caso de que el saldo no sea como mínimo céntimos
				alert("Error al modificar el saldo. Cantidad mínima a introducir: 0.50€ ");
			}else{
				$.ajax({
					type:"POST",
					dataType:"html",
					url: "./ServletModificarSaldo",
					data:$.param({
						usuario:usuariologueado,
						saldoactual:saldoactual,
						cantidadasumar:parseFloat(saldo)
					}),
					success: function(result){
						auxsaldo=saldo;
						alert(result);
						$("#saldodisponible").val(saldo);
					
					},
					error: function(){
						alert("Error durante la acción")
					}
				});
			}
		})
		$("#btnformulario").on("click",function(event){
			event.preventDefault();
			var nombreusuario = $("#nombreusuario").val();
			var nombre = $("#nombre").val();
			var apellidos = $("#apellidos").val();
			var clave = $("#clave").val();
			var email = $("#email").val();
			
			$.ajax({
				type:"POST",
				dataType:"html",
				url: "./ServletModificacionUsuario",
				data:$.param({
					nombreusuario:nombreusuario,
					nombre:nombre,
					apellidos:apellidos,
					clave:clave,
					email:email
					
				}),
				success: function(result){
					alert(result);				
					$("#nombre").val("");
					$("#apellidos").val("");
					$("#clave").val("");
					$("#email").val("");
				},
				error: function(){
					alert("Error durante la acción")
				}
			})
		})
		$("#btneliminarcuenta").click(function(){
			var confirmacion=window.confirm("Está a punto de eliminar su cuenta. ¿Está seguro/a de ello?"); 
			if(confirmacion==true){
				$.ajax({
					type:"POST",
					dataType:"html",
					url: "./ServletEliminarUsuario",
					data:$.param({
						usuario:usuariologueado
					}),
					success: function(result){
						alert(result);
						document.location.href="index.html";

					},
					error: function(){
						alert("Error durante la acción")
					}
				});
			}
		})
		//para volver al listado de películas
		$("#btnvolver").click(function(){
			document.location.href="web.html?username="+usuariologueado;
		})
})

function devolver(idpelicula){
	$.ajax({
	    type:"POST",
		dataType:"html",
		url: "./ServletDevolucion",
		data:$.param({
			id:idpelicula,
			usuario:usuariologueado,
		}),
		success: function(result){
			alert(result);
		},
		error: function(){
		 	alert("Error durante la acción")
		}
	});
	
}