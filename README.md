# 【保姆级SSM教程】高并发朋友圈点赞项目设计

## 项目部署方法：

- `git clone https://github.com/JackieLing/Sell-Circle-Option.git`

### 所用技术栈：

- java
- spring
- mybatis
- springmvc

- html+css+js
- jsp
- servlet
- mysql
- redis
- MQ

项目还在维护更新中，`Start`一下可以和我一起维护呀，我会持续开发最新模块，完善项目。

github地址：https://github.com/JackieLing/Sell-Circle-Option.git

# 一、数据库建表设计

## 1.1、创建user表(用户表)

```sql
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : springmvc-mybatis-book

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-01 18:11:23
*/

-- ----------------------------
-- Table structure for ay_user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `account` varchar(20) default null,
  PRIMARY KEY (`id`),
  key `user_name_index`(`name`) using btree,
  key `user_account_index`(`account`) using btree
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

```

## 1.2、创建mood表(说说表)

```sql
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : springmvc-mybatis-book

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-01 18:11:23
*/
-- ----------------------------
-- Table structure for  mood
-- ----------------------------

CREATE TABLE `mood` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `content` varchar(256) DEFAULT NULL,
  `user_id` varchar(32) default null,
  `publish_time` datetime default null,
  `praise_num` int(11) default null,
  PRIMARY KEY (`id`),
  key `mood_user_id_index` (`user_id`) using btree
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

```

## 1.3、创建user_mood_praise_rel表(点赞关联表)

```sql
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : springmvc-mybatis-book

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-01 18:11:23
*/
-- ----------------------------
-- Table structure for  user_mood_praise_rel
-- ----------------------------

CREATE TABLE `user_mood_praise_rel` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) default null,
  `mood_id` varchar(32) default null,
  PRIMARY KEY (`id`),
  key `user_mood_praise_rel_user_id_index`(`user_id`) using btree,
  key `user_mood_praise_rel_mood_id_index`(`mood_id`) using btree
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

```

## 1.4、数据库初始化数据

### 1.4.1、user表初始化数据

```sql
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : springmvc-mybatis-book

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-01 18:11:23
*/
-- ----------------------------
-- Table structure for  user_mood_praise_rel  data  insert
-- ----------------------------
insert into `user` VALUES ('3','阿毅','ay');
insert into `user` VALUES ('2','阿兰','al');
```

### 1.4.2、mood表初始化数据

```sql
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : springmvc-mybatis-book

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-01 18:11:23
*/

-- ----------------------------
-- Table structure for  mood  data  insert
-- ----------------------------
insert into `mood` VALUES ('1','今天天气真好','1','2018-06-30 22:09:06','100');
insert into `mood` VALUES ('2','厦门真美，么么哒！','2','2018-07-29 17:09:06','99');

```

# 二、实体类设计

## 2.1、实体类结构图示：进行属性封装

