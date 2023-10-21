package skyclash.skyclash.kitscards;

import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import skyclash.skyclash.main;

public class HomingTask extends BukkitRunnable {
    Arrow arrow;
    LivingEntity target;
    int time;

    public HomingTask(Arrow arrow, LivingEntity target) {
        this.arrow = arrow;
        this.time = 0;
        this.target = target;
        runTaskTimer(main.getPlugin(main.class), 1L, 1L);
    }

    public void run() {
        double speed = this.arrow.getVelocity().length();
        if ((this.arrow.isOnGround()) || (this.arrow.isDead()) || (this.target.isDead()) || (this.time > 10*20)) {
            cancel();
            return;
        }

        Vector toTarget = this.target.getLocation().clone().add(new Vector(0.0D, 0.5D, 0.0D)).subtract(this.arrow.getLocation()).toVector();
        Vector dirVelocity = this.arrow.getVelocity().clone().normalize();
        Vector dirToTarget = toTarget.clone().normalize();
        double angle = dirVelocity.angle(dirToTarget);

        double newSpeed = 0.9D * speed + 0.14D;
        Vector newVelocity;
        if (angle < 0.12D) {
            newVelocity = dirVelocity.clone().multiply(newSpeed);
        } else {
            Vector newDir = dirVelocity.clone().multiply((angle - 0.12D) / angle).add(dirToTarget.clone().multiply(0.12D / angle));
            newDir.normalize();
            newVelocity = newDir.clone().multiply(newSpeed);
        }
        this.arrow.setVelocity(newVelocity.add(new Vector(0.0D, 0.03D, 0.0D)));
        this.arrow.getWorld().playEffect(this.arrow.getLocation(), Effect.SMOKE, 0);

        this.time++;
    }
}
