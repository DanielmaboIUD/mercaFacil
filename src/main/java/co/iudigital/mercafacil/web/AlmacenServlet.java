package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.AlmacenDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.model.Almacen;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/almacenes")
public class AlmacenServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/almacenes/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/almacenes/formulario.jsp";

    private final AlmacenDAO dao = new AlmacenDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response, new Almacen(), true, null);
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
            Almacen almacen = dao.buscarPorId(request.getParameter("id"));
            if (almacen == null) {
                mostrarLista(request, response, "El almacén ya no existe.");
            } else {
                mostrarFormulario(request, response, almacen, false, null);
            }
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        Almacen almacen = new Almacen(limpio(request, "codAlmacen"), limpio(request, "nombre"),
                limpio(request, "direccion"), limpio(request, "nombreGerente"));
        String error = validar(almacen);
        if (error != null) {
            mostrarFormulario(request, response, almacen, esNuevo, error);
            return;
        }
        try {
            if (esNuevo) {
                dao.insertar(almacen);
            } else {
                dao.actualizar(almacen);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarFormulario(request, response, almacen, esNuevo, e.getMessage());
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

    private String validar(Almacen almacen) {
        return Validador.primerError(
                Validador.texto(almacen.getCodAlmacen(), "Código", 5),
                Validador.texto(almacen.getNombre(), "Nombre", 60),
                Validador.texto(almacen.getDireccion(), "Dirección", 120),
                Validador.texto(almacen.getNombreGerente(), "Nombre del gerente", 80));
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<Almacen> almacenes = List.of();
        Pagina paginacion = Pagina.de(request.getParameter("pagina"), 0);
        String filtro = Filtro.leer(request);
        try {
            paginacion = Pagina.de(request.getParameter("pagina"), dao.contar(filtro));
            almacenes = dao.listarPagina(filtro, paginacion.getOffset(), paginacion.getTamano());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        request.setAttribute("almacenes", almacenes);
        request.setAttribute("paginacion", paginacion);
        request.setAttribute("q", filtro);
        request.setAttribute("error", error);
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response,
                                   Almacen almacen, boolean esNuevo, String error)
            throws ServletException, IOException {
        request.setAttribute("almacen", almacen);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/almacenes?pagina="
                + Pagina.leer(request.getParameter("pagina")) + Filtro.sufijo(request));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en almacenes: " + e.getMessage(), e);
    }
}