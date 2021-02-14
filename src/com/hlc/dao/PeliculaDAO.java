package com.hlc.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hlc.connection.DBConnection;
import com.hlc.vo.Pelicula;


public class PeliculaDAO implements IPelicula{

	@Override
	/*M�todo que se utilizar� en el momento de aparici�n de la web. Lo que hace es devolver un string en formato JSON con todas las pel�culas que hay en la base 
	de datos*/
	public String listarPeliculas() {
		Gson json= new Gson(); //objeto para JSON
		DBConnection con = new DBConnection();
		String sql ="Select * from peliculas"; //cojo todas las pel�culas de la BD
		try {
			ArrayList<Pelicula> datos= new ArrayList<Pelicula>(); //ArrayList donde se van a almacenar cada una de las pel�culas que se vayan obteniendo
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
				
				//comprobaci�n y asignaci�n del atributo estreno:
				/*Como en la base de datos se guarda como una cadena de texto, cuando se devuelve
				 * hay que comprobar su valor y dependiendo del que sea pasarlo a booleano*/
				if(cadenaestreno.equals("true")) {
					estreno=true;
				}else if(cadenaestreno.equals("false")) {
					estreno=false;
				}
				
				Pelicula pelicula = new Pelicula(id,titulo,genero,actorppal,copiasdisponibles,estreno);
			
			//System.out.println(pelicula.toString());
			//Se mira si hay copias de la pel�cula, en caso de que haya se a�ade a los datos que se 
			//van a devolver, si no no
				if(pelicula.getCopiasdisponibles()>0) {
					datos.add(pelicula);
				}
			}
			
		st.close();
		
		return json.toJson(datos); //paso el ArrayList de pel�culas a un JSON y lo paso como respuesta
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}

