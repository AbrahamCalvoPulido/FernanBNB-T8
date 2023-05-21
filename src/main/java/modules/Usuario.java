package modules;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Usuario implements Serializable {

    //Atributos
    private int id;
    private String nombre;
    private String clave;
    private String email;
    private ArrayList<Reserva> reservas;
    private boolean verificado;

    /**
     * Constructor de la clase Usuario
     *
     * @param nombre Nombre del usuario
     * @param clave Clave del usuario
     * @param email Email del usuario
     */
    public Usuario(int id,String nombre, String clave, String email) {
        this.id=id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        this.reservas = new ArrayList<>();
        this.verificado=false;
    }

    public Usuario(int id, String nombre, String clave, String email, boolean verificado) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        this.reservas = new ArrayList<>();
        this.verificado = verificado;
    }

    /**
     *
     * Getters y Setters de la clase Usuarios
     */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(ArrayList<Reserva> reservas) {
        this.reservas = reservas;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    /**
     * Método para agregar una reserva a la lista de reservasPendientes del usuario
     * @return Devuelve las reservas pendientes del usuario
     */
    public ArrayList<Reserva> reservasPendientes(){
        ArrayList<Reserva> reservasPendientes = new ArrayList<>();
        for (Reserva r :
                reservas) {
            if (r.getFechaInicio().plusDays(r.getNoches()).isAfter(LocalDate.now()))
                reservasPendientes.add(r);
        }
    return reservasPendientes;
    }

    /**
     * Método que enseña el histórico de reservas del usuario
     * @return Devuelve todas las reservas que ha realizado el usuario
     */
    public ArrayList<Reserva> reservasHistorico(){
        ArrayList<Reserva> reservasHistorico = new ArrayList<>();
        for (Reserva r :
                reservas) {
            if (r.getFechaInicio().plusDays(r.getNoches()).isBefore(LocalDate.now()))
                reservasHistorico.add(r);
        }
        return reservasHistorico;
    }

    /**
     * Método que añade una reserva a la lista de reservas del usuario
     * @param reserva
     * @return aniade una reserva
     */
    public void addReserva(Reserva reserva){
        reservas.add(reserva);
    }

    /**
     * Método que elimina la reserva
     * @param reserva
     * @return Borra una reserva
     */
    public void deleteReserva(Reserva reserva){
        reservas.remove(reserva);
    }

    /**
     * Método que resume y pinta la información del usuario
     * @return el resumen de las reservas que ha realizado el usuario o de la clase
     */
    public static String pintaResumen(){
        return "";
    }

    @Override
    public String toString() {
        return "\n════════════ Perfil Usuario ════════════\n"+
                "Usuario: " + email + "\n"+
                "Nombre: " + nombre+"\n" ;
    }
}
