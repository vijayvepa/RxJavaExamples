package spockexample

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.Month

class PlusMinusMonthSpec extends Specification {

    static final LocalDate START_DATE =
            LocalDate.of(2016, Month.JANUARY, 1)

    @Unroll
    def '#date +/- 1 month gives back the same date'() {
        expect:
        date == date.plusMonths(1).minusMonths(1)

        where:
        date << (0..365).collect {
            day -> START_DATE.plusDays(day)
        }
    }
}
