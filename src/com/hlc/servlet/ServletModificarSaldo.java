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
 * Servlet implementation class ServletModificarSaldo
 */
@WebServlet("/ServletModificarSaldo")
public class ServletModificarSaldo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletModificarSaldo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UsuarioDAO usuario = new UsuarioDAO();
		//Se coge de la petici�n el nombre de usuario que tiene que cambiar el saldo, su saldo actual y lo que se le a�ade a tal cantidad
		String nombreusuario=request.getParameter("usuario");
		float saldo=Float.parseFloat(request.getParameter("saldoactual"));
		float asumar=Float.parseFloat(request.getParameter("cantidadasumar"));
		
		String ret = usuario.modificarSaldo(nombreusuario,saldo,asumar); //se llama al m�todo que modifica el saldo
		response.setContentType("text/html;charset=UTF-8");
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
