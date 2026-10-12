# Uygulama Akışı
* Uygulama ilk ayağa kaldırıldığında stores.json'daki mağaza biliglerini DB'ye kaydeder.
* Kurye konum bilgisini gönderdiğinde ilk olarak o kurye DB'de var mı diye kontrol edilir. 
Normalde kuryeler token ile istek attıklarından token içerisinden ID alınır ve kontrol bu şekilde yapılır fakat
uygulama ilk ayağa kalktığında bizim DB'mizde kuryeler kayıtlı olmadığı için eğer o kurye bizde yoksa kaydediyoruz.
* Kuryeler 100m ve 60 saniye kuralına göre konum verisi işlenir.
* Kurallar sağlanırsa mağaza girişi tabloya kaydedilir.
* Test edilebilirlik için bir streamer ekledim. burada her 10 saniyede bir kuryelerden konum bilgisi geliyormuş gibi servis çağırılıyor.
* Stream edilen data işlenirken (normalde olmaması gerekse bile) console'dan gerekli bilgiler kontrol edilebilir.

# Swagger & H2
* Swagger UI: http://localhost:8080/swagger-ui/index.html
* OpenAPI JSON: http://localhost:8080/v3/api-docs
* H2 Console: http://localhost:8080/h2-console 
{
    spring.datasource.url=jdbc:h2:mem:courierdb
    spring.datasource.username=sa
    spring.datasource.password=
}

# Kullandığım Teknolojiler
* Java 17
* Spring Boot
* Spring WEB
* Spring Data JPA
* H2 Database
* Maven 
* Swagger
* Lombok

# Mimari Yapı
* Controller : HTTP isteklerini karşılar ve servis yapısını çağırır
* Service : Bussiness Logic'leri yönetir
* Repository : Veritabanı erişim işlemlerini gerçekleştirir
* Entity : Veritabanı tablolarını temsil eder.
* DTO : API request ve response'larında taşınan verileri temsil eder.

# Kullanılan Tasarım Yapıları
* DTO
* Repository Pattern
* Dependency Injection
* Katmanlı Mimari