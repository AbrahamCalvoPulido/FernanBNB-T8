package dao;

import modules.Vivienda;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOViviendaSQL implements DAOVivienda {

    /**
     * Método para insertar una vivienda en la base de datos
     * @param vivienda
     * @param daoManager
     * @param idPropietario
     * @return
     */
    @Override
    public boolean insertarVivienda(Vivienda vivienda, DAOManager daoManager,int idPropietario) {
        String sentencia = String.format("INSERT INTO Vivienda VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s','%s');",
                vivienda.getId(),
                vivienda.getTitulo(),
                vivienda.getDescripcion(),
                vivienda.getLocalidad(),
                vivienda.getProvincia(),
                vivienda.getMaxOcupantes(),
                vivienda.getPrecioNoche(),
                idPropietario);
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para eliminar una vivienda en la base de datos
     * @param vivienda
     * @param daoManager
     * @return
     */
    @Override
    public boolean deleteVivienda(Vivienda vivienda, DAOManager daoManager) {
        String sentencia = String.format("DELETE FROM Vivienda WHERE id = '%s';", vivienda.getId());
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para eliminar todas las viviendas en la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public boolean deleteAll(DAOManager daoManager) {
        String sentencia = String.format("DELETE FROM Vivienda;");
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para leer todas las viviendas en la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Vivienda> readAll(DAOManager daoManager) {
        ArrayList<Vivienda> viviendas = new ArrayList<>();
        String sentencia = String.format("SELECT * FROM Vivienda;");
        try (Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {

            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Vivienda vivienda = new Vivienda(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("localidad"),
                        rs.getString("provincia"),
                        rs.getInt("maxOcupantes"),
                        rs.getDouble("precioNoches"));
                viviendas.add(vivienda);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return viviendas;
    }

    /**
     * Método para leer una vivienda mediante la ciudad en la base de datos
     * @param ciudad
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Vivienda> readViviendaCiudad(String ciudad, DAOManager daoManager) {
        ArrayList<Vivienda> viviendas = new ArrayList<>();
        String sentencia = "SELECT * FROM Vivienda WHERE localidad LIKE '%" + ciudad + "%';";
        try (Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {

            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Vivienda vivienda = new Vivienda(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("localidad"),
                        rs.getString("provincia"),
                        rs.getInt("maxOcupantes"),
                        rs.getDouble("precioNoches"));
                viviendas.add(vivienda);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return viviendas;
    }

    /**
     * Metodo para leer una vivienda mediante el id del propietario en la base de datos
     * @param idPropietario
     * @param daoManager
     * @return
     */
    public ArrayList<Vivienda> readViviendabyIDPropietario(int idPropietario, DAOManager daoManager) {
        ArrayList<Vivienda> viviendas = new ArrayList<>();
        String sentencia = String.format("SELECT * FROM Vivienda WHERE idPropietario=%d;", idPropietario);
        try (Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {

            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Vivienda vivienda = new Vivienda(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("localidad"),
                        rs.getString("provincia"),
                        rs.getInt("maxOcupantes"),
                        rs.getDouble("precioNoches"));
                viviendas.add(vivienda);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return viviendas;
    }

    /**
     * Método para leer una vivienda mediante la provincia en la base de datos
     * @param provincia
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Vivienda> readViviendaProvincia(String provincia, DAOManager daoManager) {

        ArrayList<Vivienda> viviendas = new ArrayList<>();
        String sentencia = "SELECT * FROM Vivienda WHERE provincia LIKE '%" + provincia + "%';";
        try (Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {

            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Vivienda vivienda = new Vivienda(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("localidad"),
                        rs.getString("provincia"),
                        rs.getInt("maxOcupantes"),
                        rs.getDouble("precioNoches"));
                viviendas.add(vivienda);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return viviendas;
    }
}
