package api

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondText
import org.koin.ext.isInt
import service.TimeTrackingService

class TimeTrackingController(private val service: TimeTrackingService) {

    suspend fun arrival(call: ApplicationCall) {
        try{
            val params = call.receiveParameters()
            val userId = params["user_id"]
            val time = params["time"]

            if(invalidParams(userId, time)) {
                respondInvalid(call)
            }

            call.respondText { service.setUserArrival(userId!!.toInt(), params["time"].orEmpty()) }
        } catch (e: Throwable) {
            respondError(e, call)
        }
    }

    private suspend fun respondInvalid(call: ApplicationCall) {
        call.respond(HttpStatusCode.BadRequest, "ERROR: must provide valid parameters")
    }

    suspend fun exit(call: ApplicationCall) {
        try{
            val params = call.receiveParameters()
            val userId = params["user_id"]
            val time = params["time"]

            if(invalidParams(userId, time)) {
                respondInvalid(call)
            }

            call.respondText { service.setUserExit(userId!!.toInt(), params["time"].orEmpty()) }
        } catch (e: Throwable) {
            respondError(e, call)
        }
    }

    suspend fun getTimeReportsForUser(call: ApplicationCall) {
        try{
            val params = call.request.queryParameters
            val userId = params["user_id"]

            if(!validUserId(userId)) {
                respondInvalid(call)
            }

            call.respondText {  service.getReports(userId!!.toInt()) }
        } catch (e: Throwable) {
            respondError(e, call)
        }
    }

    private suspend fun respondError(e: Throwable, call: ApplicationCall) {
        e.printStackTrace() // replace with proper logging
        call.respond(HttpStatusCode.InternalServerError, "Internal server error occured")
    }

    private fun invalidParams(userId: String?, time: String?) =
        !validUserId(userId) or time.isNullOrEmpty()

    private fun validUserId(userId: String?) = !userId.isNullOrEmpty() && userId.isInt()
}