![](https://cdn.jsdelivr.net/gh/JackieLing/mage1/img/20201221104404.png)

## 2.2、创建对应实体类的Dao层和Mapper文件

### 2.2.1、创爱`User`类的`Dao`层`UserDao`接口，生成一个抽象方法`findAll`方法用户查询用户。

![](https://cdn.jsdelivr.net/gh/JackieLing/mage1/img/20201221105109.png)

```java
package com.ay.dao;

import com.ay.model.User;

public interface UserDao {
    //查询用户
    User findAll(int id);
}

```

### 2.2.2、在resource目录下创建mapper文件，新建一个UserMapper.xml文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ay.dao.UserDao">
    <resultMap id="userMap" type="com.ay.model.User">
        <id property="id" column="id"/>
        <id property="name" column="name"/>
        <id property="account" column="account"/>
    </resultMap>
 
    <sql id="table_column">
        id,
        name,
        account
    </sql>

    <select id="find" resultMap="userMap">
        select
        <include refid="table_column"/>
        from user
        <where>
            id=#{id}
        </where>
    </select>
</mapper>

```

### 2.2.3、在dao层新建 MoodDao接口

```java
package com.ay.dao;

import com.ay.model.Mood;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodDao {

    List<Mood> findAll();
}

```

### 2.2.4、在resource目录的mapper文件下创建MoodMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.ay.dao.MoodDao">
    <cache-ref namespace="com.ay.dao.UserDao"/>

    <resultMap id="moodMap" type="com.ay.model.Mood">
        <id property="id" column="id"/>
        <result property="content" column="content"/>
        <result property="userId" column="user_id"/>
        <result property="praiseNum" column="praise_num"/>
        <result property="publishTime" column="publish_time"/>
    </resultMap>

    <sql id="table_column">
        id,
        content,
        user_id,
        praise_num,
        publish_time
    </sql>

    <select id="findAll" resultMap="moodMap">
        select
        <include refid="table_column"/>
        from mood
    </select>
</mapper>

```

### 2.2.5、在dao层创建UserMoodPraiseRelDao接口

```java
package com.ay.dao;

import com.ay.model.UserMoodPraiseRel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 描述：用户说说点赞关联DAO
 *
 * @author Ay
 * @create 2018/07/01
 **/
@Repository
public interface UserMoodPraiseRelDao {

    boolean save(@Param("userMoodPraiseRel") UserMoodPraiseRel userMoodPraiseRel);
}

```

### 2.2.6在mapper文件下创建UserMoodPraiseRelMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ay.dao.UserMoodPraiseRelDao">

    <insert id="save" useGeneratedKeys="true" keyProperty="id"
            parameterType="com.ay.model.UserMoodPraiseRel">
        insert into user_mood_praise_rel (user_id, mood_id)
        VALUE (#{userMoodPraiseRel.userId}, #{userMoodPraiseRel.moodId})
    </insert>
</mapper>

```



## 2.3、创建Service层和DTO类

#### 2.3.1创建dto包下的MoodDTO类

```java
package com.ay.dto;

import com.ay.model.Mood;

import java.io.Serializable;

/**
 * 描述：说说DTO
 *
 * @author Ay
 * @date 2018/1/6.
 */
public class MoodDTO extends Mood implements Serializable {
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户的账号
     */
    private String userAccount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
}

```

#### 2.3.2创建dto包下的UserDTO类

```java
package com.ay.dto;

import com.ay.model.User;

/**
 * 描述：用户DTO
 *
 * @author Ay
 * @create 2018/07/01
 **/
public class UserDTO extends User {

}

```

#### 2.3.4在Service层创建UserService接口

```java
package com.ay.service;

import com.ay.dto.UserDTO;

/**
 * 描述：用户服务接口
 *
 * @author Ay
 * @date 2018/1/6.
 */
public interface UserService {

    UserDTO find(String id);

}

```

#### 2.3.5在Service层创建MoodService接口

```java
package com.ay.service;

import com.ay.dto.MoodDTO;

import java.util.List;

public interface MoodService {
    //查询所有的说说
    List<MoodDTO> findAll();
}

```

#### 2.3.6在service的Impl文件下新建UserServiceImpl.java

```java
package com.ay.service.impl;

import com.ay.dao.UserDao;
import com.ay.dto.UserDTO;
import com.ay.model.User;
import com.ay.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：用户服务类
 *
 * @author Ay
 * @date 2018/1/6.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public UserDTO find(String id) {
        User user = userDao.find(id);
        return converModel2DTO(user);
    }

    private UserDTO converModel2DTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setAccount(user.getAccount());
        userDTO.setName(user.getName());
        return userDTO;
    }
}

```

#### 2.3.7、在service的Impl文件下新建MoodServiceImpl.java

```java
package com.ay.service.impl;

import com.ay.dao.MoodDao;
import com.ay.dao.UserDao;
import com.ay.dto.MoodDTO;
import com.ay.model.Mood;
import com.ay.model.User;
import com.ay.service.MoodService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述：说说服务类
 *
 * @author Ay
 * @date 2018/1/6.
 */
@Service
public class MoodServiveImpl implements MoodService {
    @Resource
    private MoodDao moodDao;
    @Resource
    private UserDao userDao;

    public List<MoodDTO> findAll() {
        List<Mood> moodList = moodDao.findAll();
        return converModel2DTO(moodList);
    }

    private List<MoodDTO> converModel2DTO(List<Mood> moodList) {
        if (CollectionUtils.isEmpty(moodList)) return Collections.EMPTY_LIST;
        List<MoodDTO> moodDTOList = new ArrayList<MoodDTO>();
        for (Mood mood : moodList) {
            MoodDTO moodDTO = new MoodDTO();
            moodDTO.setId(mood.getId());
            moodDTO.setContent(mood.getContent());
            moodDTO.setPraiseNum(mood.getPraiseNum());
            moodDTO.setPublishTime(mood.getPublishTime());
            moodDTO.setUserId(mood.getUserId());
            moodDTOList.add(moodDTO);
            //设置用户信息
            User user = userDao.find(mood.getUserId());
            moodDTO.setUserName(user.getName());
            moodDTO.setUserAccount(user.getAccount());
        }
        return moodDTOList;
    }
}

```

#### 2.3.8、在service文件下新建MoodServiceImpl接口

```java
package com.ay.service;

import com.ay.model.UserMoodPraiseRel;

/**
 * 描述：用户说说点赞关联接口
 *
 * @author Ay
 * @date 2018/1/6.
 */
public interface UserMoodPraiseRelService {

    boolean save(UserMoodPraiseRel userMoodPraiseRel);
}

```

#### 2.3.9、在service的Impl文件下新建UserMoodPraiseRelServiceImpl

```java
package com.ay.service.impl;

import com.ay.dao.UserMoodPraiseRelDao;
import com.ay.model.UserMoodPraiseRel;
import com.ay.service.UserMoodPraiseRelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：用户说说点赞关联服务类
 *
 * @author Ay
 * @date 2018/1/6.
 */
@Service
public class UserMoodPraiseRelServiceImpl implements UserMoodPraiseRelService {

    @Resource
    private UserMoodPraiseRelDao userMoodPraiseRelDao;

    public boolean save(UserMoodPraiseRel userMoodPraiseRel) {
        return userMoodPraiseRelDao.save(userMoodPraiseRel);
    }
}

```

## 2.4、Controller层和前端页面

#### 2.4.1创建Controller层下的UserController

```java
package com.ay.controller;

import com.ay.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 描述：用户控制层
 *
 * @author Ay
 * @date 2018/6/6.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
}

