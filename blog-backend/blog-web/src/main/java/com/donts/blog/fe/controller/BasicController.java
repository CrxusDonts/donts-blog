
package com.donts.blog.fe.controller;

import com.donts.blog.entity.About;
import com.donts.blog.service.AboutService;
import jakarta.annotation.Resource;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Controller
public class BasicController {
    @Resource
    private AboutService aboutService;

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        val list = aboutService.list();
        return list.toString();
    }

    // http://127.0.0.1:8080/user
    @RequestMapping("/about")
    @ResponseBody
    public About about() {
        About about = new About();
        about.setContent("this is about content");
        about.setCreateTime(LocalDateTime.now());
        about.setUpdateTime(LocalDateTime.now());
        aboutService.save(about);
        return about;
    }

    // http://127.0.0.1:8080/save_user?name=newName&age=11
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }
}
