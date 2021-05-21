using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Reseña
    {
        public int UsuarioId { get; set; }
        public int PublicacionId { get; set; }
        public int Puntaje { get; set; }
        public string Encabezado { get; set; }
        public string Contenido { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Usuario usuario { get; set; }
        public Publicacion publicacion { get; set; }
    }
}
