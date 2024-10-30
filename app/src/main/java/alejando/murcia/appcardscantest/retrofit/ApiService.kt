package alejando.murcia.appcardscantest.retrofit

import alejando.murcia.appcardscantest.model.Card
import alejando.murcia.appcardscantest.model.VerificationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/create-verification")
    fun createVerification(@Body requestBody: Card): Call<VerificationResponse>
}
