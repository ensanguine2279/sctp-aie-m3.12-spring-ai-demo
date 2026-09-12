package sg.edu.ntu.spring_ai_demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AiService {

    @Value("${spring.ai.demo.csv.filepath}")
    private String CSV_FILE_PATH;

    public String saveSummary(String summary) {
        try {
            // File saved to resource path specified in application.properties
            Files.writeString(Path.of(CSV_FILE_PATH), summary);
            return "File saved successfully.";
        } catch (IOException e) {
            return "Could not save the file: " + e.getMessage();
        }
    }

}
