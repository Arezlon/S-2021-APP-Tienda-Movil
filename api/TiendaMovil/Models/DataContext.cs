using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class DataContext : DbContext
    {
        public DataContext(DbContextOptions<DataContext> options) : base(options) { }
        public DbSet<Comentario> Comentarios { get; set; }
        public DbSet<Compra> Compras { get; set; }
        public DbSet<Etiqueta> Etiquetas { get; set; }
        public DbSet<Mensaje> Mensajes { get; set; }
        public DbSet<Publicacion> Publicaciones { get; set; }
        public DbSet<PublicacionEtiqueta> PublicacionEtiquetas { get; set; }
        public DbSet<PublicacionImagen> PublicacionImagenes { get; set; }
        public DbSet<Reseña> Reseñas { get; set; }
        public DbSet<Direccion> Direcciones { get; set; }
        public DbSet<Usuario> Usuarios { get; set; }
    }
}
