package com.hlc.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.hlc.connection.DBConnection;
import com.hlc.vo.Pelicula;


public class PeliculaDAO implements IPelicula{

	@Override
	/*Método que se utilizará en el momento de aparición de la web. Lo que hace es devolver un string en formato JSON con todas las películas que hay en la base 
	de datos*/
	public String listarPeliculas() {
		Gson json= new Gson(); //objeto para JSON
		DBConnection con = new DBConnection();
		String sql ="Select * from peliculas order by titulo"; //cojo todas las películas de la BD ordenadas por su titulo
		try {
			ArrayList<Pelicula> datos= new ArrayList<Pelicula>(); //ArrayList donde se van a almacenar cada una de las películas que se vayan obteniendo
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				String id = rs.getString("id");  
				String titulo= rs.getString("titulo"); 
				String genero= rs.getString("genero"); 
				String  actorppal= rs.getString("actor_ppal");  
				int copiasdisponibles = rs.getInt("copias_disponibles");
				String cadenaestreno=rs.getString("estreno");
				
				boolean estreno=false; //variable que determina el estreno (booleano)
				
				//comprobación y asignación del atributo estreno:
				/*Como en la base de datos se guarda como una cadena de texto, cuando se devuelve
				 * hay que comprobar su valor y dependiendo del que sea pasarlo a booleano*/
				if(cadenaestreno.equals("true")) {
					estreno=true;
				}else if(cadenaestreno.equals("false")) {
					estreno=false;
				}
				
				Pelicula pelicula = new Pelicula(id,titulo,genero,actorppal,copiasdisponibles,estreno);
			
	
			//Se mira si hay copias de la película, en caso de que haya se añade a los datos que se 
			//van a devolver, si no no
				if(pelicula.getCopiasdisponibles()>0) {
					datos.add(pelicula);
				}
			}
			
		st.close();
		
		return json.toJson(datos); //paso el ArrayList de películas a un JSON y lo paso como respuesta
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}

