using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Mensaje
    {
        public int CompraId { get; set; }
        public int UsuarioId { get; set; }
        public string Contenido { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Compra Compra { get; set; }
        public Usuario Usuario { get; set; }
    }
}
