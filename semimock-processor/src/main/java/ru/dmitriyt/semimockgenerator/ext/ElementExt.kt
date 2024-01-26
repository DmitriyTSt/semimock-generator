package ru.dmitriyt.semimockgenerator.ext

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement

/**
 * @return список вложенных элементов с типом [ElementKind.METHOD]
 */
fun Element.methods(): List<ExecutableElement> {
    return enclosedElements.filter { it.kind == ElementKind.METHOD }.map { it as ExecutableElement }
}

/**
 * Возвращает пакет для [Element], если он у него есть как родитель, иначе кидает ошибку
 */
val Element.packageName: String
    get() = enclosingElement.let { packageElement ->
        packageElement.toString().takeIf { packageElement.kind == ElementKind.PACKAGE && it != "unnamed package" }
    } ?: error("${toString()} packageName is null")