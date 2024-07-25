package com.zhangyun.file.server.service.impl;

import com.zhangyun.file.server.db.entity.DocumentPO;
import com.zhangyun.file.server.db.mapper.DocumentMapper;
import com.zhangyun.file.server.service.IDocumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangyun
 * @since 2024-07-25
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, DocumentPO> implements IDocumentService {

}
