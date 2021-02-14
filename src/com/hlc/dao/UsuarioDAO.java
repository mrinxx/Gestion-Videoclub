package com.hlc.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.hlc.connection.DBConnection;
import com.hlc.vo.Alquiler;


public class UsuarioDAO implements IUsuario {

	@Override
	public String comprobarinicio(String nombreusuario, String clave) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql="Select * from usuarios where nombreusuario='"+nombreusuario+"'and clave='"+clave+"'"; 
		Boolean encontrado=false;
		try {
			Statement st = con.getConnection().createStatement(); 
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
			String nombre_usu= rs.getString("nombreusuario");
			String pass= rs.getString("clave");
			if(nombre_usu.equals(nombreusuario) && pass.equals(clave)) {
				encontrado=true;
				}
			}
			st.close();
			if(encontrado) {
				return "ok";
			}else {
				return "not ok";
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
            return(e.getMessage());
		}finally {
			con.desconectar();
		}
		
		
	}

	@Override
	public String registrarusuario(String nombreusuario, String clave, String nombre, String apellidos, String email, float saldo, Boolean premium) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String mensaje="";
		String sqlcomprobacionusuario="SELECT * from usuarios";
		Boolean valido=true; //para ver si el usuario es válido
		try {
			Statement stcomprobacionusuario = con.getConnection().createStatement(); 
			ResultSet rscomprobacionusuario = stcomprobacionusuario.executeQuery(sqlcomprobacionusuario);
			while(rscomprobacionusuario.next()) {
				String nombre_usu= rscomprobacionusuario.getString("nombreusuario");
				String correo= rscomprobacionusuario.getString("email");
				if(nombre_usu.equals(nombreusuario) || correo.equals(email)) {
					valido=false;
				}
			}
			stcomprobacionusuario.close();
			}
			catch(Exception e) {
				return(e.getMessage());
			}

			if(valido) {
				try {
					String sql ="Insert into usuarios(nombreusuario,clave,nombre,apellidos,email,saldo,premium) VALUES('" +nombreusuario+"','" +clave+"','" +nombre+"','"+apellidos+"','"+email+"',"+saldo+",'"+premium+"')";
					Statement st = con.getConnection().createStatement(); 
					ResultSet rs = st.executeQuery(sql);
					st.close();
					mensaje="ok";
				}catch(Exception e) {
					return(e.getMessage());
				}
			}else{
				mensaje="not ok";
			}
			
			return mensaje;
		}

	@Override
	public String verReservas(String nombreusuario) {
		// TODO Auto-generated method stub
		Gson json= new Gson(); 
		DBConnection con = new DBConnection();
		String sql ="Select * from alquileres alq, peliculas p where alq.pelicula=p.id and alq.usuario LIKE '"+nombreusuario+"'";
		try {
			
			ArrayList<Alquiler> datos= new ArrayList<Alquiler>();
			
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				String fecha=rs.getString("fecha_alquiler");
				String titulo= rs.getString("titulo"); 
				String genero= rs.getString("genero"); 
				String cadenaestreno=rs.getString("estreno");
				String estreno=""; 
				
				//comprobación y asignación del atributo estreno:
				/*Como en la base de datos se guarda como una cadena de texto, cuando se devuelve
				 * hay que comprobar su valor y dependiendo del que sea pasarlo a una cadena para pasarla a la clase 
				 * Alquiler*/
				
				if(cadenaestreno.equals("true")) {
					estreno="Si";
				}else if(cadenaestreno.equals("false")) {
					estreno="No";
				}
				Alquiler alquiler=new Alquiler(fecha,titulo,genero,estreno); //uso esta clase para poder enviar los datos
				datos.add(alquiler);
				
			}
			
		st.close();
		return json.toJson(datos); 
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}

	@Override
	public String modificarUsuario(String nombreusuario, String nombre,String apellidos,String clave,String email) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sqldatos="select * from usuarios where nombreusuario LIKE '"+nombreusuario+"'";
		
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sqldatos);
			String nombrebd="";
			String apellidosbd=""; 
			String clavebd= ""; 
			String emailbd= "";
			while(rs.next()) {
				nombrebd=rs.getString("nombre");
				apellidosbd= rs.getString("apellidos"); 
				clavebd= rs.getString("clave"); 
				emailbd=rs.getString("email");
			}
			
			if(nombre.equals("")) {
				nombre=nombrebd;
			}
			if(apellidos.equals("")) {
				apellidos=apellidosbd;
			}
			if(clave.equals("")) {
				clave=clavebd;
			}
			if(email.equals("")) {
				email=emailbd;
			}
			
			st.close();
			String sqlmodificacion="Update usuarios SET nombre='"+nombre+"', apellidos='"+apellidos+"',clave='"+clave+"', email='"+email+"' where nombreusuario LIKE '"+nombreusuario+"'";
			Statement stmodificacion = con.getConnection().createStatement();
			stmodificacion.executeQuery(sqlmodificacion);
			stmodificacion.close();
			return "Datos modificados correctamente";
		}catch(Exception e){
			return e.getMessage();
		}finally{
			con.desconectar();
		}
	}

	@Override
	public String eliminarUsuario(String nombreusuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql ="DELETE from usuarios where nombreusuario LIKE '"+nombreusuario+"'";
		try {
			Statement st = con.getConnection().createStatement();
			st.executeQuery(sql);
			return "Usuario eliminado correctamente";
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
			}
	}

	@Override
	public String comprobarSaldo(String nombreusuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql ="SELECT saldo from usuarios where nombreusuario LIKE '"+nombreusuario+"'";
		String saldo="";
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs=st.executeQuery(sql);
			while(rs.next()) {
				saldo=Float.toString(rs.getFloat("saldo"));
			}
			st.close();
			return saldo;
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
			}
		
	}

	@Override
	public String modificarSaldo(String nombreusuario, float saldo,float asumar) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		System.out.println(saldo+asumar);
		String sql ="UPDATE usuarios SET saldo ="+(saldo+asumar)+" where nombreusuario LIKE '"+nombreusuario+"'";

		try {
			Statement st = con.getConnection().createStatement();
			st.executeQuery(sql);
			st.close();
			return "Saldo modificado exitosamente";
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
			}
	}	
}