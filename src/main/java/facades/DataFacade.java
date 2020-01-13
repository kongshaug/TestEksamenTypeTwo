/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTO.Joke;
import DTO.JokeDTO;
import DTO.JokesDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import DTO.QuoteDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author benja
 */
public class DataFacade {

    private static ExecutorService executor = Executors.newFixedThreadPool(5);
    Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static DataFacade instance;

    private DataFacade() {
    }

    public static DataFacade getDataFacade() {
        if (instance == null) {

            instance = new DataFacade();
        }
        return instance;
    }

    private Joke getJokeData(String categori) throws MalformedURLException, IOException, Exception {
        try {
            URL siteURL = new URL("https://api.chucknorris.io/jokes/random?category=" + categori);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json;charset=UTF-8");
            connection.setRequestProperty("user-agent", "Application");

            try (Scanner scan = new Scanner(connection.getInputStream(), "UTF-8")) {
                String response = "";
                while (scan.hasNext()) {
                    response += scan.nextLine();
                }
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(response);
                Joke joke = GSON.fromJson(response, Joke.class);
                return joke;

            }
        } catch (Exception e) {
            throw new Exception("API request went wrong:" + e.getMessage());
        }
    }

    public JokesDTO getData(String[] categories) throws InterruptedException, ExecutionException, IOException, Exception {
        ArrayList<Joke> jokes = new ArrayList<>();
        Queue<Future<Joke>> queue = new ArrayBlockingQueue(12); // MAX QUEUE !!!!

        for (int i = 0; i < categories.length; i++) {
            int j = i;
            Future<Joke> future = executor.submit(() -> {

                Joke joke = getJokeData(categories[j]);
                return joke;
            });

            queue.add(future);
        }
        while (!queue.isEmpty()) {
            Future<Joke> joke = queue.poll();
            if (joke.isDone()) {
                Joke j = joke.get();
                jokes.add(j);
            } else {
                queue.add(joke);
            }
        }
        ArrayList<JokeDTO> DTOjokes = new ArrayList<JokeDTO>();
        for (Joke joke :  jokes) {
            DTOjokes.add(new JokeDTO(joke));
        }
        JokesDTO jokesDTO = new JokesDTO(DTOjokes);
        return jokesDTO;
    }

}
