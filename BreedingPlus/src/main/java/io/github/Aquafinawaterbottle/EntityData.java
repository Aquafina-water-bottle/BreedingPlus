package io.github.Aquafinawaterbottle;

import java.util.Optional;

import org.spongepowered.api.entity.EntityType;

/**
 * Class to store data for each entity.
 */
public class EntityData {

	/**
	 * The id value is used by the plugin to get the individual mob chance 
	 * (defined by {@link BreedingData#setIndividualMobChance(int[])} for each 
	 * mob when summoning a spawn egg.
	 */
	private int id;

	/**
	 * The entity type is the entity type given by the sponge api.
	 */
	private EntityType entityType;

	public EntityData(EntityType entityType, int id) {
		this.entityType = entityType;
		this.id = id;
	}

	/**
	 * See {@link #id}.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * See {@link #id}.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * See {@link #entityType}.
	 * 
	 * @return
	 */
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * See {@link #entityType}.
	 * 
	 * @param entityType
	 */
	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	/**
	 * Gets the plugin entity data from a given entity type.
	 * 
	 * @param entityType the sponge entity type
	 * @return the entity data if it is within the acceptable entity types
	 */
	public static Optional<EntityData> getEntityDataFrom(EntityType entityType) {

		for (EntityData acceptableEntityData : EntitiesID.ALL_ENTITIES) {
			if (acceptableEntityData.getEntityType() == entityType) {
				return Optional.of(acceptableEntityData);
			}
		}

		return Optional.empty();
	}

	/**
	 * Gets the entity name.
	 * <P>
	 * 
	 * To match the name that sponge gives the entity type with the config file, 
	 * getting the name returns a lowercase version of the entity name.
	 * 
	 * @return the entity name in lowercase
	 */
	public String getName() {
		return entityType.getName().toLowerCase();
	}

	@Override
	public String toString() {
		return "EntityData [id=" + this.id + ", name=" + this.getName() + ", entityType=" + this.entityType + "]";
	}

}
