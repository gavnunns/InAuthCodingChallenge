package generator;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Gavin Nunns
 * Date: 10/15/14
 * Time: 6:27 PM
 */
public class LocationGenerator {
    private final int maxLong = 180;
    private final int minLong = -180;
    private final int maxLat = 90;
    private final int minLat = -90;

    public Double generateLongitude(int decimalPlaces) {
        Double result = Double.parseDouble(generateRandomNumberWithDecimalBetween(decimalPlaces, minLong, maxLong));
        if (result > maxLong) result = (double) maxLong;
        if (result < minLong) result = (double)minLong;
        return result;
    }

    private String generateRandomNumberWithDecimalBetween(int decimalPlaces, int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum + getDecimalPlaces(decimalPlaces, rand);
    }

    private String getDecimalPlaces(int decimalPlaces, Random rand) {
        String decimal = ".";
        for (int i = 0; i < decimalPlaces; i++) {
            int randomInt = rand.nextInt(10);
            decimal = decimal + randomInt;
        }
        return decimal;
    }

    public Double generateLatitude(int decimalPlaces) {
        Double result = Double.parseDouble(generateRandomNumberWithDecimalBetween(decimalPlaces, minLat, maxLat));
        if (result > maxLat) result = (double) maxLat;
        if (result < minLat) result = (double) minLat;
        return result;
    }
}
