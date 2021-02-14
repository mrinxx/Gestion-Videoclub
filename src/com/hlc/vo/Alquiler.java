package com.hlc.vo;

public class Alquiler {
	private String fecha;
	private String titulo;
	private String genero;
	private String estreno;
	
	public Alquiler() {};
	
	public Alquiler(String fecha, String titulo, String genero, String estreno) {
		this.fecha = fecha;
		this.titulo = titulo;
		this.genero = genero;
		this.estreno = estreno;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEstreno() {
		return estreno;
	}

	public void setEstreno(String estreno) {
		this.estreno = estreno;
	}
}
