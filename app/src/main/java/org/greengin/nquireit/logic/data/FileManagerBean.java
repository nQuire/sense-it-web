package org.greengin.nquireit.logic.data;


import lombok.Getter;
import lombok.Setter;
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


    public String uploadFile(String context, String filename, InputStream input) throws IOException {
        File folder = new File(path, context);

        if (folder.mkdirs()) {

            String name = null;
            String extension = null;
            filename = filename.replaceAll(" ", "_");
            int extSepPos = filename.lastIndexOf('.');
            if (extSepPos > 0) {
                name = filename.substring(0, extSepPos);
                extension = filename.substring(extSepPos);
            }

            String testFilename = null;
            File f = null;
            for (int i = 0; ; i++) {
                testFilename = i > 0 ? String.format("%s_%d%s", name, i, extension) : filename;
                f = new File(folder, testFilename);
                if (!f.exists()) {
                    break;
                }
            }

            FileOutputStream output = new FileOutputStream(f);
            IOUtils.copy(input, output);
            output.close();

            return String.format("%s/%s", context, testFilename);
        } else {
            return null;
        }
    }

    public File get(String filename) {
        return new File(path, filename);
    }


}
