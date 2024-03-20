/**
 *
 *  @author Opiński Bartłomiej S18684
 *
 */

package zad1;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;


public class Service {

    String city;
    String country;
    String currencyCode;
    String currencyToRate;

    public Service (String country) {
        this.country = country;
        this.currencyCode = this.getCode();
    }


    public String getWeather(String city) {

        this.city = city;

        Map<String, Double> latlon = getLatitudeAndLongitude();
        String apiKey = "697a0873b2332ae0e6db3c7b54b5e1e3";
        String latitude = latlon.get("lat").toString();
        String longitude = latlon.get("lon").toString();

        String baseUrl = "https://api.openweathermap.org/data/2.5/weather";
        String endpoint = "?lat="+latitude+"&lon="+longitude+"&appid=";
        String url = baseUrl+endpoint+apiKey;
        String response;

        try{
            response = readUrl(url);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public Map<String,Double> getLatitudeAndLongitude()
    {
        Map<String,Double> resultMap = new HashMap<>();
        double latitude1 = 1.0;
        double longitude1 = 1.0;
        String apiKey = "697a0873b2332ae0e6db3c7b54b5e1e3";
        String getGeoLoc = "http://api.openweathermap.org/geo/1.0/direct?q="+city+"&limit=1&appid="+apiKey;
        String response = "";
        try
        {
            response = readUrl(getGeoLoc);
        }catch(Exception e){
            e.printStackTrace();
        }

        if (!response.isEmpty())
        {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Access the value by key
                latitude1 = jsonObject.getDouble("lat");
                longitude1 = jsonObject.getDouble("lon");
            }
        } else {
            System.out.println("Response is empty.");
        }
        resultMap.put("lat",latitude1);
        resultMap.put("lon",longitude1);
        return resultMap;
    }


    public Double getRateFor(String currencyCode) throws MalformedURLException
    {
        currencyToRate = currencyCode;
        String response = "";
        String apiUrl = "https://v6.exchangerate-api.com/v6/af052832d0e06a9ca3165b47/latest/" + currencyCode;

        try
        {
            response = readUrl(apiUrl);
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
        JSONObject currency = (JSONObject) new JSONObject(response).get("conversion_rates");
        return currency.getDouble(this.currencyCode);
    }

    public Double getNBPRate()
    {
        String response = "";
        if(this.getCode().equals("PLN")) return 1.0;
        else
            try {
                String url = "http://api.nbp.pl/api/exchangerates/rates/a/" + this.currencyCode + "/?format=json";
                response = readUrl(url);
            }
            catch (IOException exc) {
                exc.printStackTrace();
            }

        JSONObject currency = (JSONObject) new JSONObject(response);
        JSONArray array = (JSONArray) currency.get("rates");
        JSONObject rates = (JSONObject) array.get(0);
        return rates.getDouble("mid");

    }

    public String getCode()
    {
        String countryCode;
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale outLocale = Locale.forLanguageTag("en_GB");
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(outLocale), iso);
        }
        countryCode = countries.get(this.country);
        String currencyCode = Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
        return currencyCode;
    }

    public String readUrl(String url) throws IOException
    {

        String response = "";
        InputStream istream = new URL(url).openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
        String line;

        while ((line = br.readLine()) != null) {
            response += line;
        }
        br.close();

        return response;
    }
}
