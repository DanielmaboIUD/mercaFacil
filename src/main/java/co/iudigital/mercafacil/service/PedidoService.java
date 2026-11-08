package co.iudigital.mercafacil.service;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.dao.DetallePedidoDAO;
import co.iudigital.mercafacil.dao.ErroresSQL;
import co.iudigital.mercafacil.dao.PedidoDAO;
import co.iudigital.mercafacil.exception.ReglaNegocioException;
import co.iudigital.mercafacil.model.DetallePedido;
import co.iudigital.mercafacil.model.Pedido;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PedidoService {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final DetallePedidoDAO detalleDAO = new DetallePedidoDAO();

    public void registrar(Pedido pedido, List<DetallePedido> detalles) {
        validar(detalles);
        enTransaccion(conexion -> {
            pedidoDAO.insertar(conexion, pedido);
            insertarDetalles(conexion, detalles);
        });
    }

    public void actualizar(Pedido pedido, List<DetallePedido> detalles) {
        validar(detalles);
        enTransaccion(conexion -> {
            pedidoDAO.actualizar(conexion, pedido);
            detalleDAO.eliminarPorPedido(conexion, pedido.getNumPedido());
            insertarDetalles(conexion, detalles);
        });
    }

    /** Los detalles se borran en cascada. */
    public void eliminar(String numPedido) {
        pedidoDAO.eliminar(numPedido);
    }

    private void insertarDetalles(Connection conexion, List<DetallePedido> detalles) throws SQLException {
        for (DetallePedido detalle : detalles) {
            detalleDAO.insertar(conexion, detalle);
        }
    }

    private void validar(List<DetallePedido> detalles) {
        if (detalles.isEmpty()) {
            throw new ReglaNegocioException("Debe agregar al menos un artículo.");
        }
        Set<String> articulos = new HashSet<>();
        for (DetallePedido detalle : detalles) {
            String articulo = detalle.getCodArticulo();
            if (!articulos.add(articulo)) {
                throw new ReglaNegocioException("El artículo " + articulo + " está repetido.");
            }
            if (detalle.getCantidad() <= 0) {
                throw new ReglaNegocioException("La cantidad del artículo " + articulo + " debe ser mayor que cero.");
            }
        }
    }

    private void enTransaccion(Operacion operacion) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                operacion.ejecutar(conexion);
                conexion.commit();
            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    @FunctionalInterface
    private interface Operacion {
        void ejecutar(Connection conexion) throws SQLException;
    }
}
