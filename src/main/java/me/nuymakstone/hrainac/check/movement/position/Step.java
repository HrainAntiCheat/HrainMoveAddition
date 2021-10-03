package me.nuymakstone.hrainac.check.movement.position;

import me.nuymakstone.hrainac.HrainACPlayer;
import me.nuymakstone.hrainac.check.MovementCheck;
import me.nuymakstone.hrainac.event.MoveEvent;

public class Step extends MovementCheck {

    public Step() {
        super("step", true, 0, 5, 0.995, 5000, "&7%player% 没能绕过 step, VL: %vl%", null);
    }

    @Override
    protected void check(MoveEvent e) {

        if(e.isStep() || !(e.isOnGround() && e.getHrainACPlayer().isOnGround()) || e.isTeleportAccept() || e.hasAcceptedKnockback()) {
            return;
        }

        HrainACPlayer pp = e.getHrainACPlayer();
        double dY = e.getTo().getY() - e.getFrom().getY();

        if(dY > 0.6F || dY < -0.0784F) {
            punishAndTryRubberband(pp, e);
        }
        else {
            reward(pp);
        }
    }
}
