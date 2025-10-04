package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    private final GeminiService geminiService;

    public String generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {}", aiResponse);
        return processAiResponse(activity , aiResponse);
    }

    private String processAiResponse(Activity activity, String aiResponse){
        try{
            ObjectMapper mapper =new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\n```", "")
                    .trim();

            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();

            addAnalysisSection(fullAnalysis , analysisNode , "overall", "Overall :");
            addAnalysisSection(fullAnalysis , analysisNode , "pace", "Pace :");
            addAnalysisSection(fullAnalysis , analysisNode , "heartRate", "Heart Rate :");
            addAnalysisSection(fullAnalysis , analysisNode , "caloriesBurned", "Calories :");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aiResponse;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("Recommendation").asText();

                improvements.add(String.format("%s: %s", area, detail));
            });

            return improvements.isEmpty() ? Collections.singletonList("No Specific improvements provided") : improvements;
        }
        return improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                         Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                         {
                         "analysis":{
                         "overall": "Overall analysis here",
                         "pace": "Pace analysis here",
                         "heartRate": "Heart rate analysis here",
                         "caloriesBurn": "Calories analysis here"
                        },
                        "improvements":[{
                        "area":"Area name",
                        "recommendation": "Detailed recommendation"
                        }
                        ],
                        "suggestions":[{
                        "workout": "Workout name",
                        "description": "Detailed workout description"
                        }
                        ],
                        "safety":[{
                        "Safety Point 1",
                        "Safety Point 2",
                        }
                        ]
                        
                        Analyze this activity :
                        Activity Type: %s
                        Duration: %d minutes
                        Additional Metrics: %s
                        
                        Provide detailed analysis focusing on performance , improvements, next workout suggestion and safety guidelines
                        Ensure the response follows the EXACT JSON format shown above.
                        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics());
    }
}

