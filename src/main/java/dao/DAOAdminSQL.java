package dao;

import modules.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOAdminSQL implements DAOAdmin {


    /**
     *  Este metodo se encarga de insertar un nuevo administrador en la base de datos
     *
     * @param admin
     * @param daoManager
     * @return
     */
    @Override
    public boolean insertarAdmin(Admin admin, DAOManager daoManager) {
        String sentencia = String.format("INSERT INTO Admin VALUES (%d, '%s', '%s', '%s', '%s');",
                admin.getId(),
                admin.getNombre(),
                admin.getClave(),
                admin.getEmail(),
                admin.isVerificado()? 1 : 0);

        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     *  Este metodo se encarga de buscar un administrador en la base de datos atravez de su id
     * @param id
     * @param daoManager
     * @return
     */
    @Override
    public Admin buscarAdminbyId(int id, DAOManager daoManager) {
        String sentencia = String.format("SELECT * FROM Admin WHERE id = %d ;", id);
        try (Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("clave"),
                        rs.getString("email"));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     *Este metodo se encarga de actualizar y modificar un administrador en la base de datos
     *
     * @param admin
     * @param daoManager
     * @return
     */
    @Override
    public boolean updateAdmin(Admin admin, DAOManager daoManager) {
        String sentencia = String.format("UPDATE Admin SET nombre = '%s', clave = '%s', email = '%s' WHERE id = %d ;",
                admin.getNombre(),
                admin.getClave(),
                admin.getEmail(),
                admin.getId());
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Este metodo se encarga de eliminar todos los administradores de la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public boolean deleteALL(DAOManager daoManager) {
        String sentencia = "DELETE FROM Admin;";
        try (Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Método que lee todos los Admins del sistema
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Admin> readAllAdmins(DAOManager daoManager) {
        ArrayList<Admin> admins = new ArrayList<>();
        Admin admin;
        String sentencia;
        sentencia = "SELECT * FROM Admin";
        try {
            PreparedStatement ps = daoManager.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    admin = new Admin(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("clave"),
                            rs.getString("email"));
                    admins.add(admin);
                }
                ps.close();
            }
        } catch (Exception e) {
            return null;
        }
       return admins;
    }


}