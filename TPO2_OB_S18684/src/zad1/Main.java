/**
 *
 *  @author Opiński Bartłomiej S18684
 *
 */

package zad1;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {
  public static void main(String[] args) throws IOException {
    Service s = new Service("United States");
    String weatherJson = s.getWeather("Berlin");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();

    System.out.println(rate2);
    System.out.println(rate1
    );
    // ...
    // część uruchamiająca GUI
    new CombinedGUI(s);
  }
}
