package me.pj

import org.xml.sax.SAXParseException
import spock.lang.Specification

import java.time.LocalDateTime

class KMLGeneratorTest extends Specification {
    def 'generated kml is well-formed'() {
        given:
        String json = new File('src/test/resources/wigle_response_one_result.json').text;
        List<KMLEntity> kmls = WigleJsonParser.parse(json, LocalDateTime.now())

        when:
        def xml = KMLGenerator.generateKML(kmls)
        new XmlSlurper().parseText(xml)

        then:
        notThrown(SAXParseException)
        !xml.isEmpty()
    }

    def 'generated kml does not contain the <LineString>-element when there are no time differences between the KML-entitites'() {
        given:
        String json = new File('src/test/resources/wigle_response_many_results.json').text;
        List<KMLEntity> kmls = WigleJsonParser.parse(json, LocalDateTime.now())

        when:
        def xml = KMLGenerator.generateKML(kmls)

        then:
        !new XmlSlurper().parseText(xml).breadthFirst().any { it.name() == "LineString" }
    }

    def 'generated kml does contain the <LineString>-element when there is time differences between the KML-entitites'() {
        given: 'a list of KML-entitites where the last item has a timestamp earlier than all others'
        String json = new File('src/test/resources/wigle_response_many_results.json').text;
        List<KMLEntity> kmls = WigleJsonParser.parse(json, LocalDateTime.now())
        kmls.last().timeStamp = LocalDateTime.of(1975, 1, 1, 15, 30, 0)

        when:
        def xml = KMLGenerator.generateKML(kmls)

        then:
        new XmlSlurper().parseText(xml).breadthFirst().any { it.name() == "LineString" }
    }
}
