import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Photo

class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {
    val photos: LiveData<List<Photo>> = repository.fetchPhotos()
}
