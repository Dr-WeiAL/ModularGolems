package dev.xkmc.modulargolems.compat.materials.l2complements;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.SimpleUpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.PotionAttackModifier;
import dev.xkmc.modulargolems.content.modifier.base.PotionDefenseModifier;
import dev.xkmc.modulargolems.content.modifier.base.TargetBonusModifier;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;

import static dev.xkmc.modulargolems.init.registrate.GolemItems.regModUpgrade;
import static dev.xkmc.modulargolems.init.registrate.GolemModifiers.reg;

public class LCCompatRegistry {

	public static final Val<ConduitModifier> CONDUIT;
	public static final Val<FreezingModifier> FREEZE;
	public static final Val<SoulFlameModifier> FLAME;
	public static final Val<EnderTeleportModifier> TELEPORT;
	public static final Val<PotionAttackModifier> CURSE, INCARCERATE;
	public static final Val<PotionDefenseModifier> CLEANSE;
	public static final Val<TargetBonusModifier> POSEIDITE, TOTEMIC_GOLD;

	public static final ItemEntry<SimpleUpgradeItem> FORCE_FIELD, FREEZE_UP, FLAME_UP, TELEPORT_UP, ATK_UP, SPEED_UP,
			UPGRADE_CURSE, UPGRADE_INCARCERATE, UPGRADE_CLEANSE;

	static {
		CONDUIT = reg("conduit", ConduitModifier::new, "When in water: Reduce damage taken to %s%%. Every %s seconds, deal %s conduit damage to target in water/rain remotely. Boost following stats:");
		FREEZE = reg("freezing", FreezingModifier::new, "Potion Upgrade: Freezing", "Get Ice Blade and Ice Thorn enchantment effects. Immune to freezing damage.");
		FLAME = reg("soul_flame", SoulFlameModifier::new, "Potion Upgrade: Soul Flame", "Get Soul Flame Blade and Soul Flame Thorn enchantment effects. Immune to soul flame damage.");
		TELEPORT = reg("teleport", EnderTeleportModifier::new, "Teleport randomly to avoid physical damage. Teleport toward target when attacking. Teleport has %ss cool down.");

		CURSE = reg("curse", () -> new PotionAttackModifier(StatFilterType.MASS, 3,
				i -> new MobEffectInstance(LCEffects.CURSE.holder(), 60 * i)), "Potion Upgrade: Curse", null);
		INCARCERATE = reg("incarcerate", () -> new PotionAttackModifier(StatFilterType.MASS, 3,
				i -> new MobEffectInstance(LCEffects.INCARCERATE.holder(), 20 * i)), "Potion Upgrade: Incarceration", null);

		POSEIDITE = reg("poseidite", () -> new TargetBonusModifier(e -> e.isSensitiveToWater() || e.getType().is(EntityTypeTags.SENSITIVE_TO_IMPALING)),
				"Deal %s%% more damage to mobs sensitive to water or water based mobs");
		TOTEMIC_GOLD = reg("totemic_gold", () -> new TargetBonusModifier(e -> e.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)),
				"Deal %s%% more damage to undead mobs");
		CLEANSE = reg("cleanse", () -> new PotionDefenseModifier(1, LCEffects.CLEANSE::holder), "Potion Upgrade: Cleanse", null);

		FORCE_FIELD = regModUpgrade("force_field", () -> GolemModifiers.PROJECTILE_REJECT, LCDispatch.MODID).lang("Wither Armor Upgrade").register();
		FREEZE_UP = regModUpgrade("freezing", () -> FREEZE, LCDispatch.MODID).lang("Potion Upgrade: Freezing").register();
		FLAME_UP = regModUpgrade("soul_flame", () -> FLAME, LCDispatch.MODID).lang("Potion Upgrade: Soul Flame").register();
		TELEPORT_UP = regModUpgrade("teleport", () -> TELEPORT, LCDispatch.MODID).lang("Teleport Upgrade").register();
		ATK_UP = regModUpgrade("attack_high", () -> GolemModifiers.DAMAGE, 5, true, LCDispatch.MODID).lang("Attack Upgrade V").register();
		SPEED_UP = regModUpgrade("speed_high", () -> GolemModifiers.SPEED, 5, true, LCDispatch.MODID).lang("Speed Upgrade V").register();
		UPGRADE_CURSE = regModUpgrade("curse", () -> CURSE, LCDispatch.MODID).lang("Potion Upgrade: Curse").register();
		UPGRADE_INCARCERATE = regModUpgrade("incarcerate", () -> INCARCERATE, LCDispatch.MODID).lang("Potion Upgrade: Incarceration").register();
		UPGRADE_CLEANSE = regModUpgrade("cleanse", () -> CLEANSE, LCDispatch.MODID).lang("Potion Upgrade: Cleanse").register();
	}

	public static void register() {
		MGTagGen.OPTIONAL_ITEM.add(e -> e.addTag(MGTagGen.POTION_UPGRADES)
				.addOptional(FLAME_UP.getId())
				.addOptional(FREEZE_UP.getId())
				.addOptional(UPGRADE_CURSE.getId())
				.addOptional(UPGRADE_INCARCERATE.getId()));
		MGTagGen.OPTIONAL_ITEM.add(e -> e.addTag(MGTagGen.BLUE_UPGRADES)
				.addOptional(UPGRADE_CLEANSE.getId()));
	}

}
