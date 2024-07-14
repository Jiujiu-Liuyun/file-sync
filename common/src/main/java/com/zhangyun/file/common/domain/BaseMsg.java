package com.zhangyun.file.common.domain;

import com.zhangyun.file.common.domain.req.DownloadFileReq;
import com.zhangyun.file.common.domain.req.UploadDocDiffReq;
import com.zhangyun.file.common.domain.resp.BaseResp;
import com.zhangyun.file.common.domain.resp.DownloadFileResp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private int messageType;

    /**
     * 返回消息类型
     */
    public abstract int getMessageType();

    /**
     * 消息指令类型 - 文件标记指令
     */
    public static final int BASE_RESP = 1;
    public static final int UPLOAD_DOCUMENT_DIFF_REQ = 10;
    public static final int NOTIFY_DOC_DIFF = 20;
    public static final int DOWNLOAD_FILE_REQ = 30;
    public static final int DOWNLOAD_FILE_RESP = 31;


    private static final Map<Integer, Class<? extends BaseMsg>> MESSAGE_CLASSES = new HashMap<>();

    static {
        MESSAGE_CLASSES.put(BASE_RESP, BaseResp.class);
        MESSAGE_CLASSES.put(UPLOAD_DOCUMENT_DIFF_REQ, UploadDocDiffReq.class);
        MESSAGE_CLASSES.put(NOTIFY_DOC_DIFF, NotifyDocDiff.class);
        MESSAGE_CLASSES.put(DOWNLOAD_FILE_REQ, DownloadFileReq.class);
        MESSAGE_CLASSES.put(DOWNLOAD_FILE_RESP, DownloadFileResp.class);
    }

    /**
     * 根据消息类型字节，获得对应的消息 class
     *
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends BaseMsg> getMessageClass(int messageType) {
        return MESSAGE_CLASSES.get(messageType);
    }

}