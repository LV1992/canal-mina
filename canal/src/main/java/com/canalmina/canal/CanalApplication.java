package com.canalmina.canal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class CanalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanalApplication.class, args);
		log.info("CanalApplication start success !!!");
		Object obj = new Object();
		try {
			synchronized (obj) {
				while (true) {
					obj.wait();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
