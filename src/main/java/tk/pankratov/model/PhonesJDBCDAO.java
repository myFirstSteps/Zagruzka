/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.pankratov.model;

import java.sql.*;
import javax.sql.*;
import java.text.*;

/**
 *
 * @author pankratov
 */
public class PhonesJDBCDAO {

    final String DB_URL;
    final String LOGIN;
    final String PASSWORD;

    public PhonesJDBCDAO(String db_URL, String login, String passwd) {
        DB_URL = db_URL;
        LOGIN = login;
        PASSWORD = passwd;
    }

    public boolean savePhones(Phones phones, String mainTable, String phonesTable) {
        String firstStatement = String.format("INSERT INTO %s  values (?,?,?,?,?)", mainTable);
        String secondStatement = String.format("INSERT INTO %s  values (?,?,?)", phonesTable);
        boolean result = false;
        try (Connection con = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);) {
            try {
                con.setAutoCommit(false);
                PreparedStatement mainTableStatement = con.prepareStatement(firstStatement, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement phonesTableStatement = con.prepareStatement(secondStatement, Statement.RETURN_GENERATED_KEYS);
                mainTableStatement.setNull(1, Types.NULL);
                mainTableStatement.setString(2, phones.getIp());
                mainTableStatement.setString(3, phones.getStatus());
                mainTableStatement.setString(4, phones.getDescription());
                mainTableStatement.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(phones.getTime())));
                mainTableStatement.execute();
                ResultSet genKeys = mainTableStatement.getGeneratedKeys();
                genKeys.next();
                phonesTableStatement.setInt(3, genKeys.getInt(1));
                phonesTableStatement.setNull(1, Types.NULL);
                for (long number : phones.getPhoneNumbers()) {
                    phonesTableStatement.setLong(2, number);
                    phonesTableStatement.execute();
                }
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
            } finally {
                con.setAutoCommit(true);
            };
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
