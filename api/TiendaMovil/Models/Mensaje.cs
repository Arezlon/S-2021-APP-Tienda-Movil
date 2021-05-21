using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Mensaje
    {
        [Key]
        public int Id { get; set; }
        [ForeignKey("CompraId")]
        public int CompraId { get; set; }
        [ForeignKey("UsuarioId")]
        public int UsuarioId { get; set; }
        public string Contenido { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Compra Compra { get; set; }
        public Usuario Usuario { get; set; }
    }
}
