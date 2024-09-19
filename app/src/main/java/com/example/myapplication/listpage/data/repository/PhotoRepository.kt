import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.model.Photo
import retrofit2.Call
import retrofit2.Response

class PhotoRepository(val apiService: ApiService) {


    fun fetchPhotos(): LiveData<List<Photo>> {
        val photosLiveData = MutableLiveData<List<Photo>>()

        apiService.getPhotos().enqueue(object : retrofit2.Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                //Handle success here
                if (response.isSuccessful) {
                    photosLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                // Handle error here
                photosLiveData.value = emptyList()
            }
        })

        return photosLiveData
    }
}
