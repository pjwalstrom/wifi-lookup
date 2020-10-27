package me.pj

import spock.lang.Specification

import java.time.LocalDateTime

class WigleJsonParserTest extends Specification {
    def 'parse an arbitrary string should give no results'() {
        expect:
        WigleJsonParser.parse("foo bar", LocalDateTime.now()).isEmpty()
    }

    def 'parse json response from wigle with no results'() {
        given:
        String json = new File('src/test/resources/wigle_response_no_results.json').text;

        when:
        List result = WigleJsonParser.parse(json, LocalDateTime.now())

        then:
        result.isEmpty()
    }

    def 'parse simple json response from wigle'() {
        given:
        String json = new File('src/test/resources/wigle_response_one_result.json').text;
        LocalDateTime longTimeAgo = LocalDateTime.of(1970, 1, 1, 7, 45, 53)

        when:
        List<KMLEntity> result = WigleJsonParser.parse(json, longTimeAgo)

        then:
        result.size() == 1
        result.first().bssid == '34:6F:8A:E2:31:8F'
        result.first().ssid == 'BAR Gjest'
        result.first().road == 'John Jensens vei'
        result.first().city == 'Oslo'
        result.first().country == 'NO'
        result.first().housenumber == '23'
        result.first().postalcode == '0192'
        result.first().region == ""
        result.first().latitude == 61.9093131991235
        result.first().longitude == 13.817596441234
        result.first().timeStamp == longTimeAgo
    }

    def 'parse json response from wigle with many results and check that there are empty strings instead of nulls'() {
        given:
        String json = new File('src/test/resources/wigle_response_many_results.json').text;

        when:
        List result = WigleJsonParser.parse(json, LocalDateTime.now())

        then:
        result.size() == 100
        result.first().bssid == '0A:2C:EF:3D:25:1B'
        result.first().ssid == 'FRITZ!Box 3390'
        result.first().housenumber == ""
        result.first().road == ""
        result.first().city == ""
        result.first().postalcode == ""
        result.last().ssid == ""

    }
}
