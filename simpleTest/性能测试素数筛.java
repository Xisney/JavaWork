package leecode;


import java.util.BitSet;

public class Work {
    public static void main(String[] args) {
        int n = 2000000;  // 设定范围
        long start = System.currentTimeMillis();  // 设置起始时间
        BitSet b = new BitSet(n + 1);  // 初始化位集
        int count = 0;
        int i = 0;
        for (i = 2; i < n; i++) {
            b.set(i);
        }
        i = 2;
        while (i * i <= n) {  //避免i过大而产生冗余计算
            if (b.get(i)) {
                count++;
                int k = 2 * i;  //排除第k位
                while (k <= n) {
                    b.clear(k);
                    k += i;
                }
            }
            i++;
        }

        while (i<=n){
            if (b.get(i)) count++;
            i++;
        }
        long end =System.currentTimeMillis();
        System.out.println(count+" primes");
        System.out.println((end-start)+" ms");
    }
}

