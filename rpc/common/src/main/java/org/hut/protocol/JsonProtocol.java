package org.hut.protocol;

import com.alibaba.fastjson.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class JsonProtocol implements Protocol {

    Charset charset = Charset.defaultCharset();

    @Override
    public ByteBuffer serialization(MyRpcEntity request) {
        return charset.encode(JSONObject.toJSONString(request));
    }

    @Override
    public String deserialization(ByteBuffer buffer) {
        return charset.decode(buffer).toString();
    }

    @Override
    public MyRpcEntity getData(String desData) {
        return JSONObject.parseObject(desData, MyRpcEntity.class);
    }

}
