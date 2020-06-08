package LMS;


import java.sql.Connection;


/**
 * 程序的入口
 * **/

public class Main {
    // 存放用户名
    public static String userId;
    // 存放用户身份
    public static String identity;
    // 存放数据库连接
    public static Connection con;

    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
        frame.initMysql();
        frame.setVisible(true);
    }
}
