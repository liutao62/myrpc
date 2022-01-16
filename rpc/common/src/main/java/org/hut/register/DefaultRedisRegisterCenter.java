package org.hut.register;

public class DefaultRedisRegisterCenter implements RegisterCenter{
    @Override
    public boolean register() {
        return false;
    }

    @Override
    public boolean sub() {
        return false;
    }

    @Override
    public boolean pub() {
        return false;
    }
}
