# SemiMock генератор
Процессор аннотаций для создания бойлерплейтного класса переключения, какое апи юзать - mock или реальное.
Работает на основе `kapt`.
Генерация запускается при сборке проекта, точнее во время задачи `kaptKotlin`.

## Использование
- Прописать в интерфейсе `ApiService` аннотацию `SemiMockService` на класс и указать класс моковых вызовов (должен имплементировать `ApiService`).
- Прописать аннотацию у любого метода сервиса `SemiMock` и указать значение, реальный или моковый вызов необходимо использовать. 
- Методы, у которых не указана данная аннотация, будут использовать реальный вызов.
- Процессор аннотаций сгенерирует класс `SemiMockApiService`.

#### Класс ApiService может выглядеть следующим образом
```kotlin
@SemiMockService(MockAppApiService::class)
interface AppApiService {

    @SemiMock(isMocked = true)
    fun getUsers(): List<ApiUser>

    @SemiMock(isMocked = false)
    fun getUser(id: String): ObjectResponse<ApiUser>
}
```
#### Предоставление ApiService в приложение может выглядеть следующим образом
```kotlin
fun provideAppApiService(retrofit: Retrofit): AppApiService {
    val mockAppApiService = MockAppApiService()
    val appApiService = retrofit.create(AppApiService::class.java)
    return SemiMockAppApiService(
        mockService = mockAppApiService,
        apiSevice = appApiService,
    )
}
```

