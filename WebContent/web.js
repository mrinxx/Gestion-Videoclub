/**
 * 
 */
var url=window.location;
var parametros=url.toString().split('?');
var usuario=parametros[1].split('=');
let usuariologueado=usuario[1]; //usuario que se ha logueado. Pasado desde el index

$(document).ready(function(){
		$.ajax({
			type:"GET",
			dataType:"html",
			url: "./ServletCreacionWeb",
			success: function(result){ 
				let peliculas= JSON.parse(result);
				let html=montarTabla(peliculas);
				$("#espaciopeliculas").html(html);
			},
			error: function(){
				alert("Error durante la carga de la página");
		}
		})
		
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
				let peliculas= JSON.parse(result);
				let html=montarTabla(peliculas);
				$("#espaciopeliculas").html(html);
				},
			error: function(){
				$("#respuesta").text("Error en la operación"); //En caso de que haya un error esto es lo que se muestra
				}
			})	
		})
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
				alert(result);
				if(result=="true"){
					alert("Pelicula alquilada con éxito");
				}
				else{
					alert("Error en el alquiler de la película. Compruebe su crédito")
				}
				
			},
			error: function(){
				alert("Error durante la operación");
			}
	})
}

//Función que monta la tabla que se visualiza en la web, ya sea filtrada o no. La pongo aquí ya que este trozo de código lo 
//repetía dos veces, por lo que no era óptimo
function montarTabla(peliculas){
	let html="<table ><thead><tr><td>Titulo</td><td>Genero</td><td>Actor Principal</td><td>Precio Alquiler</td><td></td></tr><thead><tbody>";
				 
	for (let i=0; i<peliculas.length; i++){
		let id=peliculas[i]["id"];
		let titulo = peliculas[i]["titulo"];
		let genero = peliculas[i]["genero"];
		let actor = peliculas[i]["actorppal"];
		let precio = peliculas[i]["precio"];
			
		//el id del botón que se va a pulsar es el mismo que el id del elemento en la base de datos
		//con ello lo que hago es que al pulsar este botón ya tendré ubicada la película que se quiere alquilar
	html+="<tr><td>"+titulo+"</td><td>"+genero+"</td><td >"+actor+"</td><td>"+precio+" euros</td><td><button id="+id+" onclick=reservar(this.id) class=\"btn btn-warning\">Reservar</button></td></tr>";
	}
	return html;
}