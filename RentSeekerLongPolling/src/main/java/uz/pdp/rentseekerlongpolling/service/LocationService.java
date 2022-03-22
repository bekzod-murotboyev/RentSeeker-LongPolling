package uz.pdp.rentseekerlongpolling.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import uz.pdp.rentseekerlongpolling.model.locationModels.LocationsItem;
import uz.pdp.rentseekerlongpolling.model.locationModels.MapQuest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationService {
    private static final String url = "https://open.mapquestapi.com/geocoding/v1/reverse?key=iFaKV4Rp3rWRtGeJoQQYBEUnqJcxF8Cl&location=";

    public static LocationsItem getData(@NonNull Double lat, @NonNull Double lon) {
        try {
            URL url1 = new URL(url + lat + "," + lon);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            MapQuest mapQuest = new ObjectMapper().readValue(httpURLConnection.getInputStream(), MapQuest.class);
            return mapQuest.getResults().get(0).getLocations().get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
