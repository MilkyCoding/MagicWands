package me.milkycoding.magicwands.util;

import net.md_5.bungee.api.ChatColor;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:#[a-fA-F0-9]{6}:#[a-fA-F0-9]{6}>(.*?)</gradient>");

    private ChatUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String colorize(String text) {
        if (text == null) return "";
        
        text = text.replace("¶", "&");
        
        text = processGradients(text);
        
        text = processHexColors(text);
        
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static String processGradients(String text) {
        Matcher gradientMatcher = GRADIENT_PATTERN.matcher(text);
        StringBuffer gradientBuffer = new StringBuffer();
        
        while (gradientMatcher.find()) {
            String gradientText = gradientMatcher.group(1);
            String[] colors = gradientMatcher.group(0)
                .replaceAll("<gradient:|</gradient>", "")
                .split(":");
            
            String gradient = createGradient(gradientText, colors[0], colors[1]);
            gradientMatcher.appendReplacement(gradientBuffer, gradient);
        }
        gradientMatcher.appendTail(gradientBuffer);
        return gradientBuffer.toString();
    }

    private static String processHexColors(String text) {
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        StringBuffer hexBuffer = new StringBuffer();
        
        while (hexMatcher.find()) {
            String hex = hexMatcher.group();
            hexMatcher.appendReplacement(hexBuffer, ChatColor.of(hex).toString());
        }
        hexMatcher.appendTail(hexBuffer);
        return hexBuffer.toString();
    }

    private static String createGradient(String text, String startColor, String endColor) {
        Color start = Color.decode(startColor);
        Color end = Color.decode(endColor);
        
        StringBuilder result = new StringBuilder();
        int length = text.length();
        
        for (int i = 0; i < length; i++) {
            double ratio = (double) i / (length - 1);
            Color color = interpolate(start, end, ratio);
            result.append(ChatColor.of(color)).append(text.charAt(i));
        }
        
        return result.toString();
    }

    private static Color interpolate(Color start, Color end, double ratio) {
        int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
        int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
        int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));
        
        return new Color(red, green, blue);
    }
} 