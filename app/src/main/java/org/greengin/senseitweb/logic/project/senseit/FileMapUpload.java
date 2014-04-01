package org.greengin.senseitweb.logic.project.senseit;

import lombok.Getter;
import lombok.NonNull;

import java.io.InputStream;
import java.util.HashMap;

public class FileMapUpload {

    @Getter
    @NonNull
    HashMap<String, FileData> data;

    public FileMapUpload() {
        data = new HashMap<String, FileData>();
    }

    public void add(String key, String filename, InputStream filedata) {
        data.put(key, new FileData(filename, filedata));
    }

    public class FileData {
        public String filename;
        public InputStream data;

        public FileData(String filename, InputStream data) {
            this.filename = filename;
            this.data = data;
        }
    }
}
