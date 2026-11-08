package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.AlmacenDAO;
import co.iudigital.mercafacil.dao.ArticuloDAO;
import co.iudigital.mercafacil.dao.InventarioDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.Inventario;
import co.iudigital.mercafacil.model.InventarioId;
import co.iudigital.mercafacil.model.InventarioListado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/inventario")
public class InventarioServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/inventario/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/inventario/formulario.jsp";

    private final InventarioDAO dao = new InventarioDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new Inventario(), true, null);
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
            Inventario registro = dao.buscarPorId(leerId(request));
            if (registro == null) {
                mostrarLista(request, response, "El registro de inventario ya no existe.");
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
        String existencia = limpio(request, "existencia");
        String maxima = limpio(request, "cantidadMaxima");
        String minima = limpio(request, "cantidadMinima");
        Inventario registro = new Inventario(limpio(request, "codAlmacen"), limpio(request, "codArticulo"),
                Validador.aEntero(existencia), Validador.aEntero(maxima), Validador.aEntero(minima));
        String error = Validador.primerError(
                Validador.texto(registro.getCodAlmacen(), "Almacén", 5),
                Validador.texto(registro.getCodArticulo(), "Artículo", 10),
                Validador.entero(existencia, "Existencia"),
                Validador.entero(maxima, "Cantidad máxima"),
                Validador.entero(minima, "Cantidad mínima"),
                validarCantidades(registro));
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

    private String validarCantidades(Inventario registro) {
        if (registro.getExistencia() < 0) {
            return "La existencia no puede ser negativa.";
        }
        if (registro.getCantidadMaxima() <= 0) {
            return "La cantidad máxima debe ser mayor que cero.";
        }
        if (registro.getCantidadMinima() < 0) {
            return "La cantidad mínima no puede ser negativa.";
        }
        if (registro.getCantidadMinima() > registro.getCantidadMaxima()) {
            return "La cantidad mínima no puede superar la cantidad máxima.";
        }
        return null;
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

    private InventarioId leerId(HttpServletRequest request) {
        return new InventarioId(request.getParameter("codAlmacen"), request.getParameter("codArticulo"));
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<InventarioListado> registros = List.of();
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
                                   Inventario registro, boolean esNuevo, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("almacenes", almacenDAO.listar());
            request.setAttribute("articulos", articuloDAO.listar());
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
        response.sendRedirect(request.getContextPath() + "/inventario?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en inventario: " + e.getMessage(), e);
    }
}
