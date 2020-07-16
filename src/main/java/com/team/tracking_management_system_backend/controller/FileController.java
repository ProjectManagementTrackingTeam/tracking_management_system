package com.team.tracking_management_system_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.tracking_management_system_backend.entity.MyProjectFile;
import com.team.tracking_management_system_backend.entity.Project;
import com.team.tracking_management_system_backend.service.ProjectFileService;
import com.team.tracking_management_system_backend.service.ProjectService;
import com.team.tracking_management_system_backend.vo.MessageVO;
import org.hibernate.cache.spi.FilterKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class FileController {
    @Autowired
    private TextEncryptor textEncryptor;
    @Autowired
    private ProjectFileService fileService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ObjectMapper objectMapper;

    @ResponseBody
    @PostMapping("/upload/{projectId}")
    public Map uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("projectId") int projectId) {
        if (file.isEmpty()) {
            return Map.of("message", new MessageVO("文件为空"));
        }
        try {
            //文件写入
            Project project;
            if ((project = projectService.findProjectById(projectId)) == null) {
                return Map.of("message", new MessageVO("项目不存在"));
            }

            String fileName = file.getOriginalFilename();
//            System.out.println("filename=========" + fileName);

            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            File dest = new File(MyProjectFile.FILEPATH + project.getName() + "\\" + fileName);
            MyProjectFile projectFile = new MyProjectFile();
            projectFile.setName(fileName);
            projectFile.setFileKey(textEncryptor.encrypt(fileName));
            projectFile.setProject(project);
            fileService.addProjectFile(projectFile);
            //先处理完数据库再写入磁盘
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            file.transferTo(dest);
            return Map.of("files", fileService.getFileFromProject(projectId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Map.of("message", new MessageVO("文件上传失败"));
    }
    @GetMapping("/delete/{fileKey}")
    @ResponseBody
    public Map deleteFile(@PathVariable("fileKey") String fileKey){
        if (fileKey == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "文件密钥错误");
        }
        List<MyProjectFile> projectFiles = fileService.getProjectFiles();
        if (projectFiles == null || projectFiles.size() == 0){
            return Map.of("message",new MessageVO("没有该文件"));
        }
        List<MyProjectFile> collect = projectFiles.stream()
                .filter(t -> t.getFileKey().equals(fileKey))
                .limit(1)
                .collect(Collectors.toList());
        if (collect == null || collect.size() == 0){
            return Map.of("message",new MessageVO("文件不存在"));
        }
        Project project = collect.get(0).getProject();
        try {
            String name = textEncryptor.decrypt(fileKey);
            File file = new File(MyProjectFile.FILEPATH + project.getName() +"\\" +name);
            if (!file.exists()){
                return Map.of("message",new MessageVO("文件不存在"));
            }else{
                file.delete();
                fileService.deleteProjectFile(fileKey);
                return Map.of("files", fileService.getFileFromProject(project.getId()));
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "文件密钥错误");
        }


    }

    @GetMapping("/download/{fileKey}")
    public String downloadFile(@PathVariable("fileKey") String fileKey, HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = textEncryptor.decrypt(fileKey);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "文件密钥错误");
        }
        HashMap<String, MessageVO> map = new HashMap<>();
        List<MyProjectFile> projectFiles = fileService.getProjectFiles();
        List<MyProjectFile> collect = projectFiles.stream().filter(t -> t.getFileKey().equals(fileKey))
                .limit(1)
                .collect(Collectors.toList());
        if (collect == null || collect.size() == 0) {
            map.put("message", new MessageVO("该文件不存在"));
            try {
                return objectMapper.writeValueAsString(map);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        MyProjectFile myProjectFile = collect.get(0);
        String fileName = myProjectFile.getName();
        File file = new File(MyProjectFile.FILEPATH + myProjectFile.getProject().getName() + "\\" + myProjectFile.getName());
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            try {
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ";filename*=UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return objectMapper.writeValueAsString(Map.of("message", new MessageVO("下载完成")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return objectMapper.writeValueAsString(Map.of("message", new MessageVO("该文件不存在")));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
