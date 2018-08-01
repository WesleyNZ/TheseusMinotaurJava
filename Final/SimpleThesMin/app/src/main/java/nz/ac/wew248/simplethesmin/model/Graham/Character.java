package nz.ac.wew248.simplethesmin.model.Graham;

public abstract class Character {
    private Point position;

    public Character(Point startPosition) {
        position = startPosition;
    }

    public void move(String newDirection) {
        EnumDirection enumDirection = EnumDirection.valueOf(newDirection);

        position.x += enumDirection.getNewX();
        position.y += enumDirection.getNewY();
    }

    public Point getPosition() { return position; }
}
