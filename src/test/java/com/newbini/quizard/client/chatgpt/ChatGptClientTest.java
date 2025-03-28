package com.newbini.quizard.client.chatgpt;

import com.newbini.quizard.dto.request.QuizCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

class ChatGptClientTest {
    private ChatGptClient chatGptClient = new ChatGptClient(new OpenAiChatClient(new OpenAiApi(System.getenv("OPEN_AI_API_KEY"))));

    @Test
    void sendRequest() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "testfile.txt",
                "text/plain",
                ("김유정 <동백꽃>\n" +
                        "오늘도 또 우리 수탉이 막 쫓기었다.\n" +
                        "내가 점심을 먹고 나무를 하러 갈 양으로 나올 때이었다.\n" +
                        "산으로 올라서려니까 등뒤에서 푸르득푸드득, 하고 닭의 횃소리가 야단이다.\n" +
                        "깜짝 놀라서 고개를 돌려보니 아니나다르랴, 두 놈이 또 얼리었다.\n" +
                        "점순네 수탉(은 대강이가 크고 똑 오소리같이 실팍하게 생긴 놈)이 덩저리 작은 우리 수탉을 함부로 해내는 것이다.\n" +
                        "그것도 그냥 해내는 것이 아니라 푸드득 하고 면두를 쪼고 물러섰다가 좀 사이를 두고 또 푸드득 하고 모가지를 쪼았다.\n" +
                        "이렇게 멋을 부려 가며 여지없이닦아 놓는다.\n" +
                        "그러면 이 못생긴 것은 쪼일 적마다 주둥이로 땅을 받으며 그 비명이 킥, 킥할 뿐이다. 물론 미처 아물지도 않은 면두를 또 쪼이어 붉은 선혈은 뚝뚝 떨어진다.\n" +
                        "이걸 가만히 내려다보자니 내 대강이가 터져서 피가 흐르는 것같이 두 눈에서 불이 번쩍난다.\n" +
                        "대뜸 지게 막대기를 메고 달려들어 점순네 닭을 후려칠까 하다가 생각을 고쳐먹고 헛매질로 떼어만 놓았다.\n" +
                        "이번에도 점순이가 쌈을 붙여 놨을 것이다.\n" +
                        "바짝바짝 내 기를 올리느라고 그랬음에 틀림없을 것이다.\n" +
                        "고놈의 계집애가 요새로 들어서서 왜 나를 못 먹겠다고 고렇게 아르렁거리는지 모른다.\n" +
                        "나흘 전 감자 조각만 하더라도 나는 저에게 조금도 잘못한 것은 없다.\n" +
                        "계집애가 나물을 캐러 가면 갔지 남 울타리 엮는 데 쌩이질을 하는 것은 다 뭐냐.\n" +
                        "그것도발소리를 죽여 가지고 등뒤로 살며시 와서, “얘! 너 혼자만 일하니?” 하고 긴치 않은 수작을 하는 것이다.\n" +
                        "어제까지도 저와 나는 이야기도 잘 않고 서로 만나도 본 척 만 척하고 이렇게 점잖게 지내던 터이련만 오늘로 갑작스레 대견해졌음은 웬일인가.\n" +
                        "황차 망아지만한 계집애가 남 일하는 놈보. “그럼 혼자 하지 떼루 하디?” 내가 이렇게 내배앝는 소리를 하니까,\n" +
                        "“너 일하기 좋니?” 또는, “한여름이나 되거든 하지 벌써 울타리를 하니?” \n" +
                        "잔소리를 두루 늘어놓다가 남이 들을까 봐 손으로 입을 틀어막고는 그 속에서 깔깔댄다.").getBytes()
        );

        QuizCreateRequest mockQuizCreateRequest = new QuizCreateRequest(
                20,
                "어려움",
                List.of("객관식", "주관식", "O/X"),
                5,
                List.of(mockFile)
        );

        chatGptClient.sendRequest(mockQuizCreateRequest);
    }
}