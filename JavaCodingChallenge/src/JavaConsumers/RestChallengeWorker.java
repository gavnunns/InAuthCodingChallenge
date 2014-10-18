package JavaConsumers;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONObject;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Gavin Nunns
 * Date: 10/16/14
 * Time: 8:43 PM
 */
public class RestChallengeWorker {
    private final ArrayList<LocationTuple> cityList;

    public RestChallengeWorker() {

        cityList = new ArrayList<LocationTuple>();
        JSONObject locObject = this.getLatLangFromCityAndCountry("Tokyo,Japan");
        cityList.add(new LocationTuple("Tokyo", locObject.get("lat"), locObject.get("lng")));

        locObject = this.getLatLangFromCityAndCountry("Sydney,Australia");
        cityList.add(new LocationTuple("Sydney", locObject.get("lat"), locObject.get("lng")));

        locObject = this.getLatLangFromCityAndCountry("Riyadh,Saudi%20Arabia");
        cityList.add(new LocationTuple("Riyadh", locObject.get("lat"), locObject.get("lng")));

        locObject = this.getLatLangFromCityAndCountry("Zurich,Switzerland");
        cityList.add(new LocationTuple("Zurich", locObject.get("lat"), locObject.get("lng")));

        locObject = this.getLatLangFromCityAndCountry("Reykjavik,Iceland");
        cityList.add(new LocationTuple("Reykjavik", locObject.get("lat"), locObject.get("lng")));

        locObject = this.getLatLangFromCityAndCountry("Mexico%20City,Mexico");
        cityList.add(new LocationTuple("Mexico", locObject.get("lat"), locObject.get("lng")));

        locObject = this.getLatLangFromCityAndCountry("Lima,Peru");
        cityList.add(new LocationTuple("Lima", locObject.get("lat"), locObject.get("lng")));
    }

