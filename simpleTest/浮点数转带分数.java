package homework;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String num = in.nextLine();

        int te = num.indexOf(".");
        String[] k = num.split("\\.");
        int numOne = Integer.parseInt(k[0]);
        int l = k[1].length();
        char[] f = k[1].toCharArray();

        if (f[0] == '0') {
            k[1] = k[1].replace("0", "");
        }

        int numZi = Integer.parseInt(k[1]);
        int numMu = (int) Math.pow(10, l);

        int g = gcd(numMu,numZi);

        System.out.printf("%d %d %d", numOne, numZi/g, numMu/g);
    }

    public static int gcd(int a, int b) {
        int t = Math.max(a, b);
        b = Math.min(a, b);
        a = t;

        int r = a % b;
        if (r == 0) {
            return b;
        }
        return gcd(b, r);
    }
}
