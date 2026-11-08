package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.ArticuloDAO;
import co.iudigital.mercafacil.dao.GrupoDAO;
import co.iudigital.mercafacil.dao.UnidadMedidaDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.Articulo;
import co.iudigital.mercafacil.model.ArticuloListado;
import co.iudigital.mercafacil.model.Grupo;
import co.iudigital.mercafacil.model.UnidadMedida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/articulos")
public class ArticuloServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/articulos/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/articulos/formulario.jsp";

    private final ArticuloDAO dao = new ArticuloDAO();
    private final GrupoDAO grupoDAO = new GrupoDAO();
    private final UnidadMedidaDAO unidadDAO = new UnidadMedidaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new Articulo(), true, null);
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
            Articulo articulo = dao.buscarPorId(request.getParameter("id"));
            if (articulo == null) {
                mostrarLista(request, response, "El artículo ya no existe.");
            } else {
                mostrarFormulario(request, response, articulo, false, null);
            }
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        Articulo articulo = new Articulo(limpio(request, "codArticulo"), limpio(request, "nombre"),
                limpio(request, "codUnidad"), limpio(request, "codGrupo"));
        String error = validar(articulo);
        if (error != null) {
            mostrarFormulario(request, response, articulo, esNuevo, error);
            return;
        }
        try {
            if (esNuevo) {
                dao.insertar(articulo);
            } else {
                dao.actualizar(articulo);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarFormulario(request, response, articulo, esNuevo, e.getMessage());
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

    private String validar(Articulo articulo) {
        return Validador.primerError(
                Validador.texto(articulo.getCodArticulo(), "Código", 10),
                Validador.texto(articulo.getNombre(), "Nombre", 80),
                Validador.texto(articulo.getCodUnidad(), "Unidad de medida", 5),
                Validador.texto(articulo.getCodGrupo(), "Grupo", 5));
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<ArticuloListado> articulos = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            articulos = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("articulos", articulos);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   Articulo articulo, boolean esNuevo, String error)
            throws ServletException, IOException {
        List<Grupo> grupos = List.of();
        List<UnidadMedida> unidades = List.of();
        try {
            grupos = grupoDAO.listar();
            unidades = unidadDAO.listar();
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("articulo", articulo);
        request.setAttribute("grupos", grupos);
        request.setAttribute("unidades", unidades);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/articulos?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en articulos: " + e.getMessage(), e);
    }
}
