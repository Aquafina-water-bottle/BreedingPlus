package io.github.Aquafinawaterbottle;

import java.util.StringJoiner;

/**
 * Class to store the data given by the config file.
 */
public class BreedingData {

	/**
	 * This defines whether the plugin is enabled or not. If
	 * the plugin is not enabled, breeding will not generate spawn
	 * eggs and no debugging will happen when a breeding event occurs.
	 * <P>
	 * 
	 * The values under the config file can either be "true" or "false"
	 * (ignoring case). If the value is missing or set incorrectly, this 
	 * value defaults to "false".
	 */
	private boolean enabled;

	/**
	 * This defines the percent chance a spawn egg should spawn
	 * instead of a baby, given {@link #individualMobChance} if
	 * the mob is set to "-1".
	 * <P>
	 * 
	 * The values under the config file should be set as an integer
	 * between 0 and 100. If the value is missing or set incorrectly,
	 * this value defaults to "0".
	 * 
	 */
	private int globalChance;

	/**
	 * This defines the percent chance a spawn egg should spawn
	 * for each individual mob type.
	 * <P>
	 * 
	 * The values under the config file should be set as an integer
	 * between -1 and 100. Setting the value as -1 will automatically
	 * use the {@link #globalChance} value. If the value is missing
	 * or set incorrectly, this value defaults to "-1".
	 */
	private int[] individualMobChance = new int[12];

	/**
	 * This defines whether debug info for the plugin should be
	 * outputted, and at what visibility it will be outputted at.
	 * It outputs the entire event (cause, context, entity, 
	 * location, etc.) and reasons why a breeding event failed
	 * to create a spawn egg item (prng, wrong type).
	 * <P>
	 * 
	 * There are 4 options for setting the debug level:
	 * <ol start="0">
	 *  <li>Does not output anything</li>
	 *  <li>Outputs at the debug level (visible in the log file)</li>
	 *  <li>Outputs at the info level (visible in console as white text)</li>
	 *  <li>Outputs at the warn level (visible in console as yellow text)</li>
	 * </ol>
	 * <P>
	 * 
	 * If the value is missing or set incorrectly, this value
	 * defaults to "0".
	 * 
	 */
	private int debugLevel;

	/**
	 * Gets {@link #enabled}.
	 * 
	 * @return
	 */
	public boolean getEnabled() {
		return this.enabled;
	}

	/**
	 * Sets {@link #enabled}.
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets {@link #globalChance}.
	 * <P>
	 * 
	 * Note: The global chance should not be gotten because
	 * {@link #getInvididualMobChance()} should be used instead.
	 * The only time this should be used is when changing the values
	 * inside {@link #individualMobChance} from -1 to the global
	 * value.
	 * 
	 * @return
	 */
	public int getGlobalChance() {
		return this.globalChance;
	}

	/**
	 * Sets {@link #globalChance}.
	 * 
	 * @param global_chance
	 */
	public void setGlobalChance(int global_chance) {
		this.globalChance = global_chance;
	}

	/**
	 * Gets {@link #individualMobChance}.
	 * 
	 * @return
	 */
	public int[] getInvididualMobChance() {
		return this.individualMobChance;
	}

	/**
	 * Sets {@link #individualMobChance}.
	 * 
	 * @param individualMobChance
	 */
	public void setIndividualMobChance(int[] individualMobChance) {
		this.individualMobChance = individualMobChance;
	}

	/**
	 * Gets {@link #debugLevel}.
	 * 
	 * @return
	 */
	public int getDebugLevel() {
		return this.debugLevel;
	}

	/**
	 * Sets {@link #debugLevel}.
	 * 
	 * @param debugLevel
	 */
	public void setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
	}

	/**
	 * Displays all the fields of the class. Its intended 
	 * use is to display the config data of the plugin when 
	 * debugging.
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
