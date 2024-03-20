package zad1;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

public class CombinedGUI {
    Service s;
    JFrame frame;
    JPanel wikiPanel;
    JPanel dataPanel;
    JTextField cityTextField;
    JTextField currencyTextField;

    public CombinedGUI(Service s) throws IOException {
        this.s = s;

        frame = new JFrame("Weather and Wikipedia Data");
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setSize(800, 600);

        // Initialize Wiki panel
        initWikiPanel();

        // Initialize Data panel
        initDataPanel();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    private void initWikiPanel() {
        wikiPanel = new JPanel();
        wikiPanel.setLayout(new BoxLayout(wikiPanel, BoxLayout.PAGE_AXIS));
        JFXPanel jFXPanel = new JFXPanel();
        Platform.runLater(() -> {
            WebView browserView = new WebView();
            WebEngine engine = browserView.getEngine();
            engine.load("https://en.wikipedia.org/wiki/" + s.city);
            Pane root = new FlowPane();
            root.getChildren().addAll(browserView);
            root.autosize();
            jFXPanel.setScene(new Scene(root));
        });
        wikiPanel.add(jFXPanel);
        frame.add(wikiPanel);
    }

    private void initDataPanel() {
        dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.PAGE_AXIS));
        updateWeatherLabel();
        updateCurrencyLabels();

        // City and Currency input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));

        // City input field
        inputPanel.add(new JLabel("Enter City: "));
        cityTextField = new JTextField(15);
        inputPanel.add(cityTextField);

        // Currency input field
        inputPanel.add(new JLabel("Enter Currency: "));
        currencyTextField = new JTextField(3);
        inputPanel.add(currencyTextField);

        dataPanel.add(inputPanel);

        // Update button
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newCity = cityTextField.getText();
                String newCurrency = currencyTextField.getText();
                try {
                    updateData(newCity, newCurrency);
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        dataPanel.add(updateButton);

        frame.add(dataPanel);
    }

    private void updateWeatherLabel() {
        try {
            String weatherInfo = s.getWeather(s.city);//s.getWeather(s.city)
            //String jsonString = s.getWeather(s.city);
            //JSONObject jsonObject = new JSONObject(jsonString);
            //weatherInfo = jsonObject.getString("humidity"); //jsonObject.getString("humidity");
            //System.out.println(jsonObject);
            JLabel weatherLabel = new JLabel("Weather at " + s.city + ": " + weatherInfo);
            dataPanel.add(weatherLabel, 0); // Add at the beginning
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCurrencyLabels() {
        try {
            JLabel currencyLabel1 = new JLabel("Rate for " + s.currencyToRate + " in " + s.currencyCode + " is " + s.getRateFor(s.currencyToRate).toString());
            dataPanel.add(currencyLabel1, 1); // Add after the first label

            JLabel currencyLabel2 = new JLabel("Rate for " + s.currencyCode + " in PLN is " + s.getNBPRate().toString());
            dataPanel.add(currencyLabel2, 2); // Add after the second label
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData(String newCity, String newCurrency) throws MalformedURLException {
        // Update city and currency
        this.s.city = newCity;
        this.s.currencyToRate = newCurrency;

        // Reload the Wiki panel with updated city information
        frame.remove(wikiPanel);
        initWikiPanel();
        frame.revalidate();
        frame.repaint();

        // Reload the Data panel with updated currency information
        frame.remove(dataPanel);
        initDataPanel();
        frame.revalidate();
        frame.repaint();
    }
}

