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
                comentario.Estado = 1;
                comentario.Creacion = DateTime.Now;
                comentario.UsuarioId = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                contexto.Comentarios.Add(comentario);
                contexto.SaveChanges();
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
    }
}
