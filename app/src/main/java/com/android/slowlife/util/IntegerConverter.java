package com.android.slowlife.util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Created by Administrator on 2017/2/7 .
 */
public class IntegerConverter implements JsonSerializer<Integer>,
        JsonDeserializer<Integer> {

    public Integer deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        try {
            int str = json.getAsJsonPrimitive().getAsInt();
            if (str == 1002) {
                return 0;
            }
            return str;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public JsonElement serialize(Integer arg0, Type arg1,
                                 JsonSerializationContext arg2) {
        if (arg0 == null)
            return new JsonPrimitive("");
        return new JsonPrimitive(arg0);
    }
}
