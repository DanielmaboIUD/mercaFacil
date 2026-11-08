package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.GrupoDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.Grupo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/grupos")
public class GrupoServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/grupos/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/grupos/formulario.jsp";
    private static final int MAX_CODIGO = 5;
    private static final int MAX_DESCRIPCION = 60;

    private final GrupoDAO dao = new GrupoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new Grupo(), true, null);
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
            Grupo grupo = dao.buscarPorId(request.getParameter("id"));
            if (grupo == null) {
                mostrarLista(request, response, "El grupo ya no existe.");
            } else {
                mostrarFormulario(request, response, grupo, false, null);
            }
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        Grupo grupo = new Grupo(limpio(request, "codGrupo"), limpio(request, "descripcion"));
        String error = validar(grupo);
        if (error != null) {
            mostrarFormulario(request, response, grupo, esNuevo, error);
            return;
        }
        try {
            if (esNuevo) {
                dao.insertar(grupo);
            } else {
                dao.actualizar(grupo);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarFormulario(request, response, grupo, esNuevo, e.getMessage());
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

    private String validar(Grupo grupo) {
        if (grupo.getCodGrupo().isEmpty()) {
            return "El código es obligatorio.";
        }
        if (grupo.getCodGrupo().length() > MAX_CODIGO) {
            return "El código admite máximo " + MAX_CODIGO + " caracteres.";
        }
        if (grupo.getDescripcion().isEmpty()) {
            return "La descripción es obligatoria.";
        }
        if (grupo.getDescripcion().length() > MAX_DESCRIPCION) {
            return "La descripción admite máximo " + MAX_DESCRIPCION + " caracteres.";
        }
        return null;
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<Grupo> grupos = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar());
            grupos = dao.listarPagina(paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("grupos", grupos);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   Grupo grupo, boolean esNuevo, String error)
            throws ServletException, IOException {
        request.setAttribute("grupo", grupo);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/grupos?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en grupos: " + e.getMessage(), e);
    }
}
