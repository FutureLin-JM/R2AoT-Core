package com.futurelin.r2aot.common.entity.job;

import com.futurelin.occultism_rs.item.ritual_satchel.RitualSatchelItem;
import com.futurelin.r2aot.api.item.RitualmasterItemStackHandler;
import com.futurelin.r2aot.api.mixin.accessor.IRitualRecipeAccessor;
import com.klikli_dev.occultism.common.entity.ai.goal.PickupItemsGoal;
import com.klikli_dev.occultism.common.entity.job.SpiritJob;
import com.klikli_dev.occultism.common.entity.spirit.SpiritEntity;
import com.klikli_dev.occultism.crafting.recipe.RitualRecipe;
import com.klikli_dev.occultism.registry.OccultismRecipes;
import com.klikli_dev.occultism.registry.OccultismSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.futurelin.r2aot.common.entity.RitualmasterEntity.DROPPED_BY_RITUALMASTER;
import static com.futurelin.r2aot.common.entity.RitualmasterEntity.DROPPED_RESULT;

public class RitualmasterJob extends SpiritJob {

    protected PickupItemsGoal pickupItemsGoal;
    protected List<Ingredient> itemsToPickUp = new ArrayList<>();
    protected Optional<RitualRecipe> currentRecipe = Optional.empty();
    protected RitualState ritualState = RitualState.IDLE;
    protected int ritualProgress = 0;
    protected int ritualDuration = 0;
    protected boolean needsItemUse = false;
    protected boolean needsSacrifice = false;
    protected boolean itemUseFulfilled = false;
    protected boolean sacrificeFulfilled = false;

    public Consumer<PlayerInteractEvent.RightClickItem> rightClickItemListener;
    public Consumer<LivingDeathEvent> livingDeathEventListener;

    public RitualmasterJob(SpiritEntity entity) {
        super(entity);
    }

    Level level = this.entity.level();

    @Override
    protected void onInit() {
        this.entity.targetSelector.addGoal(1, this.pickupItemsGoal = new PickupItemsGoal(this.entity));
        this.itemsToPickUp = this.entity.level().getRecipeManager().getAllRecipesFor(OccultismRecipes.RITUAL_TYPE.get())
                .stream()
                .flatMap(recipe -> Stream.concat(recipe.getIngredients().stream(),
                        Stream.of(recipe.getActivationItem())))
                .collect(Collectors.toList());
        ((RitualmasterItemStackHandler) this.entity.inventory).setOnInventoryChanged(this::checkRecipe);
    }

    @Override
    public void cleanup() {
        this.entity.targetSelector.removeGoal(this.pickupItemsGoal);
        if (this.entity.inventory instanceof RitualmasterItemStackHandler handler) {
            handler.setOnInventoryChanged(null);
        }
        if (this.rightClickItemListener != null) {
            MinecraftForge.EVENT_BUS.unregister(this.rightClickItemListener);
        }
        if (this.livingDeathEventListener != null) {
            MinecraftForge.EVENT_BUS.unregister(this.livingDeathEventListener);
        }
    }

    private boolean hasSatchel() {
        return this.entity.inventory.getStackInSlot(0).getItem() instanceof RitualSatchelItem;
    }

    @Override
    public void update() {
        if (!hasSatchel()) {
            return;
        }
        switch (this.ritualState) {
            case IDLE -> {
                if (this.currentRecipe.isPresent() && checkRitualItem(this.currentRecipe.get())) {
                    startRitual();
                }
            }
            case IN_PROGRESS -> {
                if (this.needsItemUse && !this.itemUseFulfilled) {
                    return;
                }
                if (this.needsSacrifice && !this.sacrificeFulfilled) {
                    return;
                }
                this.ritualProgress++;
                if (this.ritualProgress >= this.ritualDuration) {
                    finishRitual();
                }
            }
        }
    }

    private void startRitual() {
        RitualRecipe recipe = this.currentRecipe.get();
        level.playSound(null, this.entity.blockPosition(), OccultismSounds.START_RITUAL.get(), SoundSource.BLOCKS, 1, 1);

        this.rightClickItemListener = this::onPlayerRightClickItem;
        this.livingDeathEventListener = this::onLivingDeath;
        MinecraftForge.EVENT_BUS.addListener(this.rightClickItemListener);
        MinecraftForge.EVENT_BUS.addListener(this.livingDeathEventListener);

        // this.ritualDuration = Math.max(20, (int) Math.sqrt(recipe.getDuration()) * 20);
        this.ritualDuration = 20;
        this.ritualProgress = 0;

        this.needsItemUse = recipe.requiresItemUse();
        this.needsSacrifice = recipe.requiresSacrifice();
        this.itemUseFulfilled = !this.needsItemUse;
        this.sacrificeFulfilled = !this.needsSacrifice;

        this.ritualState = RitualState.IN_PROGRESS;
    }

