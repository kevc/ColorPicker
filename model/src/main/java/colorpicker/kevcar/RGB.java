package colorpicker.kevcar;

public class RGB {

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Builder toBuilder() {
        return new Builder()
                .setRed(red)
                .setGreen(green)
                .setBlue(blue);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public static class Builder {
        private int red = 0;
        private int green = 0;
        private int blue = 0;

        public Builder() {

        }

        public Builder setRed(int red) {
            this.red = red;
            return this;
        }

        public Builder setGreen(int green) {
            this.green = green;
            return this;
        }

        public Builder setBlue(int blue) {
            this.blue = blue;
            return this;
        }

        public RGB build() {
            return new RGB(red, green, blue);
        }
    }
}
