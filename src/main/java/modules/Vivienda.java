package modules;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Vivienda implements Serializable {

    //Atributos
    private int id;
    private String titulo;
    private String descripcion;
    private String localidad;
    private String provincia;
    private int maxOcupantes;
    private double precioNoche;
    private ArrayList<Reserva> reservas;


    /**
     * Constructor Genérico de la clase Viviendas
     *
     * @param id Identificador Vivienda
     * @param titulo Título de la Vivienda
     * @param descripcion Descripción de la Vivienda
     * @param localidad Localidad de la Vivienda
     * @param provincia Provincia de la Vivienda
     * @param maxOcupantes Capacidad de la Vivienda
     * @param precioNoche Precio * noche de la Vivienda
     */
    public Vivienda(int id, String titulo, String descripcion, String localidad, String provincia, int maxOcupantes, double precioNoche) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.maxOcupantes = maxOcupantes;
        this.precioNoche = precioNoche;
        this.reservas= new ArrayList<>();

    }



    /**
     *
     * Getters & Setters de la Clase Vivienda
     */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public int getMaxOcupantes() {
        return maxOcupantes;
    }
    public void setMaxOcupantes(int maxOcupantes) {
        this.maxOcupantes = maxOcupantes;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }
    public void setPrecioNoche(int precioNoche) {
        this.precioNoche = precioNoche;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    /**
     * Método que comprueba la Disponibilidad de la Vivienda
     * @param fechaInicio LocalDate
     * @param noches int
     * @param ocupantes int
     * @return True si la Vivienda está disponible una fecha predeterminada y false en caso contrario.
     */
    public boolean tieneDisponibilidad(LocalDate fechaInicio, int noches, int ocupantes,ArrayList<Reserva> reservasImportadas){
        if (ocupantes > getMaxOcupantes()) {
            return false;
        }
        for (Reserva r : reservasImportadas) {
            if (r.periodoComprendido(fechaInicio, noches)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Método que va a realizar una Reserva dentro de la Vivienda
     * @param noches int
     * @param fechaInicio LocalDate
     * @param idUsuario int
     * @param idReserva int
     * @param ocupantes int
     */
    public  Reserva addReserva(int noches, LocalDate fechaInicio, int idUsuario, int idReserva,int ocupantes){
        Reserva r = new Reserva(idReserva,getId(),idUsuario,fechaInicio,noches,precioNoche*noches,ocupantes);
        this.reservas.add(r);

        return r;
    }

    /**
     * Método que se encarga de comprobar que no halla ninguna Reserva pendiente en cada Usuario
     * @return True si el Usuario tiene alguna Reserva pendiente y false en caso contrario
     *
     */
    public  boolean tieneReservasPendientes(){
        for (Reserva r :
                reservas) {
            if (r.getIdUsuario()!=0) return true;
        }
        return false;
    }

    /**
     * Método para eliminar una Reserva determinada
     * @param reserva
     * @return Mensaje diciendo si se ha eliminado la Reserva correctamente o Error en caso contrario
     */
    public void deleteReserva(Reserva reserva){
        reservas.remove(reserva);
    }

    /**
     * Método que se encarga de pintar los datos de la Vivienda ""SE SUPONE""
     * @return Devuelve un String con los datos de dicha Vivienda
     */
    public  String pintaDatos(){
        return "Vivienda" +"\n"+
                "id=" + id +"\n"+
                " *********" + titulo +"**********" +"\n"+
                "Descripcion: " + descripcion + "\n"+
                "Localidad: " + localidad + "\n"+
                "Provincia: " + provincia + "\n"+
                "ViajerosMax: " + maxOcupantes+ "\n"+
                "Preecio/Noche" + precioNoche;
    }

    /**
     * Método que se encarga de pintar el resumen de la vivienda para el Administrador
     * @return Devuelve un String con el resumen de la Vivienda
     */
    public static String pintaResumen(){
        return "";
    }

    /**
     * toString de la clase Vivienda con los atributos de dicha clase impresos.
     * @return Atributos de la clase Vivienda
     */
    @Override
    public String toString() {
         return "\n§§§§§§§§§§§§§§§§§§§[ █ VIVIENDA █ ]§§§§§§§§§§§§§§§§§§§ \n" +
                "ID: "+ id +'\n' +
                " Ciudad: " + getLocalidad() + '\n' +
                " Provincia: " + getProvincia() + '\n' +
                " Nombre: " + getTitulo() + '\n' +
                " Direccion: " + getDescripcion() + '\n' +
                " Numero Maximo Huespedes: " + getMaxOcupantes() +'\n' +
                " Precio por Noche: " + getPrecioNoche() +"EUR\n" +
                "§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§";
    }
}
