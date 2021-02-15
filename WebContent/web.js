/**
 * 
 */
var url=window.location;
var usuario=url.toString().split('=');
let usuariologueado=usuario[1]; //usuario que se ha logueado. Pasado desde el index

$(document).ready(function(){
	//Nada más cargarse la página, se realizará una petición para que se carguen todas las películas con unidades que hay en la base de datos
		$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletCreacionWeb",
			success: function(result){ 
				let peliculas= JSON.parse(result); //se pasa a tabla lo que se ha recibido como JSON desde el servlet
				let html=montarTabla(peliculas); //se llama a la función que crea la tabla
				$("#espaciopeliculas").html(html); //se muestra la tabla
			},
			error: function(){
				muestrainfo("error","Error durante la carga de la página");
		}
		})
		
		//Cuando se pulse el boltrón del apartado de filtrado, se cogerá el género que está en ese momento seleccionado
		$("#btnfiltrar").click(function(){	
			var genero = $("#generos").val();
		
		$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletFiltrado",
			data: $.param({
				genero: genero
			}),
			success: function(result){ 
				let peliculas= JSON.parse(result); //se pasa a tabla lo que se ha recibido como JSON desde el servlet
				let html=montarTabla(peliculas); //se monta la tabla
				$("#espaciopeliculas").html(html); //se muestra la tabla
				},
			error: function(){
				muestrainfo("error","Error durante la carga de la página"); //En caso de que haya un error esto es lo que se muestra
				}
			})	
		})
		//Cuando se haga click aquí se redirigirá al panel del usuario, el cual se pasa como parámetro
		$("#btnpanel").click(function(){
			document.location.href="panelusuario.html?username="+usuariologueado;
		})
});

//La hago fuera para poder pasarle el id de cada botón, en caso contrario no sabría cual se ha escogido	
function reservar(id){
	$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletReserva",
			data: $.param({
				nombreusuario:usuariologueado,
				idpelicula: id,
			}),
			success:function(result){
				//Si la reserva ha podido ser realizada, se muestra un mensaje exitoso, por el contrariom un mensaje de que la operación
				//ha sido fallida
				if(result=="true"){
					muestrainfo("correcto","Pelicula alquilada con éxito");
				}
				else{
					muestrainfo("error","Error en el alquiler de la película.");
				}
				
			},
			error: function(){
				muestrainfo("error","Error durante la operación");
			}
	})
}

//Función que monta la tabla que se visualiza en la web, ya sea filtrada o no. La pongo aquí ya que este trozo de código lo 
//repetía dos veces, por lo que no era óptimo
function montarTabla(peliculas){
	let html="<table class=\"table table-hover\"><thead class=\"thead-dark\"><tr><th scope=\"col\">Titulo</th><th scope=\"col\">Genero</th><th scope=\"col\">Actor Principal</th><th scope=\"col\">Precio Alquiler</th><th scope=\"col\"></th></tr><thead><tbody>";
				 
	for (let i=0; i<peliculas.length; i++){
		let id=peliculas[i]["id"];
		let titulo = peliculas[i]["titulo"];
		let genero = peliculas[i]["genero"];
		let actor = peliculas[i]["actorppal"];
		let precio = peliculas[i]["precio"];
			
		//el id del botón que se va a pulsar es el mismo que el id del elemento en la base de datos
		//con ello lo que hago es que al pulsar este botón ya tendré ubicada la película que se quiere alquilar
	html+="<tr><th scope=\"row\" >"+titulo+"</th><td>"+genero.toUpperCase()+"</td><td >"+actor+"</td><td>"+precio+" euros</td><td><button id="+id+" onclick=reservar(this.id) class=\"btn btn-warning\">Reservar</button></td></tr>";
	}
	return html;
}

//Esta función lo que hace es que dependiendo del tipo de mensaje que tiene que mostrar (correcto o error), coge el div correspondiente a ese tipo de mensaje 
//del html y muestra el mensaje que se debe mostrar en el mismo, tras 3 segundos este mensaje se esconde.
function muestrainfo(tipoinfo,textoinfo){
	$("#"+tipoinfo).show(); //concateno el tipo de la información ya que los espacios reservados en el html tienen como id correcto o error
	$("#"+tipoinfo).text(textoinfo);
				
	setTimeout(() => {
      $("#"+tipoinfo).hide();
    }, 3000);
}