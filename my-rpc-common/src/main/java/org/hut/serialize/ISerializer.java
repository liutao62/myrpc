package org.hut.serialize;

public interface ISerializer<T> {

    /**
     * 序列化
     *
     * @param entry
     * @return
     */
    byte[] serialize(T entry);

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    T deserialize(byte[] bytes);
}
