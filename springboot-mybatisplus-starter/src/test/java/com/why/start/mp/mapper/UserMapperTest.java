package com.why.start.mp.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.why.start.mp.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        final List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }


    @Test
    public void testInsert() {
        for (int i = 60; i < 200; i++) {
            User user = new User();
            user.setName("mybatis-test-GGG" + i);
            user.setPwd("mybatis-test-GGG" + i);
            int insert = userMapper.insert(user);
            System.out.println(insert);
        }
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setName("mybatis-test-DDD");
        user.setPwd("mybatis-test-DDD");
        user.setId(1244266360989552642L);

        final int i = userMapper.updateById(user);
        System.out.println(i);
    }

    //测试乐观锁
    @Test
    public void testOptimisticLocker() {
        final User user = userMapper.selectById("1244266360989552642");
        user.setName("mybatis-test-EEE");
        user.setPwd("mybatis-test-EEE");
        final int i = userMapper.updateById(user);
        System.out.println(i);
    }

    //条件查询--多个id
    @Test
    public void testSelectByBatchIds() {
        final List<User> users = userMapper.selectBatchIds(Arrays.asList(1244266360989552642L, 1244272059626848257L));
        users.forEach(System.out::println);
    }

    //条件查询--byMap
    @Test
    public void testSelectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "mybatis-test-GGG");
        final List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    //测试分页插件
    @Test
    public void testPage() {
        //参数一：当前页
        //参数二：页面大小
        final Page<User> objectPage = new Page<>(1, 5);
        userMapper.selectPage(objectPage, null);
        objectPage.getRecords().forEach(System.out::println);
    }

    //===========================================

    @Test
    public void testDeleteById() {
        final int i = userMapper.deleteById(1244266360989552642L);
    }

    @Test
    public void testDeleteByBatchIds() {
        userMapper.deleteBatchIds(Arrays.asList(1244276272238993410L, 1244276272264159234L));
    }

    @Test
    public void testDeleteByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "mybatis-test-GGG");
        final int i = userMapper.deleteByMap(map);
    }

}