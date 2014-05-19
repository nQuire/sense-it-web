package org.greengin.nquireit.logic.files;

import org.greengin.nquireit.json.JacksonObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by evilfer on 5/8/14.
 */
public class RequestsUtils {

    public static <T> T readParam(HttpServletRequest request, JacksonObjectMapper objectMapper, String paramKey, Class<T> tClass) throws IOException {
        DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) request;
        return objectMapper.readValue(multiPartRequest.getParameter(paramKey), tClass);
    }

    public static FileMapUpload getFiles(HttpServletRequest request) throws IOException {
        DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) request;
        FileMapUpload files = new FileMapUpload();

        for (String param : multiPartRequest.getParameterMap().keySet()) {
            files.getData().put(param, null);
        }

        for (Map.Entry<String, MultipartFile> entry : multiPartRequest.getFileMap().entrySet()) {
            files.add(entry.getKey(), entry.getValue().getOriginalFilename(), entry.getValue().getInputStream());
        }

        return files;
    }

    public static long getLong(HttpServletRequest request, String param, long defaultValue) {
        try {
            return Long.parseLong(request.getParameter(param));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static FileMapUpload.FileData getFile(HttpServletRequest request, String fileParam) throws IOException {
        DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) request;


        MultipartFile file = multiPartRequest.getFileMap().get(fileParam);
        return file == null ? null :
                new FileMapUpload.FileData(file.getOriginalFilename(), file.getInputStream());

    }

}
