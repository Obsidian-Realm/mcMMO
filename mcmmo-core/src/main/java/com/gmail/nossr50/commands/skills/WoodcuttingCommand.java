package com.gmail.nossr50.commands.skills;

import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.mcMMO;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WoodcuttingCommand extends SkillCommand {
    private String treeFellerLength;
    private String treeFellerLengthEndurance;
    private String doubleDropChance;
    private String doubleDropChanceLucky;

    private boolean canTreeFell;
    private boolean canLeafBlow;
    private boolean canDoubleDrop;
    private boolean canSplinter;
    private boolean canBarkSurgeon;
    private boolean canNaturesBounty;

    public WoodcuttingCommand(mcMMO pluginRef) {
        super(PrimarySkillType.WOODCUTTING, pluginRef);
    }

    @Override
    protected void dataCalculations(Player player, double skillValue) {
        // DOUBLE DROPS
        if (canDoubleDrop) {
            setDoubleDropClassicChanceStrings(player);
        }

        // TREE FELLER
        if (canTreeFell) {
            String[] treeFellerStrings = formatLengthDisplayValues(player, skillValue);
            treeFellerLength = treeFellerStrings[0];
            treeFellerLengthEndurance = treeFellerStrings[1];
        }
    }

    private void setDoubleDropClassicChanceStrings(Player player) {
        String[] doubleDropStrings = getAbilityDisplayValues(player, SubSkillType.WOODCUTTING_HARVEST_LUMBER);
        doubleDropChance = doubleDropStrings[0];
        doubleDropChanceLucky = doubleDropStrings[1];
    }

    @Override
    protected void permissionsCheck(Player player) {
        canTreeFell = pluginRef.getRankTools().hasUnlockedSubskill(player, SubSkillType.WOODCUTTING_TREE_FELLER) && pluginRef.getPermissionTools().treeFeller(player);
        canDoubleDrop = canUseSubSkill(player, SubSkillType.WOODCUTTING_HARVEST_LUMBER) && pluginRef.getRankTools().getRank(player, SubSkillType.WOODCUTTING_HARVEST_LUMBER) >= 1;
        canLeafBlow = canUseSubSkill(player, SubSkillType.WOODCUTTING_LEAF_BLOWER);
        /*canSplinter = canUseSubskill(player, SubSkillType.WOODCUTTING_SPLINTER);
        canBarkSurgeon = canUseSubskill(player, SubSkillType.WOODCUTTING_BARK_SURGEON);
        canNaturesBounty = canUseSubskill(player, SubSkillType.WOODCUTTING_NATURES_BOUNTY);*/
    }

    @Override
    protected List<String> statsDisplay(Player player, double skillValue, boolean hasEndurance, boolean isLucky) {
        List<String> messages = new ArrayList<>();

        if (canDoubleDrop) {
            messages.add(getStatMessage(SubSkillType.WOODCUTTING_HARVEST_LUMBER, doubleDropChance)
                    + (isLucky ? pluginRef.getLocaleManager().getString("Perks.Lucky.Bonus", doubleDropChanceLucky) : ""));
        }

        if (canLeafBlow) {
            messages.add(pluginRef.getLocaleManager().getString("Ability.Generic.Template", pluginRef.getLocaleManager().getString("Woodcutting.Ability.0"), pluginRef.getLocaleManager().getString("Woodcutting.Ability.1")));
        }

        if (canTreeFell) {
            messages.add(getStatMessage(SubSkillType.WOODCUTTING_TREE_FELLER, treeFellerLength)
                    + (hasEndurance ? pluginRef.getLocaleManager().getString("Perks.ActivationTime.Bonus", treeFellerLengthEndurance) : ""));
        }

        return messages;
    }

    @Override
    protected List<TextComponent> getTextComponents(Player player) {
        List<TextComponent> textComponents = new ArrayList<>();

        pluginRef.getTextComponentFactory().getSubSkillTextComponents(player, textComponents, PrimarySkillType.WOODCUTTING);

        return textComponents;
    }


}