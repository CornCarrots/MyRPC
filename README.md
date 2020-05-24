## 手写netty服务器

### 服务器结构
#### 服务器-启动器 Bootstrap
- 线程池 主从模式
    - 主线程：接收客户端请求
    - 从线程：业务逻辑处理
- 信息通道 默认为SocketChannel
- 初始化器 自定义
- 绑定端口，同步启动
- 同步关闭
#### 服务器-初始器 Initializer
对信息通道 channel 添加拦截器
#### 客户端处理器 Handler
接收 HTTP+上下文，进行处理
### 流程
1. 加入handler 处理客户端
2. channel 注册
3. channel 活跃
4. channel 处理逻辑
5. channel 处理完成
6. channel 睡眠
7. channel 注销
8. 移除handler

### channel传递数据
#### 自定义序列化协议
用于RPC中传输数据，一般将数据转换为二进制格式，节省开销。
该项目采用自定义的序列化协议，以Netty的ByteBuf为基础进行序列化：https://github.com/AzeroCoder/MySerializer
#### 自定义请求和响应协议
- 包号：即分隔符，用于分包，避免粘包现象
- 模块号：访问的资源
- 操作号：访问的方式
- 具体数据的大小和内容（需要序列化）

#### 编解码
netty在传输数据时，需要对请求进行编码，对响应进行解码。
可以通过继承ByteToMessageDecoder和MessageToByteEncoder来实现，将具体的请求或响应序列化，用ByteBuf写入缓冲区；将缓存区的内容从ByteBuf取出，反序列化并进行处理。

#### 处理器
netty的处理器链在pipeline中，逐步传递并处理。pipeline底层是一个双向链表，用context包装起来，其实就是一种“责任链”设计模式。
当netty读写数据时，会在pipeline中查找可以处理的处理器，通过fireChannelRead传递链表。
