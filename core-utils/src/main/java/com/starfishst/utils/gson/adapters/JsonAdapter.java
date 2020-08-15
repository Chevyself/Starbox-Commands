package com.starfishst.utils.gson.adapters;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/** Adapts objects to json */
public interface JsonAdapter<T> extends JsonDeserializer<T>, JsonSerializer<T> {}
