package ru.dmitriyt.semimockgenerator

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import ru.dmitriyt.semimockgenerator.ext.javaToKotlinType
import ru.dmitriyt.semimockgenerator.ext.methods
import ru.dmitriyt.semimockgenerator.ext.packageName
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

private const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@OptIn(DelicateKotlinPoetApi::class)
class SemiMockProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(SemiMockService::class.java.canonicalName, SemiMock::class.java.canonicalName)
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        roundEnv?.getElementsAnnotatedWith(SemiMockService::class.java)?.forEach { apiService ->
            val mockServiceTypeMirror: TypeMirror = try {
                apiService.getAnnotation(SemiMockService::class.java).mockService
                null
            } catch (e: MirroredTypeException) {
                e.typeMirror
            } ?: error("${apiService.simpleName} mockService get error")

            val methods = apiService.methods()

            val file = File(processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()).apply { mkdir() }

            val semiMockClassName = "SemiMock${apiService.simpleName}"
            val semiMockConstructor = FunSpec.constructorBuilder()
                .addParameter("mockService", mockServiceTypeMirror.asTypeName())
                .addParameter("apiService", apiService.asType().asTypeName())
                .build()
            val mockServiceProperty = PropertySpec
                .builder("mockService", mockServiceTypeMirror.asTypeName(), KModifier.PRIVATE)
                .initializer("mockService")
                .build()
            val apiServiceProperty = PropertySpec
                .builder("apiService", apiService.asType().asTypeName(), KModifier.PRIVATE)
                .initializer("apiService")
                .build()
            FileSpec.builder(apiService.packageName, semiMockClassName)
                .indent("    ")
                .addType(
                    TypeSpec.classBuilder(semiMockClassName)
                        .primaryConstructor(semiMockConstructor)
                        .addSuperinterface(apiService.asType().asTypeName())
                        .addProperty(mockServiceProperty)
                        .addProperty(apiServiceProperty)
                        .apply {
                            methods.forEach { method ->
                                addFunction(getMethodFunction(method))
                            }
                        }
                        .build()
                )
                .build()
                .writeTo(file)
        }

        return true
    }

    private fun getMethodFunction(method: ExecutableElement): FunSpec {
        val isMocked = method.getAnnotation(SemiMock::class.java)?.isMocked == true
        val serviceToInvoke = if (isMocked) "mockService" else "apiService"
        val paramsToInvoke = method.parameters.joinToString(", ") { it.simpleName }
        return FunSpec.builder(method.simpleName.toString())
            .apply {
                method.parameters.forEach {
                    addParameter(
                        it.simpleName.toString(),
                        it.asType().asTypeName().javaToKotlinType(),
                    )
                }
            }
            .returns(method.returnType.asTypeName().javaToKotlinType())
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("return $serviceToInvoke.${method.simpleName}($paramsToInvoke)")
            .build()
    }
}
