package us.dxtrus.commons.cooldowns;

import us.dxtrus.commons.utils.Tuple;

public class CooldownReponse extends Tuple<Boolean, Long> {
    public CooldownReponse(Boolean aBoolean, Long aLong) {
        super(aBoolean, aLong);
    }

    public boolean shouldContinue() {
        return getA();
    }

    public long timeRemaing() {
        return getB();
    }
}
