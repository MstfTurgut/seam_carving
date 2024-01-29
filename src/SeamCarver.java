import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private int[] pixels;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.width = picture.width();
        this.height = picture.height();
        this.pixels = new int[width*height];

        for (int i = 0; i < width*height; i++) {
            pixels[i] = picture.getRGB(i % width, i / width);
        }

    }

    public Picture picture() {
        Picture picture = new Picture(width(), height());
        for (int i = 0; i < width*height; i++) {
            picture.set(i % width, i / width, new Color(pixels[i]));
        }
        return picture;
    }

    public int width() { return width; }

    public int height() { return height; }


    // straightforward energy computing of a pixel
    public double energy(int x, int y) {

        if (x < 0 || x > width() - 1) throw new IllegalArgumentException();
        if (y < 0 || y > height() - 1) throw new IllegalArgumentException();

        if (x == 0 || y == 0 || y == height() - 1 || x == width() - 1) {
            return 1000.0;
        }

        double deltaX, deltaY;

        int rgbRight = pixels[y * width + x + 1];
        int rgbLeft = pixels[y * width + x - 1];

        int redRight = (rgbRight >> 16) & 0xFF;
        int greenRight = (rgbRight >> 8) & 0xFF;
        int blueRight = rgbRight & 0xFF;
        int redLeft = (rgbLeft >> 16) & 0xFF;
        int greenLeft = (rgbLeft >> 8) & 0xFF;
        int blueLeft = rgbLeft & 0xFF;

        double rx, gx, bx;
        rx = redRight - redLeft;
        gx = greenRight - greenLeft;
        bx = blueRight - blueLeft;
        deltaX = Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2);


        int rgbUp = pixels[(y - 1) * width + x];
        int rgbDown = pixels[(y + 1) * width + x];

        int redUp = (rgbUp >> 16) & 0xFF;
        int greenUp = (rgbUp >> 8) & 0xFF;
        int blueUp = rgbUp & 0xFF;
        int redDown = (rgbDown >> 16) & 0xFF;
        int greenDown = (rgbDown >> 8) & 0xFF;
        int blueDown = rgbDown & 0xFF;

        double ry, gy, by;
        ry = redUp - redDown;
        gy = greenUp - greenDown;
        by = blueUp - blueDown;
        deltaY = Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2);

        return Math.sqrt(deltaX + deltaY);
    }

    public int[] findHorizontalSeam() {

        double[] energies = new double[width()*height() + 2];

        for (int i = 0; i < energies.length - 2; i++) {
            energies[i] = energy(i % width(), i / width());
        }

        Iterable<Integer> sp;
        AcyclicHorizontalSP acyclicSP = new AcyclicHorizontalSP(energies, width());

        sp = acyclicSP.pathTo(width()*height() + 1);

        int[] seam = new int[width()];

        int i = 0;
        for (int v : sp) {
            seam[i++] = v / width();
        }
        return seam;
    }

    public int[] findVerticalSeam() {

        double[] energies = new double[width()*height() + 2];

        for (int i = 0; i < energies.length - 2; i++) {
            energies[i] = energy(i % width(), i / width());
        }

        Iterable<Integer> sp;
        AcyclicVerticalSP acyclicSP = new AcyclicVerticalSP(energies, width());
        sp = acyclicSP.pathTo(width()*height() + 1);
        int[] seam = new int[height()];

        int i = 0;
        for (int v : sp) {
            seam[i++] = v % width();
        }
        return seam;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        if (width() <= 1) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            if (i != seam.length - 1 && Math.abs(seam[i] - seam[i+1]) > 1)
                throw new IllegalArgumentException();
            if (seam[i] < 0 || seam[i] >= width())
                throw new IllegalArgumentException();
        }

        int[] updatedPixels = new int[height() * (width()-1)];

        for (int i = 0; i < height; i++) {
            int k = 0;
            for (int j = 0; j < width; j++) {
                if (j != seam[i]) {
                    updatedPixels[i * (width - 1) + k] = pixels[i * width + j];
                    k++;
                }
            }
        }

        pixels = updatedPixels;
        width--;
    }

    public void removeHorizontalSeam(int[] seam) {
        if(seam == null) throw new IllegalArgumentException();
        if(seam.length != width()) throw new IllegalArgumentException();
        if(height() <= 1) throw new IllegalArgumentException();

        for(int i = 0; i < seam.length; i++) {
            if(i != seam.length - 1 && Math.abs(seam[i] - seam[i+1]) > 1)
                throw new IllegalArgumentException();
            if(seam[i] < 0 || seam[i] >= height())
                throw new IllegalArgumentException();
        }

        int[] updatedPixels = new int[(height() - 1)*width()];

        for(int i = 0; i < width; i++) {
            int k = 0;
            for(int j = 0; j < height; j++) {
                if(j != seam[i]) {
                    updatedPixels[k * width + i] = pixels[j * width + i];
                    k++;
                }
            }
        }

        pixels = updatedPixels;
        height--;
    }

    public static void main(String[] args) {
        Picture picture = new Picture("surf.jpg");

        SeamCarver seamCarver = new SeamCarver(picture);

        for(int i = 0 ; i < 100; i++) {
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
        }

        seamCarver.picture().show();
    }
}

