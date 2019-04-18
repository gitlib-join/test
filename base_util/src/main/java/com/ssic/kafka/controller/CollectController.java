package com.ssic.kafka.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssic.kafka.properties.KafkaConfig;
import com.ssic.kafka.util.Response;
import com.ssic.kafka.util.ResultCode;

@RestController
@RequestMapping("/kafka")
public class CollectController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private KafkaTemplate kafkaTemplate;

//	private org.springframework.integration.kafka.core.KafkaTemplate consumerKafkaTemplate;
//
//	@Autowired
//	private KafkaConfig kafkaConfig;

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public Response<String> sendKafka(HttpServletRequest request, HttpServletResponse response) {
		String message = request.getParameter("message");
		try {
			// message = "8888888888888";
			if (StringUtils.isEmpty(message)) {
				logger.info("send kafka的消息={}", message);
				return new Response(ResultCode.EXCEPTION, "发送kafka失败, send message is null!", null);
			}
			logger.info("send kafka的消息={}", message);
			kafkaTemplate.send("test", "key", message);
			logger.info("send 发送kafka成功.");
			return new Response(ResultCode.SUCCESS, "send 发送kafka成功", message);
		} catch (Exception e) {
			logger.error("发送kafka失败", e);
			return new Response(ResultCode.EXCEPTION, "send 发送kafka失败", message);
		}
	}

//	@RequestMapping(value = "/recervice", method = RequestMethod.GET)
//	public void listen(HttpServletRequest request, HttpServletResponse response) {
//		KafkaConsumer<String, String> kafkaConsumer = kafkaConfig.getKafkaConsumer();
//		kafkaConsumer.subscribe(Arrays.asList("test"));
//		boolean isTrue = false;
//		while (true) {
//			ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
//			for (ConsumerRecord<String, String> record : records) {
//				System.out.println("-----------------");
//				System.out.printf("offset = %d, value = %s", record.offset(), record.value());
//				System.out.println();
//				if ("test".equals(record.topic())) {
//					isTrue = true;
//				}
//			}
//			if (isTrue) {
////				kafkaConsumer.commitAsync(); //提交offset  打开不会重复消费
//				kafkaConsumer.commitSync(); //只消费一次,消费完消息是否提交，提交后不会重复消费
//				break;
//			}
//		}
//	}

}