package ai.houzi.xiao.utils;

/**
 * 格式化类
 */
public class Format {

    public static int[] getUserLv(int num) {
        return new int[]{num / 64, num % 64 / 16, num % 64 % 16 / 4, num % 64 % 16 % 4};
    }
}
