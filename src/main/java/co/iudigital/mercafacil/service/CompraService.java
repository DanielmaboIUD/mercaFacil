package co.iudigital.mercafacil.service;

import co.iudigital.mercafacil.config.ConexionBD;
import co.iudigital.mercafacil.dao.CompraDAO;
import co.iudigital.mercafacil.dao.DetalleCompraDAO;
import co.iudigital.mercafacil.dao.ErroresSQL;
import co.iudigital.mercafacil.dao.InventarioDAO;
import co.iudigital.mercafacil.exception.ReglaNegocioException;
import co.iudigital.mercafacil.model.Compra;
import co.iudigital.mercafacil.model.DetalleCompra;
import co.iudigital.mercafacil.model.Inventario;
import co.iudigital.mercafacil.model.InventarioId;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompraService {

    private static final BigDecimal MAXIMO_TOTAL = new BigDecimal("999999999999.99");

    private final CompraDAO compraDAO = new CompraDAO();
    private final DetalleCompraDAO detalleDAO = new DetalleCompraDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();

    public void registrar(Compra compra, List<DetalleCompra> detalles) {
        validar(detalles);
        compra.setValorTotal(calcularTotal(detalles));
        // Orden fijo de artículos para que dos transacciones no se bloqueen entre sí.
        List<DetalleCompra> ordenados = new ArrayList<>(detalles);
        ordenados.sort(Comparator.comparing(DetalleCompra::getCodArticulo));
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                for (DetalleCompra detalle : ordenados) {
                    sumarAlInventario(conexion, compra.getCodAlmacen(), detalle);
                }
                compraDAO.insertar(conexion, compra);
                for (DetalleCompra detalle : ordenados) {
                    detalleDAO.insertar(conexion, detalle);
                }
                conexion.commit();
            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e, false);
        }
    }

    private void validar(List<DetalleCompra> detalles) {
        if (detalles.isEmpty()) {
            throw new ReglaNegocioException("Debe agregar al menos un artículo.");
        }
        Set<String> articulos = new HashSet<>();
        for (DetalleCompra detalle : detalles) {
            String articulo = detalle.getCodArticulo();
            if (!articulos.add(articulo)) {
                throw new ReglaNegocioException("El artículo " + articulo + " está repetido.");
            }
            if (detalle.getCantidad() <= 0) {
                throw new ReglaNegocioException("La cantidad del artículo " + articulo + " debe ser mayor que cero.");
            }
            if (detalle.getValorUnitario().signum() <= 0) {
                throw new ReglaNegocioException("El valor unitario del artículo " + articulo
                        + " debe ser mayor que cero.");
            }
        }
    }

    private BigDecimal calcularTotal(List<DetalleCompra> detalles) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCompra detalle : detalles) {
            total = total.add(detalle.getValorUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }
        if (total.compareTo(MAXIMO_TOTAL) > 0) {
            throw new ReglaNegocioException("El valor total supera el máximo permitido.");
        }
        return total;
    }

    private void sumarAlInventario(Connection conexion, String almacen, DetalleCompra detalle) throws SQLException {
        String articulo = detalle.getCodArticulo();
        Inventario inventario = inventarioDAO.sumarExistencia(conexion, new InventarioId(almacen, articulo),
                detalle.getCantidad());
        if (inventario == null) {
            throw new ReglaNegocioException("El artículo " + articulo + " no tiene inventario en el almacén "
                    + almacen + ". Regístrelo primero.");
        }
        if (inventario.getExistencia() > inventario.getCantidadMaxima()) {
            throw new ReglaNegocioException("La compra deja el artículo " + articulo + " en "
                    + inventario.getExistencia() + " unidades, por encima de la cantidad máxima ("
                    + inventario.getCantidadMaxima() + ") del almacén " + almacen + ".");
        }
    }
}
