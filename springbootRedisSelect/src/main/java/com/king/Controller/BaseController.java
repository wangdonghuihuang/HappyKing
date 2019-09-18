package com.king.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.king.service.BaseService;

@Controller
@RequestMapping(value="/base")
public class BaseController {
	@Autowired
	private BaseService baseService;
@ResponseBody
@RequestMapping(value="/queryAll")
public int queryCountController() {
	int num=baseService.queryCountService();
	return num;
}
}
