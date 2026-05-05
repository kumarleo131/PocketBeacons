package com.pocketbeacons;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;

import static com.pocketbeacons.PocketBeacons.MOD_ID;

public record ApplyBeaconEffectPayload(RegistryEntry<StatusEffect> effect) implements CustomPayload {
    public static final CustomPayload.Id<ApplyBeaconEffectPayload> ID =
            new CustomPayload.Id<>(Identifier.of(MOD_ID, "apply_beacon_effect"));

    public static final PacketCodec<RegistryByteBuf, ApplyBeaconEffectPayload> CODEC =
            PacketCodec.tuple(
                    StatusEffect.ENTRY_PACKET_CODEC, ApplyBeaconEffectPayload::effect,
                    ApplyBeaconEffectPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
