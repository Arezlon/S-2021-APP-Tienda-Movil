using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Comentario
    {
        public int UsuarioId { get; set; }
        public int PublicacionId { get; set; }
        public int UsuarioRespuestaId { get; set; }
        public string Contenido { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Usuario Usuario { get; set; }
        public Publicacion Publicacion { get; set; }
    }
}
