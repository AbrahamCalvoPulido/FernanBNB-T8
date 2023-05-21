package modules;

import java.io.Serializable;
import java.util.ArrayList;

public class Propietario  implements Serializable {

    //Atributos
    private int id;
    private String nombre;
    private String clave;
    private String email;
    private ArrayList<Vivienda> viviendas;
    private boolean verificado;

    /**
     * Constructor de la clase Propietario
     * @param id id del propietario
     * @param nombre nombre del propietario
     * @param clave clave del propietario
     * @param email email del propietario
     */
    public Propietario(int id, String nombre, String clave, String email) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        this.viviendas = new ArrayList<>();
        this.verificado=false;
    }
    public Propietario(int id, String nombre, String clave, String email,boolean verificado) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.email = email;
        this.viviendas = new ArrayList<>();
        this.verificado=verificado;
    }
    public Propietario() {
    }

    /**
     * Getter && Setter
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

    public ArrayList<Vivienda> getViviendas() {
        return viviendas;
    }

    public void setViviendas(ArrayList<Vivienda> viviendas) {
        this.viviendas = viviendas;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    /**
     * @param email1 String
     * @param clave1 String
     * @return Metodo que comprueba si el email y la clave son correctos
     */
    public  boolean login(String email1, String clave1) {
        return (email.equals(email1) && clave.equals(clave1));
    }

    /**
     * @param vivienda Vivienda
     * Método que añade una vivienda
     */
    public  void addVivienda(Vivienda vivienda) {
        viviendas.add(vivienda);
    }

    /**
     * Devuelve todas las reservas de todas las viviendas
     *
     * @return ArrayList>Reserva reservasTotales
     */
    public ArrayList<Reserva> getReservas(){
        ArrayList<Reserva> reservasTotales = new ArrayList<>();
        for (Vivienda v :
                viviendas) {
            reservasTotales.addAll(v.getReservas());
        }
        return reservasTotales;
    }


    @Override
    public String toString() {
        return "\n════════════ Perfil Propietario ════════════\n"+
                "Usuario: " + email+ "\n"+
                "Nombre: " + nombre ;
    }

}
