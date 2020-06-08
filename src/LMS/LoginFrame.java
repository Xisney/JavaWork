/*
 * Created by JFormDesigner on Wed May 27 18:23:34 GMT+08:00 2020
 */

package LMS;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.Instant;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Xie yusheng
 */
public class LoginFrame extends JFrame {

    public LoginFrame() {
        initLooksAndFeels("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        initComponents();
        addItemToComboBox();
        initBackground();
    }

    void initMysql() {
        Welcome welcome = new Welcome();
        welcome.setVisible(true);
        Main.con = new MysqlCon().getCon();
        // 使用弹窗提示数据库的链接结果，但是交互性不太好，所以暂时取消
        if (Main.con != null) {
//            JOptionPane.showMessageDialog(this, "数据库链接成功");
            welcome.dispose();
        } else {
            welcome.dispose();
            JOptionPane.showMessageDialog(this, "数据库链接失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 为复选框添加items
    private void addItemToComboBox() {
        String[] identity = {"用户", "管理员"};

        for (String item : identity) {
            comboBox1.addItem(item);
        }
    }

    // 自定义背景图
    private void initBackground() {
        ImageIcon img = new ImageIcon("src/images/loginBGP.jpg");
        JLabel pic = new JLabel(img);
        getLayeredPane().add(pic, new Integer(Integer.MIN_VALUE));

//        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
//        pic.setBounds(-getX(),-getY(),size.width ,size.height);
        pic.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        JPanel contentPane = (JPanel) getContentPane();

        // 设置其余窗格为透明
        contentPane.setOpaque(false);
        dialogPane.setOpaque(false);
        contentPanel.setOpaque(false);
        panel1.setOpaque(false);
        buttonBar.setOpaque(false);

        ImageIcon img1 = new ImageIcon("src/images/signin.jpg");
        JLabel pic1 = new JLabel(img1);
        dialog1.getLayeredPane().add(pic1,new Integer(Integer.MIN_VALUE));
        pic1.setBounds(0, 0, img1.getIconWidth(), img1.getIconHeight());
        JPanel contentPane1 = (JPanel) dialog1.getContentPane();

        contentPane1.setOpaque(false);
        panel2.setOpaque(false);
        panel3.setOpaque(false);

    }

    // 切换观感
    private void initLooksAndFeels(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    // 响应登录事件
    private void signInButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        String id = textField1.getText();
        Main.userId = id;
        String psw = new String(passwordField1.getPassword());

        // 确定登录身份,并保存于全局变量
        String identity = comboBox1.getItemAt(comboBox1.getSelectedIndex());
        Main.identity = identity;

        String command;
        // 登录身份不符合的提示信息
        String info;
        if (identity.equals("用户")) {
            command = "SELECT psw" + " FROM users" + " WHERE id LIKE ?";
            info = "未查找到相关用户信息，请注册";
        } else {
            command = "SELECT psw" + " FROM superUsers" + " WHERE id LIKE ?";
            info = "未查找到相关管理员信息";
        }

        String preCommand = "SELECT state FROM users where id LIKE ?";
        try {
            PreparedStatement statement = Main.con.prepareStatement(preCommand);
            statement.setString(1,Main.userId);
            ResultSet set = statement.executeQuery();
            if (set.next()){
                if (set.getString("state").equals("冻结")){
                    JOptionPane.showMessageDialog(this,"您的账户因不当行为已被冻结，暂时无法登录","警告",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

        }catch (SQLException e1){
            System.out.println(e1.getMessage());
        }

        try {
            PreparedStatement stat = Main.con.prepareStatement(command);
            stat.setString(1, id);
            ResultSet set = stat.executeQuery();
            if (set.next()) {
                if (psw.equals(set.getString("psw"))) {
                    // 书写登录成功的代码
                    if (identity.equals("管理员")) {
                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                    } else {
                        SimpleFrame simpleFrame = new SimpleFrame();
                        simpleFrame.setVisible(true);
                    }
                    set.close();
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "密码错误");
                }
            } else {
                JOptionPane.showMessageDialog(this, info);
            }
            set.close();
            stat.close();
        } catch (SQLException el) {
            System.out.println("signInButtonActionPerformed " + el.getMessage());
        }
    }

    // 显示注册的对话框
    private void signUpButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        dialog1.setVisible(true);
    }

    // 注册，并录入数据库
    private void confirmSignUpActionPerformed(ActionEvent e) {
        // TODO add your code here
        final String userName = textField2.getText();
        final String psw = new String(passwordField2.getPassword());
        final String conPsw = new String(passwordField3.getPassword());
        final String state = "正常";

        if (userName.equals("")){
            JOptionPane.showMessageDialog(dialog1, "用户名不能为空", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else {
            // 检查用户名是否存在
            String preCommand = "SELECT id FROM users";
            try {
                ResultSet set = Main.con.createStatement().executeQuery(preCommand);

                while (set.next()) {
                    if (userName.equals(set.getString("id"))) {
                        JOptionPane.showMessageDialog(dialog1, "用户名已被使用", "注意", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            } catch (SQLException el) {
                System.out.println(el.getMessage());
            }
        }

        if (psw.equals(""))
            JOptionPane.showMessageDialog(dialog1, "密码不能为空", "注意", JOptionPane.WARNING_MESSAGE);
        else if (conPsw.equals(""))
            JOptionPane.showMessageDialog(dialog1, "请确认密码", "注意", JOptionPane.WARNING_MESSAGE);
        else {
            if (psw.equals(conPsw)) {
                // 确认完成，将用户数据写入数据库
                String command = "INSERT INTO users VALUES(?,?,?,?)";
                // 获得秒级的时间戳，用作注册时间
                String logTime = BothEvent.converse(String.valueOf(Instant.now().getEpochSecond()));
                try {
                    PreparedStatement stat = Main.con.prepareStatement(command);
                    stat.setString(1, userName);
                    stat.setString(2, psw);
                    stat.setString(3, logTime);
                    stat.setString(4,state);

                    // 若更新成功或失败都用弹窗进行提示
                    if (stat.executeUpdate() == 1) {
                        passwordField1.setText("");
                        passwordField2.setText("");
                        passwordField3.setText("");
                        textField2.setText("");
                        dialog1.dispose();
                        JOptionPane.showMessageDialog(this, "注册成功了哈哈");
                    } else JOptionPane.showMessageDialog(dialog1, "更新失败，请检测数据库链接", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
            } else JOptionPane.showMessageDialog(dialog1, "两次密码不一致", "注意", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Xie yusheng
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        panel1 = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        label3 = new JLabel();
        label2 = new JLabel();
        passwordField1 = new JPasswordField();
        label10 = new JLabel();
        comboBox1 = new JComboBox<>();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        label9 = new JLabel();
        dialog1 = new JDialog();
        panel2 = new JPanel();
        button1 = new JButton();
        label4 = new JLabel();
        panel3 = new JPanel();
        label5 = new JLabel();
        textField2 = new JTextField();
        label8 = new JLabel();
        label6 = new JLabel();
        passwordField2 = new JPasswordField();
        label7 = new JLabel();
        passwordField3 = new JPasswordField();

        //======== this ========
        setTitle("Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 421));
        setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        setIconImage(new ImageIcon(getClass().getResource("/images/log.png")).getImage());
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax
            . swing. border. EmptyBorder( 0, 0, 0, 0) , "图书管理系统", javax. swing
            . border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .
            Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), Color. black
            ) ,dialogPane. getBorder( )) ); dialogPane. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override
            public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName (
            ) )) throw new RuntimeException( ); }} );
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //======== panel1 ========
                {
                    panel1.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.30000000000000004, 0.0, 0.4, 0.30000000000000004};

                    //---- label1 ----
                    label1.setText("\u7528\u6237\u540d\uff1a");
                    label1.setHorizontalAlignment(SwingConstants.RIGHT);
                    label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 7f));
                    panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- textField1 ----
                    textField1.setFont(textField1.getFont().deriveFont(textField1.getFont().getSize() + 7f));
                    textField1.setBackground(new Color(214, 227, 213, 46));
                    panel1.add(textField1, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 1, 1));

                    //---- label3 ----
                    label3.setText("              ");
                    panel1.add(label3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- label2 ----
                    label2.setText("\u5bc6\u7801\uff1a");
                    label2.setHorizontalAlignment(SwingConstants.RIGHT);
                    label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 7f));
                    panel1.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- passwordField1 ----
                    passwordField1.setFont(passwordField1.getFont().deriveFont(passwordField1.getFont().getSize() + 7f));
                    passwordField1.setBackground(new Color(214, 217, 223, 46));
                    panel1.add(passwordField1, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 1, 1));

                    //---- label10 ----
                    label10.setText("\u9009\u62e9\u767b\u5f55\u8eab\u4efd\uff1a");
                    label10.setHorizontalAlignment(SwingConstants.RIGHT);
                    label10.setFont(label10.getFont().deriveFont(label10.getFont().getSize() + 7f));
                    panel1.add(label10, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //---- comboBox1 ----
                    comboBox1.setFont(comboBox1.getFont().deriveFont(comboBox1.getFont().getSize() + 6f));
                    panel1.add(comboBox1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));
                }
                contentPanel.add(panel1, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("\u767b\u5f55");
                okButton.setFont(okButton.getFont().deriveFont(okButton.getFont().getSize() + 5f));
                okButton.setIcon(new ImageIcon(getClass().getResource("/images/confirm.png")));
                okButton.addActionListener(e -> signInButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("\u6ce8\u518c");
                cancelButton.setFont(cancelButton.getFont().deriveFont(cancelButton.getFont().getSize() + 5f));
                cancelButton.setIcon(new ImageIcon(getClass().getResource("/images/sign.png")));
                cancelButton.addActionListener(e -> signUpButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- label9 ----
            label9.setText("\u56fe\u4e66\u7ba1\u7406\u7cfb\u7edf\u767b\u5f55");
            label9.setHorizontalAlignment(SwingConstants.CENTER);
            label9.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
            dialogPane.add(label9, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //======== dialog1 ========
        {
            dialog1.setTitle("\u6ce8\u518c");
            dialog1.setModal(true);
            dialog1.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog1.setMinimumSize(new Dimension(400, 300));
            dialog1.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
            Container dialog1ContentPane = dialog1.getContentPane();
            dialog1ContentPane.setLayout(new BorderLayout());

            //======== panel2 ========
            {
                panel2.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (
                new javax. swing. border. EmptyBorder( 0, 0, 0, 0) , "图书管理系统"
                , javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM
                , new java .awt .Font ("D\u0069al\u006fg" ,java .awt .Font .BOLD ,12 )
                , Color. black) ,panel2. getBorder( )) ); panel2. addPropertyChangeListener (
                new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
                ) {if ("\u0062or\u0064er" .equals (e .getPropertyName () )) throw new RuntimeException( )
                ; }} );
                panel2.setLayout(new FlowLayout());

                //---- button1 ----
                button1.setText("\u786e\u8ba4\u6ce8\u518c");
                button1.setFont(button1.getFont().deriveFont(button1.getFont().getSize() + 5f));
                button1.setIcon(new ImageIcon(getClass().getResource("/images/confirm.png")));
                button1.addActionListener(e -> confirmSignUpActionPerformed(e));
                panel2.add(button1);
            }
            dialog1ContentPane.add(panel2, BorderLayout.SOUTH);

            //---- label4 ----
            label4.setText("\u7528\u6237\u6ce8\u518c");
            label4.setHorizontalAlignment(SwingConstants.CENTER);
            label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 9f));
            dialog1ContentPane.add(label4, BorderLayout.NORTH);

