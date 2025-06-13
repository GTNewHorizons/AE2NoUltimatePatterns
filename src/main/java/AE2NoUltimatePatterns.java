
import net.minecraft.item.Item;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@SuppressWarnings("unused")
@Mod(
    modid = AE2NoUltimatePatterns.MODID,
    version = Tags.VERSION,
    name = "AE2NoUltimatePattern",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:appliedenergistics2;required-after:ae2fc")
public class AE2NoUltimatePatterns {

    public static final String MODID = "ae2noultimatepatterns";
    private Item dummyItem;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        dummyItem = new DummyItem();
        GameRegistry.registerItem(dummyItem, "item.ItemEncodedUltimatePattern");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        new PosteaTransformers().run();
    }

    @Mod.EventHandler
    public void missingMapping(FMLMissingMappingsEvent event) {
        var missing = event.getAll();

        for (FMLMissingMappingsEvent.MissingMapping missingMapping : missing) {
            if (missingMapping.name.equals("appliedenergistics2:item.ItemEncodedUltimatePattern")) {
                missingMapping.remap(dummyItem);
                return;
            }
        }
    }

}
