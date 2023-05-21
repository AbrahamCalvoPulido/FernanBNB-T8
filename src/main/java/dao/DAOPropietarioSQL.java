package dao;

import modules.Propietario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOPropietarioSQL implements DAOPropietario {

    /**
     * Método para insertar un nuevo propietario en la base de datos
     * @param propietario
     * @param daoManager
     * @return
     */
    @Override
    public boolean insertarPropietario(Propietario propietario, DAOManager daoManager) {
        String sentencia = String.format("INSERT INTO Propietario VALUES (%d, '%s', '%s', '%s', '%s')",
                propietario.getId(),
                propietario.getNombre(),
                propietario.getClave(),
                propietario.getEmail(),
                propietario.isVerificado()? 1 : 0);
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método que busca un propietario por id en la base de datos
     * @param id
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Propietario> buscarPropietariosbyID(int id, DAOManager daoManager) {
        ArrayList<Propietario> propietarios = new ArrayList<>();
        String sentencia = String.format("SELECT * FROM Propietario WHERE id = %d", id);
        try (Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                propietarios.add(new Propietario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("clave"),
                        rs.getString("email"),
                        rs.getBoolean("verificado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Método que actualiza y modifica un propietario en la base de datos
     * @param propietario
     * @param daoManager
     * @return
     */
    @Override
    public boolean updatePropietario(Propietario propietario, DAOManager daoManager) {
        String sentencia = String.format("UPDATE Propietario SET nombre = '%s', clave = '%s', email = '%s' WHERE id = %d",
                propietario.getNombre(),
                propietario.getClave(),
                propietario.getEmail(),
                propietario.getId());
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Método que elimina todos los propietarios de la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public boolean deleteALL(DAOManager daoManager) {
        String sentencia = "DELETE FROM Propietario";
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Método que lee todos los propietarios de la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Propietario> readAll(DAOManager daoManager) {
        ArrayList<Propietario> propietarios = new ArrayList<>();
        String sentencia = "SELECT * FROM Propietario";
        try (Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                propietarios.add(new Propietario(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("clave"),
                        rs.getString("email"),
                        rs.getBoolean("verificado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return propietarios;

    }




}
