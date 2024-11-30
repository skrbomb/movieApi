package com.movie.service.File;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {

        //get name of the file
        String fileName=file.getOriginalFilename();

        //build the file path
        String filePath=path+ File.separator+fileName;

        //create file object
        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        //copy the file or upload file to the path
        Files.copy(file.getInputStream(), Paths.get(filePath));
        /* 使用 Files.copy 方法將上傳檔案的內容流（file.getInputStream()）寫入到目標路徑。
        *  REPLACE_EXISTING：如果目標位置已經有同名檔案，則覆蓋。*/

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath=path+File.separator+fileName;
        return new FileInputStream(filePath);
    }
}
