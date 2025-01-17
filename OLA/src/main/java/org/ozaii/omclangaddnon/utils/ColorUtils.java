package org.ozaii.omclangaddnon.utils;

import org.bukkit.ChatColor;

public class ColorUtils {

    /**
     * Convert a string with color codes into a colored string.
     * @param message The message to color.
     * @return The colored message.
     */
    public static String applyColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
