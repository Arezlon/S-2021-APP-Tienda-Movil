using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Etiqueta
    {
        [Key]
        public int Id { get; set; }
        public string Nombre { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
    }
}
