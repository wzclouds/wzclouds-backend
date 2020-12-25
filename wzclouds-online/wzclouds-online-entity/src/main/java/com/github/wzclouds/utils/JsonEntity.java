package com.github.wzclouds.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.wzclouds.common.UserSession;
import com.github.wzclouds.exception.BizException;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@Slf4j
public class JsonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会话类型,"NOTICE"为动态执行标识,动态执行方法为serverFunc
     */
    private EventName eventName;

    /**
     * NOTICI通用通知时才会使用
     */
    private ServerType serverFunc;
    private ClientType clientFunc;
    private String targetIds;
    private UserSession userSession;
    private Map<String, Object> data;

    @Builder
    public JsonEntity(EventName eventName, ServerType serverFunc, ClientType clientFunc, String targetIds, UserSession userSession, Map<String, Object> data) {
        this.eventName = eventName;
        this.serverFunc = serverFunc;
        this.clientFunc = clientFunc;
        this.targetIds = targetIds;
        this.userSession = userSession;
        this.data = data;
    }

    /**
     * 发送通知信息
     *
     * @param u
     */
    public void sendResMsg(UserSession u) {
        synchronized (u.getSession()) {
            try {
                u.getSession().sendMessage(new TextMessage(JSONObject.toJSONString(this)));
            } catch (Exception e) {
                log.info(e.getMessage());
                throw BizException.wrap("发送通知错误!");
            }
        }
    }
}
