package com.hlc.vo;

public class Usuario {
	private String nombreusuario;
	private String clave;
	private String nombre;
	private String apellidos;
	private String email;
	private float saldo;
	private Boolean premium;
	
	public Usuario() {}
	
	public Usuario(String nombreusuario, String clave, String nombre, String apellidos, String email, float saldo, Boolean premium) {
		this.nombreusuario=nombreusuario;
		this.clave=clave;
		this.nombre=nombre;
		this.apellidos=apellidos;
		this.email=email;
		this.saldo=saldo;
		this.premium=premium;
	}

		
	@Override
	public String toString() {
		//Esto lo hago para mostrar si o no en vez de true false.
		
		String cadenapremium="";
			if(premium=true) {
				cadenapremium="Si";
			}else {
				cadenapremium="No";
			}
				
		return "-Nombre de usuario: " + nombreusuario + "-Clave: " + clave + "-Nombre: " + nombre + "-Apellidos: "
				+ apellidos + "-email: " + email + "-Saldo:" + saldo + "-Premium:" + cadenapremium;
	}

	public String getNombreusuario() {
		return nombreusuario;
	}

	public void setNombreusuario(String nombreusuario) {
		this.nombreusuario = nombreusuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	public Boolean getPremium() {
		return premium;
	}

	public void setPremium(Boolean premium) {
		this.premium = premium;
	}
	
	
}
