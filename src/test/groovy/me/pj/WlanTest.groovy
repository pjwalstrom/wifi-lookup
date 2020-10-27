package me.pj

import spock.lang.Specification

class WlanTest extends Specification {
    def 'the comparison of bssids are done according to signal strength, i.e. the stronger the signal strength means the greater the bssid'() {
        given:
        Wlan.Bssid bssid1 = new Wlan.Bssid("bssid 1", 10)
        Wlan.Bssid bssid2 = new Wlan.Bssid("bssid 2", 20)
        Wlan.Bssid bssid3 = new Wlan.Bssid("bssid 3", 25)

        expect:
        bssid1 == bssid1
        bssid1 < bssid2
        bssid2 > bssid1
        bssid1 < bssid3
        bssid2 < bssid3
        bssid3 > bssid2
        bssid3 > bssid1
    }
}
