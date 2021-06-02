using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Linq;
using TiendaMovil.Models;

namespace TiendaMovil.Controllers
{
    [Route("api/[controller]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    [ApiController]
    public class TransaccionesController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public TransaccionesController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpPost("create")]
        [AllowAnonymous]
        public IActionResult Create(Transaccion transaccion)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    Usuario u = contexto.Usuarios.Find(int.Parse(User.Claims.First(c => c.Type == "Id").Value));
                    u.Fondos += transaccion.Importe;

                    transaccion.UsuarioId = u.Id;
                    transaccion.Balance = u.Fondos;
                    transaccion.Estado = 1;
                    transaccion.Creacion = DateTime.Now;

                    contexto.Transacciones.Add(transaccion);
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
                var transacciones = contexto.Transacciones
                    .Where(t => t.UsuarioId == id)
                    .Include(t => t.Usuario)
                    .Include(t => t.Compra)
                    .ThenInclude(c => c.Publicacion)
                    .ThenInclude(p => p.Usuario)
                    .OrderByDescending(t => t.Creacion)
                    .ToList();
                transacciones.AddRange(contexto.Transacciones.Where(t => t.UsuarioId == id && t.Tipo == 1).Include(t => t.Usuario).ToList());
                transacciones = transacciones.OrderByDescending(t => t.Creacion).ToList();
                return Ok(transacciones);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
    }
}
