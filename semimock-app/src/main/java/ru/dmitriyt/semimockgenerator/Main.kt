package ru.dmitriyt.semimockgenerator

import ru.dmitriyt.semimockgenerator.data.remote.MockAppApiService
import ru.dmitriyt.semimockgenerator.data.remote.SemiMockAppApiService

fun main() {
    val users = SemiMockAppApiService(MockAppApiService, MockAppApiService).getUsers()
}