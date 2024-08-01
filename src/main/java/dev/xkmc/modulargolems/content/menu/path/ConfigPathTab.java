package dev.xkmc.modulargolems.content.menu.path;

import dev.xkmc.l2menustacker.init.MouseCache;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.modulargolems.content.menu.registry.ConfigGroup;
import dev.xkmc.modulargolems.content.menu.registry.OpenConfigMenuToServer;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.network.chat.Component;

public class ConfigPathTab extends TabBase<ConfigGroup, ConfigPathTab> {

	public ConfigPathTab(int index, TabToken<ConfigGroup, ConfigPathTab> token, TabManager<ConfigGroup> manager, Component title) {
		super(index, token, manager, title);
	}

	@Override
	public void onTabClicked() {
		MouseCache.cacheMousePos();
		var entry = manager.token.editor.entry();
		ModularGolems.HANDLER.toServer(new OpenConfigMenuToServer(entry.getID(), entry.getColor(), OpenConfigMenuToServer.Type.PATH));
	}

}