import java.time.LocalDateTime

enum class ReportType {
    IN, OUT
}

data class TimeReportData(val userId: Int, val reportType: ReportType, val time: LocalDateTime)