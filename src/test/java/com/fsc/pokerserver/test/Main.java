package com.fsc.pokerserver.test;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;



public class Main {

	public static void main(String[] args) {
		Queue<String> queue = new LinkedBlockingDeque<String> ();
		
		queue.add("1");
		queue.add("2");
		queue.add("3");
		queue.add("4");
		queue.add("5");
		
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		

	}
	

}
