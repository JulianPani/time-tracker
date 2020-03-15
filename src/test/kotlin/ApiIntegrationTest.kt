import io.ktor.application.Application
import io.ktor.http.*
import io.ktor.request.queryString
import io.ktor.server.testing.*
import org.junit.Test

import org.koin.test.AutoCloseKoinTest

class ApiIntegrationTest : AutoCloseKoinTest() {

    private val time = "2018-04-21T04:23:22"
    private val time2 = "2020-02-21T08:00:01"

    @Test
    fun testReportArrival() = withTestApplication(Application::main) {
        with(
            sendArrival(time)
        )
        {
            kotlin.test.assertEquals(HttpStatusCode.OK, response.status())
            kotlin.test.assertEquals(
                "Persisted TimeReportData(userId=1, reportType=IN, time=$time)",
                response.content)
        }
    }

    @Test
    fun testReportExit() = withTestApplication(Application::main) {
        with(
            sendExit(time2)
        )
        {
            kotlin.test.assertEquals(HttpStatusCode.OK, response.status())
            kotlin.test.assertEquals(
                "Persisted TimeReportData(userId=1, reportType=OUT, time=$time2)",
                response.content)
        }
    }

    @Test
    fun testGetReports() = withTestApplication(Application::main) {
        sendArrival(time)
        sendExit(time2)
        with(
            handleRequest(HttpMethod.Get, "/time-reporting/get?user_id=1") {
            })
        {
            kotlin.test.assertEquals(HttpStatusCode.OK, response.status())
            kotlin.test.assertEquals(
                "[TimeReportData(userId=1, reportType=IN, time=$time), TimeReportData(userId=1, reportType=OUT, time=$time2)]",
                response.content)
        }
    }

    @Test
    fun `unknown route fails`() {
        withTestApplication(Application::main) {
            with(handleRequest(HttpMethod.Get, "/index.html")) {
                kotlin.test.assertFalse(requestHandled)
            }
        }
    }

    private fun TestApplicationEngine.sendArrival(time: String): TestApplicationCall {
        return handleRequest(HttpMethod.Post, "/time-reporting/arrival/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("user_id" to "1", "time" to time).formUrlEncode())
        }
    }
    private fun TestApplicationEngine.sendExit(time: String): TestApplicationCall {
        return handleRequest(HttpMethod.Post, "/time-reporting/exit/") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("user_id" to "1", "time" to time).formUrlEncode())
        }
    }
}