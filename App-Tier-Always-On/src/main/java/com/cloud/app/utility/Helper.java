package com.cloud.app.utility;

import com.cloud.app.services.VideoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.StringJoiner;

@Component
public class Helper {

    Logger logger = LoggerFactory.getLogger(Helper.class);

    private static final int BUFFER_SIZE = 4096;

    // https://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url
    // Download a file to a temp location and return its path
    public String downloadFile(String fileurl) {
        logger.info("Downloading " + fileurl);
        String savePath = null;
        try {
            URL url = new URL(fileurl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String disposition = httpConn.getHeaderField("Content-Disposition");
                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        String fileName = disposition.substring(index + 9);
                        // opens input stream from the HTTP connection
                        InputStream inputStream = httpConn.getInputStream();
                        String tempDir = System.getProperty("java.io.tmpdir");
                        savePath = tempDir + File.separator + fileName;

                        // opens an output stream to save into file
                        FileOutputStream outputStream = new FileOutputStream(savePath);

                        int bytesRead = -1;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        outputStream.close();
                        inputStream.close();
                    } else {
                        logger.error("No filename in Content-Disposition header!");
                    }
                } else {
                    logger.error("No Content-Disposition header!");
                }
            } else {
                logger.error("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (Exception e) {
            savePath = null;
            e.printStackTrace();
        }
        return savePath;
    }

    // https://stackoverflow.com/a/41292766
    public int runCommand(List<String> params) {
        logger.info("Running command: "+ String.join(" ", params));
        ProcessBuilder pb = new ProcessBuilder(params);
        pb.directory(new File("/home/ubuntu/darknet"));
        Process p = null;
        String output = "";
        try {
            p = pb.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            output = sj.toString();
            logger.info("Command output: "+output);

            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p == null ? -1 : p.exitValue();
    }
}
