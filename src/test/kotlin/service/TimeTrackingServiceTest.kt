package service

import helloAppModule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.security.InvalidParameterException
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

internal class TimeTrackingServiceTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(helloAppModule)
    }

    private val service : TimeTrackingService by inject()

    @Test
    fun `on valid request, calls repository and returns responses`()  {
        // TODO mock responses instead
        assertEquals(service.setUserArrival(123, "2020-01-03T03:04:22"),
            "Persisted TimeReportData(userId=123, reportType=IN, time=2020-01-03T03:04:22)")
        assertEquals(service.setUserExit(123, "2020-01-03T03:04:22"),
            "Persisted TimeReportData(userId=123, reportType=OUT, time=2020-01-03T03:04:22)")
    }

    @Test(expected = DateTimeParseException::class)
    fun `invalid date`()  {
        service.setUserArrival(123, "2021-01 03:04:22")
    }

    @Test(expected = InvalidParameterException::class)
    fun `dates must not be in the future`()  {
        service.setUserArrival(123, LocalDateTime.now().plusDays(1).toString())
    }

    @Test(expected = InvalidParameterException::class)
    fun `user id must be positive`()  {
        service.setUserExit(-12, "2021-01-03T03:04:22")
    }

}