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
    public class PublicacionesController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public PublicacionesController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpGet("get")]
        public IActionResult Get(int estado = -1, int usuarioId = -1)
        {
            try
            {
                var publicaciones = contexto.Publicaciones
                    .Where(p => 
                        (estado == -1 ? true : p.Estado == estado) && 
                        (usuarioId == -1 ? true : p.UsuarioId == usuarioId))
                    .Include(p => p.Usuario)
                    .ToList();
                return Ok(publicaciones);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("getById")]
        public IActionResult GetById(int publicacionId)
        {
            try
            {
                var publicacione = contexto.Publicaciones
                    .Where(p => p.Id == publicacionId)
                    .Include(p => p.Usuario)
                    .FirstOrDefault();
                return Ok(publicacione);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPost("post")]
        public IActionResult Post(Publicacion publicacion)
        {
            try
            {
                publicacion.Usuario = null;
                contexto.Publicaciones.Add(publicacion);
                contexto.SaveChanges();
                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPut("put")]
        public IActionResult Put(Publicacion publicacion)
        {
            try
            {
                var entidad = contexto.Publicaciones.FirstOrDefault(p => p.Id == publicacion.Id);
                if (entidad != null)
                {
                    contexto.Entry(entidad).CurrentValues.SetValues(publicacion);
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

        [HttpDelete("delete")]
        public IActionResult Delete(int publicacionId)
        {
            try
            {
                var entidad = contexto.Publicaciones.FirstOrDefault(p => p.Id == publicacionId);
                if (entidad != null)
                {
                    contexto.Publicaciones.Remove(entidad);
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

        [HttpPatch("baja")]
        public IActionResult Baja(int publicacionId)
        {
            try
            {
                var entidad = contexto.Publicaciones.FirstOrDefault(p => p.Id == publicacionId);
                if (entidad != null)
                {
                    entidad.Estado = 0;
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
