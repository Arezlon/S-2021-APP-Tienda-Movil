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
    public class NotificacionesController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public NotificacionesController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpGet("get")]
        public IActionResult Get()
        {
            try
            {
                int id = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                var notificaciones = contexto.Notificaciones
                    .Where(c => c.UsuarioId == id)
                    .Include(c => c.Publicacion)
                    .ThenInclude(c => c.Usuario)
                    .Include(c => c.Compra)
                    .ThenInclude(c => c.Usuario)
                    .OrderByDescending(c => c.Creacion)
                    .ToList();
                return Ok(notificaciones);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPatch("patch")]
        public IActionResult Patch(Notificacion notificacion)
        {
            try
            {
                var entidad = contexto.Notificaciones.FirstOrDefault(i => i.Id == notificacion.Id);
                if (entidad != null)
                {
                    entidad.Estado = 2;
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

        [HttpGet("gettotal")]
        public IActionResult GetTotal()
        {
            try
            {
                int id = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                int total = contexto.Notificaciones
                    .Where(c => c.UsuarioId == id && c.Estado == 1)
                    .GroupBy(c => c.Id)
                    .Count();
                return Ok(total);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
    }
}