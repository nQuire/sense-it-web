package org.greengin.nquireit.controllers.files;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.greengin.nquireit.logic.data.FileManagerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    FileManagerBean fileManager;

    @RequestMapping(value = "/image/**")
    public void image(HttpServletRequest request, HttpServletResponse response) {
        String path = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).substring("/files/image".length());
        File file = fileManager.get(path);

        response.setContentType("image/" + FilenameUtils.getExtension(path));
        try {
            IOUtils.copy(new FileInputStream(file), response.getOutputStream());
        } catch (IOException ignored) {
        }

    }

    @RequestMapping(value = "/thumb/**")
    public void thumb(HttpServletRequest request, HttpServletResponse response) {
        String path = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).substring("/files/thumb".length());
        File file = fileManager.getThumb(path);

        response.setContentType("image/" + FilenameUtils.getExtension(path));
        try {
            IOUtils.copy(new FileInputStream(file), response.getOutputStream());
        } catch (IOException ignored) {
        }

    }


}
