/*
 * Created by JFormDesigner on Fri May 29 16:01:16 GMT+08:00 2020
 */

package LMS;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * @author Xie yusheng
 */
public class MainFrame extends JFrame {
    public MainFrame() {
        initComponents();
        addItemToComboBox();
        initWelcomeLabel();
        initTableHeader();
        initBackground();
    }

    // 自定义背景图
    private void initBackground() {
        ImageIcon img = new ImageIcon("src/images/mainBG4.jpg");
        JLabel pic = new JLabel(img);
        getLayeredPane().add(pic, new Integer(Integer.MIN_VALUE));

        pic.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        JPanel contentPane = (JPanel) getContentPane();

        // 设置其余窗格为透明
        contentPane.setOpaque(false);
        panel1.setOpaque(false);
        tabbedPane1.setOpaque(false);
        panel2.setOpaque(false);
        panel3.setOpaque(false);
        panel4.setOpaque(false);
        panel5.setOpaque(false);
        panel6.setOpaque(false);
        panel7.setOpaque(false);
        panel9.setOpaque(false);
        panel14.setOpaque(false);
        panel8.setOpaque(false);
        panel10.setOpaque(false);
        panel11.setOpaque(false);
        panel12.setOpaque(false);
        panel13.setOpaque(false);
        panel15.setOpaque(false);
        panel16.setOpaque(false);
        panel17.setOpaque(false);
        panel18.setOpaque(false);
        panel19.setOpaque(false);
        panel20.setOpaque(false);
        panel21.setOpaque(false);
        panel22.setOpaque(false);
        scrollPane1.setOpaque(false);
        scrollPane2.setOpaque(false);
        scrollPane3.setOpaque(false);
        scrollPane4.setOpaque(false);
        scrollPane1.getViewport().setOpaque(false);
        scrollPane2.getViewport().setOpaque(false);
        scrollPane3.getViewport().setOpaque(false);
        scrollPane4.getViewport().setOpaque(false);
        table1.setOpaque(false);
        table2.setOpaque(false);
        table3.setOpaque(false);
        table4.setOpaque(false);
        textField1.setOpaque(false);

        ImageIcon img1 = new ImageIcon("src/images/signin.jpg");
        JLabel pic1 = new JLabel(img1);
        dialog1.getLayeredPane().add(pic1,new Integer(Integer.MIN_VALUE));
        pic1.setBounds(0, 0, img1.getIconWidth(), img1.getIconHeight());
        JPanel contentPane1 = (JPanel) dialog1.getContentPane();

        contentPane1.setOpaque(false);

        ImageIcon img2 = new ImageIcon("src/images/signin.jpg");
        JLabel pic2 = new JLabel(img2);
        dialog1.getLayeredPane().add(pic2,new Integer(Integer.MIN_VALUE));
        pic1.setBounds(0, 0, img2.getIconWidth(), img2.getIconHeight());
        JPanel contentPane2 = (JPanel) dialog2.getContentPane();

        contentPane2.setOpaque(false);


    }

    // 为复选框添加内容
    private void addItemToComboBox() {
        String[] category = {"文学", "社科", "历史", "技术", "教育", "科幻"};
        for (String s : category) {
            comboBox1.addItem(s);
            comboBox3.addItem(s);
        }

        String[] searchItems = {"编号", "书名", "作者", "出版社", "类别", "状态"};
        for (String item : searchItems) {
            comboBox2.addItem(item);
        }
    }

    // 初始化欢迎label
    private void initWelcomeLabel() {
        label1.setText("欢迎您 " + Main.userId);
    }

    // 初始化表格大小
    private void initTableHeader(){
        table1.getTableHeader().setFont(new Font("Serif",Font.PLAIN,24));
        table2.getTableHeader().setFont(new Font("Serif",Font.PLAIN,24));
        table3.getTableHeader().setFont(new Font("Serif",Font.PLAIN,24));
        table4.getTableHeader().setFont(new Font("Serif",Font.PLAIN,24));
    }

