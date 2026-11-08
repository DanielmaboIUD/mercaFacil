package co.iudigital.mercafacil.dao;

import co.iudigital.mercafacil.exception.DAOException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.sql.SQLException;
import java.util.Map;

public final class ErroresSQL {

    private static final Map<String, String> DUPLICADOS = Map.ofEntries(
            Map.entry("grupo_pkey", "Ya existe un grupo con ese código."),
            Map.entry("grupo_descripcion_key", "Ya existe un grupo con esa descripción."),
            Map.entry("unidad_medida_pkey", "Ya existe una unidad de medida con ese código."),
            Map.entry("unidad_medida_descripcion_key", "Ya existe una unidad de medida con esa descripción."),
            Map.entry("tipo_pago_pkey", "Ya existe un tipo de pago con ese código."),
            Map.entry("tipo_pago_descripcion_key", "Ya existe un tipo de pago con esa descripción."),
            Map.entry("tipo_precio_pkey", "Ya existe un tipo de precio con ese código."),
            Map.entry("tipo_precio_descripcion_key", "Ya existe un tipo de precio con esa descripción."),
            Map.entry("articulo_pkey", "Ya existe un artículo con ese código."),
            Map.entry("proveedor_pkey", "Ya existe un proveedor con ese NIT."),
            Map.entry("almacen_pkey", "Ya existe un almacén con ese código."),
            Map.entry("cliente_pkey", "Ya existe un cliente con esa cédula."),
            Map.entry("articulo_proveedor_pkey", "Ese artículo ya está asociado a ese proveedor."),
            Map.entry("inventario_pkey", "Ese artículo ya tiene inventario en ese almacén."),
            Map.entry("articulo_precio_pkey", "Ese artículo ya tiene un precio de ese tipo."),
            Map.entry("compra_pkey", "Ya existe una compra con ese número de factura."),
            Map.entry("venta_pkey", "Ya existe una venta con ese número de factura."),
            Map.entry("pedido_pkey", "Ya existe un pedido con ese número."));

    private static final Map<String, String> SECCIONES = Map.of(
            "articulo", "Artículos",
            "articulo_proveedor", "Artículo–Proveedor",
            "inventario", "Inventario",
            "articulo_precio", "Precios de artículo",
            "compra", "Compras",
            "detalle_compra", "Compras",
            "venta", "Ventas",
            "detalle_venta", "Ventas",
            "pedido", "Pedidos",
            "detalle_pedido", "Pedidos");

    private static final Map<String, String> CHECKS = Map.ofEntries(
            Map.entry("articulo_precio_valor_check", "El valor debe ser mayor que cero."),
            Map.entry("articulo_proveedor_precio_compra_check", "El precio de compra debe ser mayor que cero."),
            Map.entry("ck_inventario_min_max", "La cantidad mínima no puede superar la cantidad máxima."),
            Map.entry("inventario_cantidad_maxima_check", "La cantidad máxima debe ser mayor que cero."),
            Map.entry("inventario_cantidad_minima_check", "La cantidad mínima no puede ser negativa."),
            Map.entry("inventario_existencia_check", "La existencia no puede ser negativa."),
            Map.entry("compra_valor_total_check", "El valor total no puede ser negativo."),
            Map.entry("venta_valor_total_check", "El valor total no puede ser negativo."),
            Map.entry("detalle_compra_cantidad_check", "La cantidad debe ser mayor que cero."),
            Map.entry("detalle_venta_cantidad_check", "La cantidad debe ser mayor que cero."),
            Map.entry("detalle_pedido_cantidad_check", "La cantidad debe ser mayor que cero."),
            Map.entry("detalle_compra_valor_unitario_check", "El valor unitario debe ser mayor que cero."),
            Map.entry("detalle_venta_valor_unitario_check", "El valor unitario debe ser mayor que cero."));

    private ErroresSQL() {
    }

    public static DAOException traducir(SQLException e, boolean eliminando) {
        ServerErrorMessage detalle = e instanceof PSQLException psql ? psql.getServerErrorMessage() : null;
        String restriccion = detalle == null || detalle.getConstraint() == null ? "" : detalle.getConstraint();
        String estado = e.getSQLState() == null ? "" : e.getSQLState();
        String mensaje = switch (estado) {
            case "23505" -> DUPLICADOS.getOrDefault(restriccion, "Ya existe un registro con esos datos.");
            case "23503" -> eliminando
                    ? "No se puede eliminar porque está en uso en " + seccion(detalle) + "."
                    : "Uno de los valores seleccionados no existe.";
            case "23514" -> CHECKS.getOrDefault(restriccion, "Un valor no cumple las restricciones de la tabla.");
            default -> "Error al acceder a la base de datos.";
        };
        return new DAOException(mensaje, e);
    }

    private static String seccion(ServerErrorMessage detalle) {
        String tabla = detalle == null ? null : detalle.getTable();
        return tabla == null ? "otros registros" : SECCIONES.getOrDefault(tabla, "otros registros");
    }
}