	@Override
	/*Método que se va a utilizar para la reserva de una película por parte de un usuario*/
	public String reservar(String idpelicula, String nombreusuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sqlsaldo= "Select saldo,premium from usuarios where nombreusuario LIKE'"+nombreusuario+"'"; //Para buscar el saldo del usuario
		String sqlprecio="Select precio,copias_disponibles from peliculas where id LIKE '"+idpelicula+"'"; //Para buscar el precio de la pelicula
		
		try {			
			Statement st = con.getConnection().createStatement();
			ResultSet rssaldo=st.executeQuery(sqlsaldo);
			double saldo = 0; //para almacenar el saldo que tiene el usuario
			int precio = 0; //para almacenar el precio de la película en cuestión
			int copias = 0; //para almacenar las copias de la pelicula en cuestion
			String premium=""; //para almacenar si el usuario es premium o no
			String operacionexitosa="true"; //Este string va a controlar si se ha podio hacer la reserva o no tras comprobar una serie de requerimientos para que se pueda realizar
			
			//Se van a coger el saldo y si es premium o no del usuario que intenta realizar la reserva
			while(rssaldo.next()) {
				saldo = rssaldo.getDouble("saldo"); 
				premium=rssaldo.getString("premium");
			}
			
			ResultSet rsprecio=st.executeQuery(sqlprecio);
			
			//Se va a coger el precio y las copias disponibles de la película que se quiere alquilar
			while(rsprecio.next()) {
				precio = rsprecio.getInt("precio"); 
				copias = rsprecio.getInt("copias_disponibles");
			}
			
			double preciopremium=precio-(precio*0.10); //precio por si el usuario es premium
			double nuevosaldo=0; //almacenará el saldo resultante
			
			
			String sql ="Insert into alquileres (usuario,pelicula,fecha_alquiler) VALUES('" +nombreusuario+"','" +idpelicula+"','" +LocalDate.now().toString()+"')"; //inserción en la BD del alquiler de la pelicula
			String modificacionunidades="Update peliculas set copias_disponibles = "+(copias-1)+" where id LIKE '"+idpelicula+"'"; //Actualización de las copias de la pelicula de la reserva
			String modificacionsaldo="";
			
			//En caso de que el usuario sea premium se va a comprobar que el saldo sea mayor al precio (descontando el 10% por ser premium).
			if(premium.equals("true") && saldo>=preciopremium){
				nuevosaldo=saldo-preciopremium; //se modifica el saldo del usuario
				modificacionsaldo="Update usuarios set saldo = "+nuevosaldo+" where nombreusuario LIKE '"+nombreusuario+"'"; //consulta de modificación de saldo 
				st.executeQuery(sql);
				st.executeQuery(modificacionsaldo); //se modifica el saldo
				st.executeQuery(modificacionunidades); //se modifican las unidades
			} 
			//Por otra parte, en cado de que el usuario no sea premium se va a comprobar si tiene saldo como para pagar el precio de la película
			else if(premium.equals("false") && saldo>=precio) {
				nuevosaldo=saldo-precio;
				modificacionsaldo="Update usuarios set saldo = "+nuevosaldo+" where nombreusuario LIKE '"+nombreusuario+"'"; //consulta de modificación de saldo 
				st.executeQuery(sql);
				st.executeQuery(modificacionsaldo);
				st.executeQuery(modificacionunidades);
			}	
			//En caso de que los usuarios no tengan saldo suficiente, la operación no se va a realizar
			else {
				operacionexitosa="false"; //cambio el estado de esta variable para que quede constancia de que no se puede realizar el alquiler
			}
			
			//cierro el statement
			st.close();

			
			return operacionexitosa; //devuelvo la cadena que especifica si la operación ha salido bien o no

		}catch(Exception e){
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}
	

	@Override
	/*Método que se va a utilizar cuando se quiera filtrar el listado de películas por un genero determinado*/
	public String filtrar(String genero) {
		Gson json= new Gson(); //objeto para JSON
		DBConnection con = new DBConnection();
		String sql ="";
		
		if(genero.equals("todas")) {
			 sql ="Select * from peliculas order by titulo"; //en caso de que "no se quiera filtrar"
		}else {
			sql ="Select * from peliculas where genero LIKE'"+genero+"' order by titulo"; //busco en la BD las películas con el genero que se ha pasado como parametro
		}
		
		try {
			ArrayList<Pelicula> datos= new ArrayList<Pelicula>(); //Donde se van a almacenar las películas obtenidas
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				
				//Se cogen los datos de las películas
				String id = rs.getString("id"); 
				String titulo= rs.getString("titulo"); 
				String  actorppal= rs.getString("actor_ppal"); 
				int copiasdisponibles = rs.getInt("copias_disponibles");
				String cadenaestreno=rs.getString("estreno");
				
				boolean estreno=false; //variable que determina el estreno (booleano)
				
				//comprobación y asignación del atributo estreno:
				/*Como en la base de datos se guarda como una cadena de texto, cuando se devuelve
				 * hay que comprobar su valor y dependiendo del que sea pasarlo a booleano*/
				
				//esto se repite varias veces, se podria OPTIMIZAR
				if(cadenaestreno.equals("true")) {
					estreno=true;
				}else if(cadenaestreno.equals("false")) {
					estreno=false;
				}
				
				Pelicula pelicula = new Pelicula(id,titulo,genero,actorppal,copiasdisponibles,estreno);
			

			//Se mira si hay copias de la película, en caso de que haya se añade a los datos que se 
			//van a devolver, si no no
				if(pelicula.getCopiasdisponibles()>0) {
					datos.add(pelicula);
				}
			}
			
		st.close();
		
		return json.toJson(datos); //paso el ArrayList de películas a un JSON Y LO DEVUELVO
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}

	@Override
	/*+ -> Método que va a devolver una película*/
	public String devolver(String usuario,int numero_alquiler) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		
		String sql ="DELETE from alquileres where usuario LIKE '"+usuario+"' and numero_alquiler="+numero_alquiler; //elimino de la tabla alquileres el alquiler correspondiente
		String cogerpelicula="Select pelicula from alquileres where numero_alquiler="+numero_alquiler;
		try {
			Statement st = con.getConnection().createStatement();

			//Esto lo hago ya para coger la película que almacena la fila de los alquileres y cuyo numero de alquiler es el que se le 
			//ha pasado como parámetro
			String idpelicula=""; //variable que va a almacenar la película que se va a devolver
			ResultSet rspelicula=st.executeQuery(cogerpelicula);
			while(rspelicula.next()) {
				idpelicula=rspelicula.getString("pelicula");
			}
			
			String sqlunidades="Select copias_disponibles from peliculas where id LIKE '"+idpelicula+"'"; //busco las copias de la pelicula que se va a devolver
			st.executeQuery(sql);
			int cantidad=0; //variable que va a almacenar la cantidad de películas que hay (sin contar la que se devuelve)
			ResultSet rs = st.executeQuery(sqlunidades);
			while(rs.next()) {
				cantidad=rs.getInt("copias_disponibles"); //se almacena la cantidad
			}
			String sqlmodificacion="UPDATE peliculas set copias_disponibles ="+(cantidad+1)+" where id LIKE '"+idpelicula+"'"; //modifico la fila de la película que se ha devuelto para sumarle a la cantidad la que se ha devuelto
			st.executeQuery(sqlmodificacion);
			st.close();
			
			return "Pelicula devuelta"; //mensaje que se devuelve para mostrar en la página
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}	
}
