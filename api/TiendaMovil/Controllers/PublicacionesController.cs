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
        public IActionResult Get()
        {
            try
            {
                var publicaciones = contexto.Publicaciones.Where(p => p.Estado == 1).ToList();
                return Ok(publicaciones);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }
    }
}
