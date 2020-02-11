package com.minecraftdimensions.bungeesuitehomes.redis;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import redis.clients.jedis.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class RedisManager {
    private JedisPoolConfig poolConfig;
    private JedisPool jedisPool;
    private String host;
    private String password;
    private int port;
    private int timeout;
    private Jedis subjedis;

    private static RedisManager instance;

    private RedisManager() {

    }

    public RedisManager init(String host, String password, int port, int timeout) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.timeout = timeout;
        poolConfig = buildPoolConfig();
        setup();
        listen();
        return instance;
    }

    public static RedisManager getInstance() {
        if (instance == null) {
            instance = new RedisManager();
        }
        return instance;
    }

    private void setup() {
        jedisPool = new JedisPool(poolConfig,
                host,
                port,
                timeout,
                password,
                false);
    }


    public void listen() {
//        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port);
//        jedisShardInfo.setPassword(password);
//        subjedis = new Jedis(jedisShardInfo);
//        CompletableFuture.runAsync(() -> {
//            subjedis.subscribe(new JedisPubSub() {
//                @Override
//                public void onMessage(String channel, String message) {
//                    try {
//                        Bukkit.getLogger().info("HOMES REDIS RESPONSE: " + message);
//                        String[] args = message.split(";");
//                        if (args[0].equalsIgnoreCase("TeleportPlayerToLocation")) {
//                            if (args[1].equalsIgnoreCase(BungeeSuiteWarps.server)) {
//                                WarpsManager.teleportPlayerToWarp(args[2], new Location(Bukkit.getWorld(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), Float.parseFloat(args[7]), Float.parseFloat(args[8])));
//                            }
//                        }
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }, "WARP_RESPONSE");
//        });
    }

    public void publish(String data, String channel) {
        CompletableFuture.runAsync(() -> {
            Bukkit.getLogger().info("WARP REDIS PUBLISH: " + data + " (" + channel + ")");
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, data);
            }
        });
    }

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }
}
