using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Direccion
    {
        public int UsuarioId { get; set; }
        public string Nombre { get; set; }
        public string Pais { get; set; }
        public string Provincia { get; set; }
        public string Localidad { get; set; }
        public string Calle { get; set; }
        public int Numero { get; set; }
        public int Piso { get; set; }
        public string Referencia { get; set; }
        public long Latitud { get; set; }
        public long Longitud { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Usuario Usuario { get; set; }
    }
}