	@Override
	/*M�todo que se va a utilizar para la reserva de una pel�cula por parte de un usuario*/
	public String reservar(String idpelicula, String nombreusuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sqlsaldo= "Select saldo,premium from usuarios where nombreusuario LIKE'"+nombreusuario+"'"; //Para buscar el saldo del usuario
		String sqlprecio="Select precio,copias_disponibles from peliculas where id LIKE '"+idpelicula+"'"; //Para buscar el precio de la pelicula
		
		try {			
			Statement stsaldo = con.getConnection().createStatement();
			ResultSet rssaldo=stsaldo.executeQuery(sqlsaldo);
			double saldo = 0; //para almacenar el saldo que tiene el usuario
			int precio = 0; //para almacenar el precio de la pel�cula en cuesti�n
			int copias = 0; //para almacenar las copias de la pelicula en cuestion
			String premium=""; //para almacenar si el usuario es premium o no
			String operacionexitosa="true"; //Este string va a controlar si se ha podio hacer la reserva o no tras comprobar una serie de requerimientos para que se pueda realizar
			
			//Se van a coger el saldo y si es premium o no del usuario que intenta realizar la reserva
			while(rssaldo.next()) {
				saldo = rssaldo.getDouble("saldo"); 
				premium=rssaldo.getString("premium");
			}
			
			stsaldo.close();
			
			Statement stprecio = con.getConnection().createStatement();
			ResultSet rsprecio=stprecio.executeQuery(sqlprecio);
			
			//Se va a coger el precio y las copias disponibles de la pel�cula que se quiere alquilar
			while(rsprecio.next()) {
				precio = rsprecio.getInt("precio"); 
				copias = rsprecio.getInt("copias_disponibles");
			}
			
			double preciopremium=precio-(precio*0.10); //precio por si el usuario es premium
			
			Statement st = con.getConnection().createStatement();
			Statement stmodificacionsaldo = con.getConnection().createStatement();
			Statement stmodificacionunidades = con.getConnection().createStatement();
			double nuevosaldo=0; //almacenar� el saldo resultante
			
			
			String sql ="Insert into alquileres (usuario,pelicula,fecha_alquiler) VALUES('" +nombreusuario+"','" +idpelicula+"','" +LocalDate.now().toString()+"')"; //inserci�n en la BD del alquiler de la pelicula
			String modificacionunidades="Update peliculas set copias_disponibles = "+(copias-1)+" where id LIKE '"+idpelicula+"'"; //Actualizaci�n de las copias de la pelicula de la reserva
			String modificacionsaldo="";
			
			//En caso de que el usuario sea premium se va a comprobar que el saldo sea mayor al precio (descontando el 10% por ser premium).
			if(premium.equals("true") && saldo>=preciopremium){
				nuevosaldo=saldo-preciopremium; //se modifica el saldo del usuario
				modificacionsaldo="Update usuarios set saldo = "+nuevosaldo+" where nombreusuario LIKE '"+nombreusuario+"'"; //consulta de modificaci�n de saldo 
				st.executeQuery(sql);
				stmodificacionsaldo.executeQuery(modificacionsaldo); //se modifica el saldo
				stmodificacionunidades.executeQuery(modificacionunidades); //se modifican las unidades
			} 
			//Por otra parte, en cado de que el usuario no sea premium se va a comprobar si tiene saldo como para pagar el precio de la pel�cula
			else if(premium.equals("false") && saldo>=precio) {
				nuevosaldo=saldo-precio;
				modificacionsaldo="Update usuarios set saldo = "+nuevosaldo+" where nombreusuario LIKE '"+nombreusuario+"'"; //consulta de modificaci�n de saldo 
				st.executeQuery(sql);
				stmodificacionsaldo.executeQuery(modificacionsaldo);
				stmodificacionunidades.executeQuery(modificacionunidades);
			}	
			//En caso de que los usuarios no tengan saldo suficiente, la operaci�n no se va a realizar
			else {
				operacionexitosa="false"; //cambio el estado de esta variable para que quede constancia de que no se puede realizar el alquiler
			}
			
			//cierro todos los statements
			st.close();
			stmodificacionsaldo.close();
			stmodificacionunidades.close();
			
			return operacionexitosa; //devuelvo la cadena que especifica si la operaci�n ha salido bien o no

		}catch(Exception e){
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}
	

	@Override
	/*M�todo que se va a utilizar cuando se quiera filtrar el listado de pel�culas por un genero determinado*/
	public String filtrar(String genero) {
		Gson json= new Gson(); //objeto para JSON
		DBConnection con = new DBConnection();
		String sql ="Select * from peliculas where genero LIKE'"+genero+"'"; //busco en la BD las pel�culas con el genero que se ha pasado como parametro
		try {
			ArrayList<Pelicula> datos= new ArrayList<Pelicula>(); //Donde se van a almacenar las pel�culas obtenidas
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				
				//Se cogen los datos de las pel�culas
				String id = rs.getString("id"); 
				String titulo= rs.getString("titulo"); 
				String  actorppal= rs.getString("actor_ppal"); 
				int copiasdisponibles = rs.getInt("copias_disponibles");
				String cadenaestreno=rs.getString("estreno");
				
				boolean estreno=false; //variable que determina el estreno (booleano)
				
				//comprobaci�n y asignaci�n del atributo estreno:
				/*Como en la base de datos se guarda como una cadena de texto, cuando se devuelve
				 * hay que comprobar su valor y dependiendo del que sea pasarlo a booleano*/
				
				//esto se repite varias veces, se podria OPTIMIZAR
				if(cadenaestreno.equals("true")) {
					estreno=true;
				}else if(cadenaestreno.equals("false")) {
					estreno=false;
				}
				
				Pelicula pelicula = new Pelicula(id,titulo,genero,actorppal,copiasdisponibles,estreno);
			

			//Se mira si hay copias de la pel�cula, en caso de que haya se a�ade a los datos que se 
			//van a devolver, si no no
				if(pelicula.getCopiasdisponibles()>0) {
					datos.add(pelicula);
				}
			}
			
		st.close();
		
		return json.toJson(datos); //paso el ArrayList de pel�culas a un JSON Y LO DEVUELVO
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}

	@Override
	/*+ -> M�todo que va a devolver una pel�cula*/
	public String devolver(String idpelicula, String usuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql ="DELETE from alquileres where usuario LIKE '"+usuario+"' and pelicula LIKE '"+idpelicula+"'"; //elimino de la tabla alquileres el alquiler correspondiente
		String sqlunidades="Select copias_disponibles from peliculas where id LIKE '"+idpelicula+"'"; //busco las copias de la pelicula que se va a devolver
		try {
			Statement st = con.getConnection().createStatement();
			st.executeQuery(sql); 
			st.close();
			int cantidad=0; //variable que va a almacenar la cantidad de pel�culas que hay (sin contar la que se devuelve)
			Statement stcogercantidad = con.getConnection().createStatement();
			ResultSet rs = stcogercantidad.executeQuery(sqlunidades);
			while(rs.next()) {
				cantidad=rs.getInt("copias_disponibles"); //se almacena la cantidad
			}
			stcogercantidad.close();
			Statement stmodificarcantidad = con.getConnection().createStatement();
			String sqlmodificacion="UPDATE peliculas set copias_disponibles ="+(cantidad+1)+" where id LIKE '"+idpelicula+"'"; //modifico la fila de la pel�cula que se ha devuelto para sumarle a la cantidad la que se ha devuelto
			stmodificarcantidad.executeQuery(sqlmodificacion);
			stmodificarcantidad.close();
			
			return "Pelicula devuelta"; //mensaje que se devuelve para mostrar en la p�gina
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}	
}
