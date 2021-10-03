/*
 * This file is part of HrainMoveAddition Anticheat.
 * Copyright (C) 2018 HrainMoveAddition Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.nuymakstone.hrainac.module;

import me.nuymakstone.hrainac.HrainMoveAddition;
import me.nuymakstone.hrainac.event.*;
import me.nuymakstone.hrainac.event.Event;
import me.nuymakstone.hrainac.util.ConfigHelper;
import me.nuymakstone.hrainac.util.MathPlus;
import me.nuymakstone.hrainac.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

public class MouseRecorder {

    //TODO: Handle teleportation!!!
    //This is a mess.

    private HrainMoveAddition HrainAC;
    private final float RESOLUTION;
    private final float DEFAULT_TIME;
    private final int WIDTH;
    private final int HEIGHT;
    private final float CLICK_DOT_RADIUS = 0.7F;

    public MouseRecorder(HrainMoveAddition HrainAC) {
        this.HrainAC = HrainAC;
        RESOLUTION = (float) ConfigHelper.getOrSetDefault(3D, HrainAC.getConfig(), "mouseRecorder.resolution");
        DEFAULT_TIME = (float) ConfigHelper.getOrSetDefault(10D, HrainAC.getConfig(), "mouseRecorder.defaultRecordingTime");
        WIDTH = (int)(360 * RESOLUTION);
        HEIGHT = (int)(180 * RESOLUTION);
    }

    //to be called from main thread
    public void start(CommandSender admin, Player target, float time) {
        List<HrainACEventListener> HrainACListeners = HrainAC.getPacketHandler().getHrainACEventListeners();
        for(HrainACEventListener HrainACListener : HrainACListeners) {
            if(HrainACListener instanceof MouseRecorderListener && ((MouseRecorderListener) HrainACListener).target.equals(target)) {
                admin.sendMessage(ChatColor.RED + "" + target.getName() + " is already being recorded.");
                return;
            }
        }
        HrainACEventListener listener = new MouseRecorderListener(admin, target, time);
        admin.sendMessage(ChatColor.AQUA + "Recording mouse movements and hits of " + target.getName() + "...");

        HrainACListeners.add(listener);
    }

    //to be called from main thread
    public void stop(CommandSender admin, Player target) {
        List<HrainACEventListener> HrainACListeners = HrainAC.getPacketHandler().getHrainACEventListeners();
        for(HrainACEventListener HrainACListener : HrainACListeners) {
            if(HrainACListener instanceof MouseRecorderListener && ((MouseRecorderListener) HrainACListener).target.equals(target)) {
                MouseRecorderListener mRecLis = (MouseRecorderListener)HrainACListener;
                HrainACListeners.remove(mRecLis);
                HandlerList.unregisterAll(mRecLis);
                admin.sendMessage(ChatColor.AQUA + "Stopped recording " + target.getName());
                render(mRecLis);
                return;
            }
        }
        admin.sendMessage(ChatColor.RED + "" + target.getName() + " is not being recorded.");
    }

    private void render(MouseRecorderListener listener) {
        if(listener.admin != null)
            listener.admin.sendMessage(ChatColor.AQUA + "Rendering...");
        Bukkit.getScheduler().runTaskAsynchronously(HrainAC, () -> {
            BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setBackground(Color.BLACK);

            listener.origin.setKey(MathPlus.clampDegrees360(listener.origin.getKey()));
            listener.origin.setValue(Math.min(Math.max(listener.origin.getValue(), -90), 90) + 90);

            //how are we going to deal with spaghetti neckers or yaw spammers?
            //remember, we're dealing with raw client input; don't trust it.
            //have something in here to prevent this thread from crashing

            renderClicks(g, listener);
            renderMovement(g, listener);

            File image = new File(HrainAC.getDataFolder().getAbsolutePath() + File.separator + "recordings" + File.separator + listener.target.getName() + "-" + System.currentTimeMillis() + ".png");
            try {
                image.mkdirs();
                if (!image.exists()) {
                    image.createNewFile();
                }
                ImageIO.write(img, "PNG", image);
                if(listener.admin != null)
                    listener.admin.sendMessage(ChatColor.AQUA + "Complete! Saved image to " + image.getPath());
            }
            catch(Exception e) {
                if(listener.admin != null) {
                    listener.admin.sendMessage(ChatColor.RED + "An exception occurred while saving the image to " + image.getPath());
                    listener.admin.sendMessage(ChatColor.RED + "Check the console for more information.");
                }
                e.printStackTrace();
            }
        });
    }

    private void renderClicks(Graphics2D g, MouseRecorderListener listener) {
        g.setColor(new Color(0F, 1F, 0F, 0.4F));
        Pair<Float, Float> currCoord = new Pair<>(listener.origin);
        for(int i = 0; i < listener.motions.size(); i++) {
            float x1 = currCoord.getKey();
            float y1 = currCoord.getValue();
            float x2 = x1 + listener.motions.get(i).getKey();
            float y2 = y1 + listener.motions.get(i).getValue();

            if(listener.clicks.contains(i)) {
                g.fillOval((int)((x1 - CLICK_DOT_RADIUS) * RESOLUTION), (int)((y1 - CLICK_DOT_RADIUS) * RESOLUTION), (int)(2* CLICK_DOT_RADIUS *RESOLUTION), (int)(2* CLICK_DOT_RADIUS *RESOLUTION));
            }

            currCoord.setKey(x2);
            currCoord.setValue(y2);

            //handle if line goes off the canvas horizontally
            if(x2 >= 360) {
                currCoord.setKey(x2 % 360);
            }
            else if(x2 < 0) {
                currCoord.setKey((360 + x2) % 360);
            }
        }
    }

    private void renderMovement(Graphics2D g, MouseRecorderListener listener) {
        g.setColor(new Color(0F, 0F, 1F, 0.8F)); //starting marker color
        g.fillOval((int)((listener.origin.getKey() - CLICK_DOT_RADIUS) * RESOLUTION), (int)((listener.origin.getValue() - CLICK_DOT_RADIUS) * RESOLUTION), (int)(2* CLICK_DOT_RADIUS *RESOLUTION), (int)(2* CLICK_DOT_RADIUS *RESOLUTION));
        Pair<Float, Float> currCoord = new Pair<>(listener.origin);
        for(Pair<Float, Float> vector : listener.motions) {
            float distance = (float)MathPlus.distance2d(vector.getKey(), vector.getValue());
            g.setColor(new Color(1F, 1 / (0.3F * distance + 1), 1 / (0.3F * distance + 1), (float)Math.max(1 / (0.2F * distance + 1), 0.004)));

            float x1 = currCoord.getKey();
            float y1 = currCoord.getValue();
            float x2 = x1 + vector.getKey();
            float y2 = y1 + vector.getValue();
            g.drawLine((int)(x1 * RESOLUTION), (int)(y1 * RESOLUTION), (int)(x2 * RESOLUTION), (int)(y2 * RESOLUTION));

            currCoord.setKey(x2);
            currCoord.setValue(y2);

            //handle if line goes off the canvas horizontally
            if(x2 >= 360) {
                float slope = (y2 - y1) / (x2 - x1); //impossible for a div by 0 because this won't run unless motX > 0
                float intersection = slope * (360 - x1) + y1;
                g.drawLine(0, (int)(intersection * RESOLUTION), (int)((x2 % 360) * RESOLUTION), (int)(y2 * RESOLUTION));
                currCoord.setKey(x2 % 360);
            }
            else if(x2 < 0) {
                float slope = (y2 - y1) / (x2 - x1);
                float intersection = slope * (0 - x1) + y1;
                g.drawLine((int)((360 - 1) * RESOLUTION), (int)(intersection * RESOLUTION), (int)(((360 + x2) % 360) * RESOLUTION), (int)(y2 * RESOLUTION));
                currCoord.setKey((360 + x2) % 360);
            }
        }
    }

    private class MouseRecorderListener implements HrainACEventListener, Listener {

        private Player target;
        private CommandSender admin;
        private Pair<Float, Float> origin;
        private List<Pair<Float, Float>> motions;
        private List<Integer> clicks;
        private List<Integer> teleports;
        private int moves;
        private List<HrainACEventListener> HrainACListeners;

        MouseRecorderListener(CommandSender admin, Player target, float time) {
            this.target = target;
            this.admin = admin;
            motions = new ArrayList<>();
            clicks = new ArrayList<>();
            teleports = new ArrayList<>();
            this.moves = (time == 0 ? (int)(DEFAULT_TIME * 20) : (int)(time * 20));
            HrainACListeners = HrainAC.getPacketHandler().getHrainACEventListeners();
            Bukkit.getPluginManager().registerEvents(this, HrainAC);
        }

        @Override
        public void onEvent(Event e) {
            if(e.getPlayer().equals(target)) {
                if(e instanceof MoveEvent) {
                    MoveEvent posE = (MoveEvent)e;
                    float deltaYaw = posE.getTo().getYaw() - posE.getFrom().getYaw();
                    float deltaPitch = posE.getTo().getPitch() - posE.getFrom().getPitch();
                    int size = motions.size();
                    if(size < moves) {
                        if (size == 0)
                            origin = new Pair<>(posE.getFrom().getYaw(), posE.getFrom().getPitch());
                        motions.add(new Pair<>(deltaYaw, deltaPitch));
                        if(size % 40 == 0 && admin != null) {
                            admin.sendMessage(ChatColor.AQUA + "Recording progress for " + target.getName() + ": " + MathPlus.round((float)size / moves * 100, 2) + "%");
                        }
                    }
                    else {
                        if(admin != null)
                            admin.sendMessage(ChatColor.AQUA + "Finished recording.");
                        HrainACListeners.remove(this);
                        HandlerList.unregisterAll(this);
                        render(this);
                    }
                }
                else if(e instanceof InteractEntityEvent && ((InteractEntityEvent) e).getInteractAction() == InteractAction.ATTACK) {
                    clicks.add(motions.size());
                }
            }
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            if(e.getPlayer().equals(target)) {
                if(admin != null) {
                    admin.sendMessage(ChatColor.AQUA + "Recording progress for " + target.getName() + " interrupted because of disconnection.");
                }
                HrainACListeners.remove(this);
                HandlerList.unregisterAll(this);
                render(this);
            }

        }
    }
}
