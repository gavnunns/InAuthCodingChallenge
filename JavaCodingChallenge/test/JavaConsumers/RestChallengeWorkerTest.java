package JavaConsumers;

import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Gavin Nunns
 * Date: 10/17/14
 * Time: 11:31 AM
 */
public class RestChallengeWorkerTest {
    private RestChallengeWorker target;

    @Before
    public void init() {
        target = new RestChallengeWorker();
    }

    @Test
    public void testGetAllDataSetsAssuming10kOrMoreEntries() {
        String response = target.get("localhost", 8080, "http", "/InAuthCodingChallange/getAllDataSets");
        assertTrue(response!="");
        JSONArray jsonArray = new JSONArray(response);
        assertTrue("should have at least 10k data points", jsonArray.size() >= 10000);
    }


    //Note this is a BAD test and used as a quick and dirty check for this exersize, the test will fail if
    //location is added that already exists + I am doing nothing to validate the entry is a real lat/long value
    @Test
    public void testPostAddsANewDataPoint() {
        String response = target.get("localhost", 8080, "http", "/InAuthCodingChallange/getAllDataSets");
        assertTrue(response!="");
        JSONArray jsonArray = new JSONArray(response);
        assertTrue("should have at least 10k data points", jsonArray.size() >= 10000);
        int size = jsonArray.size();
        Random rand = new Random();
        // randomly generating invalid locations so a new value is added to the DB
        int randInt1 = rand.nextInt(1000)+180;
        int randInt2 = rand.nextInt(1000)+180;
        response = target.post("http://localhost:8080/InAuthCodingChallange/addData", randInt1, randInt2);
        JSONObject jsonObject = new JSONObject(response);
        assertTrue("returns json location object with three values id, long, lang", jsonObject.size() == 3);
        response = target.get("localhost", 8080, "http", "/InAuthCodingChallange/getAllDataSets");
        jsonArray = new JSONArray(response);
        int newSize = jsonArray.size();
        assertEquals(size+1, newSize);
    }

    @Test
    public void testGetDataWithLatLangReturnsJsonObj() {
        double latitude = 36.94084;
        double longitude = 134.73237;

        String response = target.post("http://localhost:8080/InAuthCodingChallange/addData", latitude, longitude);
        JSONObject jsonObject = new JSONObject(response);
        assertTrue("returns json location object with three values id, long, lang", jsonObject.size() == 3);

        response = target.get("localhost", 8080, "http", "/InAuthCodingChallange/getData/"+latitude+"/"+longitude);
        assertTrue(response!="");
        jsonObject = new JSONObject(response);
        assertTrue("returns json location object with three values id, long, lang", jsonObject.size() == 3);
        assertEquals(latitude, jsonObject.get("latitude"));
        assertEquals(longitude, jsonObject.get("longitude"));
    }

    @Test
    public void testGetLatlangOfCity() {
        JSONObject locInfo = target.getLatLangFromCityAndCountry("Tokyo,Japan");
        assertTrue(locInfo.get("lng") != "139.768522");
        assertTrue(locInfo.get("lat") != "35.680071");
    }

    @Test
    public void testIsLocationInTheUnitedStates() {
        JSONObject tokyoLoc = target.getLatLangFromCityAndCountry("Tokyo,Japan");
        JSONObject potLoc = target.getLatLangFromCityAndCountry("Pottsville,PA");
        assertTrue(!target.isLocationInTheUS(tokyoLoc.get("lat").toString(), tokyoLoc.get("lng").toString()));
        assertTrue(target.isLocationInTheUS(potLoc.get("lat").toString(), potLoc.get("lng").toString()));
    }

    @Test
    public void testCalculateDistanceFromCityIsLessThan5Mile() {
        double latitude = 35.680307;
        double longitude = 139.678545;

        // point is about 8k from Tokyo
        double distance = target.distanceFromCity(latitude, longitude, "Tokyo,Japan");
        System.out.println("The distance is :" + distance);

        // result is in meters so I just converted 500 miles to meters
        assertTrue(distance < 804672);
        latitude = 19.432608;
        longitude = -99.133208;
        distance = target.distanceFromCity(latitude, longitude, "Tokyo,Japan");
        System.out.println("The distance is :" + distance);
        System.out.println(distance/1000);

        // result is in meters so I just converted 500 miles to meters
        assertTrue(distance > 804672);
    }

}
