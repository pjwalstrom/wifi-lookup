package me.pj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;

public class DateTimeParser {
    private static final Logger LOG = LoggerFactory.getLogger(DateTimeParser.class);
    private static final Locale[] LOCALES = {Locale.ENGLISH, Locale.FRENCH, Locale.forLanguageTag("es"), Locale.GERMAN, Locale.ITALIAN, Locale.forLanguageTag("no")};
    private static final String[] WINDOWS_DATE_TIME_FORMATS_MONTHS = {"d. MMM yyyy HH:mm:ss", "d. MMM. yyyy HH:mm:ss", "d. MMMM yyyy HH:mm:ss"};
    // from https://www.basicdatepicker.com/samples/cultureinfo.aspx
    private static final String[] WINDOWS_DATE_TIME_FORMATS = {"yyyy/MM/dd hh:mm:ss a", "d.M.yyyy HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "d/M/yyyy h:mm:ss a", "dd-MM-yyyy H:mm:ss",
            "dd/MM/yyyy hh:mm:ss a", "dd/MM/yy hh:mm:ss a", "dd.MM.yyyy HH:mm:ss", "dd-MM-yyyy a h:mm:ss", "dd-MM-yy HH.mm.ss", "dd.MM.yy H:mm:ss",
            "yyyy/MM/dd H:mm:ss", "dd.MM.yy HH:mm:ss", "d.M.yyyy H:mm:ss", "dd-MM-yyyy HH:mm:ss", "dd/MM/yyyy H:mm:ss", "M/d/yyyy h:mm:ss a", "yyyy/M/d H:mm:ss",
            "d/M/yyyy a h:mm:ss", "d/M/yyyy H:mm:ss", "yyyy/M/d a hh:mm:ss", "d.M.yyyy. H:mm:ss", "d. M. yyyy H:mm:ss", "yyyy/M/d h:mm:ss a", "dd/MM/yy HH:mm:ss",
            "d/MM/yyyy H:mm:ss", "d-M-yyyy HH:mm:ss", "d/MM/yyyy h:mm:ss a", "yyyy-MM-dd h:mm:ss a", "d/M/yy h:mm:ss a", "yyyy-MM-dd hh:mm:ss a", "dd/MM/yyyy h:mm:ss a",
            "d.MM.yyyy H:mm:ss", "dd-MM-yy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "d-M-yyyy H:mm:ss", "dd.MM.yyyy H:mm:ss", "yyyy.MM.dd. H:mm:ss", "d/M/yyyy h.mm.ss a",
            "dd-MM-yyyy hh:mm:ss a", "dd/MM/yyyy HH.mm.ss", "d-MMM-yy HH:mm:ss", "yyyy-MM-dd a h:mm:ss", "d-MMM yy HH:mm:ss", "dd.MM.yyyy. H:mm:ss", "d. M. yyyy HH:mm:ss",
            "d/M/yyyy HH:mm:ss", "dd/MM/yyyy a hh:mm:ss", "dd/MM/yy h:mm:ss a", "dd-MM-yy a hh:mm:ss", "dd-MM-yy h.mm.ss a", "dd-MM-yyyy HH.mm.ss", "yyyy/M/d HH:mm:ss",
            "d. M. yyyy H.mm.ss", "yyyy-M-d H:mm:ss", "yyyy/M/d a h:mm:ss"};

    static LocalDateTime parseToLocalDateTime(String dateTimeString) {
        for (String dateTimeFormat : WINDOWS_DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(dateTimeFormat));
            } catch (DateTimeParseException e) {
                LOG.debug("Wrong format '" + dateTimeFormat + "' for: " + e.getMessage());
            }
        }
        return parseToLocalDateTimeWithDifferentLocales(dateTimeString);
    }

    private static LocalDateTime parseToLocalDateTimeWithDifferentLocales(String dateTimeString) {
        for (Locale locale : LOCALES) {
            LOG.debug("Short months for locale " + locale + ": " + Arrays.toString(new DateFormatSymbols(locale).getShortMonths()));
            for (String dateTimeFormat : WINDOWS_DATE_TIME_FORMATS_MONTHS) {
                try {
                    return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(dateTimeFormat, locale));
                } catch (DateTimeParseException e) {
                    LOG.debug("Wrong format '" + dateTimeFormat + "' with locale " + locale + " for: " + e.getLocalizedMessage());
                }
            }
        }

        LOG.warn("Unknown datetime format: " + dateTimeString + ", setting timestamp to now.");
        return LocalDateTime.now();
    }
}
