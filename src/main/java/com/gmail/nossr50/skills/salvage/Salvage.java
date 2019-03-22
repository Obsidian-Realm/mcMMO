package com.gmail.nossr50.skills.salvage;

import com.gmail.nossr50.mcMMO;
import org.bukkit.Material;

public class Salvage {

    public Salvage() {
        anvilMaterial = mcMMO.getConfigManager().getConfigSalvage().getGeneral().getSalvageAnvilMaterial();
        arcaneSalvageDowngrades = mcMMO.getConfigManager().getConfigSalvage().getConfigArcaneSalvage().isDowngradesEnabled();
        arcaneSalvageEnchantLoss = mcMMO.getConfigManager().getConfigSalvage().getConfigArcaneSalvage().isMayLoseEnchants();
    }
    public static Material anvilMaterial;
    public static boolean arcaneSalvageDowngrades;
    public static boolean arcaneSalvageEnchantLoss;

    protected static int calculateSalvageableAmount(short currentDurability, short maxDurability, int baseAmount) {
        double percentDamaged = (maxDurability <= 0) ? 1D : (double) (maxDurability - currentDurability) / maxDurability;

        return (int) Math.floor(baseAmount * percentDamaged);
    }
}