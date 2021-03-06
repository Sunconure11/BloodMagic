package WayofTime.bloodmagic.compat.waila;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.*;
import WayofTime.bloodmagic.compat.waila.provider.*;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class BloodMagicHwylaPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new DataProviderBloodAltar(), BlockAltar.class);
        registrar.registerNBTProvider(new DataProviderBloodAltar(), BlockAltar.class);
        registrar.registerBodyProvider(new DataProviderTeleposer(), BlockTeleposer.class);
        registrar.registerBodyProvider(new DataProviderRitualController(), BlockRitualController.class);
        registrar.registerBodyProvider(new DataProviderAlchemyArray(), BlockAlchemyArray.class);
        registrar.registerBodyProvider(new DataProviderBloodTank(), BlockBloodTank.class);
        registrar.registerNBTProvider(new DataProviderBloodTank(), BlockBloodTank.class);
        registrar.registerStackProvider(new DataProviderAlchemyArray(), BlockAlchemyArray.class);
        registrar.registerStackProvider(new DataProviderMimic(), BlockMimic.class);
        registrar.registerNBTProvider(new DataProviderMimic(), BlockMimic.class);

        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_BYPASS_SNEAK, false);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_ALTAR, true);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_TELEPOSER, true);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_RITUAL, true);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_ARRAY, true);
        registrar.addConfig(BloodMagic.MODID, Constants.Compat.WAILA_CONFIG_BLOOD_TANK, true);
    }
}
