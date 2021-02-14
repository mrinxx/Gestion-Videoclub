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
 * Servlet implementation class ServletInicioSesion
 */
@WebServlet("/ServletInicioSesion")
public class ServletInicioSesion extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ServletInicioSesion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UsuarioDAO usuario = new UsuarioDAO();
		String nombreusuario=request.getParameter("nombreusuario");
		String clave=request.getParameter("clave");

		
		String ret = usuario.comprobarinicio(nombreusuario,clave);
		response.setContentType("text/html;charset=UTF-8");
		System.out.println(ret);
		PrintWriter out = response.getWriter();
		out.print(ret); //quito ln para quitar el salto de linea y poder comparar cadenas
		
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
