/*
 * Copyright 2018 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;


@SpringBootApplication
@LineMessageHandler
@Controller
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }
    
    @Autowired
    public LineMessagingClient client;
    
    /** echo msg
     * @param event
     * @return
     */
    //@EventMapping //很吵先關掉
    public List<TextMessage> echoTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("echoTextMessageEvent: " + event);
        List<TextMessage> msgs = new ArrayList<TextMessage>();
        msgs.add(new TextMessage("ECHO:" + event.getMessage().getText()));
        
        return msgs;
    }
    

    /**預設處理，單純顯示
     * @param event
     */
    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("handleDefaultMessageEvent: " + event);
        System.out.println("handleDefaultMessageEvent: senderId " + event.getSource().getSenderId());
    }

    /**查查id是誰，只適用 一般User Id, 不是用group, room id
     * @param id
     * @return
     */
    @RequestMapping("/whois/{id}")
    @ResponseBody
    String whois(@PathVariable("id")String id) {
        final UserProfileResponse userProfileResponse;
        try {
            userProfileResponse = client.getProfile(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        System.out.println(userProfileResponse.getUserId());
        System.out.println(userProfileResponse.getDisplayName());
        System.out.println(userProfileResponse.getPictureUrl());
//        msgs.add(new TextMessage("ECHO ID:" + userProfileResponse.getUserId() ));
//        msgs.add(new TextMessage("ECHO getDisplayName:" + userProfileResponse.getDisplayName() ));
//        msgs.add(new TextMessage("ECHO getPictureUrl:" + userProfileResponse.getPictureUrl() ));
        return userProfileResponse.getDisplayName();
    }


    /**幫忙送text訊息
     * @param id
     * @param msg
     * @return
     */
    @RequestMapping("/push/{id}/{msg}")
    @ResponseBody
    String pushTextMessage(@PathVariable("id")String id, @PathVariable("msg")String msg) {
        PushMessage pushMessage = new PushMessage(id, new TextMessage(msg));
		CompletableFuture<BotApiResponse> resp = client.pushMessage(pushMessage);
		
        return "push msg " + resp.toString();
    }
}
