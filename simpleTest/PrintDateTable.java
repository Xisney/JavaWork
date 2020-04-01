import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @version 1.0 2020/4/1
 * @author ys copy from Cay Horstmann
 * function：使用Java时间库，打印一个日历表。
 */

public class Work {
    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int today = date.getDayOfMonth();

        date = date.minusDays(today - 1);  //向前跳today-1天
        DayOfWeek weekday = date.getDayOfWeek();
        int value = weekday.getValue();// 得到星期代表的数字，1-周一，7-周天

        System.out.println("Mon Tue Wed Thu Fri Sat Sun");
        for (int i = 0; i < value - 1; i++) {
            System.out.print("    ");
        }

        while (date.getMonthValue() == month) {
            System.out.printf("%3d", date.getDayOfMonth());
            if (date.getDayOfMonth() == today)
                System.out.print("*");
            else System.out.print(" ");
            date = date.plusDays(1);
            if (date.getDayOfWeek().getValue() == 1)
                System.out.println();
        }

    }
}

