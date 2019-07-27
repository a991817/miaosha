package com.dgut.springboot.mapper;

import com.dgut.springboot.bean.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
//    @Insert("insert into user(id,username,password,email) " +
//            "values(#{id},#{username},#{password},#{email})")
//    public int insertUser(User user);
//
//    @Select("select * from user where id = #{id}")
//    public User selectOne(String id);

    @Select("select * from user where id = #{id}")
    public User getUserById(@Param("id")String id);

    @Update("update user set password = #{password} where id = #{id}")
    void updateUser(User updateUser);
}
