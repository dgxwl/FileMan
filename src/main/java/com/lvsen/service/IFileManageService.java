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
	
	/**
	 * 将给定的File所表示的文件或目录删除
	 * @param f
	 */
	public void delete(File f);
}
