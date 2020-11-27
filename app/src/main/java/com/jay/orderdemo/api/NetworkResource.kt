package com.jay.orderdemo.api

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jay.orderdemo.executor.AppExecutors

/**
 * A generic class to send loading event up-stream when fetching data
 * only from network.
 *
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkResource<RequestType> @MainThread constructor() {

    /**
     * The final result LiveData
     */
    private val result = MediatorLiveData<Resource<RequestType>>()

    init {
        // Send loading state to UI
        result.value = Resource.loading(null)
        fetchFromNetwork()
    }

    /**
     * Fetch the data from network and then send it upstream to UI.
     */
    private fun fetchFromNetwork() {
        val apiResponse = fetchService()
        // Make the network call
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)

            // Dispatch the result
            response?.apply {
                when (response.isSuccessful) {
                    true -> {
                        AppExecutors.mainThread().execute {
                            setValue(Resource.success(response.body))
                        }
                    }
                    false -> {
                        AppExecutors.mainThread().execute {
                            response.message?.let {
                                setValue(Resource.error(it, null))
                            }
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<RequestType>) {
        if (result.value != newValue) result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<RequestType>> {
        return result
    }

    @MainThread//createCall
    protected abstract fun fetchService(): LiveData<ApiResponse<RequestType>>
}