package ch.njol.minecraft.hudmod.options;

import ch.njol.minecraft.config.annotations.Category;
import ch.njol.minecraft.config.annotations.DescriptionLine;
import ch.njol.minecraft.hudmod.HudMod;
import ch.njol.minecraft.uiframework.ElementPosition;

public class Options implements ch.njol.minecraft.config.Options {

	public enum AbsorptionStart {
		HEALTH_START, HEALTH_END, NEW_BAR;
	}

	@Category("hud")
	public DescriptionLine hud_info;
	@Category("hud")
	public final boolean hud_enabled = true;

	@Category("hud")
	public final boolean hud_statusBarsEnabled = true;
	@Category("hud")
	public final float hud_regenerationSpeed = 10;
	@Category("hud")
	public final boolean hud_healthText = true;
	@Category("hud")
	public final int hud_healthTextOffset = -5;
	@Category("hud")
	public final int hud_absorptionMaxBars = 5;
	@Category("hud")
	public final int hud_absorptionBarsOffset = -3;
	@Category("hud")
	public final AbsorptionStart hud_absorptionStart = AbsorptionStart.HEALTH_END;
	@Category("hud")
	public final boolean hud_healthMirror = false;
	@Category("hud")
	public final boolean hud_hungerMirror = false;
	@Category("hud")
	public final boolean hud_breathMirror = true;
	@Category("hud")
	public final boolean hud_hideBreathWithWaterBreathing = false;
	@Category("hud")
	public final boolean hud_mountHealthEnabled = true;
	@Category("hud")
	public final boolean hud_mountHealthText = true;
	@Category("hud")
	public final int hud_mountHealthTextOffset = -5;
	@Category("hud")
	public final boolean hud_mountHealthMirror = false;
	@Category("hud")
	public final boolean hud_moveOverlayMessage = true;
	@Category("hud")
	public final boolean hud_moveHeldItemTooltip = true;
	@Category("hud")
	public DescriptionLine hud_positionsInfo;
	@Category("hud")
	public final ElementPosition hud_healthBarPosition = new ElementPosition(0.5f, 0, 1.0f, -39, 0.5f, 1.0f);
	@Category("hud")
	public final ElementPosition hud_hungerBarPosition = new ElementPosition(0.5f, 59, 1.0f, -23, 0.5f, 1.0f);
	@Category("hud")
	public final ElementPosition hud_breathBarPosition = new ElementPosition(0.5f, -60, 1.0f, -23, 0.5f, 1.0f);
	@Category("hud")
	public final ElementPosition hud_mountHealthBarPosition = new ElementPosition(0.5f, 60, 1.0f, -55, 0.5f, 1.0f);
	@Category("hud")
	public final ElementPosition hud_overlayMessagePosition = new ElementPosition(0.5f, 0, 1.0f, -81, 0.5f, 1.0f);
	@Category("hud")
	public final ElementPosition hud_heldItemTooltipPosition = new ElementPosition(0.5f, 0, 1.0f, -36, 0.5f, 1.0f);

	public void onUpdate() {
		HudMod.saveConfig();
	}

}
