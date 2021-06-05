using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Linq;
using System.Threading.Tasks;
using TiendaMovil.Models;

namespace TiendaMovil.Controllers
{
    [Route("api/[controller]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    [ApiController]
    public class ComprasController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public ComprasController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpPost("create")]
        [AllowAnonymous]
        public async Task<IActionResult> Create(Compra compra)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    Usuario comprador = contexto.Usuarios.Find(int.Parse(User.Claims.First(c => c.Type == "Id").Value));
                    int VendedorId = compra.Publicacion.UsuarioId;

                    compra.PublicacionId = compra.Publicacion.Id;
                    compra.Precio = compra.Publicacion.Precio * compra.Cantidad;
                    compra.UsuarioId = comprador.Id;
                    compra.Estado = 1;
                    compra.Creacion = DateTime.Now;
                    compra.Publicacion = null;
                    contexto.Compras.Add(compra);

                    await contexto.SaveChangesAsync();

                    //Transacción 1 >> Compra
                    Transaccion trCompra = new Transaccion();
                    trCompra.CompraId = compra.Id;
                    trCompra.UsuarioId = compra.UsuarioId;
                    trCompra.Importe = compra.Precio;
                    comprador.Fondos -= compra.Precio;
                    trCompra.Balance = comprador.Fondos;
                    trCompra.Tipo = 2;
                    trCompra.Estado = 1;
                    trCompra.Creacion = DateTime.Now;
                    contexto.Transacciones.Add(trCompra);

                    //Transacción 2 >> Venta
                    Transaccion trVenta = new Transaccion();
                    trVenta.CompraId = compra.Id;
                    trVenta.UsuarioId = VendedorId;
                    trVenta.Importe = compra.Precio;
                    Usuario vendedor = contexto.Usuarios.Find(trVenta.UsuarioId);
                    vendedor.Fondos += trVenta.Importe;
                    trVenta.Balance = vendedor.Fondos;
                    trVenta.Tipo = 3;
                    trVenta.Estado = 1;
                    trVenta.Creacion = DateTime.Now;
                    contexto.Transacciones.Add(trVenta);

                    //Publicación
                    Publicacion publicacion = contexto.Publicaciones.Find(compra.PublicacionId);
                    publicacion.Stock -= compra.Cantidad;

                    if(publicacion.Stock == 0)
                    {
                        //Notificación tipo 4, publicación sin stock
                        Notificacion notificacionStock = new Notificacion();
                        notificacionStock.PublicacionId = compra.PublicacionId;
                        notificacionStock.Tipo = 4;
                        notificacionStock.UsuarioId = vendedor.Id;
                        notificacionStock.Estado = 1;
                        notificacionStock.Creacion = DateTime.Now;
                        notificacionStock.CompraId = null;
                        contexto.Notificaciones.Add(notificacionStock);
                    }

                    //Notificación tipo 1, venta de una publicación
                    Notificacion notificacion = new Notificacion();
                    notificacion.CompraId = compra.Id;
                    notificacion.PublicacionId = compra.PublicacionId;
                    notificacion.Tipo = 1;
                    notificacion.UsuarioId = vendedor.Id;
                    notificacion.Estado = 1;
                    notificacion.Creacion = DateTime.Now;
                    contexto.Notificaciones.Add(notificacion);

                    contexto.SaveChanges();
                    return Ok(true);
                }
                return BadRequest();
            }
            catch (Exception ex)
            {
                return BadRequest(ex);
            }
        }

        [HttpGet("get")]
        public IActionResult Get()
        {
            try
            {
                int id = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                var compras = contexto.Compras
                    .Where(c => c.UsuarioId == id)
                    .Include(c => c.Usuario)
                    .Include(c => c.Publicacion)
                    .ThenInclude(p => p.Usuario)
                    .OrderByDescending(c => c.Creacion)
                    .ToList();
                foreach (Compra c in compras)
                    c.Publicacion.ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == c.Publicacion.Id && i.Estado == 2).FirstOrDefault().Direccion;
                return Ok(compras);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("getventas")]
        public IActionResult GetVentas()
        {
            try
            {
                int id = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                var ventas = contexto.Compras
                    .Include(c => c.Usuario)
                    .Include(c => c.Publicacion)
                    .ThenInclude(p => p.Usuario)
                    .Where(c => c.Publicacion.UsuarioId == id)
                    .OrderByDescending(c => c.Creacion)
                    .ToList();
                foreach (Compra c in ventas)
                    c.Publicacion.ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == c.Publicacion.Id && i.Estado == 2).FirstOrDefault().Direccion;

                return Ok(ventas);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("getultima")]
        public IActionResult GetUltima()
        {
            try
            {
                int id = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                var compra = contexto.Compras
                    .Where(c => c.UsuarioId == id)
                    .Include(c => c.Usuario)
                    .Include(c => c.Publicacion)
                    .ThenInclude(p => p.Usuario)
                    .OrderByDescending(c => c.Creacion)
                    .FirstOrDefault();
                compra.Publicacion.ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == compra.Publicacion.Id && i.Estado == 2).FirstOrDefault().Direccion;
                return Ok(compra);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
        
    }
}