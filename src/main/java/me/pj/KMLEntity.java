package me.pj;

import java.time.LocalDateTime;

public class KMLEntity {
    private LocalDateTime timeStamp;
    private final String ssid;
    private final String bssid;
    private final Double latitude;
    private final Double longitude;
    private final String country;
    private final String region;
    private final String housenumber;
    private final String road;
    private final String city;
    private final String postalcode;

    private KMLEntity(Builder builder) {
        this.timeStamp = builder.timeStamp;
        this.ssid = builder.ssid;
        this.bssid = builder.bssid;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.country = builder.country;
        this.region = builder.region;
        this.housenumber = builder.housenumber;
        this.road = builder.road;
        this.city = builder.city;
        this.postalcode = builder.postalcode;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public String getRoad() {
        return road;
    }

    public String getCity() {
        return city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("KMLEntity{");
        sb.append("ssid='").append(ssid).append('\'');
        sb.append(", bssid='").append(bssid).append('\'');
        sb.append(", road='").append(road).append(' ').append(housenumber).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {
        private LocalDateTime timeStamp;
        private String ssid;
        private String bssid;
        private Double latitude;
        private Double longitude;
        private String country;
        private String region;
        private String housenumber;
        private String road;
        private String city;
        private String postalcode;

        Builder(String bssid) {
            this.bssid = bssid;
        }

        public Builder ssid(String ssid) {
            this.ssid = ssid;
            return this;
        }

        Builder timeStamp(LocalDateTime val) {
            this.timeStamp = val;
            return this;
        }

        Builder latitude(Double val) {
            this.latitude = val;
            return this;
        }

        Builder longitude(Double val) {
            this.longitude = val;
            return this;
        }

        Builder country(String val) {
            this.country = val;
            return this;
        }

        Builder region(String val) {
            this.region = val;
            return this;
        }

        Builder housenumber(String val) {
            this.housenumber = val;
            return this;
        }

        Builder road(String val) {
            this.road = val;
            return this;
        }

        Builder city(String val) {
            this.city = val;
            return this;
        }

        Builder postalcode(String val) {
            this.postalcode = val;
            return this;
        }

        KMLEntity build() {
            return new KMLEntity(this);
        }
    }
}
