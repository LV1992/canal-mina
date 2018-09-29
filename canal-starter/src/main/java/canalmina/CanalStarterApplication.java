package canalmina;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yihang.lv 2018/9/29、9:23
 */
@Slf4j
@SpringBootApplication
public class CanalStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanalStarterApplication.class, args);
        log.info("CanalStarterApplication start success !!!");
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
