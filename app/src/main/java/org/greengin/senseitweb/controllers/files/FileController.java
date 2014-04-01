package org.greengin.senseitweb.controllers.files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.greengin.senseitweb.logic.data.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping(value = "/files/")
public class FileController {

    @Autowired
    FileManager fileManager;

    @RequestMapping(value = "/image/**"/*, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE}*/)
    public void image(HttpServletRequest request, HttpServletResponse response) {
        String path = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).substring("/files/image".length());
        File file = fileManager.get(path);

        response.setContentType("image/" + FilenameUtils.getExtension(path));
        try {
            IOUtils.copy(new FileInputStream(file), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
