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
    public class ComentariosController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public ComentariosController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpGet("get")]
        public IActionResult Get(int publicacionId)
        {
            try
            {
                var comentarios = contexto.Comentarios
                    .Where(c => c.PublicacionId == publicacionId)
                    .Include(c => c.Usuario)
                    .Include(c => c.Publicacion)
                    .ToList()
                    .OrderByDescending(c => c.Creacion);
                return Ok(comentarios);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPost("create")]
        public IActionResult Create(Comentario comentario)
        {
            try
            {
                Publicacion p = comentario.Publicacion;
                comentario.Publicacion = null;
                comentario.Estado = 1;
                comentario.Creacion = DateTime.Now;
                comentario.UsuarioId = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                contexto.Comentarios.Add(comentario);

                //Notificación tipo 2, nueva pregunta de un usuario
                Notificacion notificacion = new Notificacion();
                notificacion.PublicacionId = comentario.PublicacionId;
                notificacion.Tipo = 2;
                notificacion.UsuarioId = p.UsuarioId;
                notificacion.Estado = 1;
                notificacion.Creacion = DateTime.Now;
                notificacion.CompraId = null;
                contexto.Notificaciones.Add(notificacion);

                contexto.SaveChanges();
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPatch("patch")]
        public IActionResult Patch(Comentario comentario)
        {
            try
            {
                var entidad = contexto.Comentarios.FirstOrDefault(c => c.Id == comentario.Id);
                if (entidad != null)
                {
                    contexto.Entry(entidad).CurrentValues.SetValues(comentario);

                    //Notificación tipo 5, nueva respuesta de un vendedor
                    Notificacion notificacion = new Notificacion();
                    notificacion.PublicacionId = comentario.PublicacionId;
                    notificacion.Tipo = 5;
                    notificacion.UsuarioId = comentario.UsuarioId;
                    notificacion.Estado = 1;
                    notificacion.Creacion = DateTime.Now;
                    notificacion.CompraId = null;
                    contexto.Notificaciones.Add(notificacion);

                    contexto.SaveChanges();
                    return Ok();
                }
                return BadRequest();
            }
            catch (Exception ex)
            {
                return BadRequest(ex);
            }
        }
    }
}
