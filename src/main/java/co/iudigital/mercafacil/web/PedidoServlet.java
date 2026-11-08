package co.iudigital.mercafacil.web;

import co.iudigital.mercafacil.dao.AlmacenDAO;
import co.iudigital.mercafacil.dao.ArticuloDAO;
import co.iudigital.mercafacil.dao.DetallePedidoDAO;
import co.iudigital.mercafacil.dao.PedidoDAO;
import co.iudigital.mercafacil.dao.ProveedorDAO;
import co.iudigital.mercafacil.exception.DAOException;
import co.iudigital.mercafacil.exception.ReglaNegocioException;
import co.iudigital.mercafacil.model.DetallePedido;
import co.iudigital.mercafacil.model.DetallePedidoListado;
import co.iudigital.mercafacil.model.Pedido;
import co.iudigital.mercafacil.model.PedidoListado;
import co.iudigital.mercafacil.service.PedidoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pedidos")
public class PedidoServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/jsp/pedidos/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/jsp/pedidos/formulario.jsp";
    private static final String DETALLE = "/WEB-INF/jsp/pedidos/detalle.jsp";

    private final PedidoDAO dao = new PedidoDAO();
    private final DetallePedidoDAO detalleDAO = new DetallePedidoDAO();
    private final PedidoService service = new PedidoService();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final AlmacenDAO almacenDAO = new AlmacenDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if ("nuevo".equals(accion)) {
            Pedido pedido = new Pedido();
            pedido.setFecha(LocalDate.now());
            mostrarFormulario(request, response, pedido, new ArrayList<>(), true, null);
        } else if ("editar".equals(accion)) {
            editar(request, response);
        } else if ("ver".equals(accion)) {
            mostrarDetalle(request, response);
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
            String numero = request.getParameter("num");
            Pedido pedido = dao.buscarPorId(numero);
            if (pedido == null) {
                mostrarLista(request, response, "El pedido ya no existe.");
                return;
            }
            List<FilaDetalle> filas = new ArrayList<>();
            for (DetallePedidoListado detalle : detalleDAO.listarPorPedido(numero)) {
                filas.add(new FilaDetalle(detalle.getCodArticulo(), String.valueOf(detalle.getCantidad()), ""));
            }
            mostrarFormulario(request, response, pedido, filas, false, null);
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean esNuevo = "true".equals(request.getParameter("esNuevo"));
        String fecha = limpio(request, "fecha");
        Pedido pedido = new Pedido(limpio(request, "numPedido"), Validador.aFecha(fecha),
                limpio(request, "nit"), limpio(request, "codAlmacen"));
        List<FilaDetalle> filas = FilaDetalle.leer(request);
        String error = Validador.primerError(
                Validador.texto(pedido.getNumPedido(), "Número de pedido", 15),
                Validador.fecha(fecha, "Fecha"),
                Validador.texto(pedido.getNit(), "Proveedor", 15),
                Validador.texto(pedido.getCodAlmacen(), "Almacén", 5),
                validarFilas(filas));
        if (error != null) {
            mostrarFormulario(request, response, pedido, filas, esNuevo, error);
            return;
        }
        try {
            List<DetallePedido> detalles = convertir(pedido.getNumPedido(), filas);
            if (esNuevo) {
                service.registrar(pedido, detalles);
            } else {
                service.actualizar(pedido, detalles);
            }
            Flash.exito(request, esNuevo ? "Registro creado correctamente."
                    : "Registro actualizado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException | ReglaNegocioException e) {
            getServletContext().log("Pedido rechazado: " + e.getMessage(), e);
            mostrarFormulario(request, response, pedido, filas, esNuevo, e.getMessage());
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            service.eliminar(request.getParameter("num"));
            Flash.exito(request, "Registro eliminado correctamente.");
            redirigirALista(request, response);
        } catch (DAOException e) {
            registrar(e);
            Flash.error(request, e.getMessage());
            redirigirALista(request, response);
        }
    }

    private String validarFilas(List<FilaDetalle> filas) {
        for (FilaDetalle fila : filas) {
            String error = Validador.primerError(
                    Validador.texto(fila.getCodArticulo(), "Artículo", 10),
                    Validador.entero(fila.getCantidad(), "Cantidad"));
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    private List<DetallePedido> convertir(String numPedido, List<FilaDetalle> filas) {
        List<DetallePedido> detalles = new ArrayList<>();
        for (FilaDetalle fila : filas) {
            detalles.add(new DetallePedido(numPedido, fila.getCodArticulo(), Validador.aEntero(fila.getCantidad())));
        }
        return detalles;
    }

    private String limpio(HttpServletRequest request, String parametro) {
        String valor = request.getParameter(parametro);
        return valor == null ? "" : valor.trim();
    }

    private void mostrarDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String numero = request.getParameter("num");
            PedidoListado pedido = dao.buscarListado(numero);
            if (pedido == null) {
                mostrarLista(request, response, "El pedido no existe.");
                return;
            }
            request.setAttribute("pedido", pedido);
            request.setAttribute("detalles", detalleDAO.listarPorPedido(numero));
            request.getRequestDispatcher(DETALLE).forward(request, response);
        } catch (DAOException e) {
            registrar(e);
            mostrarLista(request, response, e.getMessage());
        }
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        List<PedidoListado> registros = List.of();
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

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Pedido pedido,
                                   List<FilaDetalle> filas, boolean esNuevo, String error)
            throws ServletException, IOException {
        try {
            request.setAttribute("proveedores", proveedorDAO.listar());
            request.setAttribute("almacenes", almacenDAO.listar());
            request.setAttribute("articulos", articuloDAO.listar());
        } catch (DAOException e) {
            registrar(e);
            error = e.getMessage();
        }
        if (filas.isEmpty()) {
            filas.add(new FilaDetalle("", "", ""));
        }
        request.setAttribute("pedido", pedido);
        request.setAttribute("filas", filas);
        request.setAttribute("esNuevo", esNuevo);
        request.setAttribute("error", error);
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    private void redirigirALista(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/pedidos?pagina="
                + Pagina.leer(request.getParameter("pagina")));
    }

    private void registrar(DAOException e) {
        getServletContext().log("Error en pedidos: " + e.getMessage(), e);
    }
}
