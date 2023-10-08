package com.raajok.api;

import com.raajok.api.OpenDota.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            playerList.add(new Player(accountId, avatarfull, personaname, lastMatchTime));
        }

        return playerList;
    }

    /**
     * Search a player with a Steam ID.
     * @param id steam ID
     * @return Player
     */
    static public Player searchPlayer(int id) {
        HttpResponse<String> res = OpenDotaAPI.callAPI("/players/" + id);

        JSONObject jsonBody = new JSONObject(res.body());
        JSONObject jsonProfile = jsonBody.getJSONObject("profile");

        String name = jsonProfile.getString("personaname");
        String avatarFull = jsonProfile.getString("avatarfull");

        return new Player(id, avatarFull, name, null);
    }

    /**
     * Returns wins and losses in a Map. Calculates from most recent matches. Can be limited to a certain number of
     * games with the limit parameter.
     * wins -> (int), losses -> (int)
     * @param account_id Steam ID
     * @param limit How many matches to take into account
     * @return
     */
    static public Map<String, Integer> wl(int account_id, int limit) {
        String queryParams = "";
        if (limit > 0) {
            queryParams = "?limit=" + limit;
        }
        HttpResponse<String> res = OpenDotaAPI.callAPI("/players/" + account_id + "/wl" + queryParams);

        JSONObject jsonObject = new JSONObject(res.body());

        Map<String, Integer> wl = new HashMap<>();
        wl.put("wins", jsonObject.getInt("win"));
        wl.put("losses", jsonObject.getInt("lose"));

        return wl;
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
