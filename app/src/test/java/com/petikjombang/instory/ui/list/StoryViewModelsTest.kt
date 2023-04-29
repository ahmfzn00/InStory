package com.petikjombang.instory.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.petikjombang.instory.data.local.entity.StoryEntity
import com.petikjombang.instory.data.dummy.DataDummy
import com.petikjombang.instory.data.repo.MainDispatchRule
import com.petikjombang.instory.data.repo.StoryRepository
import com.petikjombang.instory.data.repo.getOrAwaitValue
import com.petikjombang.instory.ui.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelsTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatchRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private val dataDummy = DataDummy.generateStory()

    @Test
    fun `when getStories Should Not Null and Return Success`() = runTest {
        val data: PagingData<StoryEntity> = StoryPagingForTesting.dataPaging(dataDummy.listStory)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedStory)

        val storyViewModels = StoryViewModels(storyRepository)
        val actualStory: PagingData<StoryEntity> = storyViewModels.story.getOrAwaitValue()

        val diff = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        diff.submitData(actualStory)
        Assert.assertNotNull(diff.snapshot())
        Assert.assertEquals(dataDummy.listStory, diff.snapshot())
        Assert.assertEquals(dataDummy.listStory.size, diff.snapshot().size)
        Assert.assertEquals(dataDummy.listStory[0].id, diff.snapshot()[0]?.id)
    }
}

class StoryPagingForTesting : PagingSource<Int, LiveData<List<StoryEntity>>>(){
    companion object {
        fun dataPaging(data: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(data)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val listUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}