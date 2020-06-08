/*
 * Created by JFormDesigner on Tue Jun 02 08:48:43 GMT+08:00 2020
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
public class SimpleFrame extends JFrame {
    public SimpleFrame() {
        initComponents();
        addItemToComboBox();
        initWelcomeLabel();
        initTableHeader();
        initBackground();
    }

    // 自定义背景图
    private void initBackground(){
        ImageIcon img = new ImageIcon("src/images/mainBG4.jpg");
        JLabel pic = new JLabel(img);

        getLayeredPane().add(pic,new Integer(Integer.MIN_VALUE));
        pic.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        JPanel contentPane = (JPanel) getContentPane();

        // 设置透明
        contentPane.setOpaque(false);
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
        panel12.setOpaque(false);
        panel13.setOpaque(false);
        panel15.setOpaque(false);
        panel16.setOpaque(false);
        scrollPane1.setOpaque(false);
        scrollPane2.setOpaque(false);
        scrollPane1.getViewport().setOpaque(false);
        scrollPane2.getViewport().setOpaque(false);
        table1.setOpaque(false);
        table2.setOpaque(false);

        ImageIcon img1 = new ImageIcon("src/images/signin.jpg");
        JLabel pic1 = new JLabel(img1);
        dialog1.getLayeredPane().add(pic1,new Integer(Integer.MIN_VALUE));
        pic1.setBounds(0, 0, img1.getIconWidth(), img1.getIconHeight());
        JPanel contentPane1 = (JPanel) dialog1.getContentPane();

        contentPane1.setOpaque(false);
    }

    // 为复选框添加内容
    private void addItemToComboBox() {
        String[] searchItems = {"编号", "书名", "作者", "出版社", "类别", "状态"};
        for (String item : searchItems) {
            comboBox1.addItem(item);
        }
    }

    // 初始化表头大小
    private void initTableHeader(){
        table1.getTableHeader().setFont(new Font("Serif",Font.PLAIN,24));
        table2.getTableHeader().setFont(new Font("Serif",Font.PLAIN,24));
    }

    // 初始化欢迎label
    private void initWelcomeLabel() {
        label1.setText("欢迎您 " + Main.userId);
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
        } else if (selected==1){
            // 显示借阅图书的历史记录
            String command="SELECT books.bookid,books.bookname,books.author,bookborrow.borrowtime," +
                    "bookborrow.state,bookborrow.backtime" +
                    " FROM bookborrow,books" +
                    " WHERE bookborrow.userid LIKE ? AND bookborrow.bookid=books.bookid";

            try{
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1,Main.userId);
                ResultSet set = statement.executeQuery();
                if (!set.next()){
                    label2.setText("您暂时没有历史记录qaq");
                    return;
                }
                BothEvent.refreshTable2(set,table2);
            }catch (SQLException e1){
                System.out.println(e1.getMessage());
            }
        }else if (selected==2){
            String command;
            final String logTime;
            if (Main.identity.equals("用户"))
                command = "SELECT logtime FROM users";
            else
                command = "SELECT logtime FROM superusers";
            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                if (set.next()) {
                    logTime = set.getString("logtime");
                    label5.setText(logTime);
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
                label7.setText("你总共借阅了"+set.getRow()+"本图书");
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        }
    }

    // 对图书查询搜索事件进行响应
    private void button1SearchActionPerformed(ActionEvent e) {
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
        String target = textField1.getText();

        // 输入内容为空则显示所有内容
        // 链接数据库进行搜索并展示
        if (target.equals("")) {
            String command = "SELECT * FROM books";
            try {
                ResultSet set = Main.con.createStatement().executeQuery(command);
                BothEvent.refreshTable(set,table1);
            } catch (SQLException e1) {
                System.out.println("button2SearchActionPerformed " + e1.getMessage());
            }

        } else {
            String command = toColumn.get(comboBox1.getItemAt(comboBox1.getSelectedIndex()));

            try {
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1, target);
                ResultSet set = statement.executeQuery();
                BothEvent.refreshTable(set,table1);
            } catch (SQLException e1) {
                System.out.println("button2SearchActionPerformed " + e1.getMessage());
            }
        }
    }

    // 监听借书事件
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
    
    // 监听用户还书事件
    private void button2BackActionPerformed(ActionEvent e) {
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
                    label2.setText("您暂时没有历史记录qaq");
                    return;
                }
                BothEvent.refreshTable2(set, table2);


            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }

        }
    }
    
    // 监听修改密码事件
    private void button3ChangeActionPerformed(ActionEvent e) {
        // TODO add your code here
        dialog1.setVisible(true);
    }
    
    // 监听确认密码事件
    private void button6ActionPerformed(ActionEvent e) {
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

            try{
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1,Main.userId);
                ResultSet set = statement.executeQuery();
                if (set.next()){
                    if (!set.getString("psw").equals(oldPsw)){
                        JOptionPane.showMessageDialog(dialog1,"输入的原密码有误");
                        return;
                    }
                }
            }catch (SQLException e1){
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

            try{
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1,newPsw);
                statement.setString(2,Main.userId);
                if (statement.executeUpdate()==1)
                    JOptionPane.showMessageDialog(dialog1,"修改完成，下次登录记得输入新密码噢");
                else
                    JOptionPane.showMessageDialog(dialog1,"服务器不在线噢");
                passwordField1.setText("");
                passwordField2.setText("");
                passwordField3.setText("");
                dialog1.dispose();
            }catch (SQLException e1){
                System.out.println(e1.getMessage());
            }
        }
    }
    
    // 监听推荐书目事件
    private void button4UserSubmitActionPerformed(ActionEvent e) {
        // TODO add your code here
        final String bookName = textField2.getText();
        final String author = textField3.getText();
        final String press = textField4.getText();
        final String userId = Main.userId;
        String command;

        if (bookName.equals(""))
            JOptionPane.showMessageDialog(this, "请输入想看的书名", "注意", JOptionPane.WARNING_MESSAGE);
        else if (author.equals(""))
            JOptionPane.showMessageDialog(this, "请输入书的作者", "注意", JOptionPane.WARNING_MESSAGE);
        else if (press.equals(""))
            JOptionPane.showMessageDialog(this, "请输入出版社名", "注意", JOptionPane.WARNING_MESSAGE);
        else{
            // 确认完成，提交至数据库
            command = "INSERT INTO wishbooks VALUES(?,?,?,?)";

            try{
                PreparedStatement statement = Main.con.prepareStatement(command);
                statement.setString(1,userId);
                statement.setString(2,bookName);
                statement.setString(3,author);
                statement.setString(4,press);
                if (statement.executeUpdate()==1)
                    JOptionPane.showMessageDialog(this, "提交成功，管理员将尽快采购，敬请期待");
                else
                    JOptionPane.showMessageDialog(this, "啊偶，服务器开小差了");
                textField2.setText("");
                textField3.setText("");
                textField4.setText("");
            }catch (SQLException el){
                System.out.println(el.getMessage());
            }
        }
    }
    
    // 监听用户切换事件
    private void button7ChangeUseActionPerformed(ActionEvent e) {
        // TODO add your code here
        LoginFrame frame = new LoginFrame();
        dispose();
        frame.setVisible(true);
    }
    
    

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Xie yusheng
        panel1 = new JPanel();
        label1 = new JLabel();
        tabbedPane1 = new JTabbedPane();
        panel2 = new JPanel();
        panel6 = new JPanel();
        textField1 = new JTextField();
        comboBox1 = new JComboBox<>();
        button1 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel10 = new JPanel();
        button5 = new JButton();
        panel4 = new JPanel();
        label2 = new JLabel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        panel7 = new JPanel();
        button2 = new JButton();
        panel5 = new JPanel();
        panel8 = new JPanel();
        button3 = new JButton();
        button7 = new JButton();
        panel9 = new JPanel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        panel12 = new JPanel();
        label12 = new JLabel();
        panel3 = new JPanel();
        label14 = new JLabel();
        panel13 = new JPanel();
        button4 = new JButton();
        panel14 = new JPanel();
        label20 = new JLabel();
        textField2 = new JTextField();
        label23 = new JLabel();
        label21 = new JLabel();
        textField3 = new JTextField();
        label22 = new JLabel();
        textField4 = new JTextField();
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

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("home");
        setIconImage(new ImageIcon(getClass().getResource("/images/log.png")).getImage());
        setMinimumSize(new Dimension(950, 700));
        setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {

            panel1.setLayout(new BorderLayout());

            //---- label1 ----
            label1.setText("\u6b22\u8fce");
            label1.setHorizontalAlignment(SwingConstants.CENTER);
            label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 28));
            panel1.add(label1, BorderLayout.NORTH);

            //======== tabbedPane1 ========
            {
                tabbedPane1.setTabPlacement(SwingConstants.LEFT);
                tabbedPane1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
                tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                tabbedPane1.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                tabbedPane1.addChangeListener(e -> tabbedPane1StateChanged(e));

                //======== panel2 ========
                {
                    panel2.setLayout(new BorderLayout());

                    //======== panel6 ========
                    {
                        panel6.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel6.getLayout()).columnWeights = new double[] {0.8, 0.1, 0.1};

                        //---- textField1 ----
                        textField1.setFont(textField1.getFont().deriveFont(textField1.getFont().getSize() + 7f));
                        textField1.setBackground(new Color(214, 217, 223, 46));
                        panel6.add(textField1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- comboBox1 ----
                        comboBox1.setFont(comboBox1.getFont().deriveFont(comboBox1.getFont().getSize() + 3f));
                        panel6.add(comboBox1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button1 ----
                        button1.setText("\u641c\u7d22");
                        button1.setFont(button1.getFont().deriveFont(button1.getFont().getSize() + 3f));
                        button1.setIcon(new ImageIcon(getClass().getResource("/images/search.png")));
                        button1.addActionListener(e -> button1SearchActionPerformed(e));
                        panel6.add(button1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel2.add(panel6, BorderLayout.PAGE_START);

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
                        table1.setFont(table1.getFont().deriveFont(table1.getFont().getSize() + 10f));
                        table1.setBackground(new Color(255, 255, 255, 46));
                        scrollPane1.setViewportView(table1);
                    }
                    panel2.add(scrollPane1, BorderLayout.CENTER);

                    //======== panel10 ========
                    {
                        panel10.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel10.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0};

                        //---- button5 ----
                        button5.setText("\u501f\u9605\u6240\u9009\u56fe\u4e66");
                        button5.setFont(button5.getFont().deriveFont(button5.getFont().getSize() + 4f));
                        button5.setIcon(new ImageIcon(getClass().getResource("/images/confirm.png")));
                        button5.addActionListener(e -> button5BorrowActionPerformed(e));
                        panel10.add(button5, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel2.add(panel10, BorderLayout.PAGE_END);
                }
                tabbedPane1.addTab("\u56fe\u4e66\u67e5\u8be2", panel2);

                //======== panel4 ========
                {
                    panel4.setLayout(new BorderLayout());

                    //---- label2 ----
                    label2.setText("\u60a8\u7684\u5386\u53f2\u501f\u9605\u8bb0\u5f55\u5982\u4e0b");
                    label2.setHorizontalAlignment(SwingConstants.CENTER);
                    label2.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 23));
                    panel4.add(label2, BorderLayout.PAGE_START);

                    //======== scrollPane2 ========
                    {

                        //---- table2 ----
                        table2.setRowHeight(36);
                        table2.setRowMargin(3);
                        table2.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null, null, null, null},
                            },
                            new String[] {
                                "\u7f16\u53f7", "\u4e66\u540d", "\u4f5c\u8005", "\u501f\u4e66\u65f6\u95f4", "\u5f53\u524d\u72b6\u6001", "\u8fd8\u4e66\u65f6\u95f4"
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
                        table2.setFont(table2.getFont().deriveFont(table2.getFont().getSize() + 10f));
                        table2.setBackground(new Color(255, 255, 255, 46));
                        scrollPane2.setViewportView(table2);
                    }
                    panel4.add(scrollPane2, BorderLayout.CENTER);

                    //======== panel7 ========
                    {
                        panel7.setLayout(new GridBagLayout());

                        //---- button2 ----
                        button2.setText("\u5f52\u8fd8\u6240\u9009\u56fe\u4e66");
                        button2.setFont(button2.getFont().deriveFont(button2.getFont().getSize() + 4f));
                        button2.setIcon(new ImageIcon(getClass().getResource("/images/sunmit.png")));
                        button2.addActionListener(e -> button2BackActionPerformed(e));
                        panel7.add(button2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel4.add(panel7, BorderLayout.PAGE_END);
                }
                tabbedPane1.addTab("\u501f\u9605\u8bb0\u5f55", panel4);

                //======== panel5 ========
                {
                    panel5.setLayout(new BorderLayout());

                    //======== panel8 ========
                    {
                        panel8.setLayout(new GridBagLayout());

                        //---- button3 ----
                        button3.setText("\u4fee\u6539\u5bc6\u7801");
                        button3.setFont(button3.getFont().deriveFont(button3.getFont().getSize() + 4f));
                        button3.setIcon(new ImageIcon(getClass().getResource("/images/change.png")));
                        button3.addActionListener(e -> button3ChangeActionPerformed(e));
                        panel8.add(button3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- button7 ----
                        button7.setText("\u5207\u6362\u767b\u5f55\u8d26\u6237");
                        button7.setFont(button7.getFont().deriveFont(button7.getFont().getSize() + 4f));
                        button7.setIcon(new ImageIcon(getClass().getResource("/images/changeU.png")));
                        button7.addActionListener(e -> button7ChangeUseActionPerformed(e));
                        panel8.add(button7, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel5.add(panel8, BorderLayout.PAGE_END);

                    //======== panel9 ========
                    {
                        panel9.setLayout(new GridBagLayout());

                        //---- label4 ----
                        label4.setText(" \u60a8\u7684\u6ce8\u518c\u65f6\u95f4\uff1a");
                        label4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
                        panel9.add(label4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label5 ----
                        label5.setText("\u663e\u793a\u6ce8\u518c\u65f6\u95f4");
                        label5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
                        panel9.add(label5, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label6 ----
                        label6.setText("\u60a8\u7684\u9605\u8bfb\u91cf\uff1a");
                        label6.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
                        panel9.add(label6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label7 ----
                        label7.setText("\u8fd9\u91cc\u7edf\u8ba1\u6240\u6709\u501f\u9605\u8bb0\u5f55");
                        label7.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
                        panel9.add(label7, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));
                    }
                    panel5.add(panel9, BorderLayout.CENTER);

                    //======== panel12 ========
                    {
                        panel12.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel12.getLayout()).columnWeights = new double[] {1.0};

                        //---- label12 ----
                        label12.setText("\u4f60\u7684\u8d26\u6237\u4fe1\u606f\u5982\u4e0b");
                        label12.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 23));
                        label12.setHorizontalAlignment(SwingConstants.CENTER);
                        panel12.add(label12, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));
                    }
                    panel5.add(panel12, BorderLayout.PAGE_START);
                }
                tabbedPane1.addTab(" \u8d26\u53f7\u7ba1\u7406", panel5);

                //======== panel3 ========
                {
                    panel3.setLayout(new BorderLayout());

                    //---- label14 ----
                    label14.setText("\u5199\u4e0b\u4f60\u60f3\u4e30\u5bcc\u9986\u85cf\u7684\u4e66\u76ee");
                    label14.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
                    label14.setHorizontalAlignment(SwingConstants.CENTER);
                    panel3.add(label14, BorderLayout.NORTH);

                    //======== panel13 ========
                    {
                        panel13.setLayout(new GridBagLayout());

                        //---- button4 ----
                        button4.setText("\u63d0\u4ea4");
                        button4.setFont(button4.getFont().deriveFont(button4.getFont().getSize() + 4f));
                        button4.setIcon(new ImageIcon(getClass().getResource("/images/sunmit.png")));
                        button4.addActionListener(e -> button4UserSubmitActionPerformed(e));
                        panel13.add(button4, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel3.add(panel13, BorderLayout.SOUTH);

                    //======== panel14 ========
                    {
                        panel14.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel14.getLayout()).columnWeights = new double[] {0.6, 0.6, 0.6};

                        //---- label20 ----
                        label20.setText("\u4e66\u540d\uff1a");
                        label20.setHorizontalAlignment(SwingConstants.RIGHT);
                        label20.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 22));
                        panel14.add(label20, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- textField2 ----
                        textField2.setFont(textField2.getFont().deriveFont(textField2.getFont().getSize() + 7f));
                        textField2.setBackground(new Color(214, 217, 223, 46));
                        panel14.add(textField2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label23 ----
                        label23.setText("                        ");
                        panel14.add(label23, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //---- label21 ----
                        label21.setText("\u4f5c\u8005\uff1a");
                        label21.setHorizontalAlignment(SwingConstants.RIGHT);
                        label21.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 22));
                        panel14.add(label21, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- textField3 ----
                        textField3.setFont(textField3.getFont().deriveFont(textField3.getFont().getSize() + 7f));
                        textField3.setBackground(new Color(214, 217, 223, 46));
                        panel14.add(textField3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- label22 ----
                        label22.setText("\u51fa\u7248\u793e\uff1a");
                        label22.setHorizontalAlignment(SwingConstants.RIGHT);
                        label22.setFont(new Font("Microsoft YaHei UI", Font.ITALIC, 22));
                        panel14.add(label22, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- textField4 ----
                        textField4.setFont(textField4.getFont().deriveFont(textField4.getFont().getSize() + 7f));
                        textField4.setBackground(new Color(214, 217, 223, 46));
                        panel14.add(textField4, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    panel3.add(panel14, BorderLayout.CENTER);
                }
                tabbedPane1.addTab("\u56fe\u4e66\u5f81\u96c6", panel3);
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
                button6.addActionListener(e -> button6ActionPerformed(e));
                panel15.add(button6);
            }
            dialog1ContentPane.add(panel15, BorderLayout.SOUTH);

            //---- label15 ----
            label15.setText("\u4fee\u6539\u5bc6\u7801");
            label15.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
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
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Xie yusheng
    private JPanel panel1;
    private JLabel label1;
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JPanel panel6;
    private JTextField textField1;
    private JComboBox<String> comboBox1;
    private JButton button1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel10;
    private JButton button5;
    private JPanel panel4;
    private JLabel label2;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JPanel panel7;
    private JButton button2;
    private JPanel panel5;
    private JPanel panel8;
    private JButton button3;
    private JButton button7;
    private JPanel panel9;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JPanel panel12;
    private JLabel label12;
    private JPanel panel3;
    private JLabel label14;
    private JPanel panel13;
    private JButton button4;
    private JPanel panel14;
    private JLabel label20;
    private JTextField textField2;
    private JLabel label23;
    private JLabel label21;
    private JTextField textField3;
    private JLabel label22;
    private JTextField textField4;
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
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
