package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.ArticuloDAO;
import co.iudigital.mercafacil.dao.ArticuloPrecioDAO;
import co.iudigital.mercafacil.dao.TipoPrecioDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.ArticuloPrecio;
import co.iudigital.mercafacil.model.ArticuloPrecioId;
import co.iudigital.mercafacil.model.ArticuloPrecioListado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/articulo-precio")
public class ArticuloPrecioServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/articulo-precio/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/articulo-precio/formulario.jsp";

    private final ArticuloPrecioDAO dao = new ArticuloPrecioDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();
    private final TipoPrecioDAO tipoPrecioDAO = new TipoPrecioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new ArticuloPrecio(), true, null);
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
            ArticuloPrecio registro = dao.buscarPorId(leerId(request));
            if (registro == null) {
                mostrarLista(request, response, "El precio ya no existe.");
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
        String valor = limpio(request, "valor");
        ArticuloPrecio registro = new ArticuloPrecio(limpio(request, "codArticulo"), limpio(request, "codTipoPrecio"),
                Validador.aDecimal(valor));
        String error = Validador.primerError(
                Validador.texto(registro.getCodArticulo(), "Artículo", 10),
                Validador.texto(registro.getCodTipoPrecio(), "Tipo de precio", 5),
                Validador.decimal(valor, "Valor", 10, 2));
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

    private ArticuloPrecioId leerId(HttpServletRequest request) {
        return new ArticuloPrecioId(request.getParameter("codArticulo"), request.getParameter("codTipoPrecio"));
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<ArticuloPrecioListado> registros = List.of();
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
                                   ArticuloPrecio registro, boolean esNuevo, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("articulos", articuloDAO.listar());
            request.setAttribute("tiposPrecio", tipoPrecioDAO.listar());
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
        response.sendRedirect(request.getContextPath() + "/articulo-precio?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en articulo-precio: " + e.getMessage(), e);
    }
}
