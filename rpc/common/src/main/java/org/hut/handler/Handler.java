package org.hut.handler;

import org.hut.net.AbstractEndpoint;
import org.hut.net.BioEndpoint;
import org.hut.protocol.MyRpcEntity;

public class Handler {

//    @Autowired
//    @Lazy
    private AbstractEndpoint endpoint = new BioEndpoint();

    public MyRpcEntity requestDispatcher(MyRpcEntity requestEntity) {
        return null;
    }

    public MyRpcEntity response(MyRpcEntity responseEntity){
        return null;
    }
}
