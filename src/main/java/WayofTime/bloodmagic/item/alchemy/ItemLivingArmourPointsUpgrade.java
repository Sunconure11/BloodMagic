package WayofTime.bloodmagic.item.alchemy;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.core.RegistrarBloodMagicItems;
import WayofTime.bloodmagic.item.armour.ItemLivingArmour;
import WayofTime.bloodmagic.livingArmour.LivingArmour;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemLivingArmourPointsUpgrade extends Item implements IVariantProvider {
    public static final String DRAFT_ANGELUS = "draftAngelus";
    private static ArrayList<String> names = new ArrayList<String>();

    public ItemLivingArmourPointsUpgrade() {
        super();

        setUnlocalizedName(BloodMagic.MODID + ".livingPointUpgrade.");
        setHasSubtypes(true);
        setCreativeTab(BloodMagic.TAB_BM);

        buildItemList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (!stack.hasTagCompound())
            return;

        tooltip.addAll(Arrays.asList(TextHelper.cutLongString(TextHelper.localizeEffect("tooltip.bloodmagic.livingArmourPointsUpgrade.desc", 200))));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

        if (player == null || !player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        if (!worldIn.isRemote) {
            player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 5));
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 5));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));

            if (LivingArmour.hasFullSet(player)) {
                ItemStack chestStack = Iterables.toArray(player.getArmorInventoryList(), ItemStack.class)[2];
                LivingArmour armour = ItemLivingArmour.getLivingArmour(chestStack);
                if (armour != null) {
                    if (armour.maxUpgradePoints < 200) {
                        armour.maxUpgradePoints = 200;
                        ((ItemLivingArmour) chestStack.getItem()).setLivingArmour(chestStack, armour, true);
                        ItemLivingArmour.setLivingArmour(chestStack, armour);
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }

    private void buildItemList() {
        names.add(0, DRAFT_ANGELUS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + names.get(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public void populateVariants(Int2ObjectMap<String> variants) {
        for (String name : names)
            variants.put(names.indexOf(name), "type=" + name);
    }

    public static ItemStack getStack(String name) {
        return new ItemStack(RegistrarBloodMagicItems.POINTS_UPGRADE, 1, names.indexOf(name));
    }

    public static ItemStack getStack(String key, int stackSize) {
        ItemStack stack = getStack(key);
        stack.setCount(stackSize);

        return stack;
    }

    public static ArrayList<String> getNames() {
        return names;
    }
}
