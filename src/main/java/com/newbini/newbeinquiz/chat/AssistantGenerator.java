package com.newbini.newbeinquiz.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.web.request.Quiz;
import com.newbini.newbeinquiz.web.response.AssistantObject;
import com.newbini.newbeinquiz.web.response.ThreadObject;
import com.newbini.newbeinquiz.web.response.VectorStoreObject;
import lombok.Getter;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class AssistantGenerator {

    private final String openAiApiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    private String assistant_id = "";

    @Getter
    private String thread_id = "";

    @Getter
    private String vector_store_id = "";

    public AssistantGenerator(String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    String jsonString = "{\n" +
            "    \"questions\": [\n" +
            "      {\n" +
            "        \"type\": \"객관식\",\n" +
            "        \"question\": \"다음 중 대한민국의 수도는 어디입니까?\",\n" +
            "        \"options\": [\"서울\", \"부산\", \"대구\", \"인천\"],\n" +
            "        \"answer\": \"서울\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"주관식\",\n" +
            "        \"question\": \"이순신 장군이 활약한 해전의 이름은 무엇입니까?\",\n" +
            "        \"answer\": \"한산도 대첩\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"O/X\",\n" +
            "        \"question\": \"지구는 태양 주위를 돈다.\",\n" +
            "        \"answer\": true\n" +
            "      }\n" +
            "    ]\n" +
            "  }";

    public AssistantObject createAssistant(String type, String difficulty) throws JsonProcessingException, ParseException {


        String instruction = "저는 학술 콘텐츠 개발자입니다. \n" +
                "            대학생들을 대상으로 강의 내용을 기반으로 퀴즈를 생성합니다.\n" +
                "            저의 목적은 학생들의 시험 공부를 돕는 데에 있습니다.\n" +
                "            저는 한국어로만 대답하며 반드시 존댓말을 사용합니다.\n" +
                "            하나의 퀴즈 작성이 끝나면 세 줄은 공백으로 남기고, 그 다음 줄에 새로운 퀴즈를 출력합니다.\n" +
                "            \n" +
                "            **절대 json 형식 이외의 답변을 출력하지 않습니다.** \n" +
                "            첨부된 파일들을 바탕으로 총 **20**개의 퀴즈와 정답을 생성합니다\n" +
                "            문제 유형은 " + type + "으로 구성되어 있습니다. 다른 유형은 생성하지 않습니다.\n" +
                "            난이도는 " + difficulty + "입니다.\n" +
                "            강조된 내용은 반드시 퀴즈에 반영합니다.\n" +
                "            반환 형식은 JSON 형식입니다.\n" +
                "            **절대 json 형식 이외의 답변을 출력하지 않습니다.**\n" +
                "            **절대 코드블럭을 사용하지 않습니다.**\n" +
                "            예시는 다음과 같습니다:" + jsonString;



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("instructions", instruction);
        requestBody.put("name", "Quizard");

//        Quiz quiz = objectMapper.readValue(jsonString, Quiz.class);



        List<Map<String, Object>> toolsList = new ArrayList<>();
        Map<String,Object> toolsListFileSearch =new LinkedHashMap<>();
//        Map<String,Object> toolListFunctionTool = new LinkedHashMap<>();
//

        toolsListFileSearch.put("type","file_search");
//        toolListFunctionTool.put("name", "Quiz_Generator");
//        toolListFunctionTool.put("parameters", quiz);
//        toolsListFunction.put("type", "function");
//        toolsListFunction.put("function", toolListFunctionTool);
//
        toolsList.add(toolsListFileSearch);
        requestBody.put("tools", toolsList);
//        Map<String, String> format = new HashMap<>();
//        format.put("type", "json_object");
//        requestBody.put("response_format", format);
        requestBody.put("model", "gpt-4o-2024-05-13");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        System.out.println(requestEntity);
        String response = restTemplate.postForObject("https://api.openai.com/v1/assistants", requestEntity, String.class);
        AssistantObject assistant = objectMapper.readValue(response, AssistantObject.class);
        assistant_id = assistant.getId();

        return assistant;
    }

    public ThreadObject createThread() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
        String response = restTemplate.postForObject("https://api.openai.com/v1/threads", requestEntity, String.class);
        ThreadObject thread = objectMapper.readValue(response, ThreadObject.class);
        thread_id = thread.getId();

        return thread;
    }

    public VectorStoreObject createVectorStore() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Quiz Resources Storage");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject("https://api.openai.com/v1/vector_stores", requestEntity, String.class);
        VectorStoreObject vectorStore = objectMapper.readValue(response, VectorStoreObject.class);
        vector_store_id = vectorStore.getId();

        return vectorStore;
    }

}
