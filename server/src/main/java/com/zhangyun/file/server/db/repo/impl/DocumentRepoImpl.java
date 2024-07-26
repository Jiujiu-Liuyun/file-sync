package com.zhangyun.file.server.db.repo.impl;

import com.zhangyun.file.server.db.entity.DocumentPO;
import com.zhangyun.file.server.db.mapper.DocumentMapper;
import com.zhangyun.file.server.db.repo.DocumentRepo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangyun
 * @since 2024-07-26
 */
@Service
public class DocumentRepoImpl extends ServiceImpl<DocumentMapper, DocumentPO> implements DocumentRepo {
    @Resource
    private DocumentMapper documentMapper;

    @Override
    public void createDoc(DocumentPO documentPO) {
        documentMapper.insert(documentPO);
    }
}
