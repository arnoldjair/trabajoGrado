/*
 * Copyright (C) 2016 Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package co.edu.unicauca.trabajogradogkr.db;

import co.edu.unicauca.trabajogradogkr.exception.DBException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Arnold Jair Jimenez Vargas <ajjimenez@unicauca.edu.co>
 */
public class MysqlConnection implements DBConnection {

    private String host;
    private String dbName;
    private String username;
    private String passwd;
    private Connection con;

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public synchronized void con(String host, String dbName, String username, String passwd) throws DBException {
        //Inicializar el driver
        try {

            //Class.forName("com.mysql.jdbc.Driver");
            StringBuilder sb = new StringBuilder();
            sb.append("jdbc:mysql://").append(host).append("/").append(dbName);
            this.con = DriverManager.getConnection(sb.toString(), username, passwd);

        } catch (SQLException e) {
            throw new DBException();
        }
    }

    @Override
    public synchronized Connection getCon() {
        return this.con;
    }

}
