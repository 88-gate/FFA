package me.kaitp1016.ffa.utils

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemManager.getCustomItem
import me.kaitp1016.ffa.items.ItemManager.isCustomItem
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.StonecutterRecipe
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.inventory.CraftStonecuttingRecipe
import org.bukkit.inventory.*
import java.lang.UnsupportedOperationException

object RecipeUtils {
    val recipes = Bukkit.recipeIterator().asSequence().toMutableList()
    lateinit var furnaceRecipes: List<FurnaceRecipe>

    lateinit var recipedDatas: List<RecipeData>
    lateinit var usageMap: List<Pair<RecipeChoice, RecipeData>>
    val customChoices = listOf(BattleRoyalItemChoice::class.java)

    data class RecipeData(val ingrediens: List<Ingredient>, val typeItem: ItemStack,val extraItem:ItemStack? = null, val result: ItemStack, val recipe: Recipe) {
        fun match(items: List<ItemStack?>): Boolean {
            when(recipe) {
                is ShapelessRecipe -> {
                    val choices = this.ingrediens.toMutableList()
                    if (items.filter { it != null && !it.isEmpty }.any {item -> !choices.remove(choices.find { it.match(item) }) }) {
                        return false
                    }
                    else {
                        return choices.isEmpty()
                    }
                }
                else -> throw UnsupportedOperationException()
            }
        }
    }

    data class Ingredient(val items: List<ItemStack>, val choice: RecipeChoice?) {
        fun match(item:ItemStack?):Boolean {
            return choice == null || (item != null && this.choice.test(item.bukkitStack))
        }
    }

    init {
        updateAllData()
    }

    fun updateAllData() {
        val usages = mutableListOf<Pair<RecipeChoice, RecipeData>>()
        val datas = mutableListOf<RecipeData>()

        recipes.forEach { recipe ->
            val data = getRecipeData(recipe)
            if (data == null) return@forEach

            val checked = mutableListOf<Ingredient>()
            val ingrediens = data.ingrediens.filter { ingredient ->
                if (checked.any { ingredient.choice?.equals(ingredient) != true }) return@filter false
                checked.add(ingredient)

                return@filter true
            }

            ingrediens.forEach { ingredient ->
                val choice = ingredient.choice
                if (choice != null) {
                    usages.add(choice to data)
                }
            }

            datas.add(data)
        }

        this.usageMap = usages
        this.recipedDatas = datas
        this.furnaceRecipes = recipes.filter { it is FurnaceRecipe }.map { it as FurnaceRecipe }.toList()
    }

    fun getRecipeData(recipe: Recipe): RecipeData? {
        when (recipe) {
            is ShapedRecipe -> {
                val ingredients = MutableList(9) { Ingredient(listOf(ItemStack(Items.AIR)), null) }
                val choiceMap = recipe.choiceMap

                var row = 0
                var column = 0

                recipe.shape.forEach { line ->
                    line.forEach { char ->
                        val choice = choiceMap[char]
                        if (choice != null) {
                            ingredients[row * 3 + column] = Ingredient(getAppliableItems(choice), choice)
                        }
                        column++
                    }

                    column = 0
                    row++
                }

                val typeItem = ItemStack(Items.CRAFTING_TABLE).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("定形クラフトレシピ").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,null, recipe.result.asCraftItemStack().handle, recipe)
            }

            is ShapelessRecipe -> {
                val ingredients = recipe.choiceList.map { Ingredient(getAppliableItems(it), it) }

                val typeItem = ItemStack(Items.CRAFTING_TABLE).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("不定形クラフトレシピ").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,null, recipe.result.asCraftItemStack().handle, recipe)
            }

