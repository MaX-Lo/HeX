package de.maxlo.hex.GameObjects;

import com.badlogic.gdx.graphics.Texture;

import de.maxlo.hex.Helpers.Assets;

/**
 * Created by max on 27.09.17.
 */

public class NormalHexagon extends Hexagon {

    public NormalHexagon(Player owner) {
        super(owner);
        initTexture(owner);
    }

    public NormalHexagon(Player owner, int units) {
        super(owner, units);
        initTexture(owner);
    }

    private void initTexture(Player owner) {
        switch (owner.getColor()) {
            case none:
                setTexture(Assets.blackHex);
                break;
            case blue:
                setTexture(Assets.blueHex);
                break;
            case yellow:
                setTexture(Assets.yellowHex);
                break;
            case green:
                setTexture(Assets.greenHex);
                break;
            case red:
                setTexture(Assets.redHex);
                break;
            case purple:
                setTexture(Assets.purpleHex);
                break;
            default:
                throw new IllegalArgumentException("Unknown color");
        }
    }
}
