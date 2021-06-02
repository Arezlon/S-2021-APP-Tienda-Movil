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
    public class EtiquetasController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public EtiquetasController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpGet("get")]
        public IActionResult Get(int publicacionId)
        {
            try
            {
                List<Etiqueta> etiquetas = contexto.PublicacionEtiquetas
                    .Where(e => e.PublicacionId == publicacionId)
                    .Select(e => e.Etiqueta).ToList();
                return Ok(etiquetas);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPost("create")]
        public IActionResult Create(List<PublicacionEtiqueta> publicacionEtiquetas)
        {
            try
            {

                foreach (PublicacionEtiqueta pe in publicacionEtiquetas)
                {
                    pe.Estado = 1;
                    pe.Creacion = DateTime.Now;
                    pe.Publicacion = null;
                    Etiqueta existente = contexto.Etiquetas.Where(e => e.Nombre == pe.Etiqueta.Nombre).FirstOrDefault();
                    if (existente != null)
                    {
                        // Ya existe una etiqueta con ese nombre
                        pe.EtiquetaId = existente.Id;
                    } else
                    {
                        // No existe una etiqueta con ese nombre (alta?)
                        pe.Etiqueta.Estado = 1;
                        pe.Etiqueta.Creacion = DateTime.Now;
                        contexto.Etiquetas.Add(pe.Etiqueta);
                        contexto.SaveChanges();

                        pe.EtiquetaId = contexto.Etiquetas.Where(e => e.Nombre == pe.Etiqueta.Nombre).OrderByDescending(e => e.Creacion).LastOrDefault().Id;
                    }
                    pe.Etiqueta = null;
                }

                contexto.PublicacionEtiquetas.AddRange(publicacionEtiquetas);
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