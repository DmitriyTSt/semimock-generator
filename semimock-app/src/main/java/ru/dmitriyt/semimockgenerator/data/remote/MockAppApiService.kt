package ru.dmitriyt.semimockgenerator.data.remote

import ru.dmitriyt.semimockgenerator.data.model.ApiStreet
import ru.dmitriyt.semimockgenerator.data.model.ApiUser
import ru.dmitriyt.semimockgenerator.data.model.city.ApiCity
import ru.dmitriyt.semimockgenerator.data.model.product.ApiProduct
import java.time.LocalDate

object MockAppApiService : AppApiService {
    override fun getUsers(): List<ApiUser> {
        return listOf()
    }

    override fun getUser(id: String): ObjectResponse<ApiUser> {
        return ObjectResponse(
            ApiUser(
                id = "id",
                name = "name",
                age = 12,
                parent = null,
                street = ApiStreet(null, 1, 1f, 1.0),
                birthday = LocalDate.now(),
                product = listOf(ApiProduct(false)),
                points = listOf(1, null, 3),
                city = ApiCity(null)
            )
        )
    }
}