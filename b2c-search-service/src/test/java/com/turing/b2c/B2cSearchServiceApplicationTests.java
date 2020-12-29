package com.turing.b2c;

import com.turing.b2c.search.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {B2cSearchServiceApplication.class})
class B2cSearchServiceApplicationTests {
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Test
    void testAmqpAdmin(){
        //创建交换器
        amqpAdmin.declareExchange(new DirectExchange("b2c.direct"));
        //创建消息队列
        amqpAdmin.declareQueue(new Queue("item.update"));
        amqpAdmin.declareQueue(new Queue("item.delete"));
        //绑定交换器与消息队列
        amqpAdmin.declareBinding(new Binding(
                //绑定的目标
                "item.update",
                //绑定的类型
                Binding.DestinationType.QUEUE,
                //交换器
                "b2c.direct",
                //路由key
                "item.update",
                //参数
                null
        ));
        amqpAdmin.declareBinding(new Binding(
                //绑定的目标
                "item.delete",
                //绑定的类型
                Binding.DestinationType.QUEUE,
                //交换器
                "b2c.direct",
                //路由key
                "item.delete",
                //参数
                null
        ));
    }
}
