package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;

/**
 * An {@link IGAEvaluator} that evaluates image approximations.
 *
 * @author Mateo Imbrišak
 */

public class Evaluator implements IGAEvaluator<int[]> {

    /**
     * Keeps the image being compared.
     */
    private GrayScaleImage template;

    /**
     * Provides image to draw on/
     */
    private ThreadLocal<GrayScaleImage> im;

    /**
     * Default constructor.
     *
     * @param template target image to be approximated.
     */
    public Evaluator(GrayScaleImage template) {
        super();
        this.template = template;
        im = new ThreadLocal<>();
    }

    /**
     * Draws the data from {@code p} onto the given image {@code im}.
     *
     * @param p solution containing data to be drawn.
     * @param im image on which to draw no.
     *
     * @return drawn image.
     */
    public GrayScaleImage draw(GASolution<int[]> p, GrayScaleImage im) {
        if (im == null) {
            im = new GrayScaleImage(template.getWidth(), template.getHeight());
        }

        int[] pdata = p.getData();
        byte bgcol = (byte) pdata[0];
        im.clear(bgcol);
        int n = (pdata.length - 1) / 5;
        int index = 1;
        for (int i = 0; i < n; i++) {
            im.rectangle(pdata[index], pdata[index + 1], pdata[index + 2], pdata[index + 3], (byte) pdata[index + 4]);
            index += 5;
        }

        return im;
    }

    @Override
    public void evaluate(GASolution<int[]> p) {
        if (im.get() == null) {
            im.set(new GrayScaleImage(template.getWidth(), template.getHeight()));
        }

        GrayScaleImage image =  im.get();

        draw(p, image);

        byte[] data = image.getData();
        byte[] tdata = template.getData(); int w = image.getWidth();
        int h = image.getHeight();

        double error = 0;
        int index2 = 0;

        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                error += Math.abs(((int)data[index2]&0xFF)-((int)tdata[index2]&0xFF));
                index2++;
            }
        }

        p.fitness = -error;
    }
}
