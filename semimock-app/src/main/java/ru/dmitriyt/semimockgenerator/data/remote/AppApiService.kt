package ru.dmitriyt.semimockgenerator.data.remote

import ru.dmitriyt.semimockgenerator.SemiMock
import ru.dmitriyt.semimockgenerator.SemiMockService
import ru.dmitriyt.semimockgenerator.data.model.ApiUser

@SemiMockService(MockAppApiService::class)
interface AppApiService {

    @SemiMock(isMocked = true)
    fun getUsers(): List<ApiUser>

    @SemiMock(isMocked = false)
    fun getUser(id: String): ObjectResponse<ApiUser>
}