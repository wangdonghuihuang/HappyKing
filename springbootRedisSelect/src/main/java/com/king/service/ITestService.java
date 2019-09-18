package com.king.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.king.model.People;

public interface ITestService {
	boolean batchImport(String fileName, MultipartFile file) throws Exception;
	List<People> daochu();
}
