package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.ArticuloDAO;
import co.iudigital.mercafacil.dao.ArticuloProveedorDAO;
import co.iudigital.mercafacil.dao.ProveedorDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.ArticuloProveedor;
import co.iudigital.mercafacil.model.ArticuloProveedorId;
import co.iudigital.mercafacil.model.ArticuloProveedorListado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/articulo-proveedor")
public class ArticuloProveedorServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/articulo-proveedor/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/articulo-proveedor/formulario.jsp";

    private final ArticuloProveedorDAO dao = new ArticuloProveedorDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new ArticuloProveedor(), true, null);
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
            ArticuloProveedor registro = dao.buscarPorId(leerId(request));
            if (registro == null) {
                mostrarLista(request, response, "La asociación ya no existe.");
            } else {
                mostrarFormulario(request, response, registro, false, null);
            }
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        String valor = limpio(request, "precioCompra");
        ArticuloProveedor registro = new ArticuloProveedor(limpio(request, "codArticulo"), limpio(request, "nit"),
                Validador.aDecimal(valor));
        String error = Validador.primerError(
                Validador.texto(registro.getCodArticulo(), "Artículo", 10),
                Validador.texto(registro.getNit(), "Proveedor", 15),
                Validador.decimal(valor, "Precio de compra", 10, 2));
        if (error != null) {
            mostrarFormulario(request, response, registro, esNuevo, error);
            return;
        }
        try {
            if (esNuevo) {
                dao.insertar(registro);
            } else {
                dao.actualizar(registro);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarFormulario(request, response, registro, esNuevo, e.getMessage());
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            dao.eliminar(leerId(request));
            Flash.exito(request, "Registro eliminado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            Flash.error(request, e.getMessage());
            redirigirALista(request, response);
        }
    }

    private ArticuloProveedorId leerId(HttpServletRequest request) {
        return new ArticuloProveedorId(request.getParameter("codArticulo"), request.getParameter("nit"));
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<ArticuloProveedorListado> registros = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            registros = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("registros", registros);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   ArticuloProveedor registro, boolean esNuevo, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("articulos", articuloDAO.listar());
            request.setAttribute("proveedores", proveedorDAO.listar());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("registro", registro);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/articulo-proveedor?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en articulo-proveedor: " + e.getMessage(), e);
    }
}
