using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class PerfilDataResponse
    {
        public Usuario Usuario { get; set; }
        public int Valoracion { get; set; }
        public int CantidadReseñas { get; set; }
        public int CantidadVentas { get; set; }
        public string Reputacion { get; set; }
    }
}
