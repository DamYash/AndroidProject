package com.example.androidproject;

import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

public class GuardianApi {

    private static final String BASE_URL = "https://content.guardianapis.com";

    private static GuardianResponse jsonObjectToGuardianResponse(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");
        JSONArray jsonArrayResult = jsonObjectResponse.getJSONArray("results");
        ArrayList<GuardianResult> guardianResults = new ArrayList<>(jsonArrayResult.length());
        for (int i = 0; i < jsonArrayResult.length(); i++) {
            JSONObject data = jsonArrayResult.getJSONObject(i);
            GuardianResult guardianResult = new GuardianResult(
                    data.getString("id"),
                    data.getString("type"),
                    data.getString("sectionId"),
                    data.getString("sectionName"),
                    data.getString("webPublicationDate"),
                    data.getString("webTitle"),
                    data.getString("webUrl"),
                    data.getString("apiUrl"),
                    data.getBoolean("isHosted"),
                    data.getString("pillarId"),
                    data.getString("pillarName")
            );
            guardianResults.add(i, guardianResult);
        }
        return new GuardianResponse(
                jsonObjectResponse.getString("status"),
                jsonObjectResponse.getString("userTier"),
                jsonObjectResponse.getInt("total"),
                jsonObjectResponse.getInt("startIndex"),
                jsonObjectResponse.getInt("pageSize"),
                jsonObjectResponse.getInt("currentPage"),
                jsonObjectResponse.getInt("pages"),
                jsonObjectResponse.getString("orderBy"),
                guardianResults
        );
    }


    public static GuardianResponse search(@Nullable String query) throws Throwable {
        StringBuilder stringUrl = new StringBuilder(BASE_URL);
        stringUrl.append("/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a");
        stringUrl.append("&page-size=50");
        if (query !=null) stringUrl.append(query);

        URL url = new URL(stringUrl.toString());

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.connect();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) {
            InputStream inputStream = connection.getInputStream();
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            JSONObject jsonObject = new JSONObject(byteArrayOutputStream.toString());
            return jsonObjectToGuardianResponse(jsonObject);
        } else {
            InputStream inputStreamError = connection.getErrorStream();
            while ((length = inputStreamError.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            JSONObject errorResponse = new JSONObject(byteArrayOutputStream.toString());
            String errorMessage = errorResponse.getString("message");
            throw new HttpError(errorMessage);
        }
    }
}