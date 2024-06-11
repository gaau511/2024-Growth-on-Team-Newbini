package com.newbini.newbeinquiz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbini.newbeinquiz.dto.response.AssistantObject;
import com.newbini.newbeinquiz.dto.response.ThreadObject;
import com.newbini.newbeinquiz.dto.response.VectorStoreObject;
import lombok.Getter;
import org.apache.tomcat.util.json.ParseException;
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

    /**
     * Example answer types
     */
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
            "        \"answer\": \"O\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"O/X\",\n" +
            "        \"question\": \"거미는 곤충에 속한다.\",\n" +
            "        \"answer\": \"X\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }";

    public AssistantObject createAssistant(String type, String difficulty) throws JsonProcessingException, ParseException {

        /**
         * prompt
         * TODO 문제 개수 바꾸기
         */
        String instruction = "저는 학술 콘텐츠 개발자입니다. \n" +
                "대학생들을 대상으로 강의 내용을 기반으로 퀴즈를 생성합니다.\n" +
                "저의 목적은 학생들의 시험 공부를 돕는 데에 있습니다.\n" +
                "저는 **한국어**로만 대답하며 반드시 존댓말을 사용합니다.\n" +
                "\n" +
                "**첨부된 파일**들을 바탕으로 총 **3**개의 퀴즈와 정답을 생성합니다\n" +
                "파일에 언급되지 않은 내용은 퀴즈에 반영하지 않습니다.\n"+
                "퀴즈는 **첨부된 파일을 모두 종합해서** 만듭니다.\n" +
                "퀴즈에 누락되는 파일은 없습니다. \n" +
                "예를 들어, 단일 파일에서 모든 문제가 출제되는 경우는 없습니다.\n" +
                "객관식과 O/X 퀴즈의 답변 문항은 적정한 비율로 섞여있어야 합니다.\n" +
                "예를 들어, 정답이 모두 \"O\"이거나, 1번이어서는 안됨니다.\n"+
                "문제 유형은 " + type + "으로 구성되어 있습니다. 다른 유형은 생성하지 않습니다.\n" +
                "난이도는 " + difficulty + "입니다.\n" +
                "강조된 내용은 반드시 퀴즈에 반영합니다.\n" +
                "반환 형식은 JSON 형식입니다.\n" +
                "**절대 json 형식 이외의 답변을 출력하지 않습니다.**\n" +
                "**절대 코드블럭을 사용하지 않습니다. ```json ``` 문법을 사용하지 않습니다.**\n" +
                "예시는 다음과 같습니다:" + jsonString;



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        headers.add("OpenAI-Beta", "assistants=v2");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("instructions", instruction);
        requestBody.put("name", "Quizard");

        List<Map<String, Object>> toolsList = new ArrayList<>();
        Map<String,Object> toolsListFileSearch =new LinkedHashMap<>();

        toolsListFileSearch.put("type","file_search");

        toolsList.add(toolsListFileSearch);
        requestBody.put("tools", toolsList);

        requestBody.put("model", "gpt-4o-2024-05-13");
        requestBody.put("temperature", 0.6);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
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
