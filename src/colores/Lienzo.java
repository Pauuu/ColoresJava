package colores;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author pau
 */
public class Lienzo extends Canvas {

    private byte[] baRaster;
    private byte[] baRasterOriginal;
    private BufferedImage imagen;
    private Integer[] iaRaster;
    private MyBuffImagen imgNueva;
    private Raster rastDataImg;

    public Lienzo() {
        this.loadImage();

    }

    public byte[] copyDataRasterToByteArray(Raster ras) {

        byte[] baDataRasterSource;

        //comprueba que la imagen sea compatible para q no pete el programa
        if (ras.getDataBuffer().getDataType() != DataBuffer.TYPE_BYTE) {
            System.out.println("Imagen no compatible");
            return null;   //saltar excepcion mas q valor nulo
        }

        System.out.println("Imagen compatible");

        baDataRasterSource = ((DataBufferByte) ras.getDataBuffer()).getData();
        return baDataRasterSource;
    }

    public void convertIntArrayToBaRaster(Integer[] iArray) {

        byte[] bArray = MyBuffImagen.copyIntArrayToByteArray(iArray);
        for (int i = 0; i < bArray.length; i++) {
            baRaster[i] = bArray[i];
        }
    }

    public void convertToGrey() {

        Integer media;

        //cada tres bytes...
        for (int i = 0; i < this.iaRaster.length; i += 3) {

            //media de los tres primeros
            media = (this.iaRaster[i] + this.iaRaster[i + 1] + this.iaRaster[i + 2]) / 3;

            this.iaRaster[i] = media;
            this.iaRaster[i + 1] = media;
            this.iaRaster[i + 2] = media;
        }

        this.convertIntArrayToBaRaster(this.iaRaster);
    }

    public void getInfoImg() {

        //obtiene el rast de la imagen
        this.rastDataImg = this.imagen.getRaster();

        //obtiene la informacion del array de bytes del raster
        this.baRaster = this.copyDataRasterToByteArray(this.rastDataImg);

        //convierte el baRaster en un array de integers
        this.iaRaster = MyBuffImagen.copyByteArrayToIntArray(this.baRaster);

        //copia para poder restaurar la imagen
        this.makeByteArrayCopy(baRaster);
    }

    public void loadImage() {

        try {
            this.imagen = ImageIO.read(new File("img/fondo2.jpeg"));
            this.getInfoImg();  //guarda el obj Raster y el byteArray del raster

        } catch (Exception e) {
            System.out.println("Img no cargada");
        }
    }

    private void makeByteArrayCopy(byte[] ba) {
        for (int i = 0; i < ba.length; i++) {
            this.baRasterOriginal[i] = ba[i];
        }
    }

    public void modifyBrightness(int nivel) {

        for (int i = 0; i < this.iaRaster.length; i++) {

            this.iaRaster[i] += nivel;

            if (this.iaRaster[i] > 255) {
                this.iaRaster[i] = 255;

            } else if (this.iaRaster[i] < 0) {
                this.iaRaster[i] = 0;
            }
        }

        this.convertIntArrayToBaRaster(this.iaRaster);
    }

    public void restoreImg() {
        
        for (int i = 0; i < baRaster.length; i++) {
            this.baRaster[i] = this.baRasterOriginal[i];
        }

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.imagen, 0, 0, this.imagen.getWidth(), this.imagen.getHeight(), this);
    }

    public Lienzo getLienzo() {
        return this;
    }

}
