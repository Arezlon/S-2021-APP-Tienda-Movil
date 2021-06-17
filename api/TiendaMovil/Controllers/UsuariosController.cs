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

        [HttpPost("testdata")]
        [AllowAnonymous]
        public async Task<IActionResult> TestData()
        {
            try
            {
                Random random = new Random();
                for (int u = 0; u < 5; u++)
                {
                    Usuario usuario = new Usuario();
                    usuario.Nombre = $"usuario{u}";
                    usuario.Apellido = "test";
                    usuario.Dni = random.Next(10000000, 99999999).ToString();
                    usuario.Email = $"usuario{u}@mail.com";
                    usuario.Telefono = random.Next(100000000, 999999999).ToString();
                    usuario.Clave = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                        password: "111",
                        salt: System.Text.Encoding.ASCII.GetBytes(config["Salt"]),
                        prf: KeyDerivationPrf.HMACSHA1,
                        iterationCount: 400,
                        numBytesRequested: 256 / 8));
                    usuario.Permisos = 1;
                    usuario.Fondos = random.Next(5000, 1500000);
                    usuario.Estado = 1;
                    usuario.Creacion = DateTime.Now;

                    contexto.Usuarios.Add(usuario);
                    await contexto.SaveChangesAsync();
                    usuario.Id = contexto.Usuarios.OrderBy(e => e.Id).Last().Id;

                    int cantidadPublicaciones = random.Next(0, 10);
                    for (int p = 0; p < cantidadPublicaciones; p++)
                    {
                        Publicacion publicacion = new Publicacion();
                        publicacion.UsuarioId = usuario.Id;
                        publicacion.Titulo = $"publicacion #{p} - u #{u}";
                        publicacion.Descripcion = $"{p}";
                        publicacion.Precio = random.Next(100, 2000001);
                        publicacion.Categoria = random.Next(1, 15);
                        publicacion.Titulo += $" - ({publicacion.CategoriaNombre})";
                        publicacion.Tipo = random.Next(1, 3);
                        publicacion.Stock = publicacion.Tipo == 1 ? 1 : random.Next(1, 10);
                        publicacion.Estado = 1;
                        publicacion.Creacion = DateTime.Now;

                        contexto.Publicaciones.Add(publicacion);
                        await contexto.SaveChangesAsync();
                        publicacion.Id = contexto.Publicaciones.OrderBy(e => e.Id).Last().Id;

                        int cantidadComentarios = random.Next(0, 11);
                        // Comentarios
                        for (int c = 0; c < cantidadComentarios; c++)
                        {
                            Comentario comentario = new Comentario();
                            comentario.UsuarioId = usuario.Id;
                            comentario.PublicacionId = publicacion.Id;
                            comentario.Pregunta = "Esta es una pregunta ¿qué tal?";
                            comentario.Respuesta = random.Next(0, 3) == 0 ? "respondido!" : null; // 1/3 de probabilidad de tener respuesta
                            if (comentario.Respuesta != null)
                            {
                                //Notificación tipo 5, nueva respuesta de un vendedor
                                Notificacion notif = new Notificacion();
                                notif.PublicacionId = comentario.PublicacionId;
                                notif.Tipo = 5;
                                notif.UsuarioId = comentario.UsuarioId;
                                notif.Estado = 1;
                                notif.Creacion = DateTime.Now;
                                notif.CompraId = null;
                                contexto.Notificaciones.Add(notif);
                            }
                            comentario.Estado = 1;
                            comentario.Creacion = DateTime.Now;

                            //Notificación tipo 2, nueva pregunta de un usuario
                            Notificacion notificacion = new Notificacion();
                            notificacion.PublicacionId = comentario.PublicacionId;
                            notificacion.Tipo = 2;
                            notificacion.UsuarioId = publicacion.UsuarioId;
                            notificacion.Estado = 1;
                            notificacion.Creacion = DateTime.Now;
                            notificacion.CompraId = null;
                            contexto.Notificaciones.Add(notificacion);

                            contexto.Comentarios.Add(comentario);
                            await contexto.SaveChangesAsync();
                        }

                        int cantidadCompras = random.Next(0, 5);
                        // Compras
                        for (int cc = 0; cc < cantidadCompras; cc++)
                        {
                            if (publicacion.Stock == 0)
                                break;
                            // Generar la compra eligiendo un usuario aleatorio (que no sea el creador de la publicacion)
                            Compra compra = new Compra();
                            List<Usuario> usuarios = contexto.Usuarios.Where(u => u.Id != usuario.Id).ToList();
                            Usuario comprador = usuarios[random.Next(0, usuarios.Count)];
                            compra.UsuarioId = comprador.Id;
                            compra.PublicacionId = publicacion.Id;
                            compra.Cantidad = random.Next(1, publicacion.Stock + 1);
                            compra.Precio = publicacion.Precio;
                            compra.Estado = 1;
                            compra.Creacion = DateTime.Now;

                            contexto.Compras.Add(compra);
                            await contexto.SaveChangesAsync();
                            compra.Id = contexto.Compras.OrderBy(e => e.Id).Last().Id;

                            //Transacción 1 >> Compra >> descontar fondos del comprador
                            Transaccion trCompra = new Transaccion();
                            trCompra.CompraId = compra.Id;
                            trCompra.UsuarioId = comprador.Id;
                            trCompra.Importe = compra.Precio;
                            comprador.Fondos = Math.Max(0, comprador.Fondos - compra.Precio);
                            trCompra.Balance = comprador.Fondos;
                            trCompra.Tipo = 2;
                            trCompra.Estado = 1;
                            trCompra.Creacion = DateTime.Now;
                            contexto.Transacciones.Add(trCompra);

                            //Transacción 2 >> Venta
                            Transaccion trVenta = new Transaccion();
                            trVenta.CompraId = compra.Id;
                            trVenta.UsuarioId = publicacion.UsuarioId;
                            trVenta.Importe = compra.Precio;
                            //Usuario vendedor = contexto.Usuarios.Find(usuario);
                            usuario.Fondos += trVenta.Importe;
                            trVenta.Balance = usuario.Fondos;
                            trVenta.Tipo = 3;
                            trVenta.Estado = 1;
                            trVenta.Creacion = DateTime.Now;
                            contexto.Transacciones.Add(trVenta);

                            //Publicación
                            publicacion.Stock -= compra.Cantidad;

                            if (publicacion.Stock == 0)
                            {
                                publicacion.Estado = 0;
                                //Notificación tipo 4, publicación sin stock
                                Notificacion notificacionStock = new Notificacion();
                                notificacionStock.PublicacionId = compra.PublicacionId;
                                notificacionStock.Tipo = 4;
                                notificacionStock.UsuarioId = usuario.Id;
                                notificacionStock.Estado = 1;
                                notificacionStock.Creacion = DateTime.Now;
                                notificacionStock.CompraId = null;
                                contexto.Notificaciones.Add(notificacionStock);
                            }

                            //Notificación tipo 1, venta de una publicación
                            Notificacion notificacion = new Notificacion();
                            notificacion.CompraId = compra.Id;
                            notificacion.PublicacionId = compra.PublicacionId;
                            notificacion.Tipo = 1;
                            notificacion.UsuarioId = usuario.Id;
                            notificacion.Estado = 1;
                            notificacion.Creacion = DateTime.Now;
                            contexto.Notificaciones.Add(notificacion);

                            await contexto.SaveChangesAsync();

                            if (random.Next(0, 3) == 0) // 1/3 de probabilidad de haber una reseña
                            {
                                Reseña reseña = new Reseña();
                                reseña.UsuarioId = compra.UsuarioId;
                                reseña.PublicacionId = compra.PublicacionId;
                                reseña.Puntaje = random.NextDouble() * 10 / 2;
                                reseña.Encabezado = $"reseña de la publicacion #{publicacion.Id}";
                                reseña.Contenido = $"usuario #{reseña.UsuarioId}";
                                reseña.Estado = 1;
                                reseña.Creacion = DateTime.Now;

                                contexto.Reseñas.Add(reseña);

                                //Notificación tipo 3, nueva reseña
                                Notificacion notif = new Notificacion();
                                notif.PublicacionId = publicacion.Id;
                                notif.Tipo = 3;
                                notif.UsuarioId = publicacion.UsuarioId;
                                notif.Estado = 1;
                                notif.Creacion = DateTime.Now;
                                //notificacion.CompraId = 
                                contexto.Notificaciones.Add(notif);

                                await contexto.SaveChangesAsync();
                            }
                        }
                    }
                }

                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex);
            }
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

        [HttpGet("getdatosperfil")]
        public IActionResult ObtenerDatosPerfilUsuario(int usuarioId)
        {
            try
            {
                PerfilDataResponse datos = new PerfilDataResponse();
                int id = usuarioId == -1 ? int.Parse(User.Claims.First(x => x.Type == "Id").Value) : usuarioId;
                datos.Usuario = contexto.Usuarios.Find(id);

                List<int> publicacionesIds = contexto.Publicaciones.Where(p => p.UsuarioId == id).Select(p => p.Id).ToList();

                List<Reseña> reseñas = contexto.Reseñas.Where(r => publicacionesIds.Contains(r.PublicacionId)).ToList();
                datos.CantidadReseñas = reseñas.Count;

                datos.CantidadVentas = contexto.Compras.Where(c => publicacionesIds.Contains(c.PublicacionId)).ToList().Count;

                if (datos.CantidadReseñas > 0)
                    datos.Valoracion = (int)(Math.Round(reseñas.Average(r => r.Puntaje), 1) * 2 * 10);
                else
                    datos.Valoracion = 0;

                if (datos.CantidadVentas >= 5 && datos.CantidadReseñas >= 3)
                {
                    // Si el vendedor ya tiene una cierta cantidad de ventas y reseñas, settear su reputacion segun el porcentaje de valoraciones
                    if (datos.Valoracion > 90)
                        datos.Reputacion = "Excelente";
                    else if (datos.Valoracion > 70)
                        datos.Reputacion = "Buena";
                    else if (datos.Valoracion > 50)
                        datos.Reputacion = "Regular";
                    else if (datos.Valoracion > 30)
                        datos.Reputacion = "Mala";
                    else
                        datos.Reputacion = "Pésima";

                } else
                {
                    // Si todavía no tiene muchas reseñas, es porque es nuevo en TiendaMovil
                    datos.Reputacion = "Nuevo";
                }

                return Ok(datos);
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
                    u.Direccion = Usuario.Direccion;
                    u.Localidad = Usuario.Localidad;
                    u.Provinicia = Usuario.Provinicia;
                    u.Pais = Usuario.Pais;

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
