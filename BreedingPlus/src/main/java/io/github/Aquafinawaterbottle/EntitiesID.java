package io.github.Aquafinawaterbottle;

import org.spongepowered.api.entity.EntityTypes;

/**
 * ID values of all breedable animals excluding the villager.
 * <P>
 * 
 * It holds the Sponge EntityType of the entity as well as the plugin id number, used for the individual mob chances.
 */
public class EntitiesID {

	public static final EntityData COW = new EntityData(EntityTypes.COW, 0);
	public static final EntityData CHICKEN = new EntityData(EntityTypes.CHICKEN, 1);
	public static final EntityData DONKEY = new EntityData(EntityTypes.DONKEY, 2);
	public static final EntityData HORSE = new EntityData(EntityTypes.HORSE, 3);
	public static final EntityData LLAMA = new EntityData(EntityTypes.LLAMA, 4);
	public static final EntityData MUSHROOM_COW = new EntityData(EntityTypes.MUSHROOM_COW, 5);
	public static final EntityData OCELOT = new EntityData(EntityTypes.OCELOT, 6);
	public static final EntityData PARROT = new EntityData(EntityTypes.PARROT, 7);
	public static final EntityData PIG = new EntityData(EntityTypes.PIG, 8);
	public static final EntityData RABBIT = new EntityData(EntityTypes.RABBIT, 9);
	public static final EntityData SHEEP = new EntityData(EntityTypes.SHEEP, 10);
	public static final EntityData WOLF = new EntityData(EntityTypes.WOLF, 11);

	/**
	 * An array of all acceptable entity types, given with the plugin id {@link EntityData#setId(int)}.
	 * 
	 * An acceptable entity type is any entity type that can breed, is not a villager and within the config file.
	 */
	public static final EntityData[] ALL_ENTITIES = { COW, CHICKEN, DONKEY, HORSE, LLAMA, MUSHROOM_COW, OCELOT, PARROT,
			PIG, RABBIT, SHEEP, WOLF };

}
