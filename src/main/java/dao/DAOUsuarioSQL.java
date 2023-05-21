package dao;

import modules.Usuario;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOUsuarioSQL implements DAOUsuario {


    /**
     * Método para insertar un usuario en la base de datos
     * @param usuario
     * @param daoManager
     * @return
     */
    @Override
    public boolean insertarUsuario(Usuario usuario, DAOManager daoManager) {
        String sentencia = String.format("INSERT INTO Usuario VALUES ('%s', '%s', '%s', '%s', '%s');",
                usuario.getId(),
                usuario.getNombre(),
                usuario.getClave(),
                usuario.getEmail(),
                usuario.isVerificado()? 1 : 0);
        try(Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para buscar un usuario mediante de su email
     * @param email
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Usuario> buscarUsuariobyEmail(String email, DAOManager daoManager) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sentencia = String.format("SELECT * FROM Usuario WHERE email = '%s';", email);
        try(Statement stmt = daoManager.getConn().prepareStatement(sentencia)) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Usuario usuario = new Usuario(
               rs.getInt("id"),
               rs.getString("nombre"),
               rs.getString("clave"),
               rs.getString("email"),
               rs.getBoolean("verificado"));
                usuarios.add(usuario);
            }
            return usuarios;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método para actualizar un usuario y modificarlo en la base de datos
     * @param usuario
     * @param daoManager
     * @return
     */
    @Override
    public boolean updateUsuario(Usuario usuario, DAOManager daoManager) {
        String sentencia = String.format("UPDATE Usuario SET nombre = '%s', clave = '%s', email = '%s',verificado = '%s' WHERE id = %d;",
            usuario.getNombre(),
            usuario.getClave(),
            usuario.getEmail(),
            usuario.isVerificado()? 1 : 0,
            usuario.getId());
        try(Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean delete(Usuario usuario, DAOManager daoManager) {
        return false;
    }


    /**
     * Método para eliminar todos los usuarios de la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public boolean deleteAll(DAOManager daoManager) {
        String sentencia = String.format("DELETE FROM Usuario;");
        try(Statement stmt = daoManager.getConn().createStatement()) {
            stmt.executeUpdate(sentencia);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para leer todos los usuarios de la base de datos
     * @param daoManager
     * @return
     */
    @Override
    public ArrayList<Usuario> readAll(DAOManager daoManager) {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sentencia = String.format("SELECT * FROM Usuario;");
        try(Statement stmt = daoManager.getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery(sentencia);
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("clave"),
                        rs.getString("email"),
                        rs.getBoolean("verificado"));
                usuarios.add(usuario);
            }
            return usuarios;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
