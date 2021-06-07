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

        /* Carousel publicaciones destacadas: (Populares de la semana en toda la app, ignorando las preferencias del usuario)
	            * Creadas en la última semana
	            * Ordenadas por:
		            * Cantidad de ventas
		            * Promedio de reseñas 
		            * Cantidad de reseñas 
		            * Cantidad de comentarios
	            * Top 10
-           En el card del carousel un boton que diga "ver mas publicaciones destacadas" que redirija a una lista de publicaciones con los mismos parametros pero con paginación infinita. */
        [HttpGet("getdestacadas")]
        public IActionResult GetDestacadas()
        {
            try
            {
                string query = "SELECT TOP 10 " +
                        "p.Id, p.UsuarioId, p.Titulo, p.Descripcion, p.Precio, p.Categoria, p.Tipo, p.Stock, p.Estado, p.Creacion " +
                    "FROM Publicaciones p " +
                        "LEFT JOIN Compras c ON c.PublicacionId = p.Id " +
                        "LEFT JOIN Reseñas r ON r.PublicacionId = p.Id " +
                        "LEFT JOIN Comentarios q ON q.PublicacionId = p.Id " +
                    "GROUP BY p.Id, p.UsuarioId, p.Titulo, p.Descripcion, p.Precio, p.Categoria, p.Tipo, p.Stock, p.Estado, p.Creacion " +
                        " ORDER BY " +
                            " COUNT(c.Id) DESC, " +
                            " IIF(AVG(r.Puntaje) IS NOT NULL, AVG(r.Puntaje), 0) DESC, " +
                            " COUNT(r.Id) DESC, " +
                            " COUNT(q.Id) DESC, " +
                            " p.Id ASC";
                List<Publicacion> publicaciones = contexto.Publicaciones.FromSqlRaw(query).ToList();
                foreach (Publicacion p in publicaciones)
                    p.ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == p.Id && i.Estado == 2).FirstOrDefault()?.Direccion;

                return Ok(publicaciones);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpGet("getrecomendadas")]
        public IActionResult GetRecomendadas()
        {
            try
            {
                int usuarioId = int.Parse(User.Claims.First(c => c.Type == "Id").Value);
                Compra ultimaCompra = contexto.Compras.Where(c => c.UsuarioId == usuarioId).OrderByDescending(c => c.Id).FirstOrDefault();
                List<Publicacion> resultado = new List<Publicacion>();
                if (ultimaCompra != null)
                {
                    string query = "SELECT TOP 10 " +
                            "p.Id, p.UsuarioId, p.Titulo, p.Descripcion, p.Precio, p.Categoria, p.Tipo, p.Stock, p.Estado, p.Creacion " +
                        "FROM Publicaciones p " +
                            "LEFT JOIN Compras c ON c.PublicacionId = p.Id " +
                            "LEFT JOIN Reseñas r ON r.PublicacionId = p.Id " +
                            "LEFT JOIN PublicacionEtiquetas pe ON pe.PublicacionId = p.Id " +
                            "LEFT JOIN Etiquetas e ON pe.EtiquetaId = e.Id " +
                        "WHERE e.Nombre IN((SELECT et.Nombre FROM Compras com " +
                                "JOIN Publicaciones pub ON com.PublicacionId = pub.Id " +
                                "JOIN PublicacionEtiquetas pet ON pet.PublicacionId = pub.Id " +
                                "JOIN Etiquetas et ON et.Id = pet.EtiquetaId " +
                                $"WHERE com.Id = {ultimaCompra.Id})) AND p.Id != {ultimaCompra.PublicacionId} " +
                        "GROUP BY p.Id, p.UsuarioId, p.Titulo, p.Descripcion, p.Precio, p.Categoria, p.Tipo, p.Stock, p.Estado, p.Creacion " +
                        "ORDER BY COUNT(e.Id) DESC, COUNT(c.Id) DESC, COUNT(r.Id) DESC, IIF(AVG(r.Puntaje) IS NOT NULL, AVG(r.Puntaje), 0) DESC, p.Id ASC";
                    resultado = contexto.Publicaciones.FromSqlRaw(query).ToList();
                    if (resultado.Count < 10)
                        resultado.AddRange(contexto.Publicaciones.OrderByDescending(p => p.Id).Take(10 - resultado.Count).ToList());
                } else
                {
                    resultado = contexto.Publicaciones.OrderByDescending(p => p.Id).Take(10).ToList();
                }

                foreach (Publicacion p in resultado)
                    p.ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == p.Id && i.Estado == 2).FirstOrDefault()?.Direccion;

                return Ok(resultado);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }


        [HttpGet("getbyid")]
        public IActionResult GetById(int id)
        {
            try
            {
                var publicacione = contexto.Publicaciones
                    .Where(p => p.Id == id)
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
                    /*.Select(p => new {
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
                    })*/
                    .OrderByDescending(p => p.Creacion)
                    .ToList();
                foreach (Publicacion p in publicaciones)
                    p.ImagenDir = contexto.PublicacionImagenes.Where(i => i.PublicacionId == p.Id && i.Estado == 2).FirstOrDefault()?.Direccion;
                return Ok(publicaciones);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
    }
}
