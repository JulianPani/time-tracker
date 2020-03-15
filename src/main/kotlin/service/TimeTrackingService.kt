package service

import ReportType
import TimeReportData
import data.TimeTrackingRepository
import java.security.InvalidParameterException
import java.time.LocalDateTime

interface TimeTrackingService {
    fun setUserArrival(userId: Int, time: String): String
    fun setUserExit(userId: Int, time: String): String
    fun getReports(userId: Int): String
}

class TimeTrackingServiceImpl(private val repository: TimeTrackingRepository) : TimeTrackingService {

    override fun setUserArrival(userId: Int, time: String): String {
        val timeReportData = parseAndValidate(userId, time, ReportType.IN)
        return repository.save(timeReportData)
    }

    private fun parseAndValidate(userId: Int, timeStr: String, reportType: ReportType): TimeReportData {
        val parsedTime = LocalDateTime.parse(timeStr)
        if(parsedTime.isAfter(LocalDateTime.now())) {
            throw InvalidParameterException("The provided date $timeStr cannot be in the future")
        }

        if(userId < 0) {
            throw InvalidParameterException("UserId must be greater than 1, but was $userId")
        }

        return TimeReportData(userId, reportType, parsedTime)
    }

    override fun setUserExit(userId: Int, time: String): String {
        val timeReportData = parseAndValidate(userId, time, ReportType.OUT)
        return repository.save(timeReportData)
    }

    override fun getReports(userId: Int): String {
        val reports: Set<TimeReportData> = repository.getReports(userId)
        return reports.toString()
    }

}