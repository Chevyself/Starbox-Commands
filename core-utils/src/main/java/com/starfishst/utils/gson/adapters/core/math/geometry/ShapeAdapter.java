package com.starfishst.utils.gson.adapters.core.math.geometry;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.starfishst.core.utils.math.geometry.Shape;

/** Adapts shapes into json */
public interface ShapeAdapter<T extends Shape> extends JsonSerializer<T>, JsonDeserializer<T> {}
