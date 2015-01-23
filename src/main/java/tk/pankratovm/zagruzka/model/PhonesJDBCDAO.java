package tk.pankratovm.zagruzka.model;

import java.sql.*;
import java.text.*;
/*DAO для сохранения результата отправки в DB.
Правилно было бы создать общий интерфейс DAO, его реализацию (эту)
и фабрику DAO объектов, но в рамках данного задания это не целесообразно и 
усложнит код.
*/
public class PhonesJDBCDAO {
    //Данные для подключения к DB
    final String DB_URL;
    final String LOGIN;
    final String PASSWORD;
    final String MAIN_TABLE; //Основная таблица
    final String PHONES_TABLE; /* Таблица с номерами. Для нормализации,
    необходимо вынести номера телефонов в отдельную таблицу. 
    */

    public PhonesJDBCDAO(String db_URL, String login, String passwd, String mainTable, String phonesTable) {
        DB_URL = db_URL;
        LOGIN = login;
        PASSWORD = passwd;
        MAIN_TABLE = mainTable;
        PHONES_TABLE = phonesTable;
    }

    public boolean savePhones(Phones phones) {
        final String firstStatement = String.format("INSERT INTO %s  values (?,?,?,?,?)", MAIN_TABLE);
        final String secondStatement = String.format("INSERT INTO %s  values (?,?,?)", PHONES_TABLE);
        boolean result = false;
        try (Connection con = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);) {
            try {
                con.setAutoCommit(false);
                PreparedStatement mainTableStatement = con.prepareStatement(firstStatement, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement phonesTableStatement = con.prepareStatement(secondStatement, Statement.RETURN_GENERATED_KEYS);
                mainTableStatement.setNull(1, Types.NULL);
                mainTableStatement.setString(2, phones.getIp());
                mainTableStatement.setString(3, phones.getAnswer().getStatus().equals("")
                        ? String.valueOf(phones.getAnswer().getHttpCode()) : phones.getAnswer().getStatus());
                mainTableStatement.setString(4, phones.getAnswer().getDescription());
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
                //Если случилась ошибка, то откатываем.
                con.rollback();
            } finally {
                con.setAutoCommit(true);
            };
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    //Здесь могли бы быть заглушки для остальных методов "CRUD", но для компактности кода я их опускаю. 

}
