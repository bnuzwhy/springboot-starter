package com.why.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@RestController
public class IndexController {
    @Autowired
    private Redisson redisson;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 手写redis锁
     * @return
     */
    @RequestMapping("/deduct_stock")
    public String deductStock(){
        String lockkey = "prodect-001";
        String clientId = UUID.randomUUID().toString();



        try {
            //redis.setnx(key,value)
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockkey, clientId,10, TimeUnit.SECONDS);
            //stringRedisTemplate.expire(lockkey,10, TimeUnit.SECONDS);
            if (!result) {
                return "error";
            }

            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减成功，剩余库存：" + realStock);

            } else {
                System.out.println("扣减失败，库存不足");
            }

        }finally {
            if(clientId.equals(stringRedisTemplate.opsForValue().get(lockkey))) {
                stringRedisTemplate.delete(lockkey);
            }
        }
            return "end";

    }

    /**
     * 使用更高级的redission锁
     * @return
     */
    @RequestMapping("/deduct_stock")
    public String deductStock2(){
        //String lockkey = "prodect-001";
        String clientId = UUID.randomUUID().toString();

        RLock redisLock = redisson.getLock(clientId);

        try {
            /*//redis.setnx(key,value)
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockkey, clientId,10, TimeUnit.SECONDS);
            //stringRedisTemplate.expire(lockkey,10, TimeUnit.SECONDS);
            if (!result) {
                return "error";
            }*/
            redisLock.lock(30,TimeUnit.SECONDS);
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减成功，剩余库存：" + realStock);

            } else {
                System.out.println("扣减失败，库存不足");
            }

        }finally {
            redisLock.unlock();
        }
        return "end";

    }

    @RequestMapping("/test")
    public String test(){
        stringRedisTemplate.opsForValue().set("stock",50+"");
        return "ok";
    }
}
