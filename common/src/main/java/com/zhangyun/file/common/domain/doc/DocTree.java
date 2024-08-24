package com.zhangyun.file.common.domain.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocTree {
    private Doc node;

    private List<DocTree> subNodes;

    public Map<DocIdentity, DocTree> getSubDocIdentityMap() {
        if (CollectionUtils.isEmpty(subNodes)) {
            return Collections.emptyMap();
        }
        return subNodes.stream().collect(Collectors.toMap(subNode -> subNode.getNode().getDocIdentity(), Function.identity()));
    }
}
