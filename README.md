# DDD工程结构

## 主要内容
### 1. ddd-api
- 这个模块可有可无
- 存放Controller的返回对象，以及对外提供的Controller接口（但是感觉这个接口是可有可无的，因为本身这个module就是可有可无），符合对象+行为逻辑聚合的概念，是充血模型
- 也存放了Controller的入参对象（DTO）将数据Data转化为Object

### 2. ddd-app
- 相比于mvc，ddd将启动类单独拿出来存放到一个独立的module中，存放Application
- 存放所有的Config以及spring的yml配置文件，mybatis的xml文件、logback的配置文件也放在这里
- config:用于bean的注册，还可以搭配ConfigProperties使用，用于给bean配置一些从yml文件中读取的属性
    - @Component 没有明确角色的组件
    - @Service 在业务逻辑层（Service层）使用
    - @Repositpry 在数据访问层（dao层）使用，一般是仓储类型使用，然后再由仓储调用DAO服务
    - @Controller 用于标注控制层组件
    - @RestController Controller组件使用
    - @Mapper DAO组件使用
- test单元测试，一般测试头必须要添加的注解：
    - @Slf4j
    - @RunWith(SpringRunner.class)
    - @SpringBootTest
      而且每个测试方法要以test_为前缀，并加上@Test注解来被ioc识别为测试方法

### 3. ddd-domain
- 领域服务，一般会按照领域场景来进行包的划分，例如订单、用户、认证等场景
- 每一个领域基本上会划分为model、repository以及service
- model一般会划分为aggregates、entity以及valobj（VO）
    - 值对象：表示没有唯一标识的业务实体，例如商品的名称、描述、价格等。 这么说复杂了，反正就是用于标识Entity中的属性
    - 实体对象：表示具有唯一标识的业务实体，例如订单、商品、用户等；是Service层业务逻辑的入参和返回值，不是持久化对象
    - 聚合对象：是一组相关的实体对象的根，用于保证实体对象之间的一致性和完整性，可以多种实体类聚合，当Service的方法需要实体对象的组合参数或返回值时，可以建立聚合根
- adapt，适配器层，一般分为port和repository
    - port，用于向外提供port接口，外部服务实现port接口，使用依赖倒置的方式来调取外部服务，而不是自己来直接调取外部服务，无非是不想让domain与外部服务有过多关联，是一种解耦
    - repository，仓储层，用于向外提供port接口，介于DAO和Service层之间，用于调取DAO来实现仓储服务，无非是不想让domain与DAO外部服务有过多关联，也是一种解耦，因为DAO也是一种外部服务
- service，业务逻辑存放的包，同时也和其实现类放到一起，因为这是属于领域本身所提供的服务，对于这个模块是必须可见的，所以其实现类是不需要放到infrastructure中的，是内部服务，用于存放领域的业务逻辑，并且使用前文的port、repository调用外部服务，使用Entity来作为数据传递的入参和返回结果。



## ddd-infrastructure
- dao、port、repository、po的实现类
- 其中adapter适配器存放了port、repository的实现类，dao存放DAO服务以及持久化对象po
- 调取RPC等网关服务也可以放在这里，因为一般RPC服务是被算作外部服务Port类所使用的，所以封装在Port类内部了，然后Port作为外部接口来让domain使用
- 同时外部服务需要复杂的入参和返回值的定义时，也就是DTO，需要封装在该module中
- 缓存个人感觉也是外部服务，但是本项目却在service层中直接调用了缓存，所以这个缓存要么单独封装成一个Port，要么就将它封装在别的Port中作为属性使用

## ddd-trigger
- 执行触发动作的包，比如监听器、Controller发送http请求、定时任务、消息队列
- Controller、listener、job、mq

## ddd-type
- 类似于mvc中的common
- enum、自定义异常、Constants常量、工具类util

## 一些开发的细节
### po
- po对象编写时，可以使用sql转java实体类工具
- 要使用包装类
- 小数一般使用BigDecimal,int(8)用Integer，大于这个长度一般用Long
- 属性要驼峰命名，每个属性最好都要做数据库里面的注释
- 使用Lombok那四个基本注解




