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

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lvsen.domain.ResponseResult;
import com.lvsen.entity.FileVO;
import com.lvsen.service.FileManageService;
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
		if (StringUtils.multiIsNullOrEmpty(dept, dir)) return new ResponseResult(-100, "目录参数不能为空!");
		File dirFile = new File(session.getServletContext().getRealPath(dept + dir));
		//TODO 校验权限
		
		if (!dirFile.exists()) return new ResponseResult(-100, "该资源不存在!");
		
		ResponseResult rr;
		try {
			List<FileVO> list = fileManageService.getFileList(dirFile);
			rr = new ResponseResult();
			rr.setData(list);
			rr.setTotal(list.size());
		} catch (Exception e) {
			e.printStackTrace();
			rr = new ResponseResult(-100, "服务器异常: " + e.getMessage());
		}
		return rr;
	}
	
	/**
	 * 创建目录
	 * @return
	 */
	@RequestMapping(value = "/createDir", method = RequestMethod.POST)
	public ResponseResult createDir(String dir, HttpSession session) {
		//TODO 校验权限
		
		
		if (StringUtils.isNullOrEmpty(dir)) return new ResponseResult(-100, "目录名不能为空!");
		String realPath = session.getServletContext().getRealPath(dir);
		
		File f = new File(realPath);
		if (f.exists()) return new ResponseResult(-100, "该文件夹已存在");
		
		try {
			boolean succeed = f.mkdirs();
			if (succeed) {
				return new ResponseResult("创建成功");
			} else {
				return new ResponseResult(-100, "创建失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseResult(-100, "服务器异常: " + e.getMessage());
		}
	}
	
	/**
	 * 上传文件
	 * @param path
	 * @param file
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ResponseResult uploadFile(String path, MultipartFile file, HttpSession session) {
		//TODO 校验权限
		
		if (StringUtils.isNullOrEmpty(path)) return new ResponseResult(-100, "上传路径不能为空!");
		if (file == null) return new ResponseResult(-100, "请选择文件进行上传");
		
		try {
			String realPath = session.getServletContext().getRealPath(path);
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, file.getOriginalFilename()));
			return new ResponseResult("上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseResult(-100, "服务器异常: " + e.getMessage());
		}
	}
	
	/**
	 * 移动文件/文件夹
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	@RequestMapping(value = "/moveFile", method = RequestMethod.POST)
	public ResponseResult moveFile(String oldPath, String newPath, HttpSession session) {
		//TODO 校验权限
		
		
		if (StringUtils.multiIsNullOrEmpty(oldPath, newPath)) return new ResponseResult(-100, "参数不能为空!");
		
		String realOldPath = session.getServletContext().getRealPath(oldPath);
		File old = new File(realOldPath);
		if (!old.exists()) return new ResponseResult(-100, "源文件/文件夹 " + oldPath + " 不存在");
		String realNewPath = session.getServletContext().getRealPath(newPath);
		File neww = new File(realNewPath);
		if (neww.exists()) return new ResponseResult(-100, "目标文件/文件夹 " + newPath + " 已存在");
		
		try {
			old.renameTo(neww);
			return new ResponseResult("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseResult(-100, "服务器异常: " + e.getMessage());
		}
	}
	
	/**
	 * 重命名文件
	 * @param oldName
	 * @param newName
	 * @return
	 */
	@RequestMapping(value = "/renameFile", method = RequestMethod.POST)
	public ResponseResult renameFile(String oldName, String newName, HttpSession session) {
		//TODO 校验权限
		
		
		if (StringUtils.multiIsNullOrEmpty(oldName, newName)) return new ResponseResult(-100, "名称不能为空!");
		if (oldName.equals(newName)) return new ResponseResult(-100, "请输入新名称");
		
		String realOldName = session.getServletContext().getRealPath(oldName);
		File old = new File(realOldName);
		if (!old.exists()) return new ResponseResult(-100, "源文件/文件夹名 " + oldName + " 不存在");
		String realNewName = session.getServletContext().getRealPath(newName);
		File neww = new File(realNewName);
		if (neww.exists()) return new ResponseResult(-100, "新文件/文件夹名 " + newName + " 已存在");
		
		try {
			old.renameTo(neww);
			return new ResponseResult("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseResult(-100, "服务器异常: " + e.getMessage());
		}
	}
	
	/**
	 * 删除文件或文件夹
	 * @param path
	 * @return
	 */
	@RequestMapping(value = "/delFile", method = RequestMethod.POST)
	public ResponseResult delDir(String path, HttpSession session) {
		//TODO 校验权限
		
		
		if (StringUtils.isNullOrEmpty(path)) return new ResponseResult(-100, "路径参数不能为空!");
		
		try {
			String realPath = session.getServletContext().getRealPath(path);
			File f = new File(realPath);
			if (!f.exists()) return new ResponseResult(-100, "该文件/文件夹不存在");
			
			boolean dir = f.isDirectory();
			FileManageService.delete(f);
			return new ResponseResult("已删除该" + (dir? "文件夹" : "文件") + "");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseResult(-100, "服务器异常: " + e.getMessage());
		}
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
