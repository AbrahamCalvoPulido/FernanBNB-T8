package persistencia;

import modules.*;
import utils.Herramientas;
import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Properties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Persistencia {

    /**
     * Método que se encarga de cargar el archivo de configuración
     * @return Devuelve un objeto de tipo Configuración
     */
    public static Properties cargaProperties() {
        try {
            Properties props = new Properties();
            props.load(new FileReader("config/fernanbnb.properties"));
            return props;
        } catch (Exception e) {
            return new Properties();
        }
    }

    /**
     * Método que se encarga de leer el archivo de configuración
     * @param key
     * @return
     */
    public static String leerProperties(String key){
            return cargaProperties().getProperty(key);
    }

    /**
     * Método que se encarga de guardar el listado de Usuarios en un archivo de configuración
     * @param usuarios
     * @return
     */
    public static boolean guardarListadoUsuarios(ArrayList<Usuario> usuarios) {
        try {
            FileOutputStream fos = new FileOutputStream(cargaProperties().getProperty("datapath.users"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(usuarios);
            oos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Método que se encarga de guardar el listado de propietarios en un archivo de configuración
     * @param propietarios
     * @return
     */
    public static boolean guardarListadoPropietarios(ArrayList<Propietario> propietarios) {
        try {
            FileOutputStream fos = new FileOutputStream(cargaProperties().getProperty("datapath.propietarios"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(propietarios);
            oos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Método que se encarga de guardar el listado de Administradores en un archivo de configuración
     * @param administradores
     * @return
     */
    public static boolean guardarListadoAdministradores(ArrayList<Admin> administradores) {
        try {
            FileOutputStream fos = new FileOutputStream(cargaProperties().getProperty("datapath.administradores"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(administradores);
            oos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Método que se encarga de añadir el último acceso del usuario
     * @param id
     * @throws IOException
     */
    public static void addUltimoAcceso(String id) throws IOException {
        String ruta = "config/ultimoAcceso.properties";
        Properties properties = new Properties();
        properties.load(new FileReader(ruta));
        properties.setProperty(id, Herramientas.fechaHoraActualString());
        try {
            FileOutputStream fos = new FileOutputStream(ruta);
            properties.store(fos, "Ultimo acceso=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se encarga de leer el último acceso del usuario
     * @param id
     * @return
     * @throws IOException
     */
    public static String ultimoAccesoSave(String id) throws IOException {
        String ruta = "config/ultimoAcceso.properties";
        Properties properties = new Properties();
        properties.load(new FileReader(ruta));
        if (!properties.containsKey(id)) {
            addUltimoAcceso(id);
        }
        properties.load(new FileReader(ruta));
        return properties.getProperty(id);

    }

    /**
     * Método que carga el listado de Usuarios en un archivo de configuración
     * @return
     */
    public static ArrayList<Usuario> cargarListadoUsuarios() {
        ArrayList<Usuario> users = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(cargaProperties().getProperty("datapath.users"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (ArrayList<Usuario>) ois.readObject();
            ois.close();
            return users;
        } catch (Exception e) {
            return users;
        }
    }

    /**
     * Método que carga el listado de propietarios en un archivo de configuración
     * @return
     */
    public static ArrayList<Propietario> cargarListadoPropietarios() {
        ArrayList<Propietario> propietarios = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(cargaProperties().getProperty("datapath.propietarios"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            propietarios = (ArrayList<Propietario>) ois.readObject();
            ois.close();
            return propietarios;
        } catch (Exception e) {
            return propietarios;
        }
    }

    /**
     * Método que carga el listado de administradores en un archivo de configuración
     * @return
     */
    public static ArrayList<Admin> cargarListadoAdministradores() {
        ArrayList<Admin> administradores = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(cargaProperties().getProperty("datapath.administradores"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            administradores = (ArrayList<Admin>) ois.readObject();
            ois.close();
            return administradores;
        } catch (Exception e) {
            return administradores;
        }
    }

    /**
     * Método que escribe los logs de los usuarios del sistema
     * @param mensaje
     * @param user
     */
    public static void whriteLog(String mensaje, Object user) {
        try {
            FileWriter fw = new FileWriter(cargaProperties().getProperty("datapath.log"), true);
            fw.write(mensaje + user + Herramientas.fechaHoraActualString() + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se encarga de guardar los logs de los usuarios del sistema
     * @param mensaje
     * @param user
     */
    public static void safeLog(String mensaje, Object user) {
        try {
            FileWriter fw = new FileWriter(cargaProperties().getProperty("datapath.log"), true);
            fw.write(mensaje + user + Herramientas.fechaHoraActualString() + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que se encarga de leer el properties de configuración
     * @throws IOException
     */
    public static void leeProperties() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("config/fernanbnb.properties");
        prop.load(fis);
        fis.close();
        for (String key : prop.stringPropertyNames()) {
            System.out.println(key + ": " + prop.getProperty(key));
        }
    }

    /**
     * Método que se encarga de leer los últimos usuarios del sistema
     * @throws IOException
     */
    public static void leeUltimosUsuarios() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("config/ultimoAcceso.properties");
        prop.load(fis);
        fis.close();
        for (String key : prop.stringPropertyNames()) {
            System.out.println(key + ": " + prop.getProperty(key));
        }
    }

    public static boolean primerInicio() {
        Properties props = cargaProperties();
        boolean primerInicio = Boolean.parseBoolean(props.getProperty("primerInicio", "true"));
        props.setProperty("primerInicio", "false");
        try {
            props.store(new FileWriter("config/fernanbnb.properties"), "Prueba");

        } catch (Exception e) {

            return primerInicio;
        }
        return primerInicio;
    }
    public static boolean guardaBackupUsers() {
        Path origin = Paths.get(Persistencia.leerProperties("datapath.users"));
        Path destino = Paths.get(Persistencia.leerProperties("backupusers.path"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean cargarBackupUsers() {
        Path origin = Paths.get(Persistencia.leerProperties("backupusers.path"));
        Path destino = Paths.get(Persistencia.leerProperties("datapath.users"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean cargarBackupReservas() {
        Path origin = Paths.get(Persistencia.leerProperties("backupreservas.path"));
        Path destino = Paths.get(Persistencia.leerProperties("datapath.reservas"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean cargarBackupViviendas() {
        Path origin = Paths.get(Persistencia.leerProperties("backupviviendas.path"));
        Path destino = Paths.get(Persistencia.leerProperties("datapath.vividendas"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean guardaBackupProp() {
        Path origin = Paths.get(Persistencia.leerProperties("datapath.propietarios"));
        Path destino = Paths.get(Persistencia.leerProperties("backupprop.path"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean cargarBackupProp() {
        Path origin = Paths.get(Persistencia.leerProperties("backupprop.path"));
        Path destino = Paths.get(Persistencia.leerProperties("datapath.propietarios"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean guardaBackupAdmin() {
        Path origin = Paths.get(Persistencia.leerProperties("datapath.administradores"));
        Path destino = Paths.get(Persistencia.leerProperties("backupadmin.path"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean cargarBackupAdmin() {
        Path origin = Paths.get(Persistencia.leerProperties("backupadmin.path"));
        Path destino = Paths.get(Persistencia.leerProperties("datapath.administradores"));
        try {
            Files.copy(origin, destino, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
