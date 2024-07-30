package dev.xkmc.modulargolems.compat.curio;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2tabs.compat.common.CuriosEventHandler;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.registry.IMenuPvd;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;
import java.util.function.Consumer;

public class CurioCompatRegistry {

	public static CurioCompatRegistry INSTANCE;

	@Nullable
	public static CurioCompatRegistry get() {
		if (ModList.get().isLoaded("curios")) {
			if (INSTANCE == null) {
				INSTANCE = new CurioCompatRegistry();
			}
			return INSTANCE;
		}
		return null;
	}

	public static Optional<ItemStack> getItem(LivingEntity e, String slot) {
		return CuriosApi.getCuriosInventory(e)
				.flatMap(x -> x.findCurio(slot, 0).map(SlotResult::stack));
	}

	public static void register() {
		var ins = get();
		if (ins == null) return;
		ins.registerImpl();
	}

	public static void clientRegister() {
		var ins = get();
		if (ins == null) return;
		ins.clientRegisterImpl();
	}

	public static <T> void onJEIRegistry(Consumer<Class<? extends ITabScreen>> consumer) {
		var ins = get();
		if (ins == null) return;
		ins.onJEIRegistryImpl(consumer);
	}

	public static IMenuPvd create(AbstractGolemEntity<?, ?> entity) {
		return new GolemCuriosMenuPvd(entity, 0);
	}

	public static void tryOpen(ServerPlayer player, LivingEntity target) {
		if (get() == null) return;
		var opt = CuriosApi.getCuriosInventory(target);
		if (opt.isEmpty()) return;
		if (opt.get().getSlots() == 0) return;
		var pvd = new GolemCuriosMenuPvd(target, 0);
		CuriosEventHandler.openMenuWrapped(player, () -> player.openMenu(pvd, pvd::writeBuffer));

	}

	public MenuEntry<GolemCuriosListMenu> menuType;
	public Val<TabToken<EquipmentGroup, GolemCurioTab>> tab;

	public void registerImpl() {
		menuType = ModularGolems.REGISTRATE.menu("golem_curios", GolemCuriosListMenu::fromNetwork, () -> GolemCuriosListScreen::new).register();
		tab = GolemTabRegistry.TAB_REG.reg("curios", () -> GolemTabRegistry.EQUIPMENTS.registerTab(
				() -> GolemCurioTab::new, L2TabsLangData.CURIOS.get()));
	}

	public void clientRegisterImpl() {
	}

	private void onJEIRegistryImpl(Consumer<Class<? extends ITabScreen>> consumer) {
		consumer.accept(GolemCuriosListScreen.class);
	}

	public ItemStack getSkin(HumanoidGolemEntity le) {
		return CuriosApi.getCuriosInventory(le).flatMap(e -> e.getStacksHandler("golem_skin"))
				.map(ICurioStacksHandler::getStacks).map(e -> e.getSlots() == 0 ? null : e.getStackInSlot(0)).orElse(ItemStack.EMPTY);
	}

}
