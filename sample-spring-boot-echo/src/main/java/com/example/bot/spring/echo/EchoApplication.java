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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import com.linecorp.bot.client.LineMessagingClient;
import org.springframework.beans.factory.annotation.Autowired;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.lang.Exception;


@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }
    
    @Autowired
    public LineMessagingClient client;
    
    //@EventMapping
    public List<TextMessage> echoTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
//         final UserProfileResponse userProfileResponse;
//         try {
//             userProfileResponse = client.getProfile("U4249d90685aa7150e2f6365825cebd27").get();
//         } catch (Exception e) {
//             e.printStackTrace();
//             return null;
//         }

//         System.out.println(userProfileResponse.getUserId());
//         System.out.println(userProfileResponse.getDisplayName());
//         System.out.println(userProfileResponse.getPictureUrl());
//         List<TextMessage> msgs = new ArrayList<TextMessage>();
//         msgs.add(new TextMessage("ECHO:" + event.getMessage().getText()));
//         msgs.add(new TextMessage("ECHO ID:" + userProfileResponse.getUserId() ));
//         msgs.add(new TextMessage("ECHO getDisplayName:" + userProfileResponse.getDisplayName() ));
//         msgs.add(new TextMessage("ECHO getPictureUrl:" + userProfileResponse.getPictureUrl() ));
        String askText = event.getMessage().getText();
        System.out.println("askText: " + askText);
        if(null!=askText && askText.length()>0){
            askText = askText.trim().toUpperCase();
            System.out.println("askText trim, to upper: " + askText);
        	if(askText.contains("LINEID") && (askText.contains("什麼") || askText.contains("多少") || askText.contains("?"))){
        		List<TextMessage> msgs = new ArrayList<TextMessage>();
        		msgs.add(new TextMessage("您的LINE ID是 " + event.getSource().getSenderId()));
        		return msgs;
        	}
        }
        return null;
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
