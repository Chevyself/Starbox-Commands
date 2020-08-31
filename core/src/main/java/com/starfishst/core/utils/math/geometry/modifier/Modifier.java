package com.starfishst.core.utils.math.geometry.modifier;

import com.starfishst.core.utils.math.geometry.Shape;
import java.util.Collection;

/** Shapes can have different modifications which introduce a bunch of new usages */
public interface Modifier extends Shape {

  Collection<Shape> getShapes();
}
