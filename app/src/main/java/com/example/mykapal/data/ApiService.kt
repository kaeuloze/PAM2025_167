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
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Query

interface ApiService {

    /* ================= AUTH ================= */

    @POST("login_admin.php")
    suspend fun login(
        @Body body: LoginRequest
    ): LoginResponse


    @FormUrlEncoded
    @POST("login_admin.php")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse


    /* ================= DASHBOARD ================= */

    // Menjadi ini:
    @GET("dashboard/summary.php")
    suspend fun getDashboardSummary(): DashboardSummary


    /* ================= CREW ================= */
    // GET ALL CREW
    @GET("getCrew.php")
    suspend fun getCrew(): ApiResponse<List<Crew>>

    // INSERT CREW
    @FormUrlEncoded
    @POST("insertCrew.php")
    suspend fun insertCrew(
        @Field("nama_lengkap") namaLengkap: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("kewarganegaraan") kewarganegaraan: String,
        @Field("posisi") posisi: String,
        @Field("nomor_passport") nomorPassport: String,
        @Field("kontak") kontak: String,
        @Field("alamat") alamat: String
    ): ApiResponse<Crew>

    // UPDATE CREW
    @FormUrlEncoded
    @POST("updateCrew.php")
    suspend fun updateCrew(
        @Field("crew_id") crewId: Int,
        @Field("nama_lengkap") namaLengkap: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("kewarganegaraan") kewarganegaraan: String,
        @Field("posisi") posisi: String,
        @Field("nomor_passport") nomorPassport: String,
        @Field("kontak") kontak: String,
        @Field("alamat") alamat: String
    ): ApiResponse<Crew>

    // DELETE CREW
    @FormUrlEncoded
    @POST("deleteCrew.php")
    suspend fun deleteCrew(
        @Field("crew_id") crewId: Int
    ): ApiResponse<String>


    /* ================= CERTIFICATION (MASTER) ================= */

    @GET("getCertification.php")
    suspend fun getCertification(): ApiResponse<List<Certification>>

    @POST("insertCertification.php")
    suspend fun insertCertification(
        @Body certification: Certification
    ): ApiResponse<Any>

    @POST("updateCertification.php")
    suspend fun updateCertification(
        @Body certification: Certification
    ): ApiResponse<Any>

    @POST("deleteCertification.php")
    suspend fun deleteCertification(
        @Body body: Map<String, Int>
    ): ApiResponse<Any>


    /* ================= CREW CERTIFICATION ================= */

    @GET("getCrewCertification.php")
    suspend fun getCrewCertification(): ApiResponse<List<CrewCertification>>

    @FormUrlEncoded
    @POST("insertCrewCertification.php")
    suspend fun insertCrewCertification(
        @Field("crew_cert_id") crewCertId: Int,
        @Field("crew_id") crewId: Int,
        @Field("certification_id") certificationId: Int,
        @Field("nama_sertifikasi") namaSertifikasi: String,
        @Field("kru") kru: String,
        @Field("tanggal_terbit") tanggalTerbit: String,
        @Field("tanggal_kadaluarsa") tanggalKadaluarsa: String,
        @Field("status") status: String
    ): ApiResponse<CrewCertification>

    @FormUrlEncoded
    @POST("updateCrewCertification.php")
    suspend fun updateCrewCertification(
        @Field("crew_cert_id") crewCertId: Int,
        @Field("crew_id") crewId: Int,
        @Field("certification_id") certificationId: Int,
        @Field("nama_sertifikasi") namaSertifikasi: String,
        @Field("kru") kru: String,
        @Field("tanggal_terbit") tanggalTerbit: String,
        @Field("tanggal_kadaluarsa") tanggalKadaluarsa: String,
        @Field("status") status: String
    ): ApiResponse<CrewCertification>

    @FormUrlEncoded
    @POST("deleteCrewCertification.php")
    suspend fun deleteCrewCertification(
        @Field("crew_cert_id") crewCertId: Int
    ): ApiResponse<String>


    /* ================= SHIFT ================= */

    @GET("getShift.php")
    suspend fun getShift(): ApiResponse<List<Shift>>

    @POST("insertShift.php")
    suspend fun insertShift(
        @Body shift: Shift
    ): ApiResponse<Shift>

    @POST("updateShift.php")
    suspend fun updateShift(
        @Body shift: Shift
    ): ApiResponse<Shift>

    @POST("deleteShift.php")
    suspend fun deleteShift(
        @Body body: Map<String, Int>
    ): ApiResponse<String>


    /* ================= CREW SHIFT ================= */

    @GET("getCrewShift.php")
    suspend fun getCrewShift(): ApiResponse<List<CrewShift>>  // ← DIUBAH!

    @POST("insertCrewShift.php")
    suspend fun insertCrewShift(
        @Body data: CrewShift
    ): ApiResponse<CrewShift>

    @POST("updateCrewShift.php")
    suspend fun updateCrewShift(
        @Body data: CrewShift
    ): ApiResponse<CrewShift>

    @POST("deleteCrewShift.php")
    suspend fun deleteCrewShift(
        @Body body: Map<String, Int>
    ): ApiResponse<String>


    /* ================= NOTIFICATION ================= */

    @GET("getNotification.php")
    suspend fun getNotifications(): List<NotificationModel>  // ← DIUBAH!

}