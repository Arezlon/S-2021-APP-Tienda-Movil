using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
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
    public class PublicacionImagenesController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;
        private readonly IWebHostEnvironment environment;

        public PublicacionImagenesController(DataContext contexto, IConfiguration config, IWebHostEnvironment environment)
        {
            this.contexto = contexto;
            this.config = config;
            this.environment = environment;
        }

        [HttpGet("get")]
        public IActionResult Get(int publicacionId)
        {
            try
            {
                var imagenes = contexto.PublicacionImagenes.Where(i => i.PublicacionId == publicacionId).ToList();
                return Ok(imagenes);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPost("create")]
        public IActionResult Create(IFormFileCollection imagenes, [FromForm] int publicacionId)
        {
            try
            {
                List<IFormFile> lista = imagenes.ToList();
                var publicacion = contexto.Publicaciones.FirstOrDefault(p => p.Id == publicacionId);
                if (publicacion != null)
                {
                    string wwwPath = environment.WebRootPath;
                    string path = Path.Combine(wwwPath, "Uploads");
                    if (!Directory.Exists(path)) Directory.CreateDirectory(path);
                    DateTime hoy = DateTime.Now;

                    int cantidadImagenes = contexto.PublicacionImagenes.Count(i => i.PublicacionId == publicacionId);

                    foreach (IFormFile imagen in imagenes)
                    {
                        string fileName =
                            $"{publicacionId}_{hoy.Day}_{hoy.Month}_{hoy.Year}_-_{Guid.NewGuid()}{Path.GetExtension(imagen.FileName)}";
                        string pathCompleto = Path.Combine(path, fileName);

                        using (FileStream stream = new FileStream(pathCompleto, FileMode.Create))
                        {
                            imagen.CopyTo(stream);
                        }

                        contexto.PublicacionImagenes.Add(new PublicacionImagen
                        {
                            PublicacionId = publicacionId,
                            Direccion = Path.Combine("/Uploads", fileName),
                            Estado = (cantidadImagenes == 0 && lista.IndexOf(imagen) == 0) ? 2 : 1,
                            Creacion = DateTime.Now
                        });
                        contexto.SaveChanges();
                    }
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
        public IActionResult Delete(int imagenId)
        {
            try
            {
                var entidad = contexto.PublicacionImagenes.FirstOrDefault(i => i.Id == imagenId);
                if (entidad != null)
                {
                    if (entidad.Estado == 2)
                        contexto.PublicacionImagenes.Where(i => i.PublicacionId == entidad.PublicacionId && i.Id != entidad.Id).FirstOrDefault().Estado = 2;
                    contexto.PublicacionImagenes.Remove(entidad);
                        
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

        [HttpPatch("destacar")]
        public IActionResult Destacar(PublicacionImagen imagen)
        {
            try
            {
                var destacadaAnterior = contexto.PublicacionImagenes.Where(i => i.PublicacionId == imagen.PublicacionId && i.Estado == 2).Single();
                var entidad = contexto.PublicacionImagenes.FirstOrDefault(i => i.Id == imagen.Id);
                if (entidad != null)
                {
                    entidad.Estado = 2;
                    destacadaAnterior.Estado = 1;
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