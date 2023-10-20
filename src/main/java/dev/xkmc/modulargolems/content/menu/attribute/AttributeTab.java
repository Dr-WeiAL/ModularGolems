package dev.xkmc.modulargolems.content.menu.attribute;

import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AttributeTab extends TabBase<EquipmentGroup, AttributeTab> {

	public AttributeTab(int index, TabToken<EquipmentGroup, AttributeTab> token, TabManager<EquipmentGroup> manager,
						ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	@Override
	public void onTabClicked() {
		Minecraft.getInstance().setScreen(new AttributeScreen(manager.token.golem, MGLangData.TAB_ATTRIBUTE.get()));
	}

}