```

#### 2.4.2创建Controller层下的MoodController

```java
package com.ay.controller;

import com.ay.dto.MoodDTO;
import com.ay.service.MoodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：说说控制层
 *
 * @author Ay
 * @date 2018/1/6.
 */
@Controller
@RequestMapping("/mood")
public class MoodController {

    @Resource
    private MoodService moodService;

    @GetMapping(value = "/findAll")
    public String findAll(Model model) {
        List<MoodDTO> moodDTOList = moodService.findAll();
        model.addAttribute("moods", moodDTOList);
        return "mood";
    }

}

```

#### 2.4.3、在web-inf下创建views文件下创建mood.jsp

```jsp
<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>

<div id="moods">
    <b>说说列表:</b><br>
    <c:forEach items="${moods}" var="mood">
        ------------------------------------
        <br>
        <b>用户：</b><span id="account">${mood.userName}</span><br>
        <b>说说内容：</b><span id="content">${mood.content}</span><br>
        <b>发表时间：</b>
        <span id="publish_time">
                ${mood.publishTime}
        </span><br>
        <b>点赞数：</b><span id="praise_num">${mood.praiseNum}</span><br>
        <div style="margin-left: 350px">
                <%--<a id="praise" href="/mood/${mood.id}/praise?userId=${mood.userId}">赞</a>--%>
            <a id="praise" href="/mood/${mood.id}/praiseForRedis?userId=${mood.userId}">赞</a>
        </div>
    </c:forEach>
</div>
</body>
<script></script>
</html>


```

# 三、测试

**http://localhost:8080/SSM_war/mood/findAll**

![](https://cdn.jsdelivr.net/gh/JackieLing/mage1/img/20201227214140.png)