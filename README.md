## spring-application-event-demo


# 运行SpringEventsApplication即可，这一版是使用官方版的api直接实现，每一个携带不同参数的event需要创建不同的event类型对象，不太方便，后面会进行优化。


## 2018-02-06优化
# 1，添加com.artbite008.spring.event包，引入基于event.xml的内部消息框架
# 2，每一条消息都可以持久化，方便跟踪，然后被消息消费者异步消费。
# 3，启动com.artbite008.spring.event包下的Application.java,在浏览器输入 http://localhost:8001/event/demo/publish 可以触发后台发布一条消息。
