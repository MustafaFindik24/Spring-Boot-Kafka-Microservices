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

- Broker    : Birden fazla partitionın birlikte oluşturduğu yapıya broker denir. Her bir Kafka sunucusuna (cluster) broker adı verilir.

- Cluster   : Kafka dağıtık bir sistemdir. Birden fazla sunucudan oluşur ve verileri farklı sunucularda (cluster) tutabilir. Bu sayede daha hızlı ve daha performanslı veri akışı sağlanır.

- Offset    : Kafkanın bir subscriber (consumer) gönderdiği son mesajın numarasıdır.

- Zookeeper : Kafka cluster'da topic ve verilerin listesini saklayan, nodeların durumunu izleyen; uygulamaya bir broker eklendiği zaman veya çalışmadığı, bir problem oluştuğu zaman publisher (producer) ve subscriber (consumer) bilgilendiren bir servis olarak tanımlanabilir. 


# 📌 Apache Kafka ve Spring Boot
