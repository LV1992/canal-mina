package canalmina.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yihang.lv 2018/9/28、11:32
 */
@Component
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 覆盖spring 获取类实例的方法
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        T obj;
        try {
            obj = applicationContext.getBean(clazz);
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    public static <T> List<T> getBeansOfType(Class<T> clazz) {
        Map<String, T> map;
        try {
            map = applicationContext.getBeansOfType(clazz);
        } catch (Exception e) {
            map = null;
        }
        return map == null ? null : new ArrayList<>(map.values());
    }

    public static Map<String,Object> getBeansWithAnnotation(Class<? extends Annotation> anno){
        Map<String,Object> map;
        try{
            map = applicationContext.getBeansWithAnnotation(anno);
        }catch (Exception e){
            map = null;
        }
        return map;
    }
}
