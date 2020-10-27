package me.pj;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class WifiLookupApp {
    private static final Logger LOG = LoggerFactory.getLogger(WifiLookupApp.class);
    private static final String API_TOKEN_ENCODED = ResourceBundle.getBundle("secret").getString("wigle.api.token.encoded");
    private static final String WIGLE_URL = ResourceBundle.getBundle("application").getString("wigle.api.url");

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length != 1) {
            LOG.error("Usage: java -jar wifi-lookup-x.y.z-SNAPSHOT-jar-with-dependencies.jar inputFile");
            System.exit(-1);
        }
        LOG.info("Reading file " + args[0]);
        List<Wlan> wlans = NetshWlanFileParser.getWlans(args[0]);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        WigleRequestHandler wigleRequestHandler = new WigleRequestHandler(httpClient, WIGLE_URL, API_TOKEN_ENCODED);
        List<KMLEntity> kmlEntities = wigleRequestHandler.sendRequestsToWigle(wlans);
        httpClient.close();

        String kml = KMLGenerator.generateKML(kmlEntities);
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".kml";
        LOG.info("Writing results to " + fileName);
        Files.write(Paths.get(fileName), kml.getBytes());
    }
}
