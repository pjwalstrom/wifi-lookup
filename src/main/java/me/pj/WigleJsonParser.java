package me.pj;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WigleJsonParser {
    private static final Logger LOG = LoggerFactory.getLogger(WigleJsonParser.class);

    static List<KMLEntity> parse(String json, LocalDateTime timestamp) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        List<KMLEntity> results = new ArrayList<>();
        LOG.trace("json:" + json);
        try {
            List<Map> mappedJsons = (List<Map>) new Gson().fromJson(json, Map.class).get("results");
            for (Map map : mappedJsons) {
                results.add(createKmlEntity(timestamp, map));
            }
        } catch (JsonSyntaxException e) {
            LOG.warn("Could not parse this string as json: " + json);
        }
        return results;
    }

    private static KMLEntity createKmlEntity(LocalDateTime timestamp, Map map) {
        String city = Optional.ofNullable((String) map.get("city")).orElse("");
        String road = Optional.ofNullable((String) map.get("road")).orElse("");
        String ssid = Optional.ofNullable((String) map.get("ssid")).orElse("");
        logGeoLocation(ssid, city, road);
        return new KMLEntity.Builder((String) map.get("netid")).timeStamp(timestamp)
                .ssid(ssid)
                .latitude((Double) map.get("trilat"))
                .longitude((Double) map.get("trilong"))
                .city(city)
                .road(road)
                .housenumber(Optional.ofNullable((String) map.get("housenumber")).orElse(""))
                .postalcode(Optional.ofNullable((String) map.get("postalcode")).orElse(""))
                .country((String) map.get("country"))
                .region(Optional.ofNullable((String) map.get("region")).orElse(""))
                .build();
    }

    private static void logGeoLocation(String ssid, String city, String road) {
        String logString = ssid.isEmpty() ? ssid : ssid + ": " + (road.isEmpty() ? city : road + ", " + city);
        if (!logString.isEmpty()) {
            LOG.info(logString);
        }
    }
}
