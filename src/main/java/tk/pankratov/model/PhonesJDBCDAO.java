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
public class PhonesJDBCDAO  {
    final String DB_URL;
    final String LOGIN;
    final String PASSWORD;
    public PhonesJDBCDAO(String db_URL, String login, String passwd){
        DB_URL=db_URL;
        LOGIN=login;
        PASSWORD=passwd;
    }
    
    public boolean savePhones(Phones phones, String mainTable,String phonesTable) {
        String firstStatement=String.format("INSERT INTO %s values (?,?,?,?)",mainTable);
        boolean result=false;
        try{
            Connection con=DriverManager.getConnection(DB_URL,LOGIN,PASSWORD);
            con.setAutoCommit(false);
            PreparedStatement mainTableStatement=con.prepareStatement(firstStatement);
            mainTableStatement.setString(1, phones.getIp());
            mainTableStatement.setString(2, phones.getStatus());
            mainTableStatement.setString(3, phones.getDescription());
            mainTableStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(phones.getTime())));
            mainTableStatement.execute();
            mainTableStatement.getGeneratedKeys();
        }catch (SQLException ex){ex.printStackTrace();};
        return result;
    }
    
}
