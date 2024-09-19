import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.data.model.Photo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class PhotoRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var photoRepository: PhotoRepository
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        apiService = mock(ApiService::class.java)
        photoRepository = PhotoRepository(apiService)
    }

    @Test
    fun `fetchPhotos returns success data`() {
        // Arrange
        val expectedPhotos = listOf(
            Photo(1, 1, "facere non quis fuga fugit vitae",
                "https://via.placeholder.com/600/5e3a73",
                "https://via.placeholder.com/150/5e3a73",
                "Description here"),
        )

        val liveDataObserver = mock(Observer::class.java) as Observer<List<Photo>>

        val photosLiveData = photoRepository.fetchPhotos()
        photosLiveData.observeForever(liveDataObserver)

        // Simulate successful API response
        val call = mock(Call::class.java) as Call<List<Photo>>
        `when`(apiService.getPhotos()).thenReturn(call)

        // Trigger the response
        call.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                response.body()?.let {
                    liveDataObserver.onChanged(it)
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                liveDataObserver.onChanged(emptyList())
            }
        })
        // Simulate onResponse
        call.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                response.isSuccessful
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                liveDataObserver.onChanged(emptyList())
            }
        })

        // Act
        verify(liveDataObserver).onChanged(expectedPhotos)
    }

    @Test
    fun `fetchPhotos returns empty list on error`() {
        // Arrange
        val liveDataObserver = mock(Observer::class.java) as Observer<List<Photo>>

        val photosLiveData = photoRepository.fetchPhotos()
        photosLiveData.observeForever(liveDataObserver)

        // Simulate failed API response
        val call = mock(Call::class.java) as Call<List<Photo>>
        `when`(apiService.getPhotos()).thenReturn(call)

        // Act
        call.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                liveDataObserver.onChanged(emptyList())
            }
        })

        // Simulate onFailure
        call.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                liveDataObserver.onChanged(emptyList())
            }
        })

        // Verify
        verify(liveDataObserver).onChanged(emptyList())
    }
}

