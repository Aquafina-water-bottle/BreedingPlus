package io.github.Aquafinawaterbottle;

import java.util.Optional;

import org.spongepowered.api.entity.EntityType;

/**
 *
 */
public class EntityData {
	private int id;
	private EntityType entityType;

	public EntityData(EntityType entityType, int id) {
		this.entityType = entityType;
		this.id = id;
	}

	public String getName() {
		return entityType.getName().toLowerCase();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
	
	/**
	 * Gets the plugin entity data from a given entity type
	 * 
	 * @param entityType
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
	
}
