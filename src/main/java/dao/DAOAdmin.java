package dao;

import modules.Admin;

import java.util.ArrayList;

public interface DAOAdmin {
    public boolean insertarAdmin(Admin admin ,DAOManager daoManager);

    public Admin buscarAdminbyId(int id, DAOManager daoManager);

    public boolean updateAdmin(Admin admin, DAOManager daoManager);
    public boolean deleteALL(DAOManager daoManager);
    public ArrayList<Admin> readAllAdmins(DAOManager daoManager);
}
