package org.hut.endpoint;

import lombok.Data;

@Data
public class Command {
    private Header header;
    private byte[] bytes;
}
