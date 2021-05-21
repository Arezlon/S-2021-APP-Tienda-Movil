using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using TiendaMovil.Models;

namespace TiendaMovil.Controllers
{
    [Route("api/[controller]")]
    //[Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
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
        public IActionResult Get(int estado = 1, int usuarioId = -1)
        {
            try
            {
                var publicaciones = contexto.Publicaciones
                    .Where(p => p.Estado == estado && (usuarioId == -1 ? true : p.UsuarioId == usuarioId))
                    .ToList();
                return Ok(publicaciones);
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

        [HttpPatch("patch")]
        public IActionResult Patch(Publicacion publicacion)
        {
            try
            {
                var entidad = contexto.Publicaciones.FirstOrDefault(p => p.Id == publicacion.Id);
                if (entidad != null)
                {
                    // entidad = publicacion; // error de tracking
                    entidad.UsuarioId = publicacion.UsuarioId;
                    entidad.Titulo = publicacion.Titulo;
                    entidad.Descripcion = publicacion.Descripcion;
                    entidad.Precio = publicacion.Precio;
                    entidad.Categoria = publicacion.Categoria;
                    entidad.Tipo = publicacion.Tipo;
                    entidad.Stock = publicacion.Stock;
                    entidad.Estado = publicacion.Estado;
                    entidad.Creacion = publicacion.Creacion;

                    contexto.Publicaciones.Update(entidad);
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
    }
}
