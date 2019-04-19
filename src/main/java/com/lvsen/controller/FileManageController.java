package com.lvsen.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lvsen.domain.ResponseResult;
import com.lvsen.entity.FileVO;
import com.lvsen.service.IFileManageService;
import com.lvsen.util.StringUtils;
import com.lvsen.util.ZipUtils;

@RestController
@RequestMapping("/resource")
public class FileManageController extends BaseController {
	
	@Autowired
	private IFileManageService fileManageService;
	
	/**
	 * 获取资源列表
	 * @param dept
	 * @param dir
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getFileList/{dept}", method = RequestMethod.POST)
	public ResponseResult getFileList(@PathVariable String dept, String dir, HttpSession session) {
		if (StringUtils.isNullOrEmpty(dept)) return new ResponseResult(-100, "目录参数不能为空!");
		File dirFile = new File(session.getServletContext().getRealPath(dept + dir));
		//TODO 校验权限
		
		if (!dirFile.exists()) return new ResponseResult(-100, "该资源不存在!");
		
		ResponseResult rr;
		try {
			List<FileVO> list = fileManageService.getFileList(dirFile);
			rr = new ResponseResult();
			rr.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			rr = new ResponseResult(-100, "服务器异常: " + e.getMessage());
		}
		return rr;
	}
	
	@RequestMapping(value = "/downDir", method = RequestMethod.POST)
	public void downDir(String url, HttpSession session, HttpServletResponse response) {
		//TODO 校验权限
		
		if (StringUtils.isNullOrEmpty(url)) return ;
		try {
			File dirFile = new File(session.getServletContext().getRealPath(url));
			if (!dirFile.exists()) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("该文件夹不存在, 可能已被删除");
				return ;
			} else if (!dirFile.isDirectory()) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("该对象不是文件夹!");
				return ;
			}
			
			OutputStream out = response.getOutputStream();
			response.setContentType("application/zip");
			String downloadName = dirFile.getName().substring(dirFile.getName().lastIndexOf('/') + 1);
			response.setHeader("Content-Disposition", "attachment; filename=\"folder_" + downloadName + ".zip\"");
			ZipUtils.toZip(dirFile, out, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/downFile", method = RequestMethod.POST)
	public byte[] downFile(String url, HttpSession session, HttpServletResponse response) {
		//TODO 校验权限
		
		if (StringUtils.isNullOrEmpty(url)) return null;
		File dirFile = new File(session.getServletContext().getRealPath(url));
		if (!dirFile.exists())
			return null;
		if (dirFile.isDirectory()) {
			return null;
		}

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dirFile));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
			byte[] array = new byte[1024 * 2];
			int len;
			do {
				len = bis.read(array);
				if ((len == -1))
					break;
				bos.write(array, 0, len);
			} while (true);
			bos.flush();

			// 根据后缀获得ContentType
			response.setContentType(Files.probeContentType(dirFile.toPath()));
			response.setHeader("Content-Disposition", "attachment; filename=\"" + dirFile.getName() + "\"");

			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html; charset=utf-8");
			return ("Server Error: " + e.getMessage()).getBytes();
		}
	}
}
