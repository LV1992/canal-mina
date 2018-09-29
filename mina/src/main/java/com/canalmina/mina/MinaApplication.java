package com.canalmina.mina;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class MinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinaApplication.class, args);
        log.info("MinaApplication start success !!!");
        Object obj = new Object();
        while (true) {
            synchronized (obj) {
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
