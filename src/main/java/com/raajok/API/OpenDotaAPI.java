package com.raajok.API;

import com.raajok.API.OpenDota.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class OpenDotaAPI {

    static final private String BASE_URL = "https://api.opendota.com/api";

    /**
     * Search a player with a name. Returns a list of most likely Players.
     * @param name The player name
     * @return List of Players
     */
    static public List<Player> searchPlayer(String name) {
        // No spaces allowed in URL -> convert to %20. Make the name lowercase as well
        name = name.toLowerCase();
        name = name.replaceAll("\\s+", "%20");

        HttpResponse<String> res = OpenDotaAPI.callAPI("/search?q=" + name);

        // Search returns a list of JSON objects. Iterate over the list.
        JSONObject jsonObject = new JSONObject("{\"array\":" + res.body() + "}"); // Quick hack: res.body() returns a List [ ], so add { } around it to make a JSON object.
        JSONArray json = jsonObject.getJSONArray("array");
        List<Player> playerList = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject player = json.getJSONObject(i);
            int accountId = player.getInt("account_id");
            String avatarfull = player.getString("avatarfull");
            String personaname = player.getString("personaname");
            Number similarity = player.getNumber("similarity");

            // Last match time is not always present in the response.
            String lastMatchTime;
            try {
                 lastMatchTime = player.getString("last_match_time");
            } catch (JSONException e) {
                lastMatchTime = null;
            }

            playerList.add(new Player(accountId, avatarfull, personaname, lastMatchTime, similarity));
        }

        return playerList;

    }

    /**
     * Calls the OpenDota API with the given commandURL as the ending of the URL and returns the HttpResponse.
     * @param commandURL The part after API's Base URL (with parameters)
     * @return response
     */
    static private HttpResponse<String> callAPI(String commandURL) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + commandURL))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return null;
        }
    }
}
