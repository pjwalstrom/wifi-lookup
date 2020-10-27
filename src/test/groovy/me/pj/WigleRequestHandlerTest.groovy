package me.pj

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import spock.lang.Specification

import java.time.LocalDateTime

class WigleRequestHandlerTest extends Specification {
    WigleRequestHandler toTest
    HttpClient httpClient
    HttpResponse response
    HttpEntity entity

    def setup() {
        httpClient = Stub(HttpClient)
        response = Stub(HttpResponse)
        entity = Stub(HttpEntity)
        toTest = new WigleRequestHandler(httpClient, "http://foo", "")
    }

    def 'sending a request to wigle (using stubs) produces the correct KML entities'() {
        given:
        List<Wlan> wlans = new NetshWlanFileParser().getWlansWithJustTheStrongestBssid("src/test/resources/wlans_one_wlan.txt")
        String response = new File('src/test/resources/wigle_response_one_result.json').text;
        entity.getContent() >> new ByteArrayInputStream(response.getBytes())
        this.response.getEntity() >> entity
        httpClient.execute(_) >> this.response

        when:
        List<KMLEntity> result = toTest.sendRequestsToWigle(wlans)

        then:
        result.size() == 1
        result.first().bssid == '34:6F:8A:E2:31:8F'
        result.first().ssid == 'BAR Gjest'
        result.first().road == 'John Jensens vei'
        result.first().city == 'Oslo'
        result.first().country == 'NO'
        result.first().housenumber == '23'
        result.first().postalcode == '0192'
        result.first().region.isEmpty()
        result.first().latitude == 61.9093131991235
        result.first().longitude == 13.817596441234
        result.first().timeStamp == LocalDateTime.of(2019, 10, 22, 12, 19, 49)
    }
}
