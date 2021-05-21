using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class PublicacionEtiqueta
    {
        public int PublicacionId { get; set; }
        public int EtiquetaId { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Publicacion Publicacion { get; set; }
        public Etiqueta Etiqueta {get; set;}
}
}
