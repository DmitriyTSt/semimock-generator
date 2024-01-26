package ru.dmitriyt.semimockgenerator

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class SemiMock(val isMocked: Boolean)
