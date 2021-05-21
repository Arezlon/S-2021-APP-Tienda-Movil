using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class PublicacionEtiqueta
    {
        [Key]
        public int Id { get; set; }
        [ForeignKey("PublicacionId")]
        public int PublicacionId { get; set; }
        [ForeignKey("EtiquetaId")]
        public int EtiquetaId { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Publicacion Publicacion { get; set; }
        public Etiqueta Etiqueta {get; set;}
}
}
