package modules;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reserva implements Serializable {

    //Atributos
    private int idReserva;
    private int idVivienda;
    private int idUsuario;
    private LocalDate fechaInicio;
    private int noches;
    private double precio;
    private int ocupantes;

    /**
     * Constructor Genérico de la clase Reserva
     * @param idReserva Identificador Reserva
     * @param idVivienda Identificador Vivienda
     * @param idUsuario Identificador Usuario
     * @param fechaInicio Fecha de la Reserva
     * @param noches Noches de la Reserva
     * @param precio Precio de la Reserva
     * @param ocupantes Ocupantes de la Reserva
     */

    public Reserva(int idReserva, int idVivienda, int idUsuario, LocalDate fechaInicio, int noches, double precio, int ocupantes) {

        this.idReserva = idReserva;
        this.idVivienda = idVivienda;
        this.idUsuario = idUsuario;
        this.fechaInicio = fechaInicio;
        this.noches = noches;
        this.precio = precio;
        this.ocupantes = ocupantes;
    }

    /**
     * Getters & Setters genéricos de la clase Vivienda
     * ss
     */
    public int getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdVivienda() {
        return idVivienda;
    }
    public void setIdVivienda(int idVivienda) {
        this.idVivienda = idVivienda;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getNoches() {
        return noches;
    }
    public void setNoches(int noches) {
        this.noches = noches;
    }

    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getOcupantes() {
        return ocupantes;
    }
    public void setOcupantes(int ocupantes) {
        this.ocupantes = ocupantes;
    }

    /**
     * Método que comprueba la disponibilidad de la vivienda
     * @param fechaInicio1 LocalDate
     * @param noches1 int
     * @return true en caso de que esté disponible y false en caso contrario
     */
    public boolean periodoComprendido(LocalDate fechaInicio1,int noches1){

        LocalDate fechaFin=this.fechaInicio;
                fechaFin=fechaFin.plusDays(this.noches);
        for (int i = 0; i < noches1; i++) {
            LocalDate fechaComprobar = fechaInicio1.plusDays(i);
            if (fechaComprobar.isAfter(this.fechaInicio) && (fechaComprobar.isBefore(fechaFin))) return true;
        }
        return false;
    }



    /**
     * Metodo que pinta los datos de la Vivienda en la Reserva
     * @param vivienda Vivienda
     * @return Devuelve los datos de la Vivienda pintados
     */
    public String pintaDatos(Vivienda vivienda){
        return "Vivienda: ("+vivienda.getId()+")"+"\n"+
                "Nombre: "+vivienda.getTitulo()+"\n"+
                "Descripcion: "+vivienda.getDescripcion()+"\n"+
                "Provincia: "+vivienda.getProvincia()+"\n"+
                "Localidad: "+vivienda.getLocalidad()+"\n"+
                "Ocupantes maximos: "+vivienda.getMaxOcupantes()+"\n"+
                "Precio por noche: "+vivienda.getPrecioNoche()+"€"+"\n";
    }
    public String pintaDatosParaPropietario(Usuario usuario,Vivienda vivienda){
        return "\n▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ╬ RESERVA ╬ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n " +
                "ID Reserva: " + idReserva +"\n"+
                ((usuario==null)?"":"Usuario: " +usuario.getNombre()+ "\n")+
                "Vivienda: "+ vivienda.getTitulo() +", Descripcion: "+ vivienda.getDescripcion()+" (ID: " + idVivienda +")"+ "\n"+
                "Noches: " + this.noches +"\n"+
                "Fecha Entrada: " + this.fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"\n"+
                "Fecha Salida: " + this.fechaInicio.plusDays(noches).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"\n"+
                "Huespedes: " + this.ocupantes +"\n"+
                "Precio por noche: "+ vivienda.getPrecioNoche() +"EUR"+ "\n"+
                "Total a Pagar: " + (this.precio) +"EUR"+ "\n"+
                "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓";
    }

    /**
     * Método que va a pintar el resumen de la Reserva
     * @return Devuelve un resumen de la Reserva
     */
    public  String pintaResumen(Vivienda v,Usuario u){
        return "\n▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ╬ RESERVA ╬ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n " +
                "ID Reserva: " + idReserva +"\n"+
                "Usuario: "+  u.getNombre()+ "\n"+
                "Vivienda: "+ " "+v.getTitulo() +" (ID: " + idVivienda +")"+ "\n"+
                "Noches: " + noches +"\n"+
                "Fecha Entrada: " + fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"\n"+
                "Fecha Salida: " + fechaInicio.plusDays(noches).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"\n"+
                "Huespedes: " + ocupantes +"\n"+
                "Precio por noche: "+ precio +"EUR"+ "\n"+
                "Total a Pagar: " + precio*noches +"EUR"+ "\n"+
                "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓";
    }
    public  String pintaResumen(Vivienda v,Propietario u){
        return "\n▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ╬ RESERVA ╬ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n " +
                "ID Reserva: " + idReserva +"\n"+
                "Usuario: "+  u.getNombre()+ "\n"+
                "Vivienda: "+ " "+v.getTitulo() +" (ID: " + idVivienda +")"+ "\n"+
                "Noches: " + noches +"\n"+
                "Fecha Entrada: " + fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"\n"+
                "Fecha Salida: " + fechaInicio.plusDays(noches).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"\n"+
                "Huespedes: " + ocupantes +"\n"+
                "Precio por noche: "+ precio +"EUR"+ "\n"+
                "Total a Pagar: " + precio*noches +"EUR"+ "\n"+
                "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓";
    }


    /**
     * toString de la clase Reserva con los atributos de dicha clase impresos.
     * @return Atributos de la clase Reserva
     */
    @Override
    public String toString() {
        return "\n▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓ ╬ RESERVA ╬ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓\n " +
                "ID Reserva: " + idReserva +"\n"+
                "Usuario: "+  idUsuario+ "\n"+
                "Vivienda: "+ " "+"direccion" +" (ID: " + idVivienda +")"+ "\n"+
                "Noches: " + noches +"\n"+
                "Fecha Entrada: " + fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +"\n"+
                "Fecha Salida: " + fechaInicio.plusDays(noches).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"\n"+
                "Huespedes: " + ocupantes +"\n"+
                "Precio por noche: "+ precio +"EUR"+ "\n"+
                "Total a Pagar: " + precio*noches +"EUR"+ "\n"+
                "▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓";
    }
}