            //======== panel3 ========
            {
                panel3.setLayout(new GridBagLayout());
                ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {0.19999999999999998, 0.5, 0.30000000000000004};

                //---- label5 ----
                label5.setText(" \u7528\u6237\u540d\uff1a");
                label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 7f));
                panel3.add(label5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- textField2 ----
                textField2.setFont(textField2.getFont().deriveFont(textField2.getFont().getSize() + 7f));
                textField2.setBackground(new Color(214, 217, 223, 46));
                textField2.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                panel3.add(textField2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- label8 ----
                label8.setText("    ");
                panel3.add(label8, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- label6 ----
                label6.setText("\u5bc6\u7801\uff1a");
                label6.setFont(label6.getFont().deriveFont(label6.getFont().getSize() + 7f));
                panel3.add(label6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- passwordField2 ----
                passwordField2.setFont(passwordField2.getFont().deriveFont(passwordField2.getFont().getSize() + 7f));
                passwordField2.setBackground(new Color(214, 217, 223, 46));
                panel3.add(passwordField2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- label7 ----
                label7.setText("\u786e\u8ba4\u5bc6\u7801\uff1a");
                label7.setFont(label7.getFont().deriveFont(label7.getFont().getSize() + 7f));
                panel3.add(label7, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- passwordField3 ----
                passwordField3.setFont(passwordField3.getFont().deriveFont(passwordField3.getFont().getSize() + 7f));
                passwordField3.setBackground(new Color(214, 217, 223, 46));
                panel3.add(passwordField3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialog1ContentPane.add(panel3, BorderLayout.CENTER);
            dialog1.pack();
            dialog1.setLocationRelativeTo(dialog1.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Xie yusheng
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel panel1;
    private JLabel label1;
    private JTextField textField1;
    private JLabel label3;
    private JLabel label2;
    private JPasswordField passwordField1;
    private JLabel label10;
    private JComboBox<String> comboBox1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel label9;
    private JDialog dialog1;
    private JPanel panel2;
    private JButton button1;
    private JLabel label4;
    private JPanel panel3;
    private JLabel label5;
    private JTextField textField2;
    private JLabel label8;
    private JLabel label6;
    private JPasswordField passwordField2;
    private JLabel label7;
    private JPasswordField passwordField3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
