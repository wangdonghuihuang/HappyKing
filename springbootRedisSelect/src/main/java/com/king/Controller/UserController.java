package com.king.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.king.model.User;
import com.king.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {
	@Autowired
private UserService userService;
	@ResponseBody
    @RequestMapping(value = "/add", produces = {"application/json;charset=UTF-8"})
    public int addUser(User user){
        return userService.addUser(user);
    }

    @ResponseBody
    @RequestMapping(value = "/all/{pageNum}/{pageSize}", produces = {"application/json;charset=UTF-8"})
    public Object findAllUser(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){

        return userService.findAllUser(pageNum,pageSize);
    }
    @RequestMapping(value="/home")
    public String jiexi() {
    	return "/upload";
    }
    @RequestMapping(value="/socket")
    public String socketController(Model model) {
    	model.addAttribute("name","王");
    	return "websocket";
    }
    @ResponseBody
    @RequestMapping("/redisCache")
    public String getRedisCache() {
    	return userService.getRedisCacheService();
    }
    
}
