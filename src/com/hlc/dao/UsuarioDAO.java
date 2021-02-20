package com.hlc.dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.hlc.connection.DBConnection;
import com.hlc.vo.Alquiler;



public class UsuarioDAO implements IUsuario {

	@Override
	/*Método que se va a utilizar cuando el usuario inicie sesión, para ver si los datos son o no correctos*/
	public String comprobarinicio(String nombreusuario, String clave) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql="Select * from usuarios where nombreusuario='"+nombreusuario+"'and clave='"+clave+"'";  //búsqueda del usuario en la BD
		Boolean encontrado=false; //Booleano que indicará si el usuario está o no en la BD
		try {
			Statement st = con.getConnection().createStatement(); 
			ResultSet rs = st.executeQuery(sql);
			/*De lo que devuelve la consulta, se va a coger el nombre de usuario y la clave (aunque no haya datos en ellos lo devuelve pero vacio)*/
			while(rs.next()) {
			String nombre_usu= rs.getString("nombreusuario");
			String pass= rs.getString("clave");
			/*En caso de que los datos coincidan con los que se han pasado como parámetros desde el inicio de sesión, significará que los datos son correctos
			 * y la variable encontrado cambia su valor para dejar constancia de que el usuario es correcto*/
			if(nombre_usu.equals(nombreusuario) && pass.equals(clave)) {
				encontrado=true;
				}
			}
			st.close();
			//En caso del valor de encontrado, se devolverá ok (si se encuentra) o not ok ("si no se encuentra")
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
	/*Método que va a ser utilizado en el momento en el que se vaya a registrar un usuario*/
	public String registrarusuario(String nombreusuario, String clave, String nombre, String apellidos, String email, float saldo, Boolean premium) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String mensaje=""; //mensaje que se va a devolver al final de la funcion
		String sqlcomprobacionusuario="SELECT * from usuarios"; //Se cojen todos los datos de los usuarios
		Boolean valido=true; //para ver si el usuario es válido
		try {
			Statement st = con.getConnection().createStatement(); 
			ResultSet rscomprobacionusuario = st.executeQuery(sqlcomprobacionusuario);
			while(rscomprobacionusuario.next()) {
				String nombre_usu= rscomprobacionusuario.getString("nombreusuario");
				String correo= rscomprobacionusuario.getString("email");
				if(nombre_usu.equals(nombreusuario) || correo.equals(email)) {
					valido=false;
				}
			}
			/*En caso de que el usuario sea válido se procederá a insertarlo en la BD como un nuevo usuario de la aplicación*/
			if(valido) {
				String sql ="Insert into usuarios(nombreusuario,clave,nombre,apellidos,email,saldo,premium) VALUES('" +nombreusuario+"','" +clave+"','" +nombre+"','"+apellidos+"','"+email+"',"+saldo+",'"+premium+"')";
				st.executeQuery(sql);
				st.close();
				mensaje="ok"; //se establece el mensaje que se va a devolver como ok
			}else{
				mensaje="not ok"; //mensaje pasa a ser not ok ya que el usuario no era valido
				}
			
			return mensaje;
			}
			catch(Exception e) {
				return e.getMessage();
			}finally {
				con.desconectar();
			}
	 }

	@Override
	/*Método para usarse en el momento de ver las reservas de un determinado usuaario en el panel del mismo*/
	public String verReservas(String nombreusuario) {
		// TODO Auto-generated method stub
		Gson json= new Gson(); 
		DBConnection con = new DBConnection();
		String sql ="Select * from alquileres alq, peliculas p where alq.pelicula=p.id and alq.usuario LIKE '"+nombreusuario+"' order by p.titulo"; //Seleccion de los alquileres relacionados con el usuario 
		try {
			
			ArrayList<Alquiler> datos= new ArrayList<Alquiler>(); //ArrayList que va a almacenar los alquileres del usuario
			
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			//Se cogen los datos necesarios 
			while(rs.next()) {
				String idpelicula=rs.getString("id");
				int numero_alquiler=rs.getInt("numero_alquiler");
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
				Alquiler alquiler=new Alquiler(numero_alquiler,idpelicula,fecha,titulo,genero,estreno); //uso esta clase para poder enviar los datos
				datos.add(alquiler); //añado el alquiler a los datos que se van a devolver
				
			}
			
		st.close();
		return json.toJson(datos); //devuelvo la lista de los datos en JSON 
		}catch(Exception e){
			System.out.println(e.getMessage());
			return e.getMessage();
		}finally {
			con.desconectar();
		}
	}

