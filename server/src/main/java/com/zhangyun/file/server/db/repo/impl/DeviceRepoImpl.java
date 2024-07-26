package com.zhangyun.file.server.db.repo.impl;

import com.zhangyun.file.server.db.entity.DevicePO;
import com.zhangyun.file.server.db.mapper.DeviceMapper;
import com.zhangyun.file.server.db.repo.DeviceRepo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangyun
 * @since 2024-07-26
 */
@Service
public class DeviceRepoImpl extends ServiceImpl<DeviceMapper, DevicePO> implements DeviceRepo {

}
