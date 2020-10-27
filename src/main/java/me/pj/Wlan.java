package me.pj;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Wlan {
    private final String ssid;
    private final LocalDateTime discoveryTime;
    private final List<Bssid> bssids;

    Wlan(String ssid, LocalDateTime discoveryTime) {
        this.ssid = ssid;
        this.bssids = new ArrayList<>();
        this.discoveryTime = discoveryTime;
    }

    public LocalDateTime getDiscoveryTime() {
        return discoveryTime;
    }

    public String getSsid() {
        return ssid;
    }

    public List<Bssid> getBssids() {
        return bssids;
    }

    void addBssid(Bssid bssid) {
        bssids.add(bssid);
    }

    void removeOthersAndAddBssid(Bssid bssid) {
        bssids.clear();
        bssids.add(bssid);
    }

    static class Bssid implements Comparable<Bssid> {
        String bssid;
        Integer signalStrength;

        Bssid(String bssid, Integer signalStrength) {
            this.bssid = bssid;
            this.signalStrength = signalStrength;
        }

        public String getBssid() {
            return bssid;
        }

        public Integer getSignalStrength() {
            return signalStrength;
        }

        @Override
        public int compareTo(Bssid other) {
            return this.signalStrength.compareTo(other.signalStrength);
        }
    }
}
