/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.tpa;

public enum TeleportRequestType {
    TPA("command-tpa-incoming", "command-tpa-outgoing"),
    TPAHERE("command-tpa-here-incoming", "command-tpa-here-outgoing");

    public final String incomingMessage;
    public final String outgoingMessage;

    TeleportRequestType(final String incomingMessage, final String outgoingMessage) {
        this.incomingMessage = incomingMessage;
        this.outgoingMessage = outgoingMessage;
    }
}
