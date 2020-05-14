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