    public String post(String url, double latitude, double longitude) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String sResponse = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            buildPostVariables(latitude, longitude, httpPost);
            HttpResponse response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                sResponse = EntityUtils.toString(entity);
                System.out.println(sResponse);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return sResponse;
    }

    private void buildPostVariables(double latitude, double longitude, HttpPost httpPost) throws UnsupportedEncodingException {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    }

    public String get(String url, int port, String encoding, String path) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String response = "";
        try {
            HttpHost target = new HttpHost(url, port, encoding);
            HttpGet getRequest = new HttpGet(path);
            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();

//            System.out.println("----------------------------------------");
//            System.out.println(httpResponse.getStatusLine());
//            Header[] headers = httpResponse.getAllHeaders();
//            for (int i = 0; i < headers.length; i++) {
//                System.out.println(headers[i]);
//            }
//            System.out.println("----------------------------------------");

            if (entity != null) {
                response = EntityUtils.toString(entity);
            }
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

    public JSONObject getLatLangFromCityAndCountry(String cityCountry) {
        String response = get("www.mapquestapi.com", 80, "http", "/geocoding/v1/address?key=Fmjtd%7Cluurnuutn1%2C8a%3Do5-9wr0df&callback=renderOptions&inFormat=kvp&outFormat=json&location=" + cityCountry + "&thumbMaps=false");
        response = response.replace("renderOptions(", " ");
        response = response.replace(")", " ");
        JSONObject jsonObject = new JSONObject(response);
        JSONArray locArray = jsonObject.getJSONArray("results");
        JSONObject location = (JSONObject) locArray.get(0);
        locArray = location.getJSONArray("locations");
        location = (JSONObject) locArray.get(0);
        return (JSONObject) location.get("latLng");
    }

    public boolean isLocationInTheUS(String lat, String lng) {
        System.out.println("Latitude : " + lat + ", longitude :" + lng);
        //http://open.mapquestapi.com/geocoding/v1/reverse?key=Fmjtd%7Cluurnuutn1%2C8a%3Do5-9wr0df&callback=renderReverse&location=40.053116,-76.313603
        String response = get("www.mapquestapi.com", 80, "http", "/geocoding/v1/reverse?key=Fmjtd%7Cluurnuutn1%2C8a%3Do5-9wr0df&callback=renderReverse&location=" + lat + "," + lng);
        response = response.replace("renderReverse(", " ");
        response = response.replace(")", " ");
//        System.out.println(response);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray locArray = jsonObject.getJSONArray("results");
        JSONObject location = (JSONObject) locArray.get(0);
        locArray = location.getJSONArray("locations");
        location = (JSONObject) locArray.get(0);
        String country = location.get("adminArea1").toString();
        if (country.equals("US")) return true;
        return false;
    }

    // ended up not using this method as having the city location calculation in here just increases the number of calls to mapquest api
    public double distanceFromCity(double latitude, double longitude, String cityCountryName) {
        JSONObject cityLocation = getLatLangFromCityAndCountry(cityCountryName);
        double cityLat = Double.parseDouble(cityLocation.get("lat").toString());
        double cityLng = Double.parseDouble(cityLocation.get("lng").toString());

        return distanceOfTwoGeoPoints(latitude, longitude, cityLat, cityLng);
    }

    public double distanceOfTwoGeoPoints(double latitude, double longitude, double latitude2, double longitude2) {
        GeodeticCalculator geoCalc = new GeodeticCalculator();
        Ellipsoid reference = Ellipsoid.WGS84;

        GlobalPosition pointA = new GlobalPosition(latitude, longitude, 0.0); // Point A

        GlobalPosition pointB = new GlobalPosition(latitude2, longitude2, 0.0); // Point B

        double distance = geoCalc.calculateGeodeticCurve(reference, pointB, pointA).getEllipsoidalDistance();
        return distance;
    }

    public List getCitylistLocations() {
        return cityList;
    }

    private class LocationTuple {
        String city;
        double latitude = 0.0;
        double longitude = 0.0;

        public LocationTuple(String cityName, Object lat, Object lng) {
            city = cityName;
            latitude = Double.parseDouble(lat.toString());
            longitude = Double.parseDouble(lng.toString());
        }
    }

    public static void main(String[] args) {
        RestChallengeWorker restChallengeWorker = new RestChallengeWorker();
        String response = restChallengeWorker.get("localhost", 8080, "http", "/InAuthCodingChallange/getAllDataSets");
        JSONArray jsonArray = new JSONArray(response);

        File file = new File("results.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                StringBuilder row = new StringBuilder();
                JSONObject locObject = (JSONObject) jsonArray.get(i);
                String latitude = locObject.get("latitude").toString();
                String longitude = locObject.get("longitude").toString();

                row.append(latitude).append(",").append(longitude).append(",");
                boolean inUS = false;
                if ((Double.parseDouble(longitude) > -124.626080)&&(Double.parseDouble(longitude) < -62.361014)&&
                        (Double.parseDouble(latitude) > 18.005611)&&(Double.parseDouble(latitude) < 48.987386)) {
                    if (restChallengeWorker.isLocationInTheUS(latitude, longitude)) {
                        System.out.println(locObject.toString() + " --IN the US Woot woot--");
                        row.append("yes").append(",");
                        row.append("N/A");
                        inUS = true;
                    }
                }
                if (!inUS) {
                    row.append("no").append(",");
                    List<LocationTuple> cityListlocations = restChallengeWorker.getCitylistLocations();
                    String nearCityInfo = "Not Near a selected city";
                    for (int j = 0; j < cityListlocations.size(); j++) {
                        LocationTuple cityLocation = cityListlocations.get(j);
                        double distanceInMeters = restChallengeWorker.distanceOfTwoGeoPoints(Double.parseDouble(latitude),
                                Double.parseDouble(longitude),
                                cityLocation.latitude, cityLocation.longitude);
                        if (distanceInMeters < 804672) {
                            nearCityInfo = distanceInMeters / 1000 + "km from - " + cityLocation.city;
                            System.out.println("Location is : " + distanceInMeters / 1000 + "km from - " + cityLocation.city);
                        }
                    }
                    row.append(nearCityInfo);
                }
                row.append("\r");
                byte[] contentInBytes = row.toString().getBytes();
                fileOutputStream.write(contentInBytes);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
