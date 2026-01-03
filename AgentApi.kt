package com.myprivateagent.net

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

@JsonClass(generateAdapter = true)
data class ChatRequest(val message: String)

@JsonClass(generateAdapter = true)
data class ChatResponse(val reply: String, val approval_id: Int? = null)

@JsonClass(generateAdapter = true)
data class DecideRequest(val approve: Boolean, val decision_note: String = "")

interface AgentApi {
    @POST("/api/chat")
    suspend fun chat(@Body req: ChatRequest): ChatResponse

    @GET("/api/approvals")
    suspend fun approvals(): Map<String, Any>

    @POST("/api/approvals/{id}/decide")
    suspend fun decide(@Path("id") id: Int, @Body req: DecideRequest): Map<String, Any>

    @POST("/api/daily/run")
    suspend fun runDaily(): Map<String, Any>
}
