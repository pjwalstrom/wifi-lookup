package me.pj

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.LocalDateTime

class DateTimeParserTest extends Specification {
    @Unroll
    def '#dateToParse is properly parsed'() {
        expect:
        DateTimeParser.parseToLocalDateTime(dateToParse) == LocalDateTime.of(year, month, day, hour, minute, second)

        where:
        dateToParse                 || year | month | day | hour | minute | second
        '2019-10-21 11:56:58'       || 2019 | 10    | 21  | 11   | 56     | 58
        '21-10-2019 11.56.58'       || 2019 | 10    | 21  | 11   | 56     | 58
        '2019-10-21 13:56:58'       || 2019 | 10    | 21  | 13   | 56     | 58
        '2019-10-21 03:06:58'       || 2019 | 10    | 21  | 3    | 6      | 58
        '21.10.2019 11:56:58'       || 2019 | 10    | 21  | 11   | 56     | 58
        '1.1.1970 11:56:58'         || 1970 | 1     | 1   | 11   | 56     | 58
        '1970-1-1 11:56:58'         || 1970 | 1     | 1   | 11   | 56     | 58
        '1970-01-01 11:56:58'       || 1970 | 1     | 1   | 11   | 56     | 58
        '1/1/1970 11:56:58'         || 1970 | 1     | 1   | 11   | 56     | 58
        '1/1/1970 1:56:58'          || 1970 | 1     | 1   | 1    | 56     | 58
        '1/1/1970 1:56:58 PM'       || 1970 | 1     | 1   | 13   | 56     | 58
        '1/1/1970 11:56:58 PM'      || 1970 | 1     | 1   | 23   | 56     | 58
        '1/1/1970 1:56:58 AM'       || 1970 | 1     | 1   | 1    | 56     | 58
        '21. October 2019 11:56:58' || 2019 | 10    | 21  | 11   | 56     | 58
        '21. Oct. 2019 11:56:58'    || 2019 | 10    | 21  | 11   | 56     | 58
        '21. févr. 2019 11:56:58'   || 2019 | 2     | 21  | 11   | 56     | 58
        '21. Dez. 2019 11:56:58'    || 2019 | 12    | 21  | 11   | 56     | 58
        '21. okt. 2019 11:56:58'    || 2019 | 10    | 21  | 11   | 56     | 58
        '21. ene. 2019 11:56:58'    || 2019 | 1     | 21  | 11   | 56     | 58
    }

    def "when the datetime string cannot be parsed, today's date is returned instead"() {
        expect:
        DateTimeParser.parseToLocalDateTime("koko").toLocalDate() == LocalDate.now()
    }
}
