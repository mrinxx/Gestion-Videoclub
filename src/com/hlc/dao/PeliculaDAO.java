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
	private List<Pelicula> peliculas;
	
	public PeliculaDAO() {
		peliculas = new ArrayList<Pelicula>();
	}
	
	@Override
	public void cargarpeliculas() {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection(); //SIEMPRE se abre en el método la conexion y se cierra al final
		String sql="Select * from peliculas"; // se guarda en una variable lo que se quiere hacer
		try {
			Statement st = con.getConnection().createStatement();  //se crea un statement para poder mandar la variable con lo que se quiere hacer
			ResultSet rs = st.executeQuery(sql); //con esto se indica que va a devolver un set de resultados con la ejecucion de la consulta que le mando
			while (rs.next()) { //mientras haya datos en el set que se ha recibido en este caso se van creando alumnos para cargarlos en la BD
				String id = rs.getString("id"); 
				String titulo= rs.getString("titulo"); 
				String genero= rs.getString("genero"); 
				String  actorppal= rs.getString("actor_ppal"); 
				int copiasdisponibles = rs.getInt("copias_disponibles");
				String cadenaestreno=rs.getString("estreno");
				
				boolean estreno=false;
				if(cadenaestreno.equalsIgnoreCase("true")) {
					estreno=true;
				}else {
					estreno=false;
				}
				
				Pelicula a = new Pelicula(id,titulo,genero,actorppal,copiasdisponibles,estreno); //se crea el alumno
				peliculas.add(a); //se añade a una lista de alumnos
			}
			st.close(); // se cierra el Statement
			}catch(Exception e){
				System.out.println(e.getMessage());
			}finally {
				con.desconectar(); //siempre se desconecta
			}
		
	}

	@Override
	public String listarPeliculas() {
		Gson json= new Gson(); //objeto para JSON
		DBConnection con = new DBConnection();
		String sql ="Select * from peliculas";
		try {
			ArrayList<Pelicula> datos= new ArrayList<Pelicula>();
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
			
			//System.out.println(pelicula.toString());
			//Se mira si hay copias de la película, en caso de que haya se añade a los datos que se 
			//van a devolver, si no no
				if(pelicula.getCopiasdisponibles()>0) {
					datos.add(pelicula);
				}
			}
			
		st.close();
		
		return json.toJson(datos); //paso el ArrayList de películas a un JSON
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}

	@Override
	public String reservar(String idpelicula, String nombreusuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sqlsaldo= "Select saldo,premium from usuarios where nombreusuario LIKE'"+nombreusuario+"'"; //Para buscar el saldo del usuario
		String sqlprecio="Select precio,copias_disponibles from peliculas where id LIKE '"+idpelicula+"'"; //Para buscar el precio de la pelicula
		
		try {			
			Statement stsaldo = con.getConnection().createStatement();
			ResultSet rssaldo=stsaldo.executeQuery(sqlsaldo);
			double saldo = 0;
			int precio = 0;
			int copias = 0;
			String premium="";
			String operacionexitosa="true";
			while(rssaldo.next()) {
				saldo = rssaldo.getDouble("saldo");
				premium=rssaldo.getString("premium");
			}
			
			stsaldo.close();
			
			Statement stprecio = con.getConnection().createStatement();
			ResultSet rsprecio=stprecio.executeQuery(sqlprecio);
			while(rsprecio.next()) {
				precio = rsprecio.getInt("precio");
				copias = rsprecio.getInt("copias_disponibles");
			}
			double preciopremium=precio-(precio*0.10); //precio por si el usuario es premium
			
			Statement st = con.getConnection().createStatement();
			Statement stmodificacionsaldo = con.getConnection().createStatement();
			Statement stmodificacionunidades = con.getConnection().createStatement();
			double nuevosaldo=0; //almacenará el saldo resultante
			
			
			String sql ="Insert into alquileres (usuario,pelicula,fecha_alquiler) VALUES('" +nombreusuario+"','" +idpelicula+"','" +LocalDate.now().toString()+"')";
			String modificacionunidades="Update peliculas set copias_disponibles = "+(copias-1)+" where id LIKE '"+idpelicula+"'";
			String modificacionsaldo="";
			
			if(premium.equals("true") && saldo>=preciopremium && saldo>0.9){
				nuevosaldo=saldo-preciopremium;
				modificacionsaldo="Update usuarios set saldo = "+nuevosaldo+" where nombreusuario LIKE '"+nombreusuario+"'";
				st.executeQuery(sql);
				stmodificacionsaldo.executeQuery(modificacionsaldo);
				stmodificacionunidades.executeQuery(modificacionunidades);
			} 
			else if(premium.equals("false") && saldo>=precio) {
				nuevosaldo=saldo-precio;
				modificacionsaldo="Update usuarios set saldo = "+nuevosaldo+" where nombreusuario LIKE '"+nombreusuario+"'";
				st.executeQuery(sql);
				stmodificacionsaldo.executeQuery(modificacionsaldo);
				stmodificacionunidades.executeQuery(modificacionunidades);
			}	
			else {
				operacionexitosa="false";
			}
			
			st.close();
			stmodificacionsaldo.close();
			stmodificacionunidades.close();
			
			return operacionexitosa;

		}catch(Exception e){
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}
	

	@Override
	public String filtrar(String genero) {
		Gson json= new Gson(); //objeto para JSON
		DBConnection con = new DBConnection();
		String sql ="Select * from peliculas where genero LIKE'"+genero+"'";
		try {
			ArrayList<Pelicula> datos= new ArrayList<Pelicula>();
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				
				String id = rs.getString("id"); 
				String titulo= rs.getString("titulo"); 
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
			
			//System.out.println(pelicula.toString());
			//Se mira si hay copias de la película, en caso de que haya se añade a los datos que se 
			//van a devolver, si no no
				if(pelicula.getCopiasdisponibles()>0) {
					datos.add(pelicula);
				}
			}
			
		st.close();
		
		return json.toJson(datos); //paso el ArrayList de películas a un JSON
		
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}	
}
