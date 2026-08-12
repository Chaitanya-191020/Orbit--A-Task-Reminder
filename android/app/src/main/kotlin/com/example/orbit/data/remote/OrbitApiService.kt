package com.example.orbit.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class SpideyBriefingResponse(
    val success: Boolean,
    val data: SpideyBriefingData
)

data class SpideyBriefingData(
    val greeting: String,
    val time: String,
    val urgent_tasks: List<String>,
    val todays_tasks: List<String>,
    val spoken_summary: String
)

data class AnalyticsSummaryResponse(val success: Boolean, val data: AnalyticsData)
data class AnalyticsData(
    val focus_time_7_days: Int,
    val focus_time_30_days: Int,
    val tasks_completion_rate_30_days: Float,
    val total_habit_streaks: Int
)

data class Habit(val id: String, val title: String, val streak_count: Int)
data class HabitsResponse(val success: Boolean, val data: List<Habit>)

data class Goal(val id: String, val title: String, val progress_percentage: Float, val is_achieved: Boolean)
data class GoalsResponse(val success: Boolean, val data: List<Goal>)

interface OrbitApiService {
    @GET("spidey/briefing")
    suspend fun getSpideyBriefing(
        @Query("alarm_id") alarmId: String? = null
    ): SpideyBriefingResponse

    @GET("analytics/summary")
    suspend fun getAnalyticsSummary(): AnalyticsSummaryResponse

    @GET("habits")
    suspend fun getHabits(): HabitsResponse

    @GET("goals")
    suspend fun getGoals(): GoalsResponse
}
