using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using TiendaMovil.Models;

namespace TiendaMovil.Controllers
{
    [Route("api/[controller]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    [ApiController]
    public class UsuariosController : ControllerBase
    {
        private readonly DataContext contexto;
        private readonly IConfiguration config;

        public UsuariosController(DataContext contexto, IConfiguration config)
        {
            this.contexto = contexto;
            this.config = config;
        }

        [HttpPost("login")]
        [AllowAnonymous]
        public async Task<IActionResult> Login([FromBody] LoginRequest loginRequest)
        {
            try
            {
                string hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                        password: loginRequest.clave,
                        salt: System.Text.Encoding.ASCII.GetBytes(config["Salt"]),
                        prf: KeyDerivationPrf.HMACSHA1,
                        iterationCount: 400,
                        numBytesRequested: 256 / 8));

                var u = await contexto.Usuarios.FirstOrDefaultAsync(x => x.Email == loginRequest.email);
                if (u == null || u.Clave != hashed)
                    return BadRequest("Nombre de usuario o clave incorrecta");

                var key = new SymmetricSecurityKey(
                    System.Text.Encoding.ASCII.GetBytes(config["TokenAuthentication:SecretKey"]));
                var credenciales = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
                var claims = new List<Claim>
                {
                    new Claim(ClaimTypes.Name, u.Email),
                    new Claim("FullName", u.Nombre + " " + u.Apellido),
                    new Claim("Id", u.Id.ToString()),
                    new Claim(ClaimTypes.Role, "Usuario"),
                };

                var token = new JwtSecurityToken(
                    issuer: config["TokenAuthentication:Issuer"],
                    audience: config["TokenAuthentication:Audience"],
                    claims: claims,
                    expires: DateTime.Now.AddHours(24),
                    signingCredentials: credenciales
                );
                return Ok(new { statusCode = 10, token = new JwtSecurityTokenHandler().WriteToken(token), usuario = u });
            }
            catch (Exception ex)
            {
                return BadRequest(ex);
            }
        }

        [HttpPost("create")]
        [AllowAnonymous]
        public IActionResult Create(Usuario usuario)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    usuario.Clave = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                        password: usuario.Clave,
                        salt: System.Text.Encoding.ASCII.GetBytes(config["Salt"]),
                        prf: KeyDerivationPrf.HMACSHA1,
                        iterationCount: 400,
                        numBytesRequested: 256 / 8));
                    usuario.Permisos = 1;
                    usuario.Estado = 1;
                    usuario.Creacion = DateTime.Now;

                    contexto.Usuarios.Add(usuario);
                    contexto.SaveChanges();
                    return Ok(true);
                }
                return BadRequest();
            }
            catch (Exception ex)
            {
                return BadRequest(ex);
            }
        }
        [HttpGet("get")]
        public IActionResult ObtenerUsuario()
        {
            try
            {
                int id = int.Parse(User.Claims.First(x => x.Type == "Id").Value);
                var usuario = contexto.Usuarios.Find(id);
                return Ok(usuario);
            }
            catch (Exception ex)
            {
                return BadRequest("ERROR: " + ex);
            }
        }

        [HttpPut("edit")]
        public async Task<IActionResult> EditarUsuario([FromBody] Usuario Usuario)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    Usuario u = contexto.Usuarios.Find(int.Parse(User.Claims.First(c => c.Type == "Id").Value));
                    u.Nombre = Usuario.Nombre;
                    u.Apellido = Usuario.Apellido;
                    u.Dni = Usuario.Dni;
                    u.Telefono = Usuario.Telefono;
                    u.Email = Usuario.Email;

                    contexto.Usuarios.Update(u);
                    await contexto.SaveChangesAsync();
                    return Ok(Usuario);
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
