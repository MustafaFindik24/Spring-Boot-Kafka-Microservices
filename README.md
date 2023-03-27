# 🎯 Apache Kafka Nedir?

Kafka, herşeyin veri olarak kabul edildiği bu dönemde bize verilerin yönetimini kolaylaştıran, publish-subscriber tabanlı bir dağıtık veri akışı platformudur.

![image](https://user-images.githubusercontent.com/91599453/227554058-9150341a-4941-4d5b-bc5e-126084219ad5.png)

Gelişmiş sistemlerin birbirleriyle yaptığı veri akışı sonucunda veri kaybının önlenmesi, verilerin en hızlı ve sistemi en az etkileyecek şekilde gönderimi konusunda Kafka bize kolaylık sağlamaktadır. Veriler diske yazılır ve kafka cluster'larda kopyası oluşturularak veri kaybının önüne geçer. Gerçek zamana yakın bir şekilde veri akışını sağlar. Ölçeklenebilir olduğundan sistemde herhangi bir kesinti yaşanmaksızın sistemlerde aksama meydana gelmez. Uygulamalar arası platform desteği sağlar, bu sayede farklı platformlar arasında veri akışı sağlanmış olur. 

# 📌 Kafka Terminolojileri 

- Publisher : Veriyi gönderen uygulamadır.

- Subscriber: Veriyi alan uygulamadır.

- Producer  : Bir veya birden fazla topice veri gönderen birimdir.

- Consumer  : Bir veya birden fazla topicten veri okuyabilen birimdir.

- Topic     : Verilerin saklandığı ve listelenebildiği bir kategori. Veritabanındaki tablonun karşılığıdır.

- Partition : Veriyi tek bir yerde tutmak yerine farklı dizinlerde tutmak performans ve veri kaybının önlenmesi açısından daha verimlidir. Topicler bir veya birden fazla şekilde parçalardan (partition) oluşurlar. Bu şekilde bir topicdeki veriler birden fazla sunucuda tutulabilir.

![image](https://user-images.githubusercontent.com/91599453/227864593-bae505df-8f22-41ae-abe9-1906c0996cbf.png)

- Broker    : Birden fazla partitionın birlikte oluşturduğu yapıya broker denir. Her bir Kafka sunucusuna (cluster) broker adı verilir.

- Cluster   : Kafka dağıtık bir sistemdir. Birden fazla sunucudan oluşur ve verileri farklı sunucularda (cluster) tutabilir. Bu sayede daha hızlı ve daha performanslı veri akışı sağlanır.

- Offset    : Kafkanın bir subscriber (consumer) gönderdiği son mesajın numarasıdır.

- Zookeeper : Kafka cluster'da topic ve verilerin listesini saklayan, nodeların durumunu izleyen; uygulamaya bir broker eklendiği zaman veya çalışmadığı, bir problem oluştuğu zaman publisher (producer) ve subscriber (consumer) bilgilendiren bir servis olarak tanımlanabilir. Kafka, metadata bilgilerini saklamak için Zookeeper'ı kullanmaktadır. Bu sebeple Kafka ile kullanımı zorunludur.

# 📌 Apache Kafka ve Spring Boot

🎯 producer-service


* Bir Spring Boot projesi oluşturup Kafka kullanımı için pom.xml dosyamızın içerisine Kafka dependency eklenir.

``` xml

<dependency>
	<groupId>org.springframework.kafka</groupId>
	<artifactId>spring-kafka</artifactId>
</dependency>

```

* Kafka'yı Docker üzerinde çalıştıracağız. Bunun için docker-compose dosyası oluşturup gerekli imageları ekleyip ilgili containerları çalıştırıyoruz.

```yml

version: '3.8'
services:

  zookeeper:
    container_name: zookeeper
    image: confluentinc/cp-zookeeper:5.4.9
    restart: always
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    networks:
      - my-network

  kafka:
    container_name: kafka
    image: confluentinc/cp-kafka:6.0.9
    restart: always
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_ZOOKEEPER_CONNECT: "zookeeper:2181"
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: "1"
      KAFKA_DELETE_TOPIC_ENABLE: "true"
      KAFKA_ADVERTISED_HOST_NAME:
    networks:
      - my-network

  kafdrop:
    image: obsidiandynamics/kafdrop
    container_name: kafdrop
    restart: always
    depends_on:
      - zookeeper
      - kafka
    ports:
      - "9000:9000"
    environment:
      KAFKA_BROKER_CONNECT: kafka:29092
    networks:
      - my-network

networks:
  my-network:
    driver: bridge
    
```

* Kafkada producer ve consumer süreçlerinden bahsetmiştik. Tek bir projede bütün süreçleri kontrol etmek yerine hem producer hem de consumer için farklı projeler oluşturup microservice mimarisi mantığına göre uygulamamızı çalıştıracağız. 

application.properties dosyamızda server portunu, logging için log seviyesini ve kafkanın adresini, değerini ve topic ismini belirtiyoruz.
```properties
server.port=2333

logging.level.root= INFO

mustafafindik.kafka.address = 127.0.0.1:9092
mustafafindik.kafka.group.id = kafka-group
mustafafindik.kafka.topic = kafka-topic
```

* İlk olarak producer-service projemizde veriyi üretip kafkaya atamak için bazı configurationlar gerekli. Bunu config package ı altında oluşturup Spring IOC containerına atamak için bean olarak belirtiyoruz. application.properties sınıfında belirttiğimiz value ları ekleyip veriyi göndermek için kafkaTemplate sınıfını, veriyi üretmek içinde producerFactory sınıfını belirtiyoruz.

```java
@Configuration
public class KafkaConfiguration {

    @Value("${mustafafindik.kafka.address}")
    private String kafkaAddress;
    
    @Value("${mustafafindik.kafka.group.id}")
    private String groupId;
    
    @Bean
    public KafkaTemplate<String, User> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public ProducerFactory producerFactory(){
        Map<String,Object> producer = new HashMap<>();
        producer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        producer.put(JsonSerializer.ADD_TYPE_INFO_HEADERS,true);
        producer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        return new DefaultKafkaProducerFactory(producer);
    }
}
```
* Modelimizi oluşturup Kafka'ya veri gönderimini sağlamak için KafkaProducer sınıfını oluşturduk. KafkaTemplate sınıfını inject edip send() metoduyla veri gönderimi için ortam hazırlandı.

```java
@Component
public class KafkaProducer {
    
    private final KafkaTemplate<String, User> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void userProducer(User user){
        kafkaTemplate.send("${mustafafindik.kafka.topic}", UUID.randomUUID().toString(),user);
    }
}
```
* Service sınıfımızı oluşturup KafkaProducer sınıfını inject edip Kafka'ya veri gönderimi için metot oluşturduk ve KafkaProducer sınıfındaki metodu kullandık.

```java
@Service
public class UserServiceImpl implements UserService{
    private final KafkaProducer kafkaProducer;

    public UserServiceImpl(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
    @Override
    public void createUser(User user) {
        User saveUser = new User();
        saveUser.setUsername(saveUser.getUsername());
        saveUser.setPassword(saveUser.getPassword());
        kafkaProducer.userProducer(saveUser);
    }
}
```

* Controller sınıfında post isteği için metot oluşturduk ve logunu görebilmek için console ekranına info bastırdık.

```java
@Slf4j
@RestController
@RequestMapping("/message")
public class UserController {

    @Value("${mustafafindik.kafka.topic}")
    private String topic;
    private final KafkaTemplate<String, User> kafkaTemplate;

    public UserController(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @PostMapping
    public void sendMessage(@RequestBody User user){
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), user);
        log.info("User class send to the queue : " + user);
    }
}
```

* localhost:2333/message pathine JSON verisi olarak post isteği attıktan sonra console ekranında log görülebilir.

![image](https://user-images.githubusercontent.com/91599453/227879283-fc652d82-fd18-4f16-86e0-0736a3658b98.png)

* Docker-compose dosyamızı çalıştırmıştık. http://localhost:9000/ giderek ayağa kaldırdığımız Kafdrop uygulamasını ve aşağıda oluşturduğumuz topic i görebiliriz.

![image](https://user-images.githubusercontent.com/91599453/227879727-8d6305d7-eb8a-46d2-91a2-b04a2b51e7b6.png)

* İlgili topic e girip mesajları listele dedikten sonra attığımız post isteklerini bazı metadatalarında belirtilip listelendiğini görebiliriz.

![image](https://user-images.githubusercontent.com/91599453/227880270-406e8995-e1c4-4ffa-9cac-0893854a22d6.png)

producer-service projemizin yaptığı işlem bu kadar. Şimdi consumer-service projemizi inceleyelim.

🎯 consumer-service

* Kafka'daki veriyi dinleyip gelen veriyi veritabanına kaydedeceğiz. Bunun için öncelikle bir Spring Boot projesi oluşturup ilgili dependencylerimizi pom.xml dosyamıza ekliyoruz.

```xml
<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
	<scope>runtime</scope>
</dependency>
<dependency>
	<groupId>org.springframework.kafka</groupId>
	<artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

* Postgresql bağlantısını ve gerekli Kafka bağlantılarını sağlamak için application.properties dosyamıza property eklemesi gerçekleştiriyoruz.

```properties
mustafafindik.kafka.address = 127.0.0.1:9092
mustafafindik.kafka.group.id = kafka-group
mustafafindik.kafka.topic = kafka-topic

spring.datasource.url=jdbc:postgresql://localhost:5432/kafkapostgre
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.hikari.auto-commit=false
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

* Kafka'yı dinlemek ve gelen veriyi tüketmek (veritabanına kaydetmek gibi) için KafkaConfiguration sınıfı oluşturup kafkaListenerContainerFactory()
ve consumerFactory() metotlarımızı oluşturuyoruz.

```java
@Configuration
public class KafkaConfiguration {

    @Value("${mustafafindik.kafka.address}")
    private String kafkaAddress;
    @Value("${mustafafindik.kafka.group.id}")
    private String groupId;
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, User> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, User> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
    @Bean
    public ConsumerFactory<String, User> consumerFactory() {
        Map<String,Object> consumer = new HashMap<>();
        consumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        consumer.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumer.put(JsonDeserializer.VALUE_DEFAULT_TYPE, User.class);
        consumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumer);
    }
}
```

* producer-service projemiz ile aynı olacak şekilde modelimizi oluşturup UserConsumer sınıfı içerisinde @KafkaListener ile topic ve groupId lerini belirterek Kafka'daki veriyi dinliyoruz. 

```java
@Slf4j
@Component
public class UserConsumer {
    private final UserService userService;
    public UserConsumer(UserService userService) {
        this.userService = userService;
    }
    @KafkaListener(
            topics = "${mustafafindik.kafka.topic}",
            groupId = "${mustafafindik.kafka.group.id}")
    public void userConsumer(User user){
        log.info("User received from Kafka pool. Username : {} , password : {}",
                user.getUsername(),
                user.getPassword());
        userService.saveUser(user);
    }
}
```

* Repository ve service sınıflarımızı oluşturup veritabanına Kafka'daki veriyi kayıt ediyoruz.

```java

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void saveUser(User user) {
        User saveUser = new User();
        saveUser.setUsername(user.getUsername());
        saveUser.setPassword(user.getPassword());
        userRepository.save(saveUser);
        log.info("User saved to the database : " + saveUser.toString());
    }
}
```

* localhost:2333/message pathine POST isteği attığımız zaman consumer-service deki console ekranının log ekranı bu şekildedir.
![image](https://user-images.githubusercontent.com/91599453/227889407-ac698cf3-b5cf-4152-845a-628fedba2480.png)

* pgadmine geldiğimizde ise ilgili tabloya veriyi kaydettiğini görebiliriz.

![image](https://user-images.githubusercontent.com/91599453/227889952-c83296bf-6ed8-4327-b6e7-5f19c85b3454.png)




