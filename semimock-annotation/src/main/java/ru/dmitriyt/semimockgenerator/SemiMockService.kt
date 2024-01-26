package ru.dmitriyt.semimockgenerator

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class SemiMockService(val mockService: KClass<*>)
