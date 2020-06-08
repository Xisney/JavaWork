package LMS;

import javax.swing.*;
import java.awt.*;

public class Welcome extends JWindow {

    public Welcome(){
        JLabel pic = new JLabel(new ImageIcon("src/images/welcome.jpg"));
        JLabel info = new JLabel("加载中...",SwingConstants.CENTER);
        info.setFont(new Font("Serif",Font.PLAIN,24));
        getContentPane().add(info,BorderLayout.NORTH);
        getContentPane().add(pic,BorderLayout.CENTER);
        setSize(600,421);
        setLocationRelativeTo(null);
    }

}
