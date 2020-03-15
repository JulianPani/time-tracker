package data

import TimeReportData


interface TimeTrackingRepository {
    fun getReports(userId: Int): Set<TimeReportData>
    fun save(timeReportData: TimeReportData): String
}

class InMemoryTimeTrackingRepository : TimeTrackingRepository {

    companion object {
        private val map = mutableMapOf<Int, MutableSet<TimeReportData>>()
    }

    override fun getReports(userId: Int): Set<TimeReportData> {
        return map[userId] ?: emptySet()
    }

    override fun save(timeReportData: TimeReportData): String {
        return persist(timeReportData)
    }

    private fun persist(timeReportData: TimeReportData): String {
        map.computeIfAbsent(timeReportData.userId) { mutableSetOf() }.add(timeReportData)
        return "Persisted $timeReportData"
    }

}