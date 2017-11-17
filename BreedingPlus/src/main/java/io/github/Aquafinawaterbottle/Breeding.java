package io.github.Aquafinawaterbottle;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.ConfigDir;
import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;

//@formatter:off
@Plugin(id = PluginData.ID, name = PluginData.NAME, version = PluginData.VERSION, authors = PluginData.AUTHOR, url = PluginData.URL)
// @formatter:on

/**
 * I swear to god that I will write as much documentation as I can from now on.
 * My previous project had fuck all for documentation, and it pretty much 
 * forced me to abandon it since I had no idea what I was reading.
 * <P>
 * 
 * With that said, welcome to this plugin's documentation. This is the main class.
 * Also, this project is using gradle & eclipse. If I make another plugin, 
 * I'll try using Intellij IDEA.
 */
public class Breeding {

	/**
	 * The config path that the users can read and edit.
	 */
	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configPath;

	/**
	 * The logger to display text onto console.
	 */
	@Inject
	private Logger logger;

	/**
	 * Internal variable that does all of the work with the data given from 
	 * the config file.
	 */
	private BreedingManager breedingManager;

	/**
	 * Defines the player config file name (the config
	 * file under the config folder).
	 */
	final String PLAYER_CONFIG_NAME = "player.conf";
	
	/**
	 * Defines the default config name (the config
	 * file you see if you open up the plugin jar
	 * file with winrar/7zip/whatever).
	 */
	final String DEFAULT_CONFIG_NAME = "default.conf";

	/**
	 * Initializes the plugin by loading up the config file.
	 * 
	 * @param event runs when the server is starting up
	 */
	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		breedingManager = new BreedingManager(this);

		// Gets config data
		breedingManager.setData(getConfigData());

		// Activate listeners in this class and the BreedingManager class
		Sponge.getEventManager().registerListeners(this, breedingManager);
	}

	/**
	 * Attempts to load the player config file (the config file the server 
	 * administrators can edit).
	 * <P>
	 * 
	 * If the player config file cannot be loaded up for whatever reason, 
	 * the default config settings will be returned.
	 * 
	 * If the player config file does not exist in the expected area,
	 * the player config file will be set as the default config file.
	 * 
	 * If the default config file cannot be found, the default config 
	 * settings will still be used, except the default config file will 
	 * not be put in place of the player config file.
	 * 
	 * @return the config data
	 */
	private BreedingData getConfigData() {

		Path configFilePath = configPath.resolve(PLAYER_CONFIG_NAME);
		ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
				.setPath(configFilePath).build();

		Optional<ConfigurationNode> checkRootNode = Optional.empty();

		// I have no absolutely no idea when IOException will be thrown
		// If the exception is thrown, rootNode will remain empty
		logger.info("Loading up the player config file...");
		try {
			checkRootNode = Optional.of(loader.load());
		} catch (IOException e) {
			getLogger().error("An error occured while loading up the player config file.");
			getLogger().error(e.getMessage());

			return getDefaultConfigData();
		}

		// Checks if the plugin can't read or find the player config file
		if (checkRootNode.isPresent() && checkRootNode.get().getChildrenMap().isEmpty()) {

			// Checks if the config file can be found
			if (Files.exists(configFilePath)) {
				// Parsing error
				logger.error("An error occured while reading the config file. Check the syntax.");

			} else {
				// Otherwise, the config file should be made.
				logger.warn("The player config file does not exist. Creating config file...");
				loadDefaultConfig();
			}

			// No matter what happens, the plugin will use the default config data
			// because the player config data cannot be used.
			return getDefaultConfigData();
		}

		return getPlayerConfigData(checkRootNode.get());
	}

	/**
	 * Gets the config data as defined by the player.conf file.
	 * Anything that has the wrong value will be subject to change
	 * at {@link BreedingManager#setData(BreedingData)}.
	 * 
	 * @param rootNode the node for the player.conf file
	 * @return player config data
	 */
	private BreedingData getPlayerConfigData(ConfigurationNode rootNode) {

		// Reads the player config file.
		BreedingData playerConfigData = new BreedingData();

		boolean enablePlugin;
		int globalChance;
		int[] individualMobChance = new int[12];
		int debugLevel;

		// Gets all settings from the player config file here
		enablePlugin = rootNode.getNode("enable_plugin").getBoolean();
		globalChance = rootNode.getNode("global_chance").getInt();

		for (EntityData entityData : EntitiesID.ALL_ENTITIES) {
			individualMobChance[entityData.getId()] = rootNode.getNode("individual_mob_chance", entityData.getName())
					.getInt();
		}

		debugLevel = rootNode.getNode("debug_level").getInt();

		playerConfigData.setEnabled(enablePlugin);
		playerConfigData.setGlobalChance(globalChance);
		playerConfigData.setIndividualMobChance(individualMobChance);
		playerConfigData.setDebugLevel(debugLevel);

		getLogger().info("Successfully loaded and read the config file.");

		return playerConfigData;
	}

	/**
	 * Runs only when the player config file cannot be used.
	 * <P>
	 * 
	 * This method is hard coded into the JVM. Therefore, it 
	 * does not actually need the default configuration file to work.
	 * 
	 * @return the default config data
	 */
	private BreedingData getDefaultConfigData() {
		getLogger().warn("The default config settings will be used until the player config file can be read.");
		BreedingData defaultData = new BreedingData();

		boolean enabled = true;
		int globalChance = 15;
		int debugLevel = 0;

		// Creates an array filled with -1 for the individual mob chances
		// However, the horse entry will be 10.
		int[] individualMobChance = new int[12];
		Arrays.fill(individualMobChance, -1);
		individualMobChance[EntitiesID.HORSE.getId()] = 10;

		defaultData.setEnabled(enabled);
		defaultData.setGlobalChance(globalChance);
		defaultData.setIndividualMobChance(individualMobChance);
		defaultData.setDebugLevel(debugLevel);

		return defaultData;
	}

	/**
	 * Loads the default config given inside the plugin jar file.
	 * If it can be loaded, it creates the player.conf file to have
	 * the same contents as the default config file.
	 */
	private void loadDefaultConfig() {

		// Gets the default config file asset
		Optional<Asset> defaultConfigFile = Sponge.getAssetManager().getAsset(this, DEFAULT_CONFIG_NAME);

		// Checks whether the default config file can be loaded
		if (defaultConfigFile.isPresent()) {
			try {
				Files.createDirectories(configPath);
				defaultConfigFile.get().copyToFile(configPath.resolve(PLAYER_CONFIG_NAME));
				logger.info("Successfully created the player config file.");

			} catch (IOException e) {
				logger.error("The player config file cannot be made.");
				logger.error(e.getMessage());
			}

		} else {
			// The default config file does not exist.
			// If you reach this, what on earth is happening e_e
			logger.error("The default config file cannot be found. Therefore, the player config file cannot be made.");
		}
	}

	/**
	 * Gets the config data again if the plugin is reloaded
	 * 
	 * @param event runs when "/sponge plugins reload" is ran
	 */
	@Listener
	public void onReloadEvent(GameReloadEvent event) {
		breedingManager.setData(getConfigData());
	}

	/**
	 * Gets the logger for the plugin to output text into the console.
	 * 
	 * @return the logger
	 */
	public Logger getLogger() {
		return this.logger;
	}

}
