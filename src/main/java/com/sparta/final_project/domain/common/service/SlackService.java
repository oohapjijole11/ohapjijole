package com.sparta.final_project.domain.common.service;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.sparta.final_project.domain.common.entity.Color;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {
    private final Slack slackClient = Slack.getInstance();

    /*
    * slackUrl : 유저의 slackUrl
    * */

    //메세지 안에 링크가 들어간 경우
    public void sendSlackMessage(String slackUrl, String title, Color color, String message, String content) {
        try {
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/oohapjijole11/ohapjijole/refs/heads/dev/src/main/resources/static/brandimage.webp")
                    .username("ohapjijole")
                    .attachments(List.of(
                            Attachment.builder()
                                    .color(color.getCode()) // 메시지 색상
                                    .pretext(message)// 메시지 본문 내용
                                    .text(content)  //이동할 url   //<링크|링크 제목>  설명
                                    .build())))

            );
        }catch (IOException e) {
            log.warn("slack error ::: slackUrl : {} notification content :  {} erorrmessage : {}", slackUrl, title, e.getMessage());
            throw new OhapjijoleException(ErrorCode._NOT_AVAILABLE_SLACK_NOTIFICATION);
        }

    }

    //자세한 내용이 있는 경우
    public void sendSlackMessage(String slackUrl, String title, Color color, String message, String fieldTitle, String fieldContent) {
        try{
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/oohapjijole11/ohapjijole/refs/heads/dev/src/main/resources/static/brandimage.webp")
                    .username("ohapjijole")
                    .attachments(List.of(
                            Attachment.builder()
                                    .color(color.getCode()) // 메시지 색상
                                    .pretext(message)// 메시지 본문 내용
                                    .fields(List.of(  //메세지 세부 내용
                                            new Field(fieldTitle, fieldContent, false)
                                    ))
                                    .build())))
            );
        }catch (IOException e) {
            log.warn("slack error ::: slackUrl : {} notification content :  {} erorrmessage : {}", slackUrl, fieldTitle, e.getMessage());
            throw new OhapjijoleException(ErrorCode._NOT_AVAILABLE_SLACK_NOTIFICATION);
        }
    }

    //알림 내용이 한줄인 경우
    public void sendSlackMessage(String slackUrl, String title, String message) {
        try {
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/oohapjijole11/ohapjijole/refs/heads/dev/src/main/resources/static/brandimage.webp")
                    .username("ohapjijole")
                    .attachments(List.of(
                            Attachment.builder()
                                    .pretext(message)// 메시지 본문 내용
                                    .build())))
            );
        }catch (IOException e) {
            log.warn("slack error ::: slackUrl : {} notification content :  {} erorrmessage : {}", slackUrl, message, e.getMessage());
            throw new OhapjijoleException(ErrorCode._NOT_AVAILABLE_SLACK_NOTIFICATION);
        }
    }

}
