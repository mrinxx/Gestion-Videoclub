/**
 * 
 */

var url=window.location;
var usuario=url.toString().split('=');
let usuariologueado=usuario[1]; 

//esta variable va a almacenar el saldo más alto que tiene un usuario. Con ello se consigue que al modificar no se pueda
//meter menos saldo del que ya se tiene.
let auxsaldo=0; 

$(document).ready(function(){
	$("#nombreusuario").val(usuariologueado);
	//Esta peticion sirve para coger el saldo del usuario y colocarlo en el lugar que le corresponde nada más cargar la página
	$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletConsultaDatos",
			data:$.param({
				usuario:usuariologueado //mando el usuario al que pertenece el panel y del cual busco el saldo
			}),
			success: function(result){
				datos=result.split(",");

				$("#nombre").val(datos[0]);
				$("#apellidos").val(datos[1]);
				$("#clave").val(datos[2]);
				$("#email").val(datos[3]);
				auxsaldo=parseFloat(datos[5]);
				$("#saldodisponible").html("Saldo Disponible:"+auxsaldo+" euros");
				
				var cadenapremium=datos[4];
				if (cadenapremium=="true"){
					$("input[value='true']").prop("checked","true");
				}
				else{
					$("input[value='false']").prop("checked","true");
				}
				
				//a //modifico auxsaldo para guardar lo que tiene en un principio
				// //si la operación es exitosa se muestra donde corresponde
			},
			error: function(){
				muestrainfo("error","Error durante la carga de la página");
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
				
				//En caso de que el usuario no haya realizado ninguna reserva, se mostrará una alerta indicandolo.
				if(reservas.length==0){
					$("#reservas").html("<div class=\"alert alert-secondary\" role=\"alert\">Aún no se ha realizado ningún alquiler</div>");
				}else{
					//En caso de que si se hayan realizado reservas:
					//html = variable que almacena una cadena correspondiente a la construccion de la tabla que se va a mostrar en el panel
					let html="<table class=\"table table-hover\"><thead class=\"thead-dark\"><th scope=\"col\">#</th><th>Titulo</th><th scope=\"col\">Genero</th><th scope=\"col\">Estreno</th><th scope=\"col\">Fecha del alquiler</th><th></th></tr><thead><tbody>";
					
					//Añado una fila a la cadena correspondiente con la película
					for (let i=0; i<reservas.length; i++){
						let id = reservas[i]["id"];
						let numero_reserva=reservas[i]["numero_alquiler"];
						let titulo = reservas[i]["titulo"];
						let genero = reservas[i]["genero"];
						let estreno = reservas[i]["estreno"];
						let fecha = reservas[i]["fecha"];
						
						html+="<tr><th scope=\"row\">"+id+"</th><td>"+titulo+"</td><td>"+genero.toUpperCase()+"</td><td >"+estreno+"</td><td>"+fecha+"</td><td><button id="+numero_reserva+" onclick=devolver(this.id) class=\"btn btn-success\">Devolver Película</button></td></tr>";
						//NOTA: El id del botón es el id de la reserva que se va a devolver y el cual se coge desde los datos devueltos por el servlet. Esto es así ya que 
						//directamente puedo saber el alquiler alque se refiere justamente esa fila de la tabla y no borrar otro que tenga el mismo usuario y pelicula
					}
					$("#reservas").html(html); //muestro la tabla en el espacio reservado para ello
				}
	
			},
			error: function(){
				muestrainfo("error","Error durante la carga de la página");
		}
		})
		// + -> El usuario puede modificar su saldo siempre y cuando sea con una cantidad igual o mayor a 0.50€ 
		$("#btnmodificarsaldo").click(function(){
			var saldo=$("#addsaldo").val(); 
			var saldoactual=auxsaldo; //el saldo actual realmente es el que está almacenado en auxsaldo
			if(parseFloat(saldo)<0.50){ //En caso de que el saldo no sea como mínimo 0.50 céntimos
				muestrainfo("error","Error al modificar el saldo. Cantidad mínima a introducir: 0.50€ ");
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
						auxsaldo=saldo; //se actualiza el valor de auxsaldo
						//se recarga la página tras 2 segundos
						setTimeout("document.location.href=\"panelusuario.html?username=\"+usuariologueado;", 2000); //esto lo que hace es recargar la pagina a los dos segundos
						muestrainfo("correcto","Saldo modificado exitosamente"); //se mostrará el mensaje de que la acción ha sido correcta
						$("#saldodisponible").val(saldo); //se cambia el valor del saldo disponible que aparece en la pantalla
					
					},
					error: function(){
						muestrainfo("error","Error durante la operación");
					}
				});
			}
		})

		//Cuando se haga click en el botón de envio del formulario 
		$("#btnformulario").on("click",function(event){
			event.preventDefault(); //esto se usa para que al darle al botón directamente no se borren los datos introducidos en cada input
			//Se cojen los valores de los inputs
			var nombreusuario = $("#nombreusuario").val();
			var nombre = $("#nombre").val();
			var apellidos = $("#apellidos").val();
			var clave = $("#clave").val();
			var email = $("#email").val();
			var premium=$("input[name='premium']:checked").val(); //coge el radiobutton que esté marcado
			
			$.ajax({
				type:"POST",
				dataType:"html",
				url: "./ServletModificacionUsuario",
				data:$.param({
					nombreusuario:nombreusuario,
					nombre:nombre,
					apellidos:apellidos,
					clave:clave,
					email:email,
					premium:premium
					
				}),
				success: function(result){
					//en caso de que la modificacion haya sido correcta se mostrará un mensaje y se limpiará el formulario
					muestrainfo("correcto","Datos modificados correctamente");	
					setTimeout("document.location.href=\"panelusuario.html?username=\"+usuariologueado;", 2000); //esto lo que hace es recargar la pagina a los dos segundos
	
					$("#nombre").val("");
					$("#apellidos").val("");
					$("#clave").val("");
					$("#email").val("");
				},
				error: function(){
					muestrainfo("error","Error durante la operación");
				}
			})
		})
		
		//Cundo se haga click en el botón para eliminar la cuenta
		$("#btneliminarcuenta").click(function(){
			var nfilas = 0; //esto se compara con las filas de los alquileres del usuario
			//Para cada fila del cuerpo de la tabla, se va aumentando el numero de filas que hay en la tabla
     		$("table tbody tr").each(function() {
         		nfilas++;
     		})

			if(nfilas>0){
				muestrainfo("error","Devuelva las películas antes de darse de baja");
			}
			else{
			var confirmacion=window.confirm("Está a punto de eliminar su cuenta. ¿Está seguro/a de ello?"); //confirmación para eliminar la cuenta
			
			//Si se ha pulsado aceptar en la ventana emergente
			if(confirmacion==true){
				$.ajax({
					type:"POST",
					dataType:"html",
					url: "./ServletEliminarUsuario",
					data:$.param({
						usuario:usuariologueado
					}),
					success: function(result){
						alert(result); //se muestra como una alerta que el usuario ha sido eliminado
						document.location.href="index.html"; //se vuelve a la página principal
					},
					error: function(){
						muestrainfo("error","Error durante la operación");	
					}
				});
			}}
		})
		//para volver al listado de películas
		$("#btnvolver").click(function(){
			document.location.href="web.html?username="+usuariologueado;
		})
})

