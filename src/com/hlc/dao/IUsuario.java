package com.hlc.dao;

//Interfaz con todos los métodos necesarios referentes al propio usuario
public interface IUsuario {
	public String comprobarinicio(String nombreusuario, String clave);
	public String registrarusuario(String nombreusuario, String clave, String nombre, String apellidos, String email, float saldo, Boolean premium);
	public String modificarUsuario(String nombreusuario, String nombre,String apellidos,String clave,String email);
	public String verReservas(String nombreusuario);
	public String eliminarUsuario(String nombreusuario);
	public String comprobarSaldo(String nombreusuario); 
	public String modificarSaldo(String nombreusuario,float saldo,float asumar); 
}
