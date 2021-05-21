using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Compra
    {
        public int UsuarioId { get; set; }
        public int PublicacionId { get; set; }
        public int Cantidad { get; set; }
        public float Precio { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Usuario Usuario { get; set; }
        public Publicacion Publicacion { get; set; }
    }
}