//Esta función se hace aparte ya que se incrusta en la creación del botón en la tabla de las reservas.
//Recibe el id de una película, que se corresponde con el id del botón que se pulsa y hace una petición.
function devolver(numero_alquiler){
	$.ajax({
	    type:"POST",
		dataType:"html",
		url: "./ServletDevolucion",
		data:$.param({
			usuario:usuariologueado,
			numero_alquiler:numero_alquiler
		}),
		success: function(result){
			setTimeout("document.location.href=\"panelusuario.html?username=\"+usuariologueado;", 2000); //esto lo que hace es recargar la pagina a los dos segundos
			muestrainfo("correcto",result); //se muestra el mensaje de que se ha realizado la devolucion correctamente
			
		  
		},
		error: function(){
		 	muestrainfo("error","Error durante la operación");	
		}
	});
	
}

//Esta función lo que hace es que dependiendo del tipo de mensaje que tiene que mostrar (correcto o error), coge el div correspondiente a ese tipo de mensaje 
//del html y muestra el mensaje que se debe mostrar en el mismo, tras 5 segundos este mensaje se esconde.
function muestrainfo(tipoinfo,textoinfo){
	$("#"+tipoinfo).show(); //concateno el tipo de la información ya que los espacios reservados en el html tienen como id correcto o error
	$("#"+tipoinfo).text(textoinfo);
				
	setTimeout(() => {
      $("#"+tipoinfo).hide();
    }, 5000);
}