    private void finishRitual() {
        if (this.currentRecipe.isEmpty()) {
            if (this.rightClickItemListener != null) {
                MinecraftForge.EVENT_BUS.unregister(this.rightClickItemListener);
                this.rightClickItemListener = null;
            }
            if (this.livingDeathEventListener != null) {
                MinecraftForge.EVENT_BUS.unregister(this.livingDeathEventListener);
                this.livingDeathEventListener = null;
            }
            this.currentRecipe = Optional.empty();
            this.ritualState = RitualState.IDLE;
            this.ritualProgress = 0;
            this.ritualDuration = 0;
            return;
        }

        RitualRecipe recipe = this.currentRecipe.get();

        ItemStack activationItem = findActivationItemStack(recipe);
        if (!activationItem.isEmpty()) {
            recipe.getRitual().finish(level, this.entity.blockPosition(), null, null, activationItem);
            consumeIngredients(recipe);
        }

        if (this.rightClickItemListener != null) {
            MinecraftForge.EVENT_BUS.unregister(this.rightClickItemListener);
        }
        if (this.livingDeathEventListener != null) {
            MinecraftForge.EVENT_BUS.unregister(this.livingDeathEventListener);
        }
        this.rightClickItemListener = null;
        this.livingDeathEventListener = null;

        this.currentRecipe = Optional.empty();
        this.ritualState = RitualState.IDLE;
    }

    private ItemStack findActivationItemStack(RitualRecipe recipe) {
        for (int slot = 0; slot < this.entity.inventory.getSlots(); slot++) {
            ItemStack stack = this.entity.inventory.getStackInSlot(slot);
            if (!stack.isEmpty() && recipe.getActivationItem().test(stack)) {
                return this.entity.inventory.extractItem(slot, 1, false);
            }
        }
        return ItemStack.EMPTY;
    }

    private void consumeIngredients(RitualRecipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (int slot = 0; slot < this.entity.inventory.getSlots(); slot++) {
                ItemStack stack = this.entity.inventory.getStackInSlot(slot);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    this.entity.inventory.extractItem(slot, 1, false);
                    break;
                }
            }
        }
    }

    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (this.ritualState != RitualState.IN_PROGRESS || this.currentRecipe.isEmpty()) {
            return;
        }
        Player player = event.getEntity();
        if (!player.level().isClientSide()
                && this.currentRecipe.get().getRitual().isValidItemUse(event)) {
            this.itemUseFulfilled = true;
        }
    }

    public void onLivingDeath(LivingDeathEvent event) {
        if (this.ritualState != RitualState.IN_PROGRESS || this.currentRecipe.isEmpty()) {
            return;
        }
        LivingEntity entityLivingBase = event.getEntity();
        if (!entityLivingBase.level().isClientSide()
                && event.getSource().getEntity() instanceof Player
                && this.currentRecipe.get().getRitual().isValidSacrifice(entityLivingBase)) {
            this.sacrificeFulfilled = true;
        }
    }

    public void checkRecipe() {
        if (!hasSatchel() || this.ritualState == RitualState.IN_PROGRESS) {
            return;
        }
        for (RitualRecipe recipe : level.getRecipeManager().getAllRecipesFor(OccultismRecipes.RITUAL_TYPE.get())) {
            if (((IRitualRecipeAccessor) recipe).matches(this.entity.inventory, level, this.entity)
                    && checkRitualItem(recipe)) {
                this.currentRecipe = Optional.of(recipe);
            }
        }
    }

    // TODO: 放弃配方仪式结构物品的方案，考虑改为直接使用仪式物品
    public boolean checkRitualItem(RitualRecipe recipe) {
        return true;
    }

    @Override
    public List<Ingredient> getItemsToPickUp() {
        return this.itemsToPickUp;
    }

    @Override
    public CompoundTag writeJobToNBT(CompoundTag compound) {
        compound.putInt("ritualState", this.ritualState.ordinal());
        compound.putInt("ritualProgress", this.ritualProgress);
        if (this.currentRecipe != null && this.currentRecipe.isPresent()) {
            // persist the recipe id so we can restore the ritual after reload
            compound.putString("currentRecipe", this.currentRecipe.get().getId().toString());
        }
        return super.writeJobToNBT(compound);
    }

    @Override
    public void readJobFromNBT(CompoundTag compound) {
        super.readJobFromNBT(compound);
        this.ritualState = compound.getInt("ritualState") == 0 ? RitualState.IDLE : RitualState.IN_PROGRESS;
        this.ritualProgress = compound.getInt("ritualProgress");

        if (compound.contains("currentRecipe")) {
            String idString = compound.getString("currentRecipe");
            try {
                ResourceLocation recipeId = ResourceLocation.tryParse(idString);
                if (recipeId != null) {
                    var opt = this.entity.level().getRecipeManager().byKey(recipeId);
                    if (opt.isPresent() && opt.get() instanceof RitualRecipe ritualRecipe) {
                        this.currentRecipe = Optional.of(ritualRecipe);
                    } else {
                        // recipe not found or wrong type -> cancel ritual state
                        this.currentRecipe = Optional.empty();
                        this.ritualState = RitualState.IDLE;
                    }
                } else {
                    this.currentRecipe = Optional.empty();
                    this.ritualState = RitualState.IDLE;
                }
            } catch (Exception e) {
                this.currentRecipe = Optional.empty();
                this.ritualState = RitualState.IDLE;
            }
        }
    }

    @Override
    public boolean canPickupItem(ItemEntity entity) {
        if (!hasSatchel()) {
            return false;
        }
        if (entity.getTags().contains(DROPPED_BY_RITUALMASTER) || entity.getTags().contains(DROPPED_RESULT)) {
            return false;
        }

        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) {
            return false;
        }
        return this.itemsToPickUp.stream().anyMatch(i -> i.test(stack));
    }

    private enum RitualState {
        IDLE,
        IN_PROGRESS
    }
}
