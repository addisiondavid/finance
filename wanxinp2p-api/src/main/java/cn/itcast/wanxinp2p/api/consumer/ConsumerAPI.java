package cn.itcast.wanxinp2p.api.consumer;

import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import cn.itcast.wanxinp2p.common.domain.RestResponse;

public interface ConsumerAPI {
    RestResponse register(ConsumerRegisterDTO consumerRegisterDTO);
}
