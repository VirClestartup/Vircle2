package id.kharisma.studio.vircle.`interface`

import id.kharisma.studio.vircle.Constants.Companion.CONTENT_TYPE
import id.kharisma.studio.vircle.Constants.Companion.SERVER_KEY
import id.kharisma.studio.vircle.Model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification:PushNotification
    ): Response<ResponseBody>
}