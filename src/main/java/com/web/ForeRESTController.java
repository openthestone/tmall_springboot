package com.web;


import com.pojo.Product;
import com.pojo.User;
import com.service.ProductService;
import com.service.UserService;
import com.util.Page4Navigator;
import com.util.Result;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ForeRESTController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

//    @GetMapping("/fore")
//    public Page4Navigator<Product> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
//        start = start < 0 ? 0 : start;
//        Page4Navigator<Product> page = productService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
//        return page;
//    }
    @PostMapping("/foreregister")
    public Object register(@RequestBody User user,HttpSession session) {
        String name =  user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);

        if(exist){
            String message ="用户名已经被使用,不能使用";
            return Result.fail(message);
        }
        user.setPassword(password);
        userService.add(user);
        session.setAttribute("user", user);
        return Result.success();
    }
    @PostMapping("/forelogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name =  userParam.getName();
        name = HtmlUtils.htmlEscape(name);
        User user =userService.get(name,userParam.getPassword());
        if(null==user){
            String message ="账号密码错误";
            return Result.fail(message);
        }
        else{
            session.setAttribute("user", user);
            return Result.success();
        }
    }
    @PostMapping("foresearch")
    public Object search(String sizex, String sizey, String sizez, String strainX, String strainY, String NX, String NY, String elecX, String elecY,
                         String elecZ){
        List<Product> ps= productService.search(sizex,sizey, sizez, strainX, strainY, NX, NY, elecX, elecY, elecZ,0,20);
        return ps;
    }
}