package com.hlc.dao;

//Interfaz con todos los métodos necesarios referentes a las películas

public interface IPelicula {
	public String listarPeliculas(); 
	public String reservar(String idpelicula, String nombreusuario);
	public String filtrar(String genero);
	public String devolver(String idpelicula,String usuario);
}
