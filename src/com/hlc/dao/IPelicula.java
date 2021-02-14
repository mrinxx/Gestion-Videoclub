package com.hlc.dao;



public interface IPelicula {
	public void cargarpeliculas();
	public String listarPeliculas();
	public String reservar(String idpelicula, String nombreusuario);
	public String filtrar(String genero);
}
