package me.pj;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NetshWlanFileParser {
    static List<Wlan> getWlansWithJustTheStrongestBssid(String fileName) throws IOException {
        List<Wlan> wlans = getWlans(fileName);
        wlans.forEach(wlan -> wlan.removeOthersAndAddBssid(wlan.getBssids().stream().max(Wlan.Bssid::compareTo).get()));
        return wlans;
    }

    static List<Wlan> getWlans(String fileName) throws IOException {
        Scanner scanner = new Scanner(new File(fileName)).useDelimiter("\\bSSID\\s+\\d+\\s+:");
        String header = scanner.next();
        LocalDateTime discoveryTime;
        if (header.contains("Time:")) {
            discoveryTime = DateTimeParser.parseToLocalDateTime(header.split("Time:")[1].trim().substring(0, 19));
        } else {
            discoveryTime = LocalDateTime.now();
        }
        List<Wlan> wlans = new ArrayList<>();
        while (scanner.hasNext()) {
            wlans.add(getWlan(scanner.next(), discoveryTime));
        }
        scanner.close();
        return wlans;
    }

    private static Wlan getWlan(String clob, LocalDateTime discoveryTime) {
        String[] split = clob.split("\\bBSSID\\s+\\d+\\s+:");
        String ssid = split[0].substring(0, split[0].indexOf("\n")).trim();
        Wlan wlan = new Wlan(ssid, discoveryTime);
        for (int i = 1; i < split.length; i++) {
            String s = split[i].trim();
            String signalStrength = s.split("\\bSignal\\s+:\\s+")[1].trim();
            int newLineIndex = getNewLineIndex(s);
            Wlan.Bssid bssid = new Wlan.Bssid(s.substring(0, newLineIndex), Integer.parseInt(signalStrength.substring(0, signalStrength.indexOf("%"))));
            wlan.addBssid(bssid);
        }
        return wlan;
    }

    private static int getNewLineIndex(String s) {
        return s.contains("\r\n") ? s.indexOf("\r\n") : Math.max(s.indexOf("\n"), s.indexOf("\r"));
    }

}
