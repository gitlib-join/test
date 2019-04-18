package com.ssic.kafka.properties;

import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
	 
    @Value("${kafka.consumer.servers}")
    private String servers;
    @Value("${kafka.consumer.enable.auto.commit}")
    private boolean enableAutoCommit;
    @Value("${kafka.consumer.session.timeout}")
    private String sessionTimeout;
    @Value("${kafka.consumer.auto.commit.interval}")
    private String autoCommitInterval;
    @Value("${kafka.consumer.group.id}")
    private String groupId;
    @Value("${kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${kafka.consumer.concurrency}")
    private int concurrency;
	
	Properties properties = new Properties();
	
	public void init() {
	     properties.put("bootstrap.servers", this.servers);//xxx是服务器集群的ip
	     properties.put("group.id", this.groupId);
	     properties.put("enable.auto.commit", this.enableAutoCommit);
	     properties.put("auto.commit.interval.ms", this.autoCommitInterval);
	     properties.put("auto.offset.reset", this.autoOffsetReset);
	     properties.put("session.timeout.ms", this.sessionTimeout);
	     properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	}

	public KafkaProducer<String, String> getKafkaProducer(){
		init();
		return  new KafkaProducer<>(properties);
	}
	
	public KafkaConsumer<String, String> getKafkaConsumer(){
		init();
		return  new KafkaConsumer<>(properties);
	}
	
	
    
}
