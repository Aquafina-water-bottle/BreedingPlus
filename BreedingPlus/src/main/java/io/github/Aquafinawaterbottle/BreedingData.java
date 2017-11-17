package io.github.Aquafinawaterbottle;

import java.util.StringJoiner;

/**
 *
 */
public class BreedingData {

	private boolean enabled;
	private int globalChance;
	private int debugLevel;
	private int[] individualMobChance = new int[12];

	/**
	 * see {@link #setEnabled(boolean)}
	 * @return
	 */
	public boolean getEnabled() {
		return this.enabled;
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * 
	 * <P>
	 * 
	 * The global chance should not be gotten because {@link #getInvididualMobChance()} should be used instead.
	 * @return
	 */
	public int getGlobalChance() {
		return this.globalChance;
	}

	/**
	 * 
	 * 
	 * @param global_chance
	 */
	public void setGlobalChance(int global_chance) {
		this.globalChance = global_chance;
	}

	/**
	 * see {@link #setIndividualMobChance(int[])}
	 * @return
	 */
	public int[] getInvididualMobChance() {
		return this.individualMobChance;
	}

	/**
	 * 
	 * @param individualMobChance
	 */
	public void setIndividualMobChance(int[] individualMobChance) {
		this.individualMobChance = individualMobChance;
	}

	/**
	 * see {@link #debugLevel}
	 * @return
	 */
	public int getDebugLevel() {
		return this.debugLevel;
	}

	/**
	 * 
	 * @param debugLevel
	 */
	public void setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
	}

	/**
	 * Displays all the fields of the class.
	 * Its intended use is to display the config data of the plugin when debugging.
	 */
	@Override
	public String toString() {
		
		// StringJoiner allows the first argument to go in between given strings as a nice replacement of StringBuilder
		// The second argument is the prefix while the third argument is the suffix of the string.
		StringJoiner individualMobChanceString = new StringJoiner(", ");

		for (EntityData entityData : EntitiesID.ALL_ENTITIES) {
			StringJoiner individualEntityData = new StringJoiner("=", "[", "]");
			individualEntityData.add(entityData.getName());
			individualEntityData.add(this.individualMobChance[entityData.getId()] + "");
			individualMobChanceString.add(individualEntityData.toString());
		}

		return "[enabled=" + this.enabled + ", global_chance=" + this.globalChance + ", debug_level=" + this.debugLevel
				+ ", individualMobChance=" + individualMobChanceString.toString() + "]";
	}

}
