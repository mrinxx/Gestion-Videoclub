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
 * Servlet implementation class ServletModificacionUsuario
 */
@WebServlet("/ServletModificacionUsuario")
public class ServletModificacionUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletModificacionUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UsuarioDAO usuario=new UsuarioDAO();

		//Se toman todos los valores correspondientes al formulario de cambio de datos y que han sido pasados por la request
		String nombreusuario = request.getParameter("nombreusuario");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		String clave = request.getParameter("clave");
		String email= request.getParameter("email");
		String premium= request.getParameter("premium");


		
		String ret = usuario.modificarUsuario(nombreusuario,nombre,apellidos,clave,email,premium); //se llama al método que realiza este cambio
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(ret);
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
