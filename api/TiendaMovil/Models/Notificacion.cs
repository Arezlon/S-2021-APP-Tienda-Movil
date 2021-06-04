using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Notificacion
    {
        [Key]
        public int Id { get; set; }
        public int Tipo { get; set; }
        public string Mensaje
        {
            get
            {
                switch (Tipo)
                {
                    case 1:
                        return "Alguien compró tu publicación";
                    case 2:
                        return "Alguien hizo una pregunta en tu publicación";
                    case 3:
                        return "Alguien dejó una reseña en tu publicación"; //Sin terminar
                    case 4:
                        return "Tu publicación se quedó sin stock";
                    case 5:
                        return "Un vendedor respondió tu pregunta";
                    //agregar caso 6, stock bajo (20%) ?
                    default:
                        return "";
                }
            }
        }

        [ForeignKey("UsuarioId")]
        public int UsuarioId { get; set; }
        [ForeignKey("CompraId")]
        public int CompraId { get; set; }
        public int PublicacionId { get; set; }
        [ForeignKey("PublicacionId")]
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public Compra Compra { get; set; }
        public Publicacion Publicacion { get; set; }
        public Usuario Usuario { get; set; }
    }
}
