package generator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Gavin Nunns
 * Date: 10/15/14
 * Time: 6:35 PM
 */
public class LocationGeneratorTest {
    private LocationGenerator target;

    @Before
    public void init() {
        target = new LocationGenerator();
    }

    @Test
    public void testGenerateValidLongitudeValueAccurateDownToOneMeter() {
        double longitude = target.generateLongitude(5);
        String text = Double.toString(Math.abs(longitude));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        assertTrue(longitude >= -180);
        assertTrue(longitude <= 180);
        assertTrue(decimalPlaces <= 5);
    }

    @Test
    public void testGenerateValidLongitudeValueAccurateDownTo11k() {
        double longitude = target.generateLongitude(1);
        String text = Double.toString(Math.abs(longitude));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        assertTrue(longitude >= -180);
        assertTrue(longitude <= 180);
        assertTrue(decimalPlaces <= 1);
    }

    @Test
    public void testGenerateManyLongitudeValues() {
        int i = 0;
        while (i < 100000) {
            double longitude = target.generateLongitude(5);
            String text = Double.toString(Math.abs(longitude));
            int integerPlaces = text.indexOf('.');
            int decimalPlaces = text.length() - integerPlaces - 1;
            assertTrue("The value generated was :" + longitude + " but should have been 0-180", longitude >= -180);
            assertTrue("The value generated was :" + longitude + " but should have been 0-180", longitude <= 180);
            assertTrue("The value generated was :" + longitude + " but should have 5 decimal places", decimalPlaces <= 5);
            i++;
        }
    }

    @Test
    public void testGenerateValidLatitudeDownToOneMeter() {
        double latitude = target.generateLatitude(5);
        String text = Double.toString(Math.abs(latitude));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        assertTrue(latitude >= -90);
        assertTrue(latitude <= 90);
        assertTrue(decimalPlaces <= 5);
    }

    @Test
    public void testGenerateManyLatitudeValues() {
        int i = 0;
        while (i < 100000) {
            double latitude = target.generateLatitude(5);
            String text = Double.toString(Math.abs(latitude));
            int integerPlaces = text.indexOf('.');
            int decimalPlaces = text.length() - integerPlaces - 1;
            assertTrue("The value generated was :" + latitude + " but should have been 0-180", latitude >= -90);
            assertTrue("The value generated was :" + latitude + " but should have been 0-180", latitude <= 90);
            assertTrue("The value generated was :" + latitude + " but should have 5 decimal places", decimalPlaces <= 5);
            i++;
        }
    }
}
