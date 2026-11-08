package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.ProveedorDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.Proveedor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/proveedores")
public class ProveedorServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/proveedores/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/proveedores/formulario.jsp";

    private final ProveedorDAO dao = new ProveedorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new Proveedor(), true, null);
        } else if ("editar".equals(accion)) {
            editar(request, response);
        } else {
            mostrarLista(request, response, null);
        }
    }

    // guardar y eliminar solo por POST: un GET no debe modificar datos.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("guardar".equals(accion)) {
            guardar(request, response);
        } else if ("eliminar".equals(accion)) {
            eliminar(request, response);
        } else {
            redirigirALista(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Proveedor proveedor = dao.buscarPorId(request.getParameter("id"));
            if (proveedor == null) {
                mostrarLista(request, response, "El proveedor ya no existe.");
            } else {
                mostrarFormulario(request, response, proveedor, false, null);
            }
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        Proveedor proveedor = new Proveedor(limpio(request, "nit"), limpio(request, "nombre"),
                limpio(request, "direccion"), limpio(request, "telefono"));
        String error = validar(proveedor);
        if (error != null) {
            mostrarFormulario(request, response, proveedor, esNuevo, error);
            return;
        }
        try {
            if (esNuevo) {
                dao.insertar(proveedor);
            } else {
                dao.actualizar(proveedor);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarFormulario(request, response, proveedor, esNuevo, e.getMessage());
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            dao.eliminar(request.getParameter("id"));
            Flash.exito(request, "Registro eliminado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            Flash.error(request, e.getMessage());
            redirigirALista(request, response);
        }
    }

    private String validar(Proveedor proveedor) {
        return Validador.primerError(
                Validador.texto(proveedor.getNit(), "NIT", 15),
                Validador.texto(proveedor.getNombre(), "Nombre", 100),
                Validador.texto(proveedor.getDireccion(), "Dirección", 120),
                Validador.texto(proveedor.getTelefono(), "Teléfono", 15));
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<Proveedor> proveedores = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            proveedores = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   Proveedor proveedor, boolean esNuevo, String error)
            throws ServletException, IOException {
        request.setAttribute("proveedor", proveedor);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/proveedores?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en proveedores: " + e.getMessage(), e);
    }
}