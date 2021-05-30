using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.IO;
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
        private readonly IWebHostEnvironment environment;

        public PublicacionesController(DataContext contexto, IConfiguration config, IWebHostEnvironment environment)
        {
            this.contexto = contexto;
            this.config = config;
            this.environment = environment;
        }

        [HttpGet("get")]
        public IActionResult Get(int estado = 1, int usuarioId = -1)
        {
            try
            {
                var publicaciones = contexto.Publicaciones
                    .Where(p => 
                        (estado == -1 ? true : p.Estado == estado) && 
                        (usuarioId == -1 ? true : p.UsuarioId == usuarioId))
                    .Include(p => p.Usuario)
                    .Select(p => new { 
                        Id = p.Id, 
                        UsuarioId = p.UsuarioId, 
                        Titulo = p.Titulo, 
                        Descripcion = p.Descripcion, 
                        Precio = p.Precio, 
                        Categoria = p.Categoria, 
                        Tipo = p.Tipo, 
                        Stock = p.Stock, 
                        Estado = p.Estado, 
                        Creacion = p.Creacion, 
                        Usuario = p.Usuario, 
                        CategoriaNombre = p.CategoriaNombre,
                        TipoNombre = p.TipoNombre,
                        ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == p.Id && i.Estado == 2).FirstOrDefault().Direccion })
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

        [HttpPost("create")]
        public IActionResult Create(Publicacion publicacion)
        {
            try
            {
                publicacion.Estado = 1;
                publicacion.Creacion = DateTime.Now;
                publicacion.UsuarioId = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
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

        /// <summary>Editar toda la entidad</summary>
        [HttpPut("edit")]
        public IActionResult Edit(Publicacion publicacion)
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

        /// <summary>Eliminación real</summary>
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

        /// <summary>Eliminación lógica</summary>
        [HttpPatch("disable")]
        public IActionResult Disable(int publicacionId)
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

        [HttpGet("getcategorias")]
        public IActionResult GetCategorias()
        {
            try
            {
                return Ok(Publicacion.ObtenerCategorias());
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("gettipos")]
        public IActionResult GetTipos()
        {
            try
            {
                return Ok(Publicacion.ObtenerTipos());
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("getmias")]
        public IActionResult GetMias()
        {
            try
            {
                int id = Int32.Parse(User.Claims.First(c => c.Type == "Id").Value);
                var publicaciones = contexto.Publicaciones
                    .Where(p => p.UsuarioId == id)
                    .Include(p => p.Usuario)
                    .Select(p => new {
                        Id = p.Id,
                        UsuarioId = p.UsuarioId,
                        Titulo = p.Titulo,
                        Descripcion = p.Descripcion,
                        Precio = p.Precio,
                        Categoria = p.Categoria,
                        Tipo = p.Tipo,
                        Stock = p.Stock,
                        Estado = p.Estado,
                        Creacion = p.Creacion,
                        Usuario = p.Usuario,
                        CategoriaNombre = p.CategoriaNombre,
                        TipoNombre = p.TipoNombre,
                        ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == p.Id && i.Estado == 2).FirstOrDefault().Direccion
                    })
                    .OrderByDescending(p => p.Creacion)
                    .ToList();
                return Ok(publicaciones);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
    }
}
