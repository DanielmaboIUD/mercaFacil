package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.VistaDetalleVentaDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.VistaDetalleVenta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/detalle-ventas")
public class VistaDetalleVentaServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/detalle-ventas/lista.jsp";

    private final VistaDetalleVentaDAO dao = new VistaDetalleVentaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<VistaDetalleVenta> registros = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        String error = null;
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            registros = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            getServletContext().log("Error en detalle de ventas: " + e.getMessage(), e);
            error = e.getMessage();
        }
        request.setAttribute("registros", registros);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }
}
