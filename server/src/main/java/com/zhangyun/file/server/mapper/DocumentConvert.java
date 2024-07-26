package com.zhangyun.file.server.mapper;

import com.zhangyun.file.common.domain.doc.old.Document;
import com.zhangyun.file.server.db.entity.DocumentPO;
import org.mapstruct.Mapper;

@Mapper
public interface DocumentConvert {

    Document convert(DocumentPO documentPO);

}
