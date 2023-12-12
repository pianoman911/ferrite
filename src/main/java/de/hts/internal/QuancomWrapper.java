package de.hts.internal;

import com.quancom.qlib32.qlib32;

import java.util.BitSet;

public record QuancomWrapper(qlib32 qlib, int deviceId) {

    public static QuancomWrapper create() {
        qlib32 qlib = new qlib32();
        int deviceId = qlib.QAPIExtOpenCard(qlib32.USBOPTOREL32, 0);

        return new QuancomWrapper(qlib, deviceId);
    }

    public BitSet read() {
        int bitmask = this.qlib.QAPIExtReadDI32(this.deviceId, 0, 0);
        BitSet set = new BitSet(32);

        for (int i = 0; i < 32; i++) {
            set.set(i, (bitmask & (1 << i)) != 0);
        }

        return set;
    }

    public void write(BitSet set) {
        int bitmask = 0;

        for (int i = 0; i < 32; i++) {
            if (set.get(i)) {
                bitmask |= (1 << i);
            }
        }

        this.qlib.QAPIExtWriteDO32(this.deviceId, 0, bitmask, 0);
    }
}
