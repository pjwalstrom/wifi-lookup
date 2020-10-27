package me.pj;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WigleRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WigleRequestHandler.class);
    private final HttpClient httpClient;
    private final String url;
    private final String apiTokenEncoded;

    public WigleRequestHandler(HttpClient httpClient, String url, String apiTokenEncoded) {
        this.httpClient = httpClient;
        this.url = url;
        this.apiTokenEncoded = apiTokenEncoded;
    }

    List<KMLEntity> sendRequestsToWigle(List<Wlan> wlans) throws URISyntaxException, IOException {
        List<KMLEntity> result = new ArrayList<>();
        URIBuilder builder = new URIBuilder(url);
        for (Wlan wlan : wlans) {
            for (Wlan.Bssid bssid : wlan.getBssids()) {
                builder.setParameter("netid", bssid.getBssid());
                HttpGet httpGet = new HttpGet(builder.build());
                httpGet.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
                httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + apiTokenEncoded);
                HttpResponse response = httpClient.execute(httpGet);
                String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                LOG.debug(json);
                result.addAll(WigleJsonParser.parse(json, wlan.getDiscoveryTime()));
            }
        }
        return result;
    }
}
