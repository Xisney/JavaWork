import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        double cs, cx;
        Scanner in = new Scanner(System.in);
        Complex c1, c2;

        System.out.println("input c1:");
        cs = in.nextDouble();
        cx = in.nextDouble();
        c1 = new Complex(cs, cx);

        System.out.println("input c2:");
        cs = in.nextDouble();
        cx = in.nextDouble();
        c2 = new Complex(cs, cx);

        System.out.printf("ComplexNumber a: %.1f + %.1fi\n",
                c1.getS(), c1.getX());
        System.out.printf("ComplexNumber b: %.1f + %.1fi\n",
                c2.getS(), c2.getX());

        // 开始计算并打印
        double[] result;
        result = Complex.add(c1, c2);
        System.out.printf("(a + b) = %.1f + %.1fi\n", result[0], result[1]);

        result = Complex.subtraction(c1, c2);
        System.out.printf("(a - b) = %.1f + %.1fi\n", result[0], result[1]);

        result = Complex.multiply(c1, c2);
        System.out.printf("(a * b) = %.1f + %.1fi\n", result[0], result[1]);
    }
}

class Complex {
    private double s, x;

    Complex() {

    }

    Complex(double s, double x) {
        this.s = s;
        this.x = x;
    }

    static double[] add(Complex c1, Complex c2) {
        double[] r = new double[2];
        r[0] = c1.s + c2.s;
        r[1] = c1.x + c2.x;
        return r;
    }

    static double[] subtraction(Complex c1, Complex c2) {
        double[] r = new double[2];
        r[0] = c1.s - c2.s;
        r[1] = c1.x - c2.x;
        return r;
    }

    static double[] multiply(Complex c1, Complex c2) {
        double[] r = new double[2];
        r[0] = c1.s * c2.s - c1.x * c2.x;
        r[1] = c1.s * c2.x + c1.x * c2.s;
        return r;
    }

    public double getS() {
        return s;
    }

    public double getX() {
        return x;
    }
}

