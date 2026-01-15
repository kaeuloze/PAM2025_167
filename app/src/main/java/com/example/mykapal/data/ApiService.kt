package com.example.mykapal.data

import com.example.mykapal.data.model.Crew
import com.example.mykapal.data.model.DashboardSummary
import com.example.mykapal.data.model.LoginResponse
import com.example.mykapal.data.model.NotificationModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.mykapal.data.model.*

interface ApiService {

    /* ================= AUTH ================= */

    @POST("auth/login.php")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse


    /* ================= DASHBOARD ================= */

    @GET("dashboard/summary.php")
    suspend fun getDashboardSummary(): DashboardSummary


    /* ================= CREW ================= */

    @GET("crew/getCrew.php")
    suspend fun getCrew(): List<Crew>

    @POST("crew/insertCrew.php")
    suspend fun insertCrew(
        @Body crew: Crew
    ): Response<ApiResponse>

    @POST("crew/updateCrew.php")
    suspend fun updateCrew(
        @Body crew: Crew
    ): Response<ApiResponse>

    @POST("crew/deleteCrew.php")
    suspend fun deleteCrew(
        @Body body: Map<String, Int>
    ): Response<ApiResponse>


    /* ================= CERTIFICATION (MASTER) ================= */

    @GET("certification/getCertification.php")
    suspend fun getCertification(): List<Certification>

    @POST("certification/insertCertification.php")
    suspend fun insertCertification(
        @Body certification: Certification
    ): Response<ApiResponse>

    @POST("certification/updateCertification.php")
    suspend fun updateCertification(
        @Body certification: Certification
    ): Response<ApiResponse>

    @POST("certification/deleteCertification.php")
    suspend fun deleteCertification(
        @Body body: Map<String, Int>
    ): Response<ApiResponse>


    /* ================= CREW CERTIFICATION ================= */

    @GET("crew_certification/getCrewCertification.php")
    suspend fun getCrewCertification(): List<CrewCertification>

    @POST("crew_certification/insertCrewCertification.php")
    suspend fun insertCrewCertification(
        @Body data: CrewCertification
    ): Response<ApiResponse>

    @POST("crew_certification/updateCrewCertification.php")
    suspend fun updateCrewCertification(
        @Body data: CrewCertification
    ): Response<ApiResponse>

    @POST("crew_certification/deleteCrewCertification.php")
    suspend fun deleteCrewCertification(
        @Body body: Map<String, Int>
    ): Response<ApiResponse>


    /* ================= SHIFT ================= */

    @GET("shift/getShift.php")
    suspend fun getShift(): List<Shift>

    @POST("shift/insertShift.php")
    suspend fun insertShift(
        @Body shift: Shift
    ): Response<ApiResponse>

    @POST("shift/updateShift.php")
    suspend fun updateShift(
        @Body shift: Shift
    ): Response<ApiResponse>

    @POST("shift/deleteShift.php")
    suspend fun deleteShift(
        @Body body: Map<String, Int>
    ): Response<ApiResponse>


    /* ================= CREW SHIFT ================= */

    @GET("crew_shift/getCrewShift.php")
    suspend fun getCrewShift(): List<CrewShift>

    @POST("crew_shift/insertCrewShift.php")
    suspend fun insertCrewShift(
        @Body data: CrewShift
    ): Response<ApiResponse>

    @POST("crew_shift/updateCrewShift.php")
    suspend fun updateCrewShift(
        @Body data: CrewShift
    ): Response<ApiResponse>

    @POST("crew_shift/deleteCrewShift.php")
    suspend fun deleteCrewShift(
        @Body body: Map<String, Int>
    ): Response<ApiResponse>


    /* ================= NOTIFICATION ================= */

    @GET("notification/getNotification.php")
    suspend fun getNotifications(): List<NotificationModel>
}





