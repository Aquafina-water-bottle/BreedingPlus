package io.github.Aquafinawaterbottle;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

/**
 * 
 * 
 * Also handles debugging info.
 */
public class BreedingManager {

	// internal variables
	private Random rand = new Random();
	private Breeding breeding;

	// config options
	private BreedingData breedingData;

	public BreedingManager(Breeding breeding) {
		this.breeding = breeding;
	}

	/**
	 * Runs when an entity spawns, and decides whether a spawn egg should
	 * replace the entity or not.
	 * <P>
	 * This runs through several checks to determine whether the entity:
	 * <ol>
	 * 	<li>is summoned from a breeding event</li>
	 * 	<li>passes the prng</li>
	 * 	<li>is the proper entity type as determined by {@link Breeding#getItemType(EntityType)}</li>
	 * </ol>
	 * 
	 * @param event
	 */
	@Listener
	public void checkingSpawnEvent(SpawnEntityEvent event) {

		// checks whether the plugin is enabled or not
		if (breedingData.getEnabled()) {

			// must check whether the event context contains the key in the first place
			if (event.getContext().containsKey(EventContextKeys.SPAWN_TYPE)) {

				// checks whether the entity is summoned by a breeding event
				if (event.getContext().get(EventContextKeys.SPAWN_TYPE).get().equals(SpawnTypes.BREEDING)) {

					// Beginning of the debugging info
					outputDebugInfo(breedingData, "{ == START ==");
					outputDebugInfo(breedingData, event.toString() + " has been summoned by SpawnTypes.BREEDING");

					// Gets the list, although idk why more than one mob can spawn from a breeding event
					List<Entity> entities = event.getEntities();

					// Iterates through the list, and based off the spawn chance, it will either remain normal
					// or remove the entity and replace it with a spawn egg
					for (Entity entity : entities) {

						// Checks whether the entity type is an acceptable entity type
						Optional<EntityData> entityData = EntityData.getEntityDataFrom(entity.getType());
						if (entityData.isPresent()) {

							int randomNumber = this.rand.nextInt(99);
							int individualMobChance = breedingData.getInvididualMobChance()[entityData.get().getId()];

							// Debug info: Compare the random number given the individual mob chance
							outputDebugInfo(breedingData, entity.toString() + " will spawn if individualMobChance="
									+ individualMobChance + " is greater than randomNumber=" + randomNumber);

							if (individualMobChance > randomNumber) {

								// End of debug info: Success
								outputDebugInfo(breedingData, "SUCCESS: " + entity.toString() + " was replaced with a "
										+ entity.getType().toString() + " spawn egg");
								outputDebugInfo(breedingData, "=== END === }");

								summonEgg(entity);
								entity.remove();

							} else { // End of debug info: The random number did not work
								outputDebugInfo(breedingData, "FAIL: " + entity.toString()
										+ " could not be spawned because individualMobChance=" + individualMobChance
										+ " is not greater than randomNumber=" + randomNumber);

								outputDebugInfo(breedingData, "=== END === }");
							}

						} else { // End of debug info: The entity type is not an acceptable entity type

							outputDebugInfo(breedingData,
									"FAIL: " + entity.toString() + " is not an acceptable entity type.");
							outputDebugInfo(breedingData, "=== END === }");
						}
					}
				}
			}
		}
	}

	/**
	 * Summons the spawn_egg item, matching the entity's location and type
	 * 
	 * @param itemStack
	 * @param spawnLocation
	 */
	private void summonEgg(Entity entity) {

		// Gets the location of where the item should spawn
		Location<World> spawnLocation = entity.getLocation();

		// Creates the item
		ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.SPAWN_EGG).build();
		itemStack.offer(Keys.SPAWNABLE_ENTITY_TYPE, entity.getType());

		Extent extent = spawnLocation.getExtent();
		Entity item = extent.createEntity(EntityTypes.ITEM, spawnLocation.getPosition());
		item.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
		extent.spawnEntity(item);
	}

	/**
	 * Outputs the debug info according to {@link BreedingData#setDebugLevel(int)}.
	 * 
	 * @param breedingData
	 * @param output
	 */
	private void outputDebugInfo(BreedingData breedingData, String output) {
		int debugLevel = breedingData.getDebugLevel();
		if (debugLevel == 1) {
			this.breeding.getLogger().debug(output);
		} else if (debugLevel == 2) {
			this.breeding.getLogger().warn(output);
		}
	}

	/**
	 * 
	 * <P>
	 * 
	 * If the debug level is 1 or 2, it will display the config data here. 
	 * 
	 * @param data
	 */
	public void setData(BreedingData data) {
		this.breedingData = data;
		outputDebugInfo(breedingData, "Config Data before modification: " + breedingData.toString());

		// TODO modify shit
	}

}
