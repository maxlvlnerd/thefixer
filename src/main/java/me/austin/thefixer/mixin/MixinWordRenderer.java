package me.austin.thefixer.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

// im not even sure how but i would crash on world load because something was pushing to the vertexConsumer before it initilized
@Mixin(LevelRenderer.class)
public class MixinWordRenderer {
    @Unique
    private static final Logger LOGGER = LogUtils.getLogger();

    @Overwrite
    public static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
        try {
            PoseStack.Pose pose = poseStack.last();
            voxelShape.forAllEdges((k, l, m, n, o, p) -> {
                float q = (float) (n - k);
                float r = (float) (o - l);
                float s = (float) (p - m);
                float t = Mth.sqrt(q * q + r * r + s * s);
                q /= t;
                r /= t;
                s /= t;
                vertexConsumer.vertex(pose.pose(), (float) (k + d), (float) (l + e), (float) (m + f)).color(g, h, i, j).normal(pose.normal(), q, r, s).endVertex();
                vertexConsumer.vertex(pose.pose(), (float) (n + d), (float) (o + e), (float) (p + f)).color(g, h, i, j).normal(pose.normal(), q, r, s).endVertex();
            });
        } catch (IllegalStateException ex) {
            LOGGER.error("An IllegalStateException occurred while rendering shape: {}", e);
        }
    }
}
