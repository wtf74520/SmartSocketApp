import java.sql.Timestamp

data class MqttResponse(
    val id: Int,
    val deviceId: Int,
    val timestamp: Timestamp,
    val current: Float,
    val power: Float,
    val on: String
)