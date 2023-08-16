package skyclash.skyclash.cooldowns.utils;

import java.text.DecimalFormat;

public class UtilMath {
    public static double trim(double untrimmeded, int decimal) {
        StringBuilder format = new StringBuilder("#.#");

        for(int i = 1; i < decimal; i++) {
            format.append("#");
        }
        DecimalFormat twoDec = new DecimalFormat(format.toString());
        return Double.parseDouble(twoDec.format(untrimmeded));
    }
}
