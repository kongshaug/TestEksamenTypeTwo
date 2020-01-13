/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTO.JokeDTO;
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

    private JokeDTO getJokeData(String categori) throws MalformedURLException, IOException, Exception {
       try {
            URL siteURL = new URL("https://api.chucknorris.io/jokes/random?category="+ categori);
            HttpURLConnection connection = (HttpURLConnection) 
	    siteURL.openConnection();
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
           
                    JokeDTO joke = GSON.fromJson(response, JokeDTO.class);
                    return joke;
               
            }
        } catch (Exception e) {
            throw new Exception("API request went wrong:" + e.getMessage());
        }
    }

    public List<JokeDTO> getData(String[] categories) throws InterruptedException, ExecutionException, IOException, Exception {
        List<JokeDTO> jokes = new ArrayList<>();
        Queue<Future<JokeDTO>> queue = new ArrayBlockingQueue(5); // MAX QUEUE !!!!
      
        for (int i = 0; i < categories.length; i++) {
            int j = i;
            Future<JokeDTO> future = executor.submit(() -> {
                
                JokeDTO joke = getJokeData(categories[j]);
                return joke;
            });

            queue.add(future);
        }
        while (!queue.isEmpty()) {
            Future<JokeDTO> joke = queue.poll();
            if (joke.isDone()) {
                JokeDTO j = joke.get();
                 jokes.add(j);
            } else {
                queue.add(joke);
            }
        }

        return jokes;
    }
    
    
    
  

}

