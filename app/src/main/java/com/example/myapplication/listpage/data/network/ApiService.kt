
import com.example.myapplication.data.model.Photo
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("photos")
    fun getPhotos(): Call<List<Photo>>
}
