## 根据netty实现RPC框架
  
### 服务注册中心
#### 服务启动 Bootstrap
1. 扫描指定路径中的需要注册的服务及其暴露的方法，缓存在本地
2. 获取服务的实现类，初始化并注册（放入本地内存中）
3. 监听端口

#### 服务处理
1. 解析请求体，获取模块号和操作号和有关参数信息
2. 通过注册好的对象，反射执行方法，获取执行结果
3. 序列化结果并返回响应

### 客户调用端
#### 调用端启动
和服务端一样，进行扫描，缓存信息

#### 调用指定服务
1. 生成代理
2. 如果服务为本地实现类直接调用本地方法，否则进行rpc调用
3. 将调用的服务和方法封装为符合指定协议的请求体，发送请求到指定ip和端口
4. 将响应反序列化，得到调用结果
5. 返回代理的执行结果

### channel传递数据
#### 自定义序列化协议
用于RPC中传输数据，一般将数据转换为二进制格式，节省开销。
该项目采用自定义的序列化协议，以Netty的ByteBuf为基础进行序列化：https://github.com/AzeroCoder/MySerializer
#### 自定义网络协议
- 包号：即分隔符，用于分包，避免粘包现象
- 模块号：访问的资源，映射为类
- 操作号：访问的方式，映射为方法
- 具体数据的大小和内容（需要序列化）

![Snipaste_2021-03-04_10-50-20](https://user-images.githubusercontent.com/37919277/109904022-93896500-7cd7-11eb-8cb2-922c092b77f6.png)
![image](https://user-images.githubusercontent.com/37919277/109904276-f1b64800-7cd7-11eb-85e5-a249733bef57.png)