	@Override
	/*Método que se utilizará en el momento de modificar los datos de un usuario desde su panel de usuario*/
	public String modificarUsuario(String nombreusuario, String nombre,String apellidos,String clave,String email,String premium) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sqldatos="select * from usuarios where nombreusuario LIKE '"+nombreusuario+"'"; //selecciono los datos actuales del usuario
		
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs = st.executeQuery(sqldatos);
			//Las siguientes variables guardarán los datos del usuario que tienen en la base de datos 
			String nombrebd="";
			String apellidosbd=""; 
			String clavebd= ""; 
			String emailbd= "";
			String premiumbd="";
			while(rs.next()) {
				nombrebd=rs.getString("nombre");
				apellidosbd= rs.getString("apellidos"); 
				clavebd= rs.getString("clave"); 
				emailbd=rs.getString("email");
				premiumbd=rs.getString("premium");
			}
			
			//en caso de que algunos de los campos de ese formulario NO estén completos, se pondrán los datos que ya se tenían en la BD relacionados con el mismo
			//si esto no se hiciese así, quedarían datos en blanco
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
			if(premium.equals("")) {
				premium=premiumbd;
			}
			
			String sqlmodificacion="Update usuarios SET nombre='"+nombre+"', apellidos='"+apellidos+"',clave='"+clave+"', email='"+email+"', premium='"+premium+"' where nombreusuario LIKE '"+nombreusuario+"'";
			st.executeQuery(sqlmodificacion); //se modifica el usuario 
			st.close();
			return "OK"; //se devuelve para mostrarse en la web este mensaje
		}catch(Exception e){
			return e.getMessage();
		}finally{
			con.desconectar();
		}
	}

	@Override
	/*Método para eliminar al usuario al pulsar el botón especifico para ello desde la BD*/
	public String eliminarUsuario(String nombreusuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql ="DELETE from usuarios where nombreusuario LIKE '"+nombreusuario+"'"; //Sentencia para eliminar al usuario pasado como parámetro de la BD
		try {
			Statement st = con.getConnection().createStatement();
			st.executeQuery(sql);
			return "Usuario eliminado correctamente";  //mensaje que se devuelve para mostrar en la web
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
			}
	}

	@Override
	/*Método que se va a usar para comprobar el saldo del usuario que esté logueado nada más entrar en el panel del usuario*/
	public String comprobarDatos(String nombreusuario) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		String sql ="SELECT * from usuarios where nombreusuario LIKE '"+nombreusuario+"'"; //consulta para obtener el saldo
		String datos="";
		try {
			Statement st = con.getConnection().createStatement();
			ResultSet rs=st.executeQuery(sql);
			while(rs.next()) {
				String saldo=Float.toString(rs.getFloat("saldo")); //cojo el saldo de la consulta y haco la conversion de float a string
				String nombre=rs.getString("nombre");
				String apellidos=rs.getString("apellidos");
				String clave=rs.getString("clave");
				String email=rs.getString("email");
				String premium=rs.getString("premium");
				
				datos=nombre+","+apellidos+","+clave+","+email+","+premium+","+saldo; //se almacenan los datos obtenidos en la cadena que se va a devolver
			}
						
			st.close();
			return datos; //devuelvo todos los datos que se han obtenido
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
			}
		
	}

	@Override
	/*Método para modificar el saldo de un determinado usuario . Recibe el usuario a modificar, el saldo que tiene y la cantidad que se le va a añadir a dicho saldo*/
	public String modificarSaldo(String nombreusuario, float saldo,float asumar) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		//Consulta de modificación en la que se establece el saldo del usuario como la suma de la cantidad original+la cantidad que se añade desde el formulario
		String sql ="UPDATE usuarios SET saldo ="+(saldo+asumar)+" where nombreusuario LIKE '"+nombreusuario+"'";

		try {
			Statement st = con.getConnection().createStatement();
			st.executeQuery(sql);
			st.close();
			return "OK"; //devuelvo el mensaje a mostrar en la web
		}catch(Exception e) {
			return e.getMessage();
		}finally {
			con.desconectar();
			}
	}
	
}