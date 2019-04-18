package com.lvsen.service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lvsen.entity.FileVO;

@Service
public class FileManageService implements IFileManageService {

	@Override
	public List<FileVO> getFileList(File f) {
		List<FileVO> list;
		
		if (f.isDirectory()) {  //是目录
			File[] listFile = f.listFiles();
			list = new ArrayList<>(listFile.length);
			for (File file : listFile) {
				FileVO vo = new FileVO();
				vo.setName(file.getName());
				vo.setLength(getFileSizeStr(getTotalSizeOfFilesInDir(file)));
				vo.setDirectory(file.isDirectory());
				list.add(vo);
			}
		} else {  //文件或其他
			list = new ArrayList<>(1);
			FileVO vo = new FileVO();
			vo.setName(f.getName());
			vo.setLength(getFileSizeStr(f.length()));
			vo.setDirectory(f.isDirectory());
			list.add(vo);
		}
		return list;
	}

	public void delete(File f) {
		if (f.isDirectory()) {
			File[] subs = f.listFiles();
			for (int i = 0; i < subs.length; i++) {
				File sub = subs[i];
				delete(sub);
			}
		}
		f.delete();
	}

	private String getFileSizeStr(long length) {
		DecimalFormat df = new DecimalFormat("#.0");
		String str;
		if (length >= 1073741824) {
			str = df.format((double)length / 1024 / 1024 / 1024) + " GB";
		} else if (length >= 1048576) {
			str = df.format((double)length / 1024 / 1024) + " MB";
		} else if (length >= 1024) {
			str = df.format((double)length / 1024) + " KB";
		} else {
			str = length + " B";
		}
		return str;
	}
	
	// 来自https://www.cnblogs.com/tomcattd/p/3380306.html
	private long getTotalSizeOfFilesInDir(final File file) {
		if (file.isDirectory()) {
			final File[] children = file.listFiles();
			long total = 0;
			if (children != null)
				for (final File child : children)
					total += getTotalSizeOfFilesInDir(child);
			return total;
		} else {
			return file.length();
		}
	}
}
