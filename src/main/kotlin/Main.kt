import api.TimeTrackingController
import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.response.*
import io.ktor.server.engine.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import data.InMemoryTimeTrackingRepository
import data.TimeTrackingRepository
import org.koin.dsl.module
import service.TimeTrackingService
import service.TimeTrackingServiceImpl

fun main(args: Array<String>) {

    // Start Ktor web server
    embeddedServer(Netty, commandLineEnvironment(args)).start()

}

fun Application.main() {
    // Install Ktor features
    install(DefaultHeaders)
    install(CallLogging)

    // Declare Koin (framework for dependency injection)
    install(Koin) {
        modules(helloAppModule)
    }

    // Lazy inject dependency
    val controller: TimeTrackingController by inject()

    // Routing section
    routing {
        get("/hello") {
            call.respondText("Hi there!")
        }
        post("/time-reporting/arrival") {
            controller.arrival(call)
        }
        post("/time-reporting/exit") {
            controller.exit(call)
        }
        get("/time-reporting/get") {
            controller.getTimeReportsForUser(call)
        }
    }
}

val helloAppModule = module {
    single { TimeTrackingController(get()) }
    single<TimeTrackingService> { TimeTrackingServiceImpl(get()) } // get() Will resolve data.HelloRepository
    single<TimeTrackingRepository> { InMemoryTimeTrackingRepository() }
}