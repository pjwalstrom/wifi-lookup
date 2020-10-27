package me.pj

import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class NetshWlanFileParserTest extends Specification {
    def 'when the input is some arbitrary text, the result should be empty'() {
        expect:
        NetshWlanFileParser.getWlans("src/test/resources/wlans_garbage.txt").isEmpty()
    }

    def 'when there are no wlans found, the result should be empty'() {
        expect:
        NetshWlanFileParser.getWlans("src/test/resources/wlans_empty.txt").isEmpty()
    }

    def 'when there is no time information, the date of the discovery time should be today'() {
        when:
        List<Wlan> wlans = NetshWlanFileParser.getWlans("src/test/resources/wlans_missing_time.txt")

        then:
        wlans.first().discoveryTime.toLocalDate() == LocalDate.now()
    }

    def 'parse wlans from Location 2 correctly'() {
        expect:
        NetshWlanFileParser.getWlans("src/test/resources/wlans_loc2.txt").size() == 11
    }

    def 'parse wlans from Location 1 correctly'() {
        when:
        List<Wlan> wlans = NetshWlanFileParser.getWlans("src/test/resources/wlans_loc1.txt")

        then:
        wlans.size() == 13
        wlans.first().ssid == "AndroidAP"
        wlans.first().bssids.size() == 2
        wlans.first().bssids.first().bssid == "88:bd:45:aa:21:34"
        wlans.first().bssids.first().signalStrength == 21
        wlans.first().bssids.last().bssid == "87:2d:e8:fb:66:0e"
        wlans.first().bssids.last().signalStrength == 10
        wlans.first().discoveryTime == LocalDateTime.of(2019, 10, 21, 11, 56, 58)

        wlans.find({ (it.ssid == "BAR Gjest") }).bssids.size() == 8

        wlans.last().ssid == "Gjestenett Foo"
        wlans.last().bssids.size() == 1
        wlans.last().bssids.first().bssid == "58:7e:8f:6e:5e:aa"
        wlans.last().bssids.first().signalStrength == 26
    }

    def 'get wlans optimized for a request to wigle, containing only the strongest one of the bssids for each ssid'() {
        when:
        List<Wlan> wlans = NetshWlanFileParser.getWlansWithJustTheStrongestBssid("src/test/resources/wlans_loc1.txt")

        then:
        wlans.size() == 13
        wlans.first().ssid == "AndroidAP"
        wlans.first().bssids.size() == 1
        wlans.first().bssids.first().bssid == "88:bd:45:aa:21:34"
        wlans.first().bssids.first().signalStrength == 21

        wlans.find({ (it.ssid == "BAR Gjest") }).bssids.size() == 1
        wlans.find({ (it.ssid == "BAR Gjest") }).bssids.first().signalStrength == 99
    }

    def 'parse different occurrences of newline'() {
        expect:
        NetshWlanFileParser.getNewLineIndex(textWithNewline) == newLineIndex

        where:
        textWithNewline || newLineIndex
        'foo\nbar'      || 3
        'foo\rbar'      || 3
        'foobar\r\nqux' || 6
    }
}
