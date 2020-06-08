package LMS;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class BothEvent {

    // 对table进行更新显示
    public static void refreshTable(ResultSet set, JTable table) {
        TableModel model = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                try {
                    set.last();
                    int rowNum = set.getRow();
                    set.beforeFirst();
                    return rowNum;
                } catch (SQLException e1) {
                    System.out.println("refreshTable1 model " + e1.getMessage());
                }
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 6;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                try {
                    set.absolute(rowIndex + 1);
                    return set.getObject(columnIndex + 1);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
                return null;
            }

            @Override
            public String getColumnName(int column) {
                String[] names = {"编号", "书名", "作者", "出版社", "类别", "状态"};
                return names[column];
            }

        };
        table.setModel(model);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Serif",Font.PLAIN,24));
    }

    public static void refreshTable2(ResultSet set, JTable table) {
        TableModel model = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                try {
                    set.last();
                    int rowNum = set.getRow();
                    set.beforeFirst();
                    return rowNum;
                } catch (SQLException e1) {
                    System.out.println("refreshTable1 model " + e1.getMessage());
                }
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 6;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                try {
                    set.absolute(rowIndex + 1);
                    return set.getObject(columnIndex + 1);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
                return null;
            }

            @Override
            public String getColumnName(int column) {
                String[] names = {"编号", "书名", "作者", "借书时间", "当前状态", "还书时间"};
                return names[column];
            }

        };
        table.setModel(model);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Serif",Font.PLAIN,24));
    }

    public static void refreshTableWish(ResultSet set, JTable table) {
        TableModel model = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                try {
                    set.last();
                    int rowNum = set.getRow();
                    set.beforeFirst();
                    return rowNum;
                } catch (SQLException e1) {
                    System.out.println("refreshTable1 model " + e1.getMessage());
                }
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                try {
                    set.absolute(rowIndex + 1);
                    return set.getObject(columnIndex + 1);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
                return null;
            }

            @Override
            public String getColumnName(int column) {
                String[] names = {"提交用户", "书名", "作者", "出版社"};
                return names[column];
            }

        };
        table.setModel(model);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Serif",Font.PLAIN,24));
    }

    public static void refreshTableManager(ResultSet set,JTable table){
        TableModel model = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                try {
                    set.last();
                    int rowNum = set.getRow();
                    set.beforeFirst();
                    return rowNum;
                } catch (SQLException e1) {
                    System.out.println("refreshTable1 model " + e1.getMessage());
                }
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                try {
                    set.absolute(rowIndex + 1);
                    return set.getObject(columnIndex + 1);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
                return null;
            }

            @Override
            public String getColumnName(int column) {
                String[] names = {"用户名", "注册时间", "账户状态"};
                return names[column];
            }

        };
        table.setModel(model);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Serif",Font.PLAIN,24));
    }

    // 将字符串时间戳转换为yyyy-mm-dd格式的字符串
    public static String converse(String s) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date temp = new Date(Long.parseLong(s) * 1000);
        return dateFormat.format(temp);
    }
}
