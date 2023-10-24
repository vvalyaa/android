package com.company;

import com.google.gson.Gson;
import java.io.*;
import java.net.URL;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.GsonBuilder;

class WeatherResponse {
    private MainWeatherInfo main;
    private String name;

    public MainWeatherInfo getMain() {
        return main;
    }

    public String getName() {
        return name;
    }
}

class MainWeatherInfo {
    private double temp;

    public double getTemp() {
        return temp;
    }
}

public class Main {
    private static final String API_KEY = "83497f5f6f074662d073948a8ff4c66c";
    private static final String PROXY_HOST = "proxy.isu.ru";
    private static final int PROXY_PORT = 3128;

    public static void main(String[] args) {

        // Настройки прокси-сервера для сетевого соединения
        System.setProperty("https.proxyHost", PROXY_HOST);
        System.setProperty("https.proxyPort", String.valueOf(PROXY_PORT));

        List<String> cities = new ArrayList<>();
        cities.add("Moscow");
        cities.add("Saint Petersburg");
        cities.add("Novosibirsk");
        cities.add("Yekaterinburg");
        cities.add("Nizhny Novgorod");
        cities.add("Kazan");
        cities.add("Chelyabinsk");
        cities.add("Omsk");
        cities.add("Samara");
        cities.add("Rostov-on-Don");
        cities.add("Angarsk");

        try {
            List<WeatherResponse> weatherResponses = new ArrayList<>();
            for (String city : cities) {

                //"&units=metric&apiKey=" - указываем "метрическую" систему измерения темпераутуры или градусы Цельсия
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&apiKey=" + API_KEY);
                // Созадем соединение на основе URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Читаем ответ от сервера
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonStringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonStringBuilder.append(line);
                }
                reader.close();
                connection.disconnect();

                Gson gson = new GsonBuilder().create();
                //Десериализуем из json в объект WeatherResponse
                WeatherResponse weatherResponse = gson.fromJson(jsonStringBuilder.toString(), WeatherResponse.class);
                weatherResponses.add(weatherResponse);
            }

            URL weatherUrl = new URL("http://api.openweathermap.org/data/2.5/weather?q=Irkutsk&units=metric&apiKey=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) weatherUrl.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
            reader.close();
            connection.disconnect();

            Gson gson = new GsonBuilder().create();
            WeatherResponse irkutskWeatherResponse = gson.fromJson(jsonStringBuilder.toString(), WeatherResponse.class);
            weatherResponses.add(irkutskWeatherResponse);

            //Сортируем по температуре
            Collections.sort(weatherResponses, (weather1, weather2) -> {
                if (weather1.getMain().getTemp() > weather2.getMain().getTemp()) {
                    return -1;
                } else if (weather1.getMain().getTemp() < weather2.getMain().getTemp()) {
                    return 1;
                }
                return 0;
            });

            for (WeatherResponse weatherResponse : weatherResponses) {
                System.out.println(weatherResponse.getName() + ": " + (int) weatherResponse.getMain().getTemp() + "°C");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
