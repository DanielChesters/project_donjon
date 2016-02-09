package com.oni.donjon.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.NumberUtils;
import com.oni.donjon.screen.GameScreen;

/**
 * @author Daniel Chesters (on 22/05/14).
 */
public class Tile {
    public static final float SIZE = 32f;
    private TileType type;
    private Rectangle rectangle;
    private boolean visible;
    private Body body;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override public String toString() {
        return "Tile{" +
            "type=" + type +
            ", rectangle=" + rectangle +
            ", visible=" + visible +
            '}';
    }

    public Tile() {
        this.rectangle = new Rectangle();
    }

    public Tile(float x, float y, TileType type, boolean visible, World world) {
        this.rectangle = new Rectangle(x, y, SIZE, SIZE);
        this.type = type;
        this.visible = visible;
        if (type.getCategoryBits() != GameScreen.NOTHING_BIT) {
            this.body = createBody(world);
        }
    }

    public Body createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(rectangle.x * SIZE + rectangle.getWidth() / 2,
            rectangle.y * SIZE + rectangle.getHeight() / 2);
        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width / 2, rectangle.height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = type.getCategoryBits();
        fixtureDef.filter.maskBits = GameScreen.PLAYER_BIT | GameScreen.LIGHT_BIT;
        body.createFixture(fixtureDef);
        polygonShape.dispose();

        return body;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tile tile = (Tile) o;

        return this.rectangle.getX() == tile.rectangle.getX()
            && this.rectangle.getY() == tile.rectangle.getY();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + NumberUtils.floatToIntBits(rectangle.getX());
        result = prime * result + NumberUtils.floatToIntBits(rectangle.getY());
        return result;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
