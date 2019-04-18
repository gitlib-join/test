package com.ssic.kafka.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class Response<T> implements Serializable {

	private static final long serialVersionUID = 7782981616682775073L;

	public Response() {

	}

	public Response(T data) {
		this.data = data;
	}
	
	public Response(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public Response(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	public static void main(String[] args) {
		 List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		 Map<String, String> map = new HashMap<String, String>();
		  map.put("key1", "value1");
		  map.put("key2", "value2");
		  list.add(map);
		  map = new HashMap<String, String>();
			  map.put("key11", "value11");
			  map.put("key22", "value22");
			  list.add(map);
			 System.out.println(list.toString());
		  
	}

	/*public Response(int status, String message, T data, int importNum) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.importNum = importNum;
	}*/

	@Getter
	@Setter
	public int status = 200;

	@Getter
	@Setter
	public int countPage;
	
//	@Getter
//	@Setter
//	public int importNum;

	@Getter
	@Setter
	public String message;
	@Getter
	@Setter
	public String type;//0教委1团餐
	@Getter
	@Setter
	public String binding;//0未绑定
	@Getter
	@Setter
	public String redisKey;
	@Getter
	@Setter
	public String path;

	@Getter
	@Setter
	public T data;

	@Getter
	@Setter
	public Object parentData;

}