package com.starfishst.utils.gson.factories;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public interface InterfaceTypeAdapterFactory<E> extends TypeAdapterFactory {

  /**
   * Get the class of the interface
   *
   * @return the class of the interface
   */
  @NotNull
  Class<E> getClazz();

  @Override
  default <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (getClazz().isAssignableFrom(type.getRawType())) {
      TypeAdapter<T> typeAdapter = gson.getDelegateAdapter(this, type);
      return new TypeAdapter<T>() {
        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
          typeAdapter.write(jsonWriter, t);
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
          return typeAdapter.read(jsonReader);
        }
      }.nullSafe();
    } else {
      return null;
    }
  }
}
