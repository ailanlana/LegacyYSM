package com.elfmcys.yesstevemodel.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

public final class GsonUtils {
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(Gson gson, Reader reader, Type type, boolean enableLenient) {
        try {
            JsonReader jsonreader = new JsonReader(reader);
            jsonreader.setLenient(enableLenient);
            return (T) gson.getAdapter(TypeToken.get(type)).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }

    @Nullable
    public static <T> T fromJson(Gson gson, String reader, Type type, boolean enableLenient) {
        return fromJson(gson, new StringReader(reader), type, enableLenient);
    }
}
