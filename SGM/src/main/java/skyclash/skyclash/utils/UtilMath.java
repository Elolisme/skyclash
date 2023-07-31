package skyclash.skyclash.utils;

import java.text.DecimalFormat;

public class UtilMath {
    public static double trim(double untrimmeded, int decimal) {
        String format = "#.#";

        for(int i = 1; i < decimal; i++) {
            format = format + "#";
        }
        DecimalFormat twoDec = new DecimalFormat(format);
        return Double.parseDouble(twoDec.format(untrimmeded));
    }
}
