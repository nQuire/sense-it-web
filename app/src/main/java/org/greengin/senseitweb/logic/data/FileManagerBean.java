package org.greengin.senseitweb.logic.data;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;


@Service
public class FileManagerBean implements InitializingBean {

    @Autowired
    private ServletContext context;

    @Getter
    File path;

    @Setter
    String basePath;

    @Override
    public void afterPropertiesSet() throws Exception {
        path = new File(this.context.getRealPath("") + "/" + basePath);
        if (!path.exists()) {
            path.mkdirs();
        }
    }


    public void uploadFile(String context, String filename, InputStream input) throws IOException {
        File folder = new File(path, context);
        folder.mkdirs();
        File f = new File(folder, filename);
        FileOutputStream output = new FileOutputStream(f);
        IOUtils.copy(input, output);
        output.close();
    }

    public File get(String filename) {
        return new File(path, filename);
    }


}
