using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Transaccion
    {
        public enum TiposEnum
        {
            Carga = 1,
            Compra = 2,
            Venta = 3
        }

        [Key]
        public int Id { get; set; }
        public int CompraId { get; set; }
        public int UsuarioId { get; set; }
        public double Importe { get; set; }
        public double Balance { get; set; }
        public int Tipo { get; set; }
        public string TiposNombre => Tipo > 0 ? ((TiposEnum)Tipo).ToString() : "";
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Compra Compra { get; set; }
        public Usuario Usuario { get; set; }
       
        
    }
}
