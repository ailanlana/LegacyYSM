package com.elfmcys.yesstevemodel.network.message;

import com.elfmcys.yesstevemodel.network.message.util.ExtendedBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public interface IExtendedBufferMessage extends IMessage {
    void toBytes(ExtendedBuffer buf);

    void fromBytes(ExtendedBuffer buf);

    @Override
    default void toBytes(ByteBuf buf) {
        this.toBytes(new ExtendedBuffer(buf));
    }

    @Override
    default void fromBytes(ByteBuf buf) {
        this.fromBytes(new ExtendedBuffer(buf));
    }
}
