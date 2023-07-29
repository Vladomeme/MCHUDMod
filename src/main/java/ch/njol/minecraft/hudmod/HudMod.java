package ch.njol.minecraft.hudmod;

import ch.njol.minecraft.config.Config;
import ch.njol.minecraft.hudmod.elements.BreathBar;
import ch.njol.minecraft.hudmod.elements.HealthBar;
import ch.njol.minecraft.hudmod.elements.HeldItemTooltip;
import ch.njol.minecraft.hudmod.elements.HungerBar;
import ch.njol.minecraft.hudmod.elements.MountHealthBar;
import ch.njol.minecraft.hudmod.elements.OverlayMessage;
import ch.njol.minecraft.hudmod.options.ConfigMenu;
import ch.njol.minecraft.hudmod.options.Options;
import ch.njol.minecraft.uiframework.ModSpriteAtlasHolder;
import ch.njol.minecraft.uiframework.hud.Hud;
import com.google.gson.JsonParseException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HudMod implements ClientModInitializer {

	public static final String MOD_IDENTIFIER = "njols-hud";

	public static final String OPTIONS_FILE_NAME = "njols-hud.json";

	public static Options options = new Options();

	public static ModSpriteAtlasHolder HUD_ATLAS;

	// Completely customized elements
	public static final BreathBar breathBar = new BreathBar();
	public static final HealthBar healthBar = new HealthBar();
	public static final HungerBar hungerBar = new HungerBar();
	public static final MountHealthBar mountHealthBar = new MountHealthBar();

	// Movable vanilla elements
	public static final HeldItemTooltip heldItemTooltip = new HeldItemTooltip();
	public static final OverlayMessage overlayMessage = new OverlayMessage();

	@Override
	public void onInitializeClient() {

		try {
			options = Config.readJsonFile(Options.class, OPTIONS_FILE_NAME);
		} catch (FileNotFoundException e) {
			// Config file doesn't exist, so use default config (and write config file).
			try {
				Config.writeJsonFile(options, OPTIONS_FILE_NAME);
			} catch (IOException ex) {
				// ignore
			}
		} catch (IOException | JsonParseException e) {
			// Any issue with the config file silently reverts to the default config
			e.printStackTrace();
		}

		Hud.INSTANCE.addElement(breathBar);
		Hud.INSTANCE.addElement(healthBar);
		Hud.INSTANCE.addElement(hungerBar);
		Hud.INSTANCE.addElement(mountHealthBar);
		Hud.INSTANCE.addElement(heldItemTooltip);
		Hud.INSTANCE.addElement(overlayMessage);

		try {
			Class.forName("com.terraformersmc.modmenu.api.ModMenuApi");
			Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
			ConfigMenu.registerTypes();
		} catch (ClassNotFoundException e) {
			// ignore
		}
	}

	public static void saveConfig() {
		MinecraftClient.getInstance().execute(() -> {
			try {
				Config.writeJsonFile(options, OPTIONS_FILE_NAME);
			} catch (IOException ex) {
				// ignore
			}
		});
	}

	public static void registerSprites() {
		if (HUD_ATLAS == null) {
			HUD_ATLAS = ModSpriteAtlasHolder.createAtlas(MOD_IDENTIFIER, "hud");
		} else {
			HUD_ATLAS.clearSprites();
		}
		HealthBar.registerSprites(HUD_ATLAS);
		HungerBar.registerSprites(HUD_ATLAS);
		BreathBar.registerSprites(HUD_ATLAS);
		MountHealthBar.registerSprites(HUD_ATLAS);
	}

	public static NavigableMap<Integer, Identifier> findLevelledSprites(ModSpriteAtlasHolder atlas, String subDirectory, String fileNamePrefix) {
		NavigableMap<Integer, Identifier> map = new TreeMap<>();
		Pattern overlayPattern = Pattern.compile(Pattern.quote(fileNamePrefix) + "(\\d+)\\.png$");
		List<Identifier> foundIcons = new ArrayList<>();
		MinecraftClient.getInstance().getResourceManager().streamResourcePacks()
			.forEach(rp -> rp.findResources(ResourceType.CLIENT_RESOURCES, atlas.getNamespace(), "textures/" + atlas.getAtlasName() + "/" + subDirectory,
				(path, supplier) -> {
					if (overlayPattern.matcher(path.getPath()).find()) {
						foundIcons.add(path);
					}
				}));
//				.filter(ids -> !ids.isEmpty())
//				.reduce((a, b) -> b);
		for (Identifier foundIcon : foundIcons) {
			Matcher m = overlayPattern.matcher(foundIcon.getPath());
			if (!m.find()) {
				continue;
			}
			int level = Integer.parseInt(m.group(1));
			Identifier identifier = atlas.registerSprite(foundIcon.getPath().substring("textures//".length() + atlas.getAtlasName().length(), foundIcon.getPath().length() - ".png".length()));
			map.put(level, identifier);
		}
		return map;
	}

}
