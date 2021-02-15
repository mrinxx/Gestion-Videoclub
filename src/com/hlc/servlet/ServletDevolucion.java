package com.hlc.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hlc.dao.PeliculaDAO;

/**
 * Servlet implementation class ServletDevolver
 */
@WebServlet("/ServletDevolucion")
public class ServletDevolucion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDevolucion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PeliculaDAO pelicula=new PeliculaDAO();
		String id=request.getParameter("id"); //se coge de la request el id de la pelicula
		String usuario=request.getParameter("usuario");//se coge de la request el nombre del usuario
		
		String ret=pelicula.devolver(id, usuario); //se llama al método para realizar la devolucion
		PrintWriter out = response.getWriter();
		out.print(ret);
		
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
