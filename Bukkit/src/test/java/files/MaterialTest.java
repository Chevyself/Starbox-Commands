package files;

import org.bukkit.Material;

public class MaterialTest {

  public static void main(String[] args) {
    for (Material value : Material.values()) {
      System.out.println(value);
    }
    System.out.println(Material.valueOf("diamond_sword"));
  }
}
