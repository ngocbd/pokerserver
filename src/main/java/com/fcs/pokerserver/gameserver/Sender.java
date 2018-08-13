/*
The MIT License (MIT)
Copyright (c) 2018 by Ngocbd
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.fcs.pokerserver.gameserver;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;


/**
 * The messages is added into the MessageQueue
 * */
class MessageQueue 
{	
	private String topic;
	private String content;
	/**
	 * The constructor with 2 parameters topic and content.
	 * @param String topic, String content
	 * */
	public MessageQueue(String topic, String content) {
		super();
		this.topic = topic;
		this.content = content;
	}
	
	/**
	 * Return the topic.
	 * @return String topic.
	 * */
	public String getTopic() {
		return topic;
	}
	
	/**
	 * The method to set value for topic
	 * @param String topic
	 * */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	/**
	 * Return the Content.
	 * @return String content
	 * */
	public String getContent() {
		return content;
	}
	
	/**
	 * The method to set value for the content.
	 * @param String content
	 * */
	public void setContent(String content) {
		this.content = content;
	}
}

/**
 * The class to get the messages in the Queue to send.
 * */
public class Sender extends Thread{
	Queue<MessageQueue> queue = new LinkedBlockingDeque<MessageQueue> ();
	MqttClient client;
	
	/**
	 * The constructor with the parameter is MqqtClient and start thread. 
	 * */
	public Sender(MqttClient client) {
		super();
		this.client = client;
		this.start();
	}

	/**
	 * The method to add the topic, content into the Queue.
	 * @param String topic, String content.
	 * */
	public void add(String topic,String content)
	{
		queue.add(new MessageQueue(topic, content));
	}
	
	/**
	 * The method to run the queue.
	 * */
	public void run()
	{
		try {
			while(1==1)
			{
				MessageQueue mq = queue.poll();
				if(mq==null) continue;
				else
				{
					//Thread.sleep(1);
					//System.out.println(mq.getTopic()+":"+mq.getContent());
					client.publish(mq.getTopic(), mq.getContent().getBytes(), 2, false);
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The main method
	 * */
	public static void main(String[] args) {
		MqttClient myClient;
		MqttConnectOptions connOpt;

		String BROKER_URL = "tcp://broker.hivemq.com:1883";
		String SERVER_TOPIC = "/pokerserver/server";
		
		connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		

		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, "pokerserver"+System.currentTimeMillis());
			myClient.connect(connOpt);
			Sender sender= new Sender(myClient);
			sender.add("/pokerserver/server", "test1");
			sender.add("/pokerserver/server", "test2");
			sender.add("/pokerserver/server", "test3");
			sender.add("/pokerserver/server", "test4");
			
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}


