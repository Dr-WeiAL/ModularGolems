package dev.xkmc.modulargolems.content.modifier.immunes;

import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import net.minecraft.world.entity.Entity;

public class PlayerImmuneModifier extends GolemModifier {

	public PlayerImmuneModifier() {
		super(StatFilterType.HEALTH, 1);
	}

	@Override
	public boolean onAttacked(AbstractGolemEntity<?, ?> entity, DamageData.Attack event, int level) {
		if (level == 0) return false;
		Entity source = event.getSource().getEntity();
		if (source == null) return false;
		return entity.isAlliedTo(source);
	}

}
