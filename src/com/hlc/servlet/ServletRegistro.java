package com.hlc.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hlc.dao.UsuarioDAO;

/**
 * Servlet implementation class ServletRegistro
 */
@WebServlet("/ServletRegistro")
public class ServletRegistro extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletRegistro() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UsuarioDAO usuario = new UsuarioDAO();
		
		//Se cogen los datos correspondientes al formulario de registro que se han pasado por la petición
		String nombreusuario=request.getParameter("nombreusuario");
		String clave=request.getParameter("clave");
		String nombre= request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		String email = request.getParameter("email");
		float saldo=Float.parseFloat(request.getParameter("saldo"));
		String cadenapremium=request.getParameter("premium");
		
		//Aquí se transforma el valor de lo que se recibe como premium desde la petición, lo cual es una cadena, al tipo correcto, booleano
		Boolean premium=false;
		if (cadenapremium.equalsIgnoreCase("true")) {
			premium=true;
		}
		
		String respuesta = usuario.registrarusuario(nombreusuario,clave,nombre,apellidos,email,saldo,premium); 
		response.setContentType("text/html;charset=UTF-8");
		System.out.println(respuesta);
		PrintWriter out = response.getWriter();
		out.print(respuesta);
		
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
