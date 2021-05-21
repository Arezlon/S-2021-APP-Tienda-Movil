using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Publicacion
    {
        public enum CategoriasEnum
        {
            uno = 1,
            dos = 2,
            tres = 3
        }

        public enum TiposEnum
        {
            usado = 1,
            nuevo = 2
        }

        [Key]
        public int Id { get; set; }
        [ForeignKey("UsuarioId")]
        public int UsuarioId { get; set; }
        public string Titulo { get; set; }
        public string Descripcion { get; set; }
        public float Precio { get; set; }
        public int Categoria { get; set; }
        public int Tipo { get; set; }
        public int Stock { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Usuario Usuario { get; set; }
        public string CategoriaNombre => Categoria > 0 ? ((CategoriasEnum)Categoria).ToString() : "";
        public string TipoNombre => Tipo > 0 ? ((TiposEnum)Tipo).ToString() : "";
    }
}
