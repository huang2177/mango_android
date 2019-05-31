package com.paizhong.manggo.http;

import android.os.Handler;
import android.os.HandlerThread;
import com.google.gson.Gson;
import com.paizhong.manggo.bean.other.ChatMsgBean;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by zab on 2018/11/26 0026.
 */
public class Mq{
    private final String EXCHANGE = "rabbit_exchange_A";
    private final String TYPE = "fanout";
    private static Mq mq;
    private Gson gson;
    private Handler handler;


    public static Mq get() {
        if (mq == null){
            synchronized (Mq.class){
                if (mq == null){
                    mq = new Mq();
                }
            }
        }
        return mq;
    }

    private Mq(){

        gson = new Gson();
        HandlerThread handlerThread = new HandlerThread("MqHandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }


     Runnable runnable = new Runnable() {
         @Override
         public void run() {
             connectMq();
         }
     };


    public void startRun(){
        try {
            handler.removeCallbacks(runnable);
            handler.post(runnable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopRun(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (channel != null){
                        channel.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    if (connection !=null){
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private void connectMq(){
        try {
            if (factory == null){
                factory = new ConnectionFactory();
                //测试：192.168.1.251   预生产：47.100.180.58
                factory.setHost("r.shengqiyuankeji.com");
                //测试：5672
                factory.setPort(5672);
                //测试：mango
                factory.setUsername("mango");
                //测试：mango
                factory.setPassword("mango123");
                //网络异常重连
                factory.setAutomaticRecoveryEnabled(true);
                //心跳
                factory.setRequestedHeartbeat(60);
                //失败重连 默认5秒
                //factory.setNetworkRecoveryInterval();
            }
            if (connection == null || !connection.isOpen()){
                connection = factory.newConnection();
                channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE,TYPE,false);
                String queueName = channel.queueDeclare().getQueue();
                channel.basicQos(1);
                channel.queueBind(queueName, EXCHANGE, "");
                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                        try {
                            String message = new String(body, "UTF-8");
                            ChatMsgBean chatMsgBean = gson.fromJson(message, ChatMsgBean.class);
                            if (chatMsgBean !=null){
                                EventBus.getDefault().post(chatMsgBean);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                channel.basicConsume(queueName, true, consumer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public boolean isOpen(){
        try {
            return connection != null && connection.isOpen();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
