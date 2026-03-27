package com.elfmcys.yesstevemodel.network.message.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("unused")
public class ExtendedBuffer extends PacketBuffer {
    public ExtendedBuffer(ByteBuf wrapped) {
        super(wrapped);
    }

    @Override
    public void writeStringToBuffer(String str) {
        try {
            super.writeStringToBuffer(str);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    @Override
    public String readStringFromBuffer(int maxLength) {
        try {
            return super.readStringFromBuffer(maxLength);
        } catch (IOException e) {
            throw new DecoderException(e);
        }
    }

    public String readStringFromBuffer() {
        return this.readStringFromBuffer(Short.MAX_VALUE);
    }

    @Override
    public void writeNBTTagCompoundToBuffer(NBTTagCompound nbt) {
        try {
            super.writeNBTTagCompoundToBuffer(nbt);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    @Override
    public NBTTagCompound readNBTTagCompoundFromBuffer() {
        try {
            return super.readNBTTagCompoundFromBuffer();
        } catch (IOException e) {
            throw new DecoderException(e);
        }
    }

    @Override
    public void writeItemStackToBuffer(ItemStack stack) {
        try {
            super.writeItemStackToBuffer(stack);
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    @Override
    public ItemStack readItemStackFromBuffer() {
        try {
            return super.readItemStackFromBuffer();
        } catch (IOException e) {
            throw new DecoderException(e);
        }
    }

    public void writeResourceLocationToBuffer(ResourceLocation resourceLocationIn) {
        this.writeStringToBuffer(resourceLocationIn.toString());
    }

    public ResourceLocation readResourceLocationFromBuffer() {
        return new ResourceLocation(this.readStringFromBuffer());
    }

    public void writeTimeToBuffer(Date time) {
        this.writeLong(time.getTime());
    }

    public Date readTimeFromBuffer() {
        return new Date(this.readLong());
    }

    public void writeUniqueIdToBuffer(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUniqueIdFromBuffer() {
        return new UUID(this.readLong(), this.readLong());
    }

    public void writeByteArrayToBuffer(byte[] array) {
        this.writeVarIntToBuffer(array.length);
        this.writeBytes(array);
    }

    public byte[] readByteArrayFromBuffer() {
        return this.readByteArrayFromBuffer(this.readableBytes());
    }

    public byte[] readByteArrayFromBuffer(int maxLength) {
        int i = this.readVarIntFromBuffer();
        if (i > maxLength) {
            throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxLength);
        } else {
            byte[] abyte = new byte[i];
            this.readBytes(abyte);
            return abyte;
        }
    }

    /**
     * Writes an array of VarInts to the buffer, prefixed by the length of the array (as a VarInt).
     */
    public void writeVarIntArrayToBuffer(int[] array) {
        this.writeVarIntToBuffer(array.length);
        for (int i : array) {
            this.writeVarIntToBuffer(i);
        }
    }

    public int[] readVarIntArrayFromBuffer() {
        return this.readVarIntArrayFromBuffer(this.readableBytes());
    }

    public int[] readVarIntArrayFromBuffer(int maxLength) {
        int i = this.readVarIntFromBuffer();
        if (i > maxLength) {
            throw new DecoderException("VarIntArray with size " + i + " is bigger than allowed " + maxLength);
        } else {
            int[] aint = new int[i];
            for (int j = 0; j < aint.length; ++j) {
                aint[j] = this.readVarIntFromBuffer();
            }
            return aint;
        }
    }

    /**
     * Writes an array of longs to the buffer, prefixed by the length of the array (as a VarInt).
     */
    public void writeLongArrayToBuffer(long[] array) {
        this.writeVarIntToBuffer(array.length);
        for (long i : array) {
            this.writeLong(i);
        }
    }

    /**
     * Reads a length-prefixed array of longs from the buffer.
     */
    @SideOnly(Side.CLIENT)
    public long[] readLongArrayFromBuffer(@Nullable long[] array) {
        return this.readLongArrayFromBuffer(array, this.readableBytes() / 8);
    }

    @SideOnly(Side.CLIENT)
    public long[] readLongArrayFromBuffer(@Nullable long[] array, int maxLength) {
        int i = this.readVarIntFromBuffer();
        if (array == null || array.length != i) {
            if (i > maxLength) {
                throw new DecoderException("LongArray with size " + i + " is bigger than allowed " + maxLength);
            }
            array = new long[i];
        }
        for (int j = 0; j < array.length; ++j) {
            array[j] = this.readLong();
        }
        return array;
    }

    public void writeChatComponentToBuffer(IChatComponent component) {
        this.writeStringToBuffer(IChatComponent.Serializer.func_150696_a(component));
    }

    public IChatComponent readChatComponentFromBuffer() {
        return IChatComponent.Serializer.func_150699_a(this.readStringFromBuffer());
    }

    public void writeEnumValueToBuffer(Enum<?> value) {
        this.writeVarIntToBuffer(value.ordinal());
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T readEnumValueFromBuffer(Class<T> enumClass) {
        return (T) ((Enum<T>[]) enumClass.getEnumConstants())[this.readVarIntFromBuffer()];
    }
}
