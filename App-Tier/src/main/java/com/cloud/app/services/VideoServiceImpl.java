package com.cloud.app.services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloud.app.utility.Helper;
import com.cloud.app.utility.Constants;

@Component
public class VideoServiceImpl implements VideoService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private Helper helper;
    
    @Autowired
    private SqsService sqsService;

    private void storeResults(String filename, String results) {
        System.out.println("Detected objects => " + filename + " : " + results);
        s3Service.putObject(filename, results);
    }

    private List<String> parseResults(String resultPath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(resultPath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // delete results file for cleanup
//        new File(resultPath).delete();

        Pattern pattern = Pattern.compile("(\\w+): \\d+%"); // tvmonitor: 67%
        Matcher matcher = pattern.matcher(content);
        Set<String> objects = new HashSet<>();
        while(matcher.find()) {
            objects.add(matcher.group(1));
        }

        return new ArrayList<>(objects);
    }

    private String detectObjects(String videoPath) {
        String resultPath = videoPath.concat("results");
        List<String> params = Arrays.asList("bash", "-c", "xvfb-run ./darknet"
                + " detector demo cfg/coco.data cfg/yolov3-tiny.cfg yolov3-tiny.weights "
                + videoPath + " -dont_show > " + resultPath);
        int exitCode = helper.runCommand(params);
        // delete video file for cleanup
//        new File(videoPath).delete();

        return (exitCode == 0 && new File(resultPath).exists()) ? resultPath : null;
    }

    public boolean processVideo(String processId) {
        String videoPath = helper.downloadFile(Constants.VIDEO_URL);
        if (videoPath == null) {
            System.out.println("Video download failed!");
            return false;
        }
        String resultPath = detectObjects(videoPath);
        if (resultPath == null) {
            System.out.println("Object detection failed!");
            return false;
        }
        List<String> results = parseResults(resultPath);
        if (results == null) {
            System.out.println("Parse results failed!");
            return false;
        }
        String fileName = new File(videoPath).getName();
        String s3Key = processId.concat("_").concat(fileName);
        String allResults = String.join(",", results);
        String finalResponse = s3Key.concat(":").concat(allResults);
        storeResults(s3Key, allResults);
        // Put results in sqs also for response to request
        storeResultsInOutputQueue(finalResponse, processId);
        return true;
    }

	private void storeResultsInOutputQueue(String allResults, String processId) {
		sqsService.putMessageInOutputQueue(allResults);
		
	}

}
