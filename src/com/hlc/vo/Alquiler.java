package com.hlc.vo;

/*Esta función no viene explícitamente en el enunciodo pero la utilizo para que en el momento de ver las reservas del usuario, sea más fácil
 * guardar los datos de las mismas en un ArrayList para pasarlo a JSON */
public class Alquiler {
	/*Los campos que tiene esta clase son los que se necesitan en el momento de visualizar las reservas excepto el id de la película, el cual 
	 * usaré para realizar acciones*/
	private int numero_alquiler;
	private String id; //id de la película
	private String fecha; //fecha del alquiler
	private String titulo; //titulo de la pelicula
	private String genero; //genero de la pelicula
	private String estreno; //si la pelicula es de estreno o no
	
	public Alquiler() {};
	
	public Alquiler(int numero_alquiler,String id,String fecha, String titulo, String genero, String estreno) {
		this.numero_alquiler=numero_alquiler;
		this.id=id;
		this.fecha = fecha;
		this.titulo = titulo;
		this.genero = genero;
		this.estreno = estreno;
	}

	
	public int getNumero_alquiler() {
		return numero_alquiler;
	}

	public void setNumero_alquiler(int numero_alquiler) {
		this.numero_alquiler = numero_alquiler;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
