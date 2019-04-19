package com.lvsen.service;

import java.io.File;
import java.util.List;

import com.lvsen.entity.FileVO;

public interface IFileManageService {
	
	/**
	 * 获取目录下的内容
	 * @param f
	 * @return 返回null表示该目录不存在
	 */
	public List<FileVO> getFileList(File f);
}
