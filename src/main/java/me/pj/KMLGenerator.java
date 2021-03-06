package me.pj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KMLGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(KMLGenerator.class);

    static String generateKML(List<KMLEntity> kmlEntities) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://earth.google.com/kml/2.0\">");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earliest = LocalDateTime.now();
        LocalDateTime latest = LocalDateTime.of(1970, 1, 1, 0, 0);
        sb.append("<Document>")
                .append("<name>")
                .append(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .append("</name>")
                .append("<description>Generated by WigleScannerApp ")
                .append(now)
                .append("</description>")
                .append("<Style id=\"l1\"><LineStyle><color>ffffffff</color><width>2</width></LineStyle></Style>")
                .append("<Style id=\"p1\"><LineStyle><color>99ffffff</color><width>2</width></LineStyle><PolyStyle><color>1affffff</color><colorMode>normal</colorMode><fill>1</fill><outline>1</outline></PolyStyle></Style>")
                .append("<Folder><name>Points</name><open>0</open>");
        for (KMLEntity kmlEntity : kmlEntities) {
            sb.append("<Placemark>")
                    .append("<name>")
                    .append(kmlEntity.getSsid())
                    .append("</name>")
                    .append("<description>")
                    .append(kmlEntity)
                    .append("</description>")
                    .append("<Point>")
                    .append("<coordinates>")
                    .append(kmlEntity.getLongitude())
                    .append(",")
                    .append(kmlEntity.getLatitude())
                    .append("</coordinates>")
                    .append("</Point>")
                    .append("<TimeStamp>")
                    .append("<when>")
                    .append(kmlEntity.getTimeStamp())
                    .append("</when>")
                    .append("</TimeStamp>")
                    .append("</Placemark>");
            earliest = kmlEntity.getTimeStamp().isBefore(earliest) ? kmlEntity.getTimeStamp() : earliest;
            latest = kmlEntity.getTimeStamp().isAfter(latest) ? kmlEntity.getTimeStamp() : latest;
        }
        LOG.debug("earliest: " + earliest);
        LOG.debug("latest: " + latest);
        sb.append("</Folder>");
        if (kmlEntities.size() > 1 && !earliest.isEqual(latest)) {
            addLinesElementToStringBuilder(kmlEntities, sb, earliest, latest);
        }
        sb.append("</Document>")
                .append("</kml>");
        LOG.debug(sb.toString());
        return sb.toString();
    }

    private static void addLinesElementToStringBuilder(List<KMLEntity> kmlEntities, StringBuilder sb, LocalDateTime earliest, LocalDateTime latest) {
        sb.append("<Folder><name>Lines</name><open>0</open>")
                .append("<Placemark>")
                .append("<name>")
                .append(earliest)
                .append(" - ")
                .append(latest)
                .append("</name>")
                .append("<styleUrl>#l1</styleUrl>")
                .append("<TimeSpan><begin>")
                .append(earliest)
                .append("</begin>")
                .append("<end>")
                .append(latest)
                .append("</end></TimeSpan>")
                .append("<LineString><extrude>1</extrude><tessellate>1</tessellate>")
                .append("<coordinates>");
        for (KMLEntity kmlEntity : kmlEntities) {
            sb.append(kmlEntity.getLongitude()).append(',').append(kmlEntity.getLatitude()).append(' ');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("</coordinates>")
                .append("</LineString>")
                .append("</Placemark>")
                .append("</Folder>");
    }
}
