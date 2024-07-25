package com.zhangyun.file.server.service.impl;

import com.zhangyun.file.server.db.entity.DocumentPO;
import com.zhangyun.file.server.db.mapper.DocumentMapper;
import com.zhangyun.file.server.service.IDocumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangyun
 * @since 2024-07-25
 */
@Service
@Slf4j
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, DocumentPO> implements IDocumentService {
    @Resource
    private DocumentMapper documentMapper;

    public void createDoc(DocumentPO documentPO) {
        log.info("创建文档, doc: {}", documentPO);
        documentMapper.insert(documentPO);
    }
}
