using Microsoft.OData.Edm;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace TiendaMovil.Models
{
    public class Usuario
    {
        public enum PermisosEnum
        {
            Usuario = 1,
            Administrador = 2
        }

        [Key]
        public int Id { get; set; }
        public string Nombre { get; set; }
        public string Apellido { get; set; }
        public string Dni { get; set; }
        public string Email { get; set; }
        public string Telefono { get; set; }
        public string Clave { get; set; }
        public double Fondos { get; set; }
        public int Permisos { get; set; }
        public int Estado { get; set; }
        public DateTime Creacion { get; set; }
        public string Direccion { get; set; }
        public string Localidad { get; set; }
        public string Provinicia { get; set; }
        public string Pais { get; set; }
        public string PermisosNombre => Permisos > 0 ? ((PermisosEnum)Permisos).ToString() : "";
    }
}
