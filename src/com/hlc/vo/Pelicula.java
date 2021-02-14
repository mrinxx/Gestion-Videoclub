package com.hlc.vo;

public class Pelicula {
	private String id;
	private String titulo;
	private String genero;
	private String actorppal;
	private int copiasdisponibles;
	private boolean estreno;
	private float precio; //podria ser int pero lo pongo así por la rebaja 
	
	public Pelicula() {}
	
	public Pelicula(String id, String titulo, String genero, String actorppal, int copiasdisponibles, Boolean estreno) {
		this.id=id;
		this.titulo=titulo;
		this.genero=genero;
		this.actorppal=actorppal;
		this.copiasdisponibles=copiasdisponibles;
		this.estreno=estreno;
		this.precio=calcularprecio(estreno); //llamo a una funcion para que ponga el precio de alquiler 
	}
	
	//en caso de que la pelicula sea de estreno, el precio va a ser 2 y por el contrario de 1
	//Esto se hace para que el momento de la creación dependiendo del atributo estreno se 
	//calcule su precio
	private float calcularprecio(boolean estreno) {
		if(estreno==true) {
				return 2;
		}
		else {
			return 1;
		}
	}
	
	@Override
	public String toString() {
		//Esto lo hago para mostrar si o no en vez de true false.
		
		String cadenaestreno="";
		if(estreno=true) {
			cadenaestreno="Si";
		}else {
			cadenaestreno="No";
		}
		
		return "-Id:" + id + "-Titulo:" + titulo + "-Actor Principal:" + actorppal + "-Copias disponibles:"
				+ copiasdisponibles + "-Estreno=" + cadenaestreno;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getGenero() {
		return genero;
	}
	

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getActorppal() {
		return actorppal;
	}

	public void setActorppal(String actorppal) {
		this.actorppal = actorppal;
	}

	public int getCopiasdisponibles() {
		return copiasdisponibles;
	}

	public void setCopiasdisponibles(int copiasdisponibles) {
		this.copiasdisponibles = copiasdisponibles;
	}

	public boolean isEstreno() {
		return estreno;
	}

	public void setEstreno(boolean estreno) {
		this.estreno = estreno;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}	
}
