package dao;

import modules.Reserva;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DAOReservaSQL implements DAOReserva {

    /**
     * Método para insertar una reserva en la base de datos
     * @param reserva
     * @param daoManager
     * @return
     */
    @Override
    public boolean insertarReserva(Reserva reserva, DAOManager daoManager) {
        String sentencia=String.format("INSERT INTO Reserva VALUES(%d, %d, %d, '%s', '%s', '%s', '%s');",
                reserva.getIdReserva(),
                reserva.getIdVivienda(),
                reserva.getIdUsuario(),
                reserva.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                reserva.getNoches(),
                reserva.getPrecio(),
                reserva.getOcupantes());
        try(Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para buscar una reserva con id en la base de datos
     * @param id
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Reserva> buscarReservabyID(int id, DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sentencia=String.format("SELECT * FROM Reserva WHERE idReserva=%d;", id);
        try(Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("idReserva"),
                        rs.getInt("idVivienda"),
                        rs.getInt("idUsuario"),
                        LocalDate.parse(rs.getString("fechaInicio"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getInt("noches"),
                        rs.getDouble("precio"),
                        rs.getInt("ocupantes"));
                reservas.add(reserva);
            }
            return reservas;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        }

    /**
     * Método para buscar una reserva a través de una id de vivienda
     * @param id
     * @param daoManager
     * @return
     */
    public ArrayList<Reserva> buscarReservabyIDVivienda(int id, DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sentencia=String.format("SELECT * FROM Reserva WHERE idVivienda=%d;", id);
        try(Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("idReserva"),
                        rs.getInt("idVivienda"),
                        rs.getInt("idUsuario"),
                        LocalDate.parse(rs.getString("fechaInicio"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getInt("noches"),
                        rs.getDouble("precio"),
                        rs.getInt("ocupantes"));
                reservas.add(reserva);
            }
            return reservas;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Método para buscar una reserva a través de un usuario
     * @param id
     * @param daoManager
     * @return
     */
    public ArrayList<Reserva> buscarReservabyIDUsuario(int id, DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sentencia=String.format("SELECT * FROM Reserva WHERE idUsuario=%d;", id);
        try(Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("idReserva"),
                        rs.getInt("idVivienda"),
                        rs.getInt("idusuario"),
                        LocalDate.parse(rs.getString("fechaInicio"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getInt("noches"),
                        rs.getDouble("precio"),
                        rs.getInt("ocupantes"));
                reservas.add(reserva);
            }
            return reservas;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean updateReserva(Reserva reserva, DAOManager daoManager) {

        return false;
    }

    /**
     * Método para eliminar una reserva en la base de datos
     * @param reserva
     * @param daoManager
     * @return
     */
    @Override
    public boolean delete(Reserva reserva, DAOManager daoManager) {
        String sentencia=String.format("DELETE FROM Reserva WHERE idReserva=%d;", reserva.getIdReserva());
        try(Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para eliminar todas las reservas en la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public boolean deleteAll(DAOManager daoManager) {
        String sentencia=String.format("DELETE FROM Reserva;");
        try(Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para leer todas las reservas en la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Reserva> readAll(DAOManager daoManager) {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String sentencia=String.format("SELECT * FROM Reserva;");
        try(Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("idReserva"),
                        rs.getInt("idVivienda"),
                        rs.getInt("idUsuario"),
                        LocalDate.parse(rs.getString("fechaInicio"), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getInt("noches"),
                        rs.getDouble("precio"),
                        rs.getInt("ocupantes"));
                reservas.add(reserva);
            }
            return reservas;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
