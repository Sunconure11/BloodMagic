package WayofTime.bloodmagic.item;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ItemActivationCrystal extends ItemBindableBase implements IVariantProvider {
    public static String[] names = {"weak", "awakened", "creative"};

    public ItemActivationCrystal() {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".activationCrystal.");
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names[stack.getItemDamage()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextHelper.localize("tooltip.bloodmagic.activationCrystal." + names[stack.getItemDamage()]));

        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public void populateVariants(Int2ObjectMap<String> variants) {
        variants.put(0, "type=weak");
        variants.put(1, "type=demonic");
        variants.put(2, "type=creative");
    }

    public int getCrystalLevel(ItemStack stack) {
        return stack.getItemDamage() > 1 ? Integer.MAX_VALUE : stack.getItemDamage() + 1;
    }
}