    // 监听选项卡的变化,用于选项卡上的所有组件
    private void tabbedPane1StateChanged(ChangeEvent e) {
        // TODO add your code here
        int selected = tabbedPane1.getSelectedIndex();
        if (selected == 0) {
            // 读取数据库中的数据并显示
            String command = "SELECT * FROM books";

            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTable(set, table1);

            } catch (SQLException e1) {
                System.out.println("tabbedPane1StateChanged " + e1.getMessage());
            }
        } else if (selected == 1) {
            // 显示借阅图书的历史记录
            String command = "SELECT books.bookid,books.bookname,books.author,bookborrow.borrowtime," +
                    "bookborrow.state,bookborrow.backtime" +
                    " FROM bookborrow,books" +
                    " WHERE bookborrow.userid LIKE ? AND bookborrow.bookid=books.bookid";

            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, Main.userId);
                ResultSet set = statement.executeQuery();
                if (!set.next()) {
                    label8.setText("您暂时没有历史记录qaq");
                    return;
                }
                BothEvent.refreshTable2(set, table2);
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } else if (selected == 2) {
            // null
        } else if (selected == 3) {

            String command;
            final String logTime;
            if (Main.identity.equals("用户"))
                command = "SELECT logtime FROM users";
            else
                command = "SELECT logtime FROM superusers";
            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                if (set.next()) {
                    logTime = BothEvent.converse(set.getString("logtime"));
                    label12.setText(logTime);
                }
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }

            // 统计借阅记录
            command = "SELECT * FROM bookborrow WHERE userid LIKE ?";
            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, Main.userId);
                ResultSet set = statement.executeQuery();
                set.last();
                label13.setText("你总共借阅了" + set.getRow() + "本图书");
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } else if (selected == 4) {
            // 显示用户信息，并显示是否被冻结
            String command = "SELECT id,logtime,state FROM users";

            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTableManager(set, table4);
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }


        } else if (selected == 5) {
            // 从wishbooks中读取数据并显示
            String command = "SELECT * FROM wishbooks";

            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTableWish(set, table3);

            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }


    // 提交数据入库
    private void submitActionPerformed(ActionEvent e) {
        // TODO add your code here
        String bookName = textField1.getText();
        String author = textField2.getText();
        String press = textField3.getText();
        final String state = "在馆";
        String category = comboBox1.getItemAt(comboBox1.getSelectedIndex());

        if (bookName.equals("")){
            JOptionPane.showMessageDialog(this,"请输入书名");
            return;
        }
        else if (author.equals("")){
            JOptionPane.showMessageDialog(this,"请输入作者名");
            return;
        }
        else if (press.equals("")){
            JOptionPane.showMessageDialog(this,"请输入出版社名");
            return;
        }

        // 一次性添加的书本数量
        try{
            int amount = Integer.parseInt(textField4.getText());
        }catch (NumberFormatException e1){
            JOptionPane.showMessageDialog(this,"请输入合法的数量");
            return;
        }

        int amount = Integer.parseInt(textField4.getText());

        // 以秒级别的时间戳作为id
        String id = String.valueOf(Instant.now().getEpochSecond());

        String command = "INSERT INTO books VALUES(?,?,?,?,?,?)";

        if (amount <= 0 || amount >= 100) {
            JOptionPane.showMessageDialog(this, "一次添加的数量不能小于0或者大于99", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 记录插入失败的数量
        int failNum = 0;
        int totalNum = amount;
        // 插入至数据库
        while (amount > 0) {
            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                id = String.valueOf(Long.parseLong(id) + 1);
                statement.setString(1, id);
                statement.setString(2, bookName);
                statement.setString(3, author);
                statement.setString(4, press);
                statement.setString(5, category);
                statement.setString(6, state);
                statement.executeUpdate();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
                failNum++;
            }
            amount--;
        }

        // 显示成功入库的图书的数量
        if (failNum == 0) {
            JOptionPane.showMessageDialog(this, "成功入库" + totalNum + "本图书");

        } else {
            JOptionPane.showMessageDialog(this, "成功入库" + (totalNum - failNum) + "本图书，" + "失败" + failNum + "本图书");
        }
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    // 图书查询页面进行搜索
    private void button2SearchActionPerformed(ActionEvent e) {
        // TODO add your code here
        // 建立table列名到数据库映射
        HashMap<String, String> toColumn = new HashMap<>();
        toColumn.put("编号", "SELECT * FROM books WHERE bookid LIKE ?");
        toColumn.put("书名", "SELECT * FROM books WHERE bookname LIKE ?");
        toColumn.put("作者", "SELECT * FROM books WHERE author LIKE ?");
        toColumn.put("出版社", "SELECT * FROM books WHERE press LIKE ?");
        toColumn.put("类别", "SELECT * FROM books WHERE category LIKE ?");
        toColumn.put("状态", "SELECT * FROM books WHERE state LIKE ?");

        // 详细搜索的目标
        String target = textField5.getText();

        // 输入内容为空则显示所有内容
        // 链接数据库进行搜索并展示
        if (target.equals("")) {
            String command = "SELECT * FROM books";
            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTable(set, table1);
            } catch (SQLException e1) {
                System.out.println("button2SearchActionPerformed " + e1.getMessage());
            }

        } else {
            String command = toColumn.get(comboBox2.getItemAt(comboBox2.getSelectedIndex()));

            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, target);
                ResultSet set = statement.executeQuery();
                BothEvent.refreshTable(set, table1);
            } catch (SQLException e1) {
                System.out.println("button2SearchActionPerformed " + e1.getMessage());
            }
        }

    }

    // 监听借阅图书事件
    private void button5BorrowActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table1.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "未选中任何图书");
        } else {
            String[] selectedBookId = new String[selectedRows.length];
            String beforeState;
            for (int i = 0; i < selectedRows.length; i++) {
                // 所选图书包含已借出的图书则退出
                beforeState = (String) table1.getValueAt(selectedRows[i], 5);
                if (beforeState.equals("借出")) {
                    JOptionPane.showMessageDialog(this, "所选图书包含已借出的图书", "注意", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                selectedBookId[i] = (String) table1.getValueAt(selectedRows[i], 0);
            }
            // 统计成功借阅的数量
            int count = 0;
            // 写入数据库
            String command = "INSERT INTO bookborrow VALUES(?,?,?,?,?)";
            String borrowTime = BothEvent.converse(String.valueOf(Instant.now().getEpochSecond()));
            System.out.println(borrowTime);
            // 更改状态为借出
            final String state = "借出";
            final String afterState = "在借";
            String alterState = "UPDATE books SET state=? WHERE bookid LIKE ?";
            try {
                // 更新bookborrow表
                PreparedStatement statement = Main.con.prepareStatement(command);
                // 更新books表
                PreparedStatement statement1 = Main.con.prepareStatement(alterState);
                for (String bookId : selectedBookId) {
                    // 写入bookborrow表
                    statement.setString(1, bookId);
                    statement.setString(2, Main.userId);
                    statement.setString(3, borrowTime);
                    statement.setString(4, afterState);
                    statement.setString(5, " ");
                    count += statement.executeUpdate();

                    // 写入books表
                    statement1.setString(1, state);
                    statement1.setString(2, bookId);
                    statement1.executeUpdate();
                }
                // 刷新table1
                command = "SELECT * FROM books";
                try {
                    ResultSet set = Main.con.createStatement().executeQuery(command);
                    BothEvent.refreshTable(set, table1);
                } catch (SQLException e1) {
                    System.out.println("tabbedPane1StateChanged " + e1.getMessage());
                }

                if (count == selectedRows.length) JOptionPane.showMessageDialog(this, "成功借阅" + count + "本图书");
                else
                    JOptionPane.showMessageDialog(this, "成功借阅" + count + "本图书," + "有" + (selectedRows.length - count) + "本图书落入虚空");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "啊偶，服务器开小差了");
                System.out.println("button5BorrowActionPerformed" + e1.getMessage());
            }
        }
    }

    // 监听归还事件
    private void button7BackActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table2.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "未选中行");
        } else {
            String[] selectedBookId = new String[selectedRows.length];
            String beforeState;
            for (int i = 0; i < selectedRows.length; i++) {
                beforeState = (String) table2.getValueAt(selectedRows[i], 4);
                if (beforeState.equals("已还")) {
                    JOptionPane.showMessageDialog(this, "所选项包含已归还书目", "注意", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                selectedBookId[i] = (String) table2.getValueAt(selectedRows[i], 0);
            }

            // 归还图书，并更新，books，bookborrow表
            String command = "UPDATE bookborrow SET state=?,backtime=? WHERE bookid LIKE ?";
            String command1 = "UPDATE books SET state=? WHERE bookid LIKE ?";

            final String state = "已还";
            final String state1 = "在馆";
            int count = 0;
            String backTime = BothEvent.converse(String.valueOf(Instant.now().getEpochSecond()));
            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                PreparedStatement statement1 = Main.con.prepareStatement(command1);
                for (String bookId : selectedBookId) {
                    statement.setString(1, state);
                    statement.setString(2, backTime);
                    statement.setString(3, bookId);
                    count += statement.executeUpdate();

                    statement1.setString(1, state1);
                    statement1.setString(2, bookId);
                    statement1.executeUpdate();
                }

                if (count == selectedRows.length) JOptionPane.showMessageDialog(this, "成功归还" + count + "本图书");
                else
                    JOptionPane.showMessageDialog(this, "成功归还" + count + "本图书," + "有" + (selectedRows.length - count) + "本图书落入虚空");
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }

            // 更新table2
            command = "SELECT books.bookid,books.bookname,books.author,bookborrow.borrowtime," +
                    "bookborrow.state,bookborrow.backtime" +
                    " FROM bookborrow,books" +
                    " WHERE bookborrow.userid LIKE ? AND bookborrow.bookid=books.bookid";

            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, Main.userId);
                ResultSet set = statement.executeQuery();
                if (!set.next()) {
                    label8.setText("您暂时没有历史记录qaq");
                    return;
                }
                BothEvent.refreshTable2(set, table2);


            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }


        }
    }

    // 监听更改密码事件，显示改密对话框
    private void button3ChangePswActionPerformed(ActionEvent e) {
        // TODO add your code here
        dialog1.setVisible(true);
    }

    // 监听修改密码事件，确认修改密码
    private void button6ConfirmPswActionPerformed(ActionEvent e) {
        // TODO add your code here
        final String oldPsw = new String(passwordField3.getPassword());
        final String newPsw = new String(passwordField1.getPassword());
        final String confirmPsw = new String(passwordField2.getPassword());
        String command;

        if (oldPsw.equals(""))
            JOptionPane.showMessageDialog(dialog1, "请输入原密码", "注意", JOptionPane.WARNING_MESSAGE);
        else {
            // 检查输入的原密码是否正确
            if (Main.identity.equals("用户"))
                command = "SELECT psw FROM users WHERE id LIKE ?";
            else
                command = "SELECT psw FROM superusers WHERE id LIKE ?";

            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, Main.userId);
                ResultSet set = statement.executeQuery();
                if (set.next()) {
                    if (!set.getString("psw").equals(oldPsw)) {
                        JOptionPane.showMessageDialog(dialog1, "输入的原密码有误");
                        return;
                    }
                }
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        }


        if (newPsw.equals(""))
            JOptionPane.showMessageDialog(dialog1, "请输入新密码", "注意", JOptionPane.WARNING_MESSAGE);
        else if (confirmPsw.equals(""))
            JOptionPane.showMessageDialog(dialog1, "请确认密码", "注意", JOptionPane.WARNING_MESSAGE);
        else if (!confirmPsw.equals(newPsw))
            JOptionPane.showMessageDialog(dialog1, "修改后两次密码不一致", "注意", JOptionPane.WARNING_MESSAGE);
        else {
            // 确认完成
            // 写入数据库，并提示
            if (Main.identity.equals("用户"))
                command = "UPDATE users SET psw=? WHERE id LIKE ?";
            else
                command = "UPDATE superusers SET psw=? WHERE id LIKE ?";

            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, newPsw);
                statement.setString(2, Main.userId);
                if (statement.executeUpdate() == 1)
                    JOptionPane.showMessageDialog(dialog1, "修改完成，下次登录记得输入新密码噢");
                else
                    JOptionPane.showMessageDialog(dialog1, "服务器不在线噢");
                passwordField1.setText("");
                passwordField2.setText("");
                passwordField3.setText("");
                dialog1.dispose();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    // 监听管理员购书事件
    private void button8BuyBooksActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table3.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "请选择已采购的书目");
        } else {
            dialog2.setVisible(true);
        }
    }

    // 监听用户想看的提交事件
    private void button9WishBookActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table3.getSelectedRows();

        String[] userId = new String[selectedRows.length];
        String[] bookName = new String[selectedRows.length];
        String[] author = new String[selectedRows.length];
        String[] press = new String[selectedRows.length];

        int amount = Integer.parseInt(textField6.getText());
        String category = comboBox3.getItemAt(comboBox3.getSelectedIndex());
        final String state = "在馆";
        String id = String.valueOf(Instant.now().getEpochSecond());

        if (amount <= 0 || amount >= 100) {
            JOptionPane.showMessageDialog(dialog2, "数量应该在1-99之间");
            return;
        }

        for (int i = 0; i < selectedRows.length; i++) {
            userId[i] = (String) table3.getValueAt(selectedRows[i], 0);
            bookName[i] = (String) table3.getValueAt(selectedRows[i], 1);
            author[i] = (String) table3.getValueAt(selectedRows[i], 2);
            press[i] = (String) table3.getValueAt(selectedRows[i], 3);
        }

        String command = "DELETE FROM wishbooks WHERE userid LIKE ?" +
                " AND bookname LIKE ? AND author LIKE ? AND press LIKE ?";

        try {
            PreparedStatement statement = Main.con.prepareStatement(command);

            int count = 0;
            for (int i = 0; i < selectedRows.length; i++) {
                statement.setString(1, userId[i]);
                statement.setString(2, bookName[i]);
                statement.setString(3, author[i]);
                statement.setString(4, press[i]);

                count += statement.executeUpdate();
            }

            String command1 = "INSERT INTO books VALUES(?,?,?,?,?,?)";
            int totalNum = amount;
            while (amount > 0) {
                try {

                    PreparedStatement statement1 = Main.con.prepareStatement(command1);
                    id = String.valueOf(Long.parseLong(id) + 1);
                    statement1.setString(1, id);
                    statement1.setString(2, bookName[0]);
                    statement1.setString(3, author[0]);
                    statement1.setString(4, press[0]);
                    statement1.setString(5, category);
                    statement1.setString(6, state);
                    statement1.executeUpdate();
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
                amount--;
            }
            JOptionPane.showMessageDialog(dialog2, "成功采购" + count + "种图书" + "共计" + totalNum + "本");

            // 更新table3的显示
            command = "SELECT * FROM wishbooks";
            ResultSet set = Main.con.createStatement().executeQuery(command);
            BothEvent.refreshTableWish(set, table3);
        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
        }
        textField6.setText("");
        dialog2.dispose();
    }

    // 监听管理员删除用户想看记录
    private void button10DeleteWishActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table3.getSelectedRows();

        String[] userId = new String[selectedRows.length];
        String[] bookName = new String[selectedRows.length];
        String[] author = new String[selectedRows.length];
        String[] press = new String[selectedRows.length];

        for (int i = 0; i < selectedRows.length; i++) {
            userId[i] = (String) table3.getValueAt(selectedRows[i], 0);
            bookName[i] = (String) table3.getValueAt(selectedRows[i], 1);
            author[i] = (String) table3.getValueAt(selectedRows[i], 2);
            press[i] = (String) table3.getValueAt(selectedRows[i], 3);
        }


        String command = "DELETE FROM wishbooks WHERE userid LIKE ?" +
                " AND bookname LIKE ? AND author LIKE ? AND press LIKE ?";
        try {
            PreparedStatement statement = Main.con.prepareStatement(command);

            int count = 0;
            for (int i = 0; i < selectedRows.length; i++) {
                statement.setString(1, userId[i]);
                statement.setString(2, bookName[i]);
                statement.setString(3, author[i]);
                statement.setString(4, press[i]);

                count += statement.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "成功删除" + count + "条记录");
        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
        }

    }

    // 监听管理员冻结事件
    private void button12BanActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table4.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "请选择要冻结的用户");
        } else {
            String[] userId = new String[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                userId[i] = (String) table4.getValueAt(selectedRows[i], 0);
            }

            final String state = "冻结";
            String command = "UPDATE users SET state=? WHERE id LIKE ?";
            int count = 0;
            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                for (int i = 0; i < selectedRows.length; i++) {
                    statement.setString(1, state);
                    statement.setString(2, userId[i]);

                    count += statement.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "成功冻结" + count + "位用户");

                command = "SELECT id,logtime,state FROM users";
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTableManager(set, table4);

            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }

        }
    }

    // 监听管理员删除用户事件
    private void button13DeleteUserActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table4.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的用户");
        } else {
            String[] userId = new String[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                userId[i] = (String) table4.getValueAt(selectedRows[i], 0);
            }


            String command = "DELETE FROM users WHERE id LIKE ?";
            int count = 0;
            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                for (int i = 0; i < selectedRows.length; i++) {
                    statement.setString(1, userId[i]);

                    count += statement.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "成功删除" + count + "位用户");

                command = "SELECT id,logtime,state FROM users";
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTableManager(set, table4);

            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }

            command = "DELETE FROM bookborrow WHERE userid LIKE ?";
            try{
                PreparedStatement statement = Main.con.prepareStatement(command);
                for (String id: userId) {
                    statement.setString(1,id);
                    statement.executeUpdate();
                }
            }catch (SQLException e1){
                System.out.println(e1.getMessage());
            }
        }
    }

    // 监听管理员解冻用户事件
    private void button14NotBanActionPerformed(ActionEvent e) {
        // TODO add your code here
        int[] selectedRows = table4.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "请选择要解冻的用户");
        } else {
            String[] userId = new String[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                userId[i] = (String) table4.getValueAt(selectedRows[i], 0);
            }

            final String state = "正常";
            String command = "UPDATE users SET state=? WHERE id LIKE ?";
            int count = 0;
            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                for (int i = 0; i < selectedRows.length; i++) {
                    statement.setString(1, state);
                    statement.setString(2, userId[i]);

                    count += statement.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "成功解冻" + count + "位用户");

                command = "SELECT id,logtime,state FROM users";
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTableManager(set, table4);

            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }

        }

    }

    // 监听切换登录用户事件
    private void button15ChangeUserActionPerformed(ActionEvent e) {
        // TODO add your code here
        LoginFrame frame = new LoginFrame();
        dispose();
        frame.setVisible(true);

    }
    
    
    // 监听搜索用户事件
    private void button11SearchUserActionPerformed(ActionEvent e) {
        // TODO add your code here
        String target = textField7.getText();

        if (target.equals("")) {
            String command = "SELECT id,logtime,state FROM users";
            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTableManager(set, table4);
            } catch (SQLException e1) {
                System.out.println("button2SearchActionPerformed " + e1.getMessage());
            }

        } else {
            String command = "SELECT id,logtime,state FROM users WHERE id LIKE ?";

            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, target);
                ResultSet set = statement.executeQuery();
                BothEvent.refreshTableManager(set,table4);
            } catch (SQLException e1) {
                System.out.println("button2SearchActionPerformed " + e1.getMessage());
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Xie yusheng
        panel1 = new JPanel();
        label1 = new JLabel();
        tabbedPane1 = new JTabbedPane();
        panel2 = new JPanel();
        panel8 = new JPanel();
        textField5 = new JTextField();
        comboBox2 = new JComboBox<>();
        button2 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel12 = new JPanel();
        button5 = new JButton();
        panel4 = new JPanel();
        label8 = new JLabel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        panel13 = new JPanel();
        button7 = new JButton();
        panel5 = new JPanel();
        panel6 = new JPanel();
        label2 = new JLabel();
        textField1 = new JTextField();
        label7 = new JLabel();
        label3 = new JLabel();
        textField2 = new JTextField();
        label4 = new JLabel();
        textField3 = new JTextField();
        label6 = new JLabel();
        textField4 = new JTextField();
        label5 = new JLabel();
        comboBox1 = new JComboBox<>();
        panel7 = new JPanel();
        button1 = new JButton();
        label11 = new JLabel();
        panel9 = new JPanel();
        panel10 = new JPanel();
        button3 = new JButton();
        button15 = new JButton();
        panel11 = new JPanel();
        label9 = new JLabel();
        label12 = new JLabel();
        label14 = new JLabel();
        label10 = new JLabel();
        label13 = new JLabel();
        panel17 = new JPanel();
        label20 = new JLabel();
        panel3 = new JPanel();
        panel21 = new JPanel();
        button12 = new JButton();
        button13 = new JButton();
        button14 = new JButton();
        scrollPane4 = new JScrollPane();
        table4 = new JTable();
        panel22 = new JPanel();
        label27 = new JLabel();
        textField7 = new JTextField();
        button11 = new JButton();
        panel14 = new JPanel();
        label22 = new JLabel();
        panel18 = new JPanel();
        button8 = new JButton();
        button10 = new JButton();
        scrollPane3 = new JScrollPane();
        table3 = new JTable();
        dialog1 = new JDialog();
        panel15 = new JPanel();
        button6 = new JButton();
        label15 = new JLabel();
        panel16 = new JPanel();
        label16 = new JLabel();
        passwordField3 = new JPasswordField();
        label19 = new JLabel();
        label17 = new JLabel();
        passwordField1 = new JPasswordField();
        label18 = new JLabel();
        passwordField2 = new JPasswordField();
        dialog2 = new JDialog();
        label23 = new JLabel();
        panel19 = new JPanel();
        button9 = new JButton();
        panel20 = new JPanel();
        label24 = new JLabel();
        textField6 = new JTextField();
        label26 = new JLabel();
        label25 = new JLabel();
        comboBox3 = new JComboBox<>();

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/images/home.png")).getImage());
        setTitle("home");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 700));
        setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {

            panel1.setLayout(new BorderLayout());

            //---- label1 ----
            label1.setText("\u6b22\u8fce");
            label1.setHorizontalAlignment(SwingConstants.CENTER);
            label1.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 30));
            panel1.add(label1, BorderLayout.NORTH);

            //======== tabbedPane1 ========
            {
                tabbedPane1.setFont(tabbedPane1.getFont().deriveFont(tabbedPane1.getFont().getSize() + 10f));
                tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                tabbedPane1.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                tabbedPane1.setTabPlacement(SwingConstants.LEFT);
                tabbedPane1.setBackground(new Color(214, 217, 223));
                tabbedPane1.addChangeListener(e -> tabbedPane1StateChanged(e));

                //======== panel2 ========
                {
                    panel2.setLayout(new BorderLayout());

                    //======== panel8 ========
                    {
                        panel8.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel8.getLayout()).columnWeights = new double[] {0.7999999999999999, 0.1, 0.1};

                        //---- textField5 ----
                        textField5.setFont(textField5.getFont().deriveFont(textField5.getFont().getSize() + 7f));
                        textField5.setBackground(new Color(214, 217, 223, 46));
                        panel8.add(textField5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- comboBox2 ----
                        comboBox2.setFont(comboBox2.getFont().deriveFont(comboBox2.getFont().getSize() + 2f));
                        panel8.add(comboBox2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button2 ----
                        button2.setText("\u641c\u7d22");
                        button2.setIcon(new ImageIcon(getClass().getResource("/images/search.png")));
                        button2.setFont(button2.getFont().deriveFont(button2.getFont().getSize() + 5f));
                        button2.addActionListener(e -> button2SearchActionPerformed(e));
                        panel8.add(button2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel2.add(panel8, BorderLayout.PAGE_START);

                    //======== scrollPane1 ========
                    {

                        //---- table1 ----
                        table1.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null, null, null, null},
                            },
                            new String[] {
                                "\u7f16\u53f7", "\u4e66\u540d", "\u4f5c\u8005", "\u51fa\u7248\u793e", "\u7c7b\u522b", "\u72b6\u6001"
                            }
                        ) {
                            Class<?>[] columnTypes = new Class<?>[] {
                                String.class, String.class, String.class, String.class, String.class, String.class
                            };
                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                return columnTypes[columnIndex];
                            }
                        });
                        table1.setRowHeight(36);
                        table1.setRowMargin(3);
                        table1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
                        table1.setBackground(new Color(255, 255, 255, 46));
                        scrollPane1.setViewportView(table1);
                    }
                    panel2.add(scrollPane1, BorderLayout.CENTER);

                    //======== panel12 ========
                    {
                        panel12.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel12.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0};

                        //---- button5 ----
                        button5.setText("\u501f\u9605\u6240\u9009\u56fe\u4e66");
                        button5.setFont(button5.getFont().deriveFont(button5.getFont().getSize() + 5f));
                        button5.setIcon(new ImageIcon(getClass().getResource("/images/confirm.png")));
                        button5.addActionListener(e -> button5BorrowActionPerformed(e));
                        panel12.add(button5, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel2.add(panel12, BorderLayout.PAGE_END);
                }
                tabbedPane1.addTab("\u56fe\u4e66\u67e5\u8be2", panel2);

                //======== panel4 ========
                {
                    panel4.setLayout(new BorderLayout());

                    //---- label8 ----
                    label8.setText("\u60a8\u7684\u5386\u53f2\u501f\u9605\u8bb0\u5f55\u5982\u4e0b");
                    label8.setHorizontalAlignment(SwingConstants.CENTER);
                    label8.setFont(label8.getFont().deriveFont(label8.getFont().getSize() + 10f));
                    panel4.add(label8, BorderLayout.PAGE_START);

                    //======== scrollPane2 ========
                    {

                        //---- table2 ----
                        table2.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null, null, null, " "},
                            },
                            new String[] {
                                "\u7f16\u53f7", "\u4e66\u540d", "\u4f5c\u8005", "\u501f\u4e66\u65f6\u95f4", "\u5f53\u524d\u72b6\u6001", "\u8fd8\u4e66\u65f6\u95f4"
                            }
                        ) {
                            Class<?>[] columnTypes = new Class<?>[] {
                                String.class, String.class, Object.class, String.class, String.class, String.class
                            };
                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                return columnTypes[columnIndex];
                            }
                        });
                        table2.setRowMargin(3);
                        table2.setRowHeight(36);
                        table2.setFont(table2.getFont().deriveFont(table2.getFont().getSize() + 10f));
                        table2.setIntercellSpacing(new Dimension(1, 3));
                        table2.setBackground(new Color(255, 255, 255, 46));
                        scrollPane2.setViewportView(table2);
                    }
                    panel4.add(scrollPane2, BorderLayout.CENTER);

                    //======== panel13 ========
                    {
                        panel13.setLayout(new GridBagLayout());

                        //---- button7 ----
                        button7.setText("\u5f52\u8fd8\u6240\u9009\u56fe\u4e66");
                        button7.setFont(button7.getFont().deriveFont(button7.getFont().getSize() + 5f));
                        button7.setIcon(new ImageIcon(getClass().getResource("/images/sunmit.png")));
                        button7.addActionListener(e -> button7BackActionPerformed(e));
                        panel13.add(button7, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel4.add(panel13, BorderLayout.PAGE_END);
                }
                tabbedPane1.addTab("\u501f\u9605\u8bb0\u5f55", panel4);

                //======== panel5 ========
                {
                    panel5.setLayout(new BorderLayout());

                    //======== panel6 ========
                    {
                        panel6.setOpaque(false);
                        panel6.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel6.getLayout()).columnWeights = new double[] {0.6, 0.5, 0.5, 0.4};

                        //---- label2 ----
                        label2.setText("\u4e66\u540d\uff1a");
                        label2.setHorizontalAlignment(SwingConstants.RIGHT);
                        label2.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 19));
                        panel6.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- textField1 ----
                        textField1.setFont(textField1.getFont().deriveFont(textField1.getFont().getSize() + 7f));
                        textField1.setBackground(new Color(214, 217, 223, 46));
                        textField1.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                        panel6.add(textField1, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label7 ----
                        label7.setText("                       ");
                        panel6.add(label7, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //---- label3 ----
                        label3.setText("\u4f5c\u8005\uff1a");
                        label3.setHorizontalAlignment(SwingConstants.RIGHT);
                        label3.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 19));
                        panel6.add(label3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- textField2 ----
                        textField2.setFont(textField2.getFont().deriveFont(textField2.getFont().getSize() + 7f));
                        textField2.setBackground(new Color(214, 217, 223, 46));
                        panel6.add(textField2, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label4 ----
                        label4.setText("\u51fa\u7248\u793e\uff1a");
                        label4.setHorizontalAlignment(SwingConstants.RIGHT);
                        label4.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 19));
                        panel6.add(label4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- textField3 ----
                        textField3.setFont(textField3.getFont().deriveFont(textField3.getFont().getSize() + 7f));
                        textField3.setBackground(new Color(214, 217, 223, 46));
                        panel6.add(textField3, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label6 ----
                        label6.setText("\u6570\u91cf\uff1a");
                        label6.setHorizontalAlignment(SwingConstants.RIGHT);
                        label6.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 19));
                        panel6.add(label6, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- textField4 ----
                        textField4.setFont(textField4.getFont().deriveFont(textField4.getFont().getSize() + 7f));
                        textField4.setBackground(new Color(214, 217, 223, 46));
                        panel6.add(textField4, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label5 ----
                        label5.setText("\u7c7b\u522b\uff1a");
                        label5.setHorizontalAlignment(SwingConstants.RIGHT);
                        label5.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 19));
                        panel6.add(label5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- comboBox1 ----
                        comboBox1.setToolTipText("\u8bf7\u9009\u62e9\u7c7b\u522b");
                        comboBox1.setEditable(true);
                        comboBox1.setFont(comboBox1.getFont().deriveFont(comboBox1.getFont().getSize() + 7f));
                        comboBox1.setBackground(new Color(214, 217, 223, 46));
                        panel6.add(comboBox1, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel5.add(panel6, BorderLayout.CENTER);

                    //======== panel7 ========
                    {
                        panel7.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel7.getLayout()).columnWeights = new double[] {1.0, 0.0};

                        //---- button1 ----
                        button1.setText("\u63d0\u4ea4\u5165\u5e93");
                        button1.setFont(new Font("Microsoft YaHei UI", button1.getFont().getStyle(), button1.getFont().getSize() + 7));
                        button1.setIcon(new ImageIcon(getClass().getResource("/images/sunmit.png")));
                        button1.addActionListener(e -> submitActionPerformed(e));
                        panel7.add(button1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel5.add(panel7, BorderLayout.PAGE_END);

                    //---- label11 ----
                    label11.setText(" \u8bf7\u6dfb\u52a0\u4e66\u76ee");
                    label11.setFont(label11.getFont().deriveFont(label11.getFont().getSize() + 16f));
                    label11.setHorizontalAlignment(SwingConstants.CENTER);
                    panel5.add(label11, BorderLayout.PAGE_START);
                }
                tabbedPane1.addTab("\u56fe\u4e66\u5165\u5e93", panel5);

                //======== panel9 ========
                {
                    panel9.setLayout(new BorderLayout());

                    //======== panel10 ========
                    {
                        panel10.setBorder(new EmptyBorder(2, 2, 2, 2));
                        panel10.setLayout(new GridBagLayout());

                        //---- button3 ----
                        button3.setText("\u4fee\u6539\u5bc6\u7801");
                        button3.setFont(button3.getFont().deriveFont(button3.getFont().getSize() + 5f));
                        button3.setIcon(new ImageIcon(getClass().getResource("/images/change.png")));
                        button3.addActionListener(e -> button3ChangePswActionPerformed(e));
                        panel10.add(button3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button15 ----
                        button15.setText("\u5207\u6362\u767b\u5f55\u8d26\u6237");
                        button15.setFont(button15.getFont().deriveFont(button15.getFont().getSize() + 5f));
                        button15.setIcon(new ImageIcon(getClass().getResource("/images/changeU.png")));
                        button15.addActionListener(e -> button15ChangeUserActionPerformed(e));
                        panel10.add(button15, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel9.add(panel10, BorderLayout.PAGE_END);

                    //======== panel11 ========
                    {
                        panel11.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel11.getLayout()).columnWeights = new double[] {0.5, 0.5, 0.30000000000000004};

                        //---- label9 ----
                        label9.setText("\u60a8\u7684\u6ce8\u518c\u65f6\u95f4\uff1a");
                        label9.setHorizontalAlignment(SwingConstants.RIGHT);
                        label9.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 23));
                        panel11.add(label9, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label12 ----
                        label12.setText("\u8fd9\u91cc\u663e\u793a\u65f6\u95f4");
                        label12.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 23));
                        panel11.add(label12, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label14 ----
                        label14.setText("      ");
                        panel11.add(label14, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //---- label10 ----
                        label10.setText("\u60a8\u7684\u603b\u5171\u9605\u8bfb\u91cf\uff1a");
                        label10.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 23));
                        label10.setHorizontalAlignment(SwingConstants.RIGHT);
                        panel11.add(label10, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label13 ----
                        label13.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 23));
                        label13.setText("\u8fd9\u91cc\u7edf\u8ba1\u6240\u6709\u501f\u9605\u8bb0\u5f55\u548c");
                        panel11.add(label13, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));
                    }
                    panel9.add(panel11, BorderLayout.CENTER);

                    //======== panel17 ========
                    {
                        panel17.setLayout(new GridBagLayout());

                        //---- label20 ----
                        label20.setText("\u4f60\u7684\u8d26\u6237\u4fe1\u606f\u5982\u4e0b");
                        label20.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 27));
                        panel17.add(label20, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));
                    }
                    panel9.add(panel17, BorderLayout.PAGE_START);
                }
                tabbedPane1.addTab("\u8d26\u53f7\u7ba1\u7406", panel9);

                //======== panel3 ========
                {
                    panel3.setLayout(new BorderLayout());

                    //======== panel21 ========
                    {
                        panel21.setLayout(new GridBagLayout());

                        //---- button12 ----
                        button12.setText("\u51bb\u7ed3\u7528\u6237");
                        button12.setFont(button12.getFont().deriveFont(button12.getFont().getSize() + 5f));
                        button12.setBackground(UIManager.getColor("Button.background"));
                        button12.setIcon(new ImageIcon(getClass().getResource("/images/lock.png")));
                        button12.addActionListener(e -> button12BanActionPerformed(e));
                        panel21.add(button12, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button13 ----
                        button13.setText("\u5220\u9664\u7528\u6237");
                        button13.setFont(button13.getFont().deriveFont(button13.getFont().getSize() + 5f));
                        button13.setIcon(new ImageIcon(getClass().getResource("/images/delete.png")));
                        button13.addActionListener(e -> button13DeleteUserActionPerformed(e));
                        panel21.add(button13, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button14 ----
                        button14.setText("\u89e3\u51bb\u7528\u6237");
                        button14.setFont(button14.getFont().deriveFont(button14.getFont().getSize() + 5f));
                        button14.setIcon(new ImageIcon(getClass().getResource("/images/unlock.png")));
                        button14.addActionListener(e -> button14NotBanActionPerformed(e));
                        panel21.add(button14, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel3.add(panel21, BorderLayout.PAGE_END);

                    //======== scrollPane4 ========
                    {

                        //---- table4 ----
                        table4.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null},
                            },
                            new String[] {
                                "\u7528\u6237\u540d", "\u6ce8\u518c\u65f6\u95f4", "\u8d26\u6237\u72b6\u6001"
                            }
                        ) {
                            Class<?>[] columnTypes = new Class<?>[] {
                                String.class, String.class, String.class
                            };
                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                return columnTypes[columnIndex];
                            }
                        });
                        table4.setRowHeight(30);
                        table4.setRowMargin(3);
                        table4.setFont(table4.getFont().deriveFont(table4.getFont().getSize() + 10f));
                        table4.setBackground(new Color(255, 255, 255, 46));
                        scrollPane4.setViewportView(table4);
                    }
                    panel3.add(scrollPane4, BorderLayout.CENTER);

                    //======== panel22 ========
                    {
                        panel22.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel22.getLayout()).columnWeights = new double[] {0.19999999999999998, 0.5, 0.30000000000000004};

                        //---- label27 ----
                        label27.setText("\u641c\u7d22\u7528\u6237\u540d\uff1a");
                        label27.setHorizontalAlignment(SwingConstants.RIGHT);
                        label27.setFont(label27.getFont().deriveFont(label27.getFont().getSize() + 8f));
                        panel22.add(label27, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- textField7 ----
                        textField7.setFont(textField7.getFont().deriveFont(textField7.getFont().getSize() + 7f));
                        textField7.setBackground(new Color(214, 217, 223, 46));
                        panel22.add(textField7, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button11 ----
                        button11.setText("\u641c\u7d22");
                        button11.setFont(button11.getFont().deriveFont(button11.getFont().getSize() + 7f));
                        button11.setIcon(new ImageIcon(getClass().getResource("/images/search.png")));
                        button11.addActionListener(e -> button11SearchUserActionPerformed(e));
                        panel22.add(button11, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel3.add(panel22, BorderLayout.PAGE_START);
                }
                tabbedPane1.addTab("\u7528\u6237\u7ba1\u7406", panel3);

                //======== panel14 ========
                {
                    panel14.setLayout(new BorderLayout());

                    //---- label22 ----
                    label22.setText("\u7528\u6237\u60f3\u770b\u7684\u4e66\u7c4d\u90fd\u5728\u4e0b\u9762\u5662\uff0c\u8bf7\u5c3d\u5feb\u91c7\u8d2d");
                    label22.setHorizontalAlignment(SwingConstants.CENTER);
                    label22.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
                    panel14.add(label22, BorderLayout.NORTH);

                    //======== panel18 ========
                    {
                        panel18.setLayout(new GridBagLayout());

                        //---- button8 ----
                        button8.setText("\u786e\u8ba4\u5df2\u8d2d");
                        button8.setFont(button8.getFont().deriveFont(button8.getFont().getSize() + 5f));
                        button8.setIcon(new ImageIcon(getClass().getResource("/images/confirm.png")));
                        button8.addActionListener(e -> button8BuyBooksActionPerformed(e));
                        panel18.add(button8, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button10 ----
                        button10.setText("\u5220\u9664\u8be5\u6761\u8bb0\u5f55");
                        button10.setFont(button10.getFont().deriveFont(button10.getFont().getSize() + 5f));
                        button10.setIcon(new ImageIcon(getClass().getResource("/images/delete.png")));
                        button10.addActionListener(e -> button10DeleteWishActionPerformed(e));
                        panel18.add(button10, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel14.add(panel18, BorderLayout.SOUTH);

                    //======== scrollPane3 ========
                    {

                        //---- table3 ----
                        table3.setRowHeight(36);
                        table3.setRowMargin(3);
                        table3.setFont(table3.getFont().deriveFont(table3.getFont().getSize() + 10f));
                        table3.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null, null},
                            },
                            new String[] {
                                "\u63d0\u4ea4\u7528\u6237", "\u4e66\u540d", "\u4f5c\u8005", "\u51fa\u7248\u793e"
                            }
                        ) {
                            Class<?>[] columnTypes = new Class<?>[] {
                                String.class, String.class, String.class, String.class
                            };
                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                return columnTypes[columnIndex];
                            }
                        });
                        table3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        table3.setMaximumSize(new Dimension(1215752191, 36));
                        table3.setBackground(new Color(255, 255, 255, 46));
                        scrollPane3.setViewportView(table3);
                    }
                    panel14.add(scrollPane3, BorderLayout.CENTER);
                }
                tabbedPane1.addTab("\u7528\u6237\u60f3\u770b", panel14);
            }
            panel1.add(tabbedPane1, BorderLayout.CENTER);
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //======== dialog1 ========
        {
            dialog1.setFont(dialog1.getFont().deriveFont(dialog1.getFont().getSize() + 3f));
            dialog1.setModal(true);
            dialog1.setMinimumSize(new Dimension(400, 300));
            dialog1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            Container dialog1ContentPane = dialog1.getContentPane();
            dialog1ContentPane.setLayout(new BorderLayout());

            //======== panel15 ========
            {
                panel15.setLayout(new FlowLayout());

                //---- button6 ----
                button6.setText("\u786e\u8ba4\u4fee\u6539");
                button6.setFont(button6.getFont().deriveFont(button6.getFont().getSize() + 5f));
                button6.addActionListener(e -> button6ConfirmPswActionPerformed(e));
                panel15.add(button6);
            }
            dialog1ContentPane.add(panel15, BorderLayout.SOUTH);

            //---- label15 ----
            label15.setText("\u4fee\u6539\u5bc6\u7801");
            label15.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
            label15.setHorizontalAlignment(SwingConstants.CENTER);
            dialog1ContentPane.add(label15, BorderLayout.NORTH);

            //======== panel16 ========
            {
                panel16.setLayout(new GridBagLayout());
                ((GridBagLayout)panel16.getLayout()).columnWeights = new double[] {0.1, 0.5, 0.30000000000000004};

                //---- label16 ----
                label16.setText("\u8bf7\u8f93\u5165\u539f\u5bc6\u7801\uff1a");
                label16.setHorizontalAlignment(SwingConstants.RIGHT);
                label16.setFont(label16.getFont().deriveFont(label16.getFont().getSize() + 7f));
                panel16.add(label16, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- passwordField3 ----
                passwordField3.setFont(passwordField3.getFont().deriveFont(passwordField3.getFont().getSize() + 7f));
                passwordField3.setBackground(new Color(214, 217, 223, 46));
                panel16.add(passwordField3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label19 ----
                label19.setText("     ");
                panel16.add(label19, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label17 ----
                label17.setText("\u8bf7\u8f93\u5165\u65b0\u5bc6\u7801\uff1a");
                label17.setHorizontalAlignment(SwingConstants.RIGHT);
                label17.setFont(label17.getFont().deriveFont(label17.getFont().getSize() + 7f));
                panel16.add(label17, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- passwordField1 ----
                passwordField1.setFont(passwordField1.getFont().deriveFont(passwordField1.getFont().getSize() + 7f));
                passwordField1.setBackground(new Color(214, 217, 223, 46));
                panel16.add(passwordField1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label18 ----
                label18.setText("\u8bf7\u786e\u8ba4\u65b0\u5bc6\u7801\uff1a");
                label18.setHorizontalAlignment(SwingConstants.RIGHT);
                label18.setFont(label18.getFont().deriveFont(label18.getFont().getSize() + 7f));
                panel16.add(label18, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- passwordField2 ----
                passwordField2.setFont(passwordField2.getFont().deriveFont(passwordField2.getFont().getSize() + 7f));
                passwordField2.setBackground(new Color(214, 217, 223, 46));
                panel16.add(passwordField2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
            }
            dialog1ContentPane.add(panel16, BorderLayout.CENTER);
            dialog1.pack();
            dialog1.setLocationRelativeTo(dialog1.getOwner());
        }

        //======== dialog2 ========
        {
            dialog2.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog2.setMinimumSize(new Dimension(400, 300));
            dialog2.setModal(true);
            Container dialog2ContentPane = dialog2.getContentPane();
            dialog2ContentPane.setLayout(new BorderLayout());

            //---- label23 ----
            label23.setText("\u8bf7\u8865\u5145\u5269\u4f59\u4fe1\u606f\u4e4b\u95f4\u5165\u5e93");
            label23.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
            label23.setHorizontalAlignment(SwingConstants.CENTER);
            dialog2ContentPane.add(label23, BorderLayout.NORTH);

            //======== panel19 ========
            {

                panel19.setLayout(new GridBagLayout());

                //---- button9 ----
                button9.setText("\u63d0\u4ea4\u5165\u5e93");
                button9.setFont(button9.getFont().deriveFont(button9.getFont().getSize() + 5f));
                button9.addActionListener(e -> button9WishBookActionPerformed(e));
                panel19.add(button9, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
            }
            dialog2ContentPane.add(panel19, BorderLayout.SOUTH);

            //======== panel20 ========
            {
                panel20.setLayout(new GridBagLayout());
                ((GridBagLayout)panel20.getLayout()).columnWeights = new double[] {0.1, 0.5, 0.5, 0.30000000000000004};

                //---- label24 ----
                label24.setText(" \u6570\u91cf\uff1a");
                label24.setHorizontalAlignment(SwingConstants.RIGHT);
                label24.setFont(label24.getFont().deriveFont(label24.getFont().getSize() + 7f));
                panel20.add(label24, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- textField6 ----
                textField6.setFont(textField6.getFont().deriveFont(textField6.getFont().getSize() + 7f));
                textField6.setBackground(new Color(214, 217, 223, 46));
                panel20.add(textField6, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label26 ----
                label26.setText("      ");
                panel20.add(label26, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label25 ----
                label25.setText("\u7c7b\u522b\uff1a");
                label25.setHorizontalAlignment(SwingConstants.RIGHT);
                label25.setFont(label25.getFont().deriveFont(label25.getFont().getSize() + 7f));
                panel20.add(label25, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox3 ----
                comboBox3.setFont(comboBox3.getFont().deriveFont(comboBox3.getFont().getSize() + 3f));
                panel20.add(comboBox3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
            }
            dialog2ContentPane.add(panel20, BorderLayout.CENTER);
            dialog2.pack();
            dialog2.setLocationRelativeTo(dialog2.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Xie yusheng
    private JPanel panel1;
    private JLabel label1;
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JPanel panel8;
    private JTextField textField5;
    private JComboBox<String> comboBox2;
    private JButton button2;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel12;
    private JButton button5;
    private JPanel panel4;
    private JLabel label8;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JPanel panel13;
    private JButton button7;
    private JPanel panel5;
    private JPanel panel6;
    private JLabel label2;
    private JTextField textField1;
    private JLabel label7;
    private JLabel label3;
    private JTextField textField2;
    private JLabel label4;
    private JTextField textField3;
    private JLabel label6;
    private JTextField textField4;
    private JLabel label5;
    private JComboBox<String> comboBox1;
    private JPanel panel7;
    private JButton button1;
    private JLabel label11;
    private JPanel panel9;
    private JPanel panel10;
    private JButton button3;
    private JButton button15;
    private JPanel panel11;
    private JLabel label9;
    private JLabel label12;
    private JLabel label14;
    private JLabel label10;
    private JLabel label13;
    private JPanel panel17;
    private JLabel label20;
    private JPanel panel3;
    private JPanel panel21;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JScrollPane scrollPane4;
    private JTable table4;
    private JPanel panel22;
    private JLabel label27;
    private JTextField textField7;
    private JButton button11;
    private JPanel panel14;
    private JLabel label22;
    private JPanel panel18;
    private JButton button8;
    private JButton button10;
    private JScrollPane scrollPane3;
    private JTable table3;
    private JDialog dialog1;
    private JPanel panel15;
    private JButton button6;
    private JLabel label15;
    private JPanel panel16;
    private JLabel label16;
    private JPasswordField passwordField3;
    private JLabel label19;
    private JLabel label17;
    private JPasswordField passwordField1;
    private JLabel label18;
    private JPasswordField passwordField2;
    private JDialog dialog2;
    private JLabel label23;
    private JPanel panel19;
    private JButton button9;
    private JPanel panel20;
    private JLabel label24;
    private JTextField textField6;
    private JLabel label26;
    private JLabel label25;
    private JComboBox<String> comboBox3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
