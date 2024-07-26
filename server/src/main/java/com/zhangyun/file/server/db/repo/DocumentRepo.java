package com.zhangyun.file.server.db.repo;

import com.zhangyun.file.server.db.entity.DocumentPO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangyun
 * @since 2024-07-26
 */
public interface DocumentRepo extends IService<DocumentPO> {

    void createDoc(DocumentPO documentPO);
}
