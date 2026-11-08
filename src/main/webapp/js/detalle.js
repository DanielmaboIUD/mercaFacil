(function () {
    const filas = document.getElementById('filas');
    const plantilla = document.getElementById('plantilla-fila');
    const total = document.getElementById('total');
    const formato = new Intl.NumberFormat('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

    // En compras el precio se digita; en ventas viene del artículo elegido.
    function precioDe(fila) {
        const campo = fila.querySelector('[name="valorUnitario"]');
        if (campo) {
            return Number(campo.value) || 0;
        }
        const opcion = fila.querySelector('[name="codArticulo"]').selectedOptions[0];
        return Number(opcion && opcion.dataset.precio) || 0;
    }

    function recalcular() {
        // Los pedidos no manejan valores.
        if (!total) {
            return;
        }
        let suma = 0;
        filas.querySelectorAll('.fila-detalle').forEach(function (fila) {
            const precio = precioDe(fila);
            const subtotal = precio * (Number(fila.querySelector('[name="cantidad"]').value) || 0);
            const celdaPrecio = fila.querySelector('.precio');
            if (celdaPrecio) {
                celdaPrecio.textContent = formato.format(precio);
            }
            fila.querySelector('.subtotal').textContent = formato.format(subtotal);
            suma += subtotal;
        });
        total.textContent = formato.format(suma);
    }

    document.getElementById('agregar-fila').addEventListener('click', function () {
        filas.appendChild(plantilla.content.querySelector('.fila-detalle').cloneNode(true));
        recalcular();
    });

    filas.addEventListener('click', function (evento) {
        if (evento.target.classList.contains('quitar')) {
            evento.target.closest('.fila-detalle').remove();
            recalcular();
        }
    });

    filas.addEventListener('input', recalcular);
    filas.addEventListener('change', recalcular);
    recalcular();
})();