            is FurnaceRecipe -> {
                val ingredients = listOf(Ingredient(getAppliableItems(recipe.inputChoice), recipe.inputChoice))

                val typeItem = ItemStack(Items.FURNACE).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("かまどのレシピ").toMCComponent())
                }

                val extraItem = ItemStack(Items.COAL).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("燃料").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,extraItem, recipe.result.asCraftItemStack().handle, recipe)
            }

            is BlastingRecipe -> {
                val ingredients = listOf(Ingredient(getAppliableItems(recipe.inputChoice), recipe.inputChoice))

                val typeItem = ItemStack(Items.BLAST_FURNACE).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("溶鉱炉のレシピ").toMCComponent())
                }

                val extraItem = ItemStack(Items.COAL).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("燃料").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,extraItem, recipe.result.asCraftItemStack().handle, recipe)
            }

            is SmokingRecipe -> {
                val ingredients = listOf(Ingredient(getAppliableItems(recipe.inputChoice), recipe.inputChoice))

                val typeItem = ItemStack(Items.SMOKER).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("燻製器のレシピ").toMCComponent())
                }

                val extraItem = ItemStack(Items.COAL).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("燃料").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,extraItem, recipe.result.asCraftItemStack().handle, recipe)
            }

            is CampfireRecipe -> {
                val ingredients = listOf(Ingredient(getAppliableItems(recipe.inputChoice), recipe.inputChoice))

                val typeItem = ItemStack(Items.CAMPFIRE).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("キャンプファイヤーで焼くレシピ").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,null, recipe.result.asCraftItemStack().handle, recipe)
            }

            is CraftStonecuttingRecipe -> {
                val ingredients = listOf(Ingredient(getAppliableItems(recipe.inputChoice), recipe.inputChoice))

                val typeItem = ItemStack(Items.STONECUTTER).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("石切り台のレシピ").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,null, recipe.result.asCraftItemStack().handle, recipe)
            }

            is SmithingTransformRecipe -> {
                val ingredients = listOf(recipe.base, recipe.addition, recipe.template).map { Ingredient(getAppliableItems(it), it) }.toMutableList()

                val typeItem = ItemStack(Items.SMITHING_TABLE).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("鍛冶台クラフト").toMCComponent())
                }

                return RecipeData(ingredients, typeItem,null, recipe.result.asCraftItemStack().handle, recipe)
            }

            else -> {
                return null
            }
        }
    }

    fun getAppliableItems(choice: RecipeChoice?): List<ItemStack> {
        when (choice) {
            is RecipeChoice.MaterialChoice -> {
                return choice.choices.filter { !it.isAir }.map { ItemStack.fromBukkitCopy(org.bukkit.inventory.ItemStack(it)) }
            }

            is RecipeChoice.ExactChoice -> {
                return choice.choices.filter { it.asCraftItemStack().handle != null }.map {
                    it.asCraftItemStack().handle
                }
            }

            is BattleRoyalItemChoice -> {
                return listOf(choice.item.createItem().asCraftItemStack().handle)
            }

            null -> {
                return listOf(ItemStack.EMPTY)
            }

            else -> {
                return listOf(ItemStack(Items.BARRIER).apply {
                    this.set(DataComponents.ITEM_NAME, Component.text("不明なレシピチョイス(報告してください)").toMCComponent())
                })
            }
        }
    }

    fun getRecipeUsage(item: ItemStack?): List<RecipeData> {
        if (item == null) return listOf()
        val bukkitItem = item.bukkitStack

        return usageMap.filter { it.first.test(bukkitItem) }.map { it.second }
    }

    fun getRecipe(item: org.bukkit.inventory.ItemStack):List<RecipeData> {
        if (item.isCustomItem()) {
            return getRecipe(item.getCustomItem()!!)
        }
        else {
            return getRecipe(item.type)
        }
    }

    fun getRecipe(material: Material?): List<RecipeData> {
        if (material == null) return listOf()

        return recipes.mapNotNull { recipe ->
            if (!this.isSupported(recipe)) return@mapNotNull null

            val recipe = getRecipeData(recipe)
            if (recipe == null || recipe.result.bukkitStack.type != material) return@mapNotNull null

            return@mapNotNull recipe
        }
    }

    fun getRecipe(item: CustomItem): List<RecipeData> {
        return recipes.mapNotNull { recipe ->
            if (!this.isSupported(recipe) || recipe.result.getCustomItem() != item) return@mapNotNull null

            val data = getRecipeData(recipe)
            return@mapNotNull data
        }
    }

    fun isCustomChoice(choice: RecipeChoice?): Boolean {
        return choice != null && customChoices.any { it.isInstance(choice) }
    }

    fun isSupported(recipe: Recipe): Boolean {
        return recipe is ShapedRecipe || recipe is ShapelessRecipe || recipe is FurnaceRecipe || recipe is BlastingRecipe || recipe is SmokingRecipe || recipe is CampfireRecipe || recipe is StonecutterRecipe || recipe is SmithingTransformRecipe
    }

    class BattleRoyalItemChoice: RecipeChoice {
        val item: CustomItem

        constructor(item: CustomItem) {
            this.item = item
        }

        override fun getItemStack(): org.bukkit.inventory.ItemStack {
            return item.createItem()
        }

        override fun clone(): RecipeChoice {
            return BattleRoyalItemChoice(item)
        }

        override fun test(item: org.bukkit.inventory.ItemStack): Boolean {
            return item.isCustomItem() && item.getCustomItem() == this.item
        }
    }
}