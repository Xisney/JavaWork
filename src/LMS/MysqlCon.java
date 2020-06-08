package LMS;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {
    private final String userName="root";
    private final String passWord="253977";


    public Connection getCon(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("数据库驱动加载成功");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/work?serverTimezone=CST",
                    userName,passWord);
            System.out.println("数据库链接成功");
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
