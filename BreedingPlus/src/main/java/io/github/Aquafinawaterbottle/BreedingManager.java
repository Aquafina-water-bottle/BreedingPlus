package io.github.Aquafinawaterbottle;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
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

	/**
	 * General constructor for the this class.
	 * <P>
	 * 
	 * All this does is store {@link #breeding}, of which its only purpose is to
	 * provide a logger that is identical to the Breeding class.
	 * 
	 * @param breeding internal variable to store the main class's contents.
	 */
	public BreedingManager(Breeding breeding) {
		this.breeding = breeding;
	}

	/**
	 * Runs when an entity spawns, and decides whether a spawn egg should
	 * replace the entity or not.
	 * <P>
	 * This runs through several checks:
	 * <ol>
	 *  <li>The plugin is enabled</li>
	 *  <li>The entity is not summoned by the plugin</li>
	 * 	<li>The entity is summoned from a breeding event</li>
	 *  <li>The entity is the proper entity type as determined by {@link EntitiesID#ALL_ENTITIES}</li>
	 * 	<li>The entity passes the prng chosen by the JVM</li>
	 * </ol>
	 * 
	 * @param event runs when any entity spawns in the world
	 */
	@Listener
	public void checkingSpawnEvent(SpawnEntityEvent event) {

		// Checks whether the plugin is enabled or not
		if (breedingData.getEnabled() == false) {
			return;
		}

		/* Checks whether this plugin summoned the entity or not.
		 * This happens when the spawn egg item is spawned, and it clutters up the
		 * debugging info.
		 * 
		 * Note: getSource() gets the first part of the cause
		 */
		PluginContainer currentPlugin = Sponge.getPluginManager().getPlugin(PluginData.ID).get();
		if (event.getSource().equals(currentPlugin)) {
			return;
		}

		// Must check whether the event context contains the key in the first place
		if (event.getContext().containsKey(EventContextKeys.SPAWN_TYPE)) {

			// Checks whether the entity is summoned by a breeding event
			if (event.getContext().get(EventContextKeys.SPAWN_TYPE).get().equals(SpawnTypes.BREEDING)) {

				// Beginning of the debugging info
				outputDebugStart(breedingData);
				outputDebugInfo(breedingData,
						"The following has been summoned by SpawnTypes.BREEDING:" + event.toString());

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
							outputDebugEnd(breedingData);

							summonEgg(entity);
							entity.remove();

						} else { // End of debug info: The random number did not work
							outputDebugInfo(breedingData, "FAIL: " + entity.toString() + " could not be spawned");
							outputDebugEnd(breedingData);
						}

					} else { // End of debug info: The entity type is not an acceptable entity type

						outputDebugInfo(breedingData,
								"FAIL: " + entity.toString() + " is not an acceptable entity type.");
						outputDebugEnd(breedingData);
					}
				}
			}
		}
	}

	/**
	 * Summons the spawn_egg item, matching the entity's location and type.
	 * 
	 * @param entity determines the location of the item entity and the type of spawn egg
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
	 * @param breedingData data required to get the debug level
	 * @param output message to output
	 */
	private void outputDebugInfo(BreedingData breedingData, String output) {
		int debugLevel = breedingData.getDebugLevel();
		switch (debugLevel) {
		case 1:
			this.breeding.getLogger().debug("(DEBUG) " + output);
			break;
		case 2:
			this.breeding.getLogger().info("(DEBUG) " + output);
			break;
		case 3:
			this.breeding.getLogger().warn("(DEBUG) " + output);
			break;
		}
	}

	/**
	 * Predefined info that is outputted at the start of a debug session
	 * using {@link #outputDebugInfo(BreedingData, String)}
	 * 
	 * @param data data required to get the debug level
	 */
	private void outputDebugStart(BreedingData data) {
		String startMessage = "<< == START ==";
		outputDebugInfo(data, startMessage);
	}

	/**
	 * Predefined info that is outputted at the end of a debug session
	 * using {@link #outputDebugInfo(BreedingData, String)}
	 * 
	 * @param data data required to get the debug level
	 */
	private void outputDebugEnd(BreedingData data) {
		String endMessage = "=== END === >>";
		outputDebugInfo(data, endMessage);
	}

	/**
	 * Sets the data of the breeding manager to whatever the inputted data is.
	 * <P>
	 * 
	 * Any incorrect values will be fixed here, as said by
	 * {@link BreedingData#setGlobalChance(int)},
	 * {@link BreedingData#setIndividualMobChance(int[])}, 
	 * and {@link BreedingData#setDebugLevel(int)}.
	 * 
	 * Config data will outputted according to {@link BreedingData#setDebugLevel(int)}.
	 * 
	 * @param data the inputted data
	 */
	public void setData(BreedingData data) {
		this.breedingData = data;
		outputDebugInfo(breedingData, "Config Data before modification: " + breedingData.toString());

		// Checks the settings to see if the values are in the correct range, and
		// replaces any -1 values in the individual mob chance to the global chance.

		// Checks whether global_chance is an integer between 0 and 100, defaults to 0
		if ((breedingData.getGlobalChance() >= 0 && breedingData.getGlobalChance() <= 100) == false) {
			breedingData.setGlobalChance(0);
		}

		// Checks whether a value is -1 or is not a value between 0 and 100, will be set to the global chance
		for (EntityData entityData : EntitiesID.ALL_ENTITIES) {
			int individualMobChance = breedingData.getInvididualMobChance()[entityData.getId()];
			if (individualMobChance == -1 || !(individualMobChance >= 0 && individualMobChance <= 100)) {
				breedingData.getInvididualMobChance()[entityData.getId()] = breedingData.getGlobalChance();
			}
		}

		outputDebugInfo(breedingData, "Config Data after modification: " + breedingData.toString());
	}

}
