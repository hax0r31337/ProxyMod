package me.liuli.proxymod;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.util.Properties;

public class ProxyMod {

    private final static ProxyMod instance;

    static {
        instance = new ProxyMod();
    }

    private boolean proxyEnabled;
    private String proxyAddress;
    private ProxyType proxyType;

    private ProxyMod() {
        this.loadConfig();
    }

    public void loadConfig() {
        final File configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/proxymod.properties");
        try {
            final Properties properties = new Properties();
            if (configFile.exists()) {
                properties.load(Files.newInputStream(configFile.toPath()));
            }
            this.proxyEnabled = Boolean.parseBoolean(properties.getProperty("proxyEnabled", "false"));
            this.proxyAddress = properties.getProperty("proxyAddress", "127.0.0.1:10808");
            this.proxyType = ProxyType.valueOf(properties.getProperty("proxyType", "SOCKS"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        final Properties properties = new Properties();
        properties.setProperty("proxyEnabled", Boolean.toString(this.proxyEnabled));
        properties.setProperty("proxyAddress", this.proxyAddress);
        properties.setProperty("proxyType", this.proxyType.name());
        try {
            final File configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/proxymod.properties");
            properties.store(Files.newOutputStream(configFile.toPath()), null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static ProxyMod getInstance() {
        return instance;
    }

    public boolean isProxyEnabled() {
        return this.proxyEnabled;
    }

    public void setProxyEnabled(final boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public String getProxyAddress() {
        return this.proxyAddress;
    }

    public void setProxyAddress(final String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public ProxyType getProxyType() {
        return this.proxyType;
    }

    public void setProxyType(final ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    public Proxy getProxy() {
        if (!this.proxyEnabled) {
            return null;
        }
        try {
            final String addressStr = this.proxyAddress.split(":")[0];
            final int port = Integer.parseInt(this.proxyAddress.split(":")[1]);
            final InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(addressStr), port);
            switch (this.proxyType) {
                case HTTP:
                    return new Proxy(Proxy.Type.HTTP, address);
                case SOCKS:
                    return new Proxy(Proxy.Type.SOCKS, address);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum ProxyType {
        SOCKS, HTTP
    }
}
