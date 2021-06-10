using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using TiendaMovil.Models;

namespace TiendaMovil.Controllers
{
    [Route("api/[controller]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    [ApiController]
    public class ReseñasController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public ReseñasController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpGet("get")]
        public IActionResult Get(int publicacionId)
        {
            try
            {
                var reseñas = contexto.Reseñas
                    .Where(r => r.PublicacionId == publicacionId)
                    .Include(r => r.Usuario)
                    .Include(r => r.Publicacion)
                    .ToList()
                    .OrderByDescending(r => r.Creacion);
                return Ok(reseñas);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("getPorCompra")]
        public IActionResult GetPorCompra(int compraId)
        {
            try
            {
                Compra c = contexto.Compras.Find(compraId);
                var reseña = contexto.Reseñas
                    .Where(r => r.PublicacionId == c.PublicacionId && r.UsuarioId == c.UsuarioId)
                    .Include(r => r.Usuario)
                    .Include(r => r.Publicacion)
                    .FirstOrDefault();
                return Ok(reseña);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPost("create")]
        public IActionResult Create(Reseña reseña)
        {
            try
            {
                reseña.Estado = 1;
                reseña.Creacion = DateTime.Now;
                reseña.UsuarioId = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                reseña.PublicacionId = reseña.Publicacion.Id;
                Publicacion p = reseña.Publicacion;
                reseña.Publicacion = null;
                contexto.Reseñas.Add(reseña);
                
                //Notificación tipo 3, nueva reseña
                Notificacion notificacion = new Notificacion();
                notificacion.PublicacionId = p.Id;
                notificacion.Tipo = 3;
                notificacion.UsuarioId = p.UsuarioId;
                notificacion.Estado = 1;
                notificacion.Creacion = DateTime.Now;
                //notificacion.CompraId = 
                contexto.Notificaciones.Add(notificacion);

                contexto.SaveChanges();
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("comprobarReseña")]
        public IActionResult ComprobarReseña(int publicacionId)
        {
            try
            {
                int id = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                Reseña r = contexto.Reseñas
                    .Where(c => c.UsuarioId == id && c.PublicacionId == publicacionId)
                    .FirstOrDefault();
                return Ok(r == null);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
    }
}
