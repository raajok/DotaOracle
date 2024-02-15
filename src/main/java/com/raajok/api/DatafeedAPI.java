package com.raajok.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DatafeedAPI {

    static final private String BASE_URL = "https://www.dota2.com/datafeed";

    /**
     * Returns hero id, npc name and english name matching the name. Ignores case, whitespace and hyphens.
     * @param name Name of the hero
     * @return List with id in index 0, npc name in index 1 (can be used to get url to picture) and english name in index 2
     * @throws IllegalArgumentException
     */
    static public List<String> getIdAndNpcNameFromName(String name) throws IllegalArgumentException {
        HttpResponse<String> res = DatafeedAPI.callAPI("/herolist?language=english");
        JSONObject responseJson = new JSONObject(res.body());
        JSONArray heroes = responseJson.getJSONObject("result").getJSONObject("data").getJSONArray("heroes");

        // remove whitespace and -
        name = name.replaceAll("\\s+","");
        name = name.replaceAll("-", "");

        // loop through the array
        int id = 0;
        int i = 0;
        while (i < heroes.length()) {
            String heroName = heroes.getJSONObject(i).getString("name_english_loc");
            heroName = heroName.replaceAll("\\s+", "");
            heroName = heroName.replaceAll("-", "");
            if (name.equalsIgnoreCase(heroName)) {
                id = heroes.getJSONObject(i).getInt("id");
                break;
            }
            i++;
        }

        if (id != 0) {
            String npcName = heroes.getJSONObject(i).getString("name");
            String heroName = heroes.getJSONObject(i).getString("name_english_loc");

            ArrayList<String> idAndName = new ArrayList<>();
            idAndName.add(Integer.toString(id));
            idAndName.add(npcName);
            idAndName.add(heroName);

            return idAndName;
        } else {
            throw new IllegalArgumentException("Hero name doesn't exist.");
        }
    }

    /**
     * Returns hero id matching the name. Ignores case, whitespace and hyphens.
     * @param name Name of the hero
     * @return id
     * @throws IllegalArgumentException
     */
    static public int getIdFromName(String name) throws IllegalArgumentException {
        HttpResponse<String> res = DatafeedAPI.callAPI("/herolist?language=english");
        JSONObject responseJson = new JSONObject(res.body());
        JSONArray heroes = responseJson.getJSONObject("result").getJSONObject("data").getJSONArray("heroes");

        // remove whitespace and -
        name = name.replaceAll("\\s+","");
        name = name.replaceAll("-", "");

        // loop through the array
        int id = 0;
        int i = 0;
        while (i < heroes.length()) {
            String heroName = heroes.getJSONObject(i).getString("name_english_loc");
            heroName = heroName.replaceAll("\\s+", "");
            heroName = heroName.replaceAll("-", "");
            if (name.equalsIgnoreCase(heroName)) {
                id = heroes.getJSONObject(i).getInt("id");
                break;
            }
        }

        if (id != 0) {
            return id;
        } else {
            throw new IllegalArgumentException("Hero name doesn't exist.");
        }
    }

    /**
     * Gets the hero name corresponding to the given id.
     * @param id the hero id
     * @return The hero name
     */
    public static String getNameFromId(int id) {
        HttpResponse<String> res = DatafeedAPI.callAPI("/herolist?language=english");
        JSONObject responseJson = new JSONObject(res.body());
        JSONArray heroes = responseJson.getJSONObject("result").getJSONObject("data").getJSONArray("heroes");

        for (int i = 0; i < heroes.length(); i++) {
            JSONObject hero = heroes.getJSONObject(i);
            if (hero.getInt("id") == id) {
                return hero.getString("name_english_loc");
            }
        }

        return "<Hero name>";
    }

    /**
     * Calls the dota2 website with the given URL as the ending of the URL and returns the HttpResponse.
     * @param commandURL The part after BASE_URL (with parameters)
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
