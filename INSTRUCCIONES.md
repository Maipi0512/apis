# Estructura del ecommerce (sin Usuario por ahora)

Se sacó Usuario y Dirección de la API hasta la clase de seguridad —
Pedido y Carrito ya no piden usuarioId, se crean "vacíos" por ahora
(`POST` sin body) y se van a conectar a un usuario real cuando armen el
login.

Quedan activos: Categoría, Proveedor, Producto, Carrito, ItemCarrito,
Pedido, DetallePedido, Pago.

## Archivos que podés borrar (ya no se usan)

Como no puedo borrar archivos directamente en tu compu, borralos vos
misma desde VS Code (clic derecho > Delete, podés seleccionar varios a
la vez con Ctrl+clic):

- `model/User.java`
- `dto/UserRequestDTO.java`
- `repository/UserRepository.java`
- `service/UserService.java`
- `service/UserServiceImpl.java`
- `controller/UserController.java`
- `model/Address.java`
- `dto/AddressRequestDTO.java`
- `repository/AddressRepository.java`
- `service/AddressService.java`
- `service/AddressServiceImpl.java`
- `controller/AddressController.java`
- `exception/CartAlreadyExistsException.java` (ya no se usa, el check de "un usuario, un carrito" se saca con Usuario)
- `dto/OrderRequestDTO.java` (ya no se usa, `POST /pedidos` no recibe body)
- `dto/CartRequestDTO.java` (ya no se usa, `POST /carritos` no recibe body)

No pasa nada si te olvidás de borrar alguno: como nada los referencia,
no rompen la compilación, simplemente quedan ahí sin usarse. Pero es
más prolijo borrarlos.

## Importante: si ya habías creado las tablas `usuario`, `direccion`,
## `pedido` o `carrito` en Supabase

Borralas (Table Editor) antes de levantar de nuevo, porque:
- `pedido` y `carrito` cambiaron de estructura (perdieron la columna
  `usuario_id`), Hibernate con `ddl-auto=update` no borra columnas
  viejas solo.
- `usuario` y `direccion` ya no las va a crear nadie.

## Endpoints activos ahora

| Endpoint | Body para crear |
|---|---|
| `/categorias` | `{"nombre": "..."}` |
| `/proveedores` | `{"nombre": "...", "contacto": "..."}` |
| `/productos` | `{"nombre": "...", "precio": ..., "stock": ..., "categoriaId": ..., "proveedorId": ...}` |
| `/carritos` | *(sin body, `POST` vacío)* |
| `/items-carrito` | `{"carritoId": ..., "productoId": ..., "cantidad": ...}` |
| `/pedidos` | *(sin body, `POST` vacío)* |
| `/detalles-pedido` | `{"pedidoId": ..., "productoId": ..., "cantidad": ...}` |
| `/pagos` | `{"pedidoId": ..., "metodoPago": "...", "monto": ...}` |

## Orden recomendado para probar

```
1. POST /categorias
2. POST /proveedores
3. POST /productos       (usa el id de categoria y proveedor de arriba)
4. POST /carritos         (sin body)
5. POST /items-carrito    (usa el id de carrito y producto)
6. POST /pedidos          (sin body)
7. POST /detalles-pedido  (usa el id de pedido y producto)
8. POST /pagos            (usa el id de pedido)
```

Recordá siempre revisar el `id` real que te devuelve cada `POST` antes
de usarlo en el siguiente (sobre todo si vas borrando filas a mano en
Supabase, ahí los contadores no vuelven a arrancar en 1).

## Para la clase que viene (seguridad)

Cuando armen login/JWT, van a:
1. Recrear `User` (o como decidan llamarlo) conectado a Spring Security.
2. Agregarle de nuevo a `Order` y `Cart` el campo usuario (`@ManyToOne` /
   `@OneToOne`), ahora tomando el usuario autenticado en vez de recibir
   nada por body.
3. Reincorporar `Address` si la siguen necesitando, relacionada a ese
   usuario nuevo.
