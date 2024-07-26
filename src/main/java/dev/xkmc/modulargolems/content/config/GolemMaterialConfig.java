package dev.xkmc.modulargolems.content.config;

import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2core.util.DataGenOnly;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.http.util.Asserts;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@SerialClass
public class GolemMaterialConfig extends BaseConfig {

	public static GolemMaterialConfig get() {
		return ModularGolems.MATERIALS.getMerged();
	}

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public HashMap<ResourceLocation, HashMap<GolemStatType, Double>> stats = new HashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public HashMap<ResourceLocation, HashMap<GolemModifier, Integer>> modifiers = new HashMap<>();

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialField
	public HashMap<ResourceLocation, Ingredient> ingredients = new HashMap<>();

	public Set<ResourceLocation> getAllMaterials() {
		TreeSet<ResourceLocation> set = new TreeSet<>(stats.keySet());
		set.retainAll(modifiers.keySet());
		set.retainAll(ingredients.keySet());
		return set;
	}

	@DataGenOnly
	public Builder addMaterial(ResourceLocation id, Ingredient ingredient) {
		return new Builder(this, id, ingredient);
	}

	@DataGenOnly
	public static class Builder {

		private final GolemMaterialConfig parent;
		private final ResourceLocation id;
		private final Ingredient ingredient;

		private final HashMap<GolemStatType, Double> stats = new HashMap<>();
		private final HashMap<GolemModifier, Integer> modifiers = new HashMap<>();

		private Builder(GolemMaterialConfig parent, ResourceLocation id, Ingredient ingredient) {
			this.parent = parent;
			this.id = id;
			this.ingredient = ingredient;
		}

		public Builder addStat(GolemStatType type, double val) {
			stats.put(type, val);
			return this;
		}

		public Builder addModifier(GolemModifier modifier, int lv) {
			Asserts.check(!(modifier instanceof AttributeGolemModifier), "Material cannot use attribute modifier");
			modifiers.put(modifier, lv);
			return this;
		}

		public GolemMaterialConfig end() {
			parent.stats.put(id, stats);
			parent.modifiers.put(id, modifiers);
			parent.ingredients.put(id, ingredient);
			return parent;
		}

	}

}
