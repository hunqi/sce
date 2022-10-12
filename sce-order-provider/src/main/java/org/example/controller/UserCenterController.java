package org.example.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 功能描述
 *
 * @auhtor sunlei
 * @since 2022/10/11 11:36
 */
@RestController
@RequestMapping("/user/data")
public class UserCenterController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getTodayStatistic/{id}")
    @HystrixCommand(
            threadPoolKey = "getTodayStatistic",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "3"),
                    @HystrixProperty(name = "maxQueueSize", value = "100")
            },
            fallbackMethod = "getTodayStatisticFallback",// 服务降级方法
            // 使用commandProperties 可以配置熔断的一些细节信息
            commandProperties = {

                    // 类似kv形式
                    //这里这个参数意思是熔断超时时间2s，表示过了多长时间还没结束就进行熔断
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")
            }
    )
    public Integer getTodayStatistic(@PathVariable("id") Integer id){
        String url  ="http://sce-order-provider/order/data/getTodayFinishOrderNum/"+id;
        return restTemplate.getForObject(url, Integer.class);
    }

    // 服务降级方法 ，这里参数与返回值需要原方法保持一直
    public Integer getTodayStatisticFallback(Integer id){
        return -1;
    }

    @GetMapping("/getTodayStatisticA/{id}")
    @HystrixCommand(
            threadPoolKey = "getTodayStatisticA",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "2"),
                    @HystrixProperty(name = "maxQueueSize", value = "100")
            },
            fallbackMethod = "getTodayStatisticFallbackA",// 服务降级方法
            // 使用commandProperties 可以配置熔断的一些细节信息
            commandProperties = {
                    // 类似kv形式
                    //这里这个参数意思是熔断超时时间2s，表示过了多长时间还没结束就进行熔断
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")
            }
    )
    public Integer getTodayStatisticA(@PathVariable("id") Integer id){
        String url  ="http://sce-order-provider/order/data/getTodayFinishOrderNum/"+id;
        return restTemplate.getForObject(url, Integer.class);
    }

    // 服务降级方法 ，这里参数与返回值需要原方法保持一直
    public Integer getTodayStatisticFallbackA(Integer id){
        return -1;
    }
}
