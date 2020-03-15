package data

import TimeReportData
import org.junit.Test

import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.assertEquals

class TrackingRepositoryTest {

//    @get:Rule
//    val koinTestRule = KoinTestRule.create {
//        printLogger(Level.DEBUG)
//        modules(helloAppModule)
//    }
//
    private val time = LocalDateTime.now()


    @Test
    fun `save and get reports`() {
        val repo = InMemoryTimeTrackingRepository()
        val one = TimeReportData(10, ReportType.IN, time)
        val two = TimeReportData(10, ReportType.OUT, time)
        repo.save(one)
        repo.save(two)
        assertEquals(repo.getReports(10), setOf(one, two))
    }

    @Test
    fun `get reports only gets reports for specified user id`() {
        val repo = InMemoryTimeTrackingRepository()
        val userIdOne = TimeReportData(1111, ReportType.IN, time)
        repo.save(userIdOne)
        repo.save(TimeReportData(2222, ReportType.IN, time))

        assertEquals(repo.getReports(1111), setOf(userIdOne))
    }


    @Test
    fun `get reports for non-existent user id returns empty set`() {
        val repo = InMemoryTimeTrackingRepository()
        assertEquals(repo.getReports(34535), setOf())
    }
}