package ch.minepvp.spout.hardcoregames;

import org.spout.api.geo.discrete.Point;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyFactory;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.vanilla.inventory.player.PlayerArmorInventory;
import org.spout.vanilla.inventory.player.PlayerMainInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;

public class GameData {

    // Inventory
    public static final DefaultedKey<PlayerMainInventory> MAIN_INVENTORY = new DefaultedKeyFactory<PlayerMainInventory>("hcg_main", PlayerMainInventory.class);
    public static final DefaultedKey<PlayerArmorInventory> ARMOR_INVENTORY = new DefaultedKeyFactory<PlayerArmorInventory>("hcg_armor", PlayerArmorInventory.class);
    public static final DefaultedKey<PlayerQuickbar> QUICKBAR_INVENTORY = new DefaultedKeyFactory<PlayerQuickbar>("hcg_quickbar", PlayerQuickbar.class);

    // Health and Food
    public static final DefaultedKey<Integer> HEALTH = new DefaultedKeyImpl<Integer>("hcg_health", 0);
    public static final DefaultedKey<Integer> FOOD = new DefaultedKeyImpl<Integer>("hcg_food", 0);

    // Postition
    public static final DefaultedKey<Point> POSITION = new DefaultedKeyImpl<Point>("hcg_position", null);



}
