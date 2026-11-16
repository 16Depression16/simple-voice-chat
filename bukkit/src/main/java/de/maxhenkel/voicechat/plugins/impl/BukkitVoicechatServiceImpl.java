package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.plugins.PluginManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BukkitVoicechatServiceImpl implements BukkitVoicechatService {

    private volatile List<VoicechatPlugin> plugins = Collections.emptyList();

    @Override
    public void registerPlugin(VoicechatPlugin plugin) {
        if (updatePlugin(plugin)) {
            refreshPluginManager();
        }
    }

    private synchronized boolean updatePlugin(VoicechatPlugin plugin) {
        List<VoicechatPlugin> updated = new ArrayList<>(plugins);
        for (int i = 0; i < updated.size(); i++) {
            VoicechatPlugin current = updated.get(i);
            if (Objects.equals(current.getPluginId(), plugin.getPluginId())) {
                if (current == plugin) {
                    return false;
                }
                updated.set(i, plugin);
                plugins = Collections.unmodifiableList(updated);
                return true;
            }
        }
        updated.add(plugin);
        plugins = Collections.unmodifiableList(updated);
        return true;
    }

    private void refreshPluginManager() {
        PluginManager manager = PluginManager.getIfInitialized();
        if (manager != null) {
            manager.refresh();
        }
    }

    public List<VoicechatPlugin> getPlugins() {
        return plugins;
    }
}
