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





