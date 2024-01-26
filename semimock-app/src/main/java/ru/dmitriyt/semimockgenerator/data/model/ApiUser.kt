package ru.dmitriyt.semimockgenerator.data.model

import ru.dmitriyt.semimockgenerator.data.model.city.ApiCity
import ru.dmitriyt.semimockgenerator.data.model.product.ApiProduct
import java.time.LocalDate

data class ApiUser(
    val id: String?,
    val name: String?,
    val age: Int?,
    val parent: ApiUser?,
    val street: ApiStreet?,
    val birthday: LocalDate?,
    val product: List<ApiProduct?>?,
    val points: List<Int?>?,
    val city: ApiCity?,
)
