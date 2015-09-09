package bsu.controller;

import bsu.controller.parser.Parser;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
@RequestMapping("/request")
public class RequestController {

    @RequestMapping(method = RequestMethod.GET, value = "/parseFile11")
    @ResponseBody
    public String ttt(){
        return "Rita Pavlova";
    }


    @RequestMapping(value="/parseFile", method = RequestMethod.POST)
    public @ResponseBody
    FileSystemResource UploadFile(@RequestParam(value="file", required=true) MultipartFile file) {
        String fileName=file.getOriginalFilename();
        File texFile = null;
        try {
            texFile = Parser.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(fileName);
        return new FileSystemResource(texFile);
    }
}
