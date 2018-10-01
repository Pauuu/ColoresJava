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
    private BufferedImage imgOriginal;
    private MyBuffImagen imgNueva;
    private Raster rastDataImg;

    public Lienzo() {
        this.loadImage();
        this.getInfoImg();
        this.convertToGrey(baRaster);
    }

    public void convertToGrey(byte[] aBytes) {
        
        
        int b0;
        int b1;
        int b2;
        
        //cada tres bytes...
        for (int i = 0; i < aBytes.length; i += 3) {
            
            //cpnvertimos los tres primeros bytes a ints
            b0 = Byte.toUnsignedInt(aBytes[i]);
            b1 = Byte.toUnsignedInt(aBytes[i + 1]);
            b2 = Byte.toUnsignedInt(aBytes[i + 2]);

            //media de los tres primeros
            Integer media = (b0 + b1 + b2) / 3;
            
            //convertir resultado de ints a bytes
            Byte bMedia = media.byteValue();
            
            //asignar media a los tres primeros bytes 
            aBytes[i] = bMedia;
            aBytes[i + 1] = bMedia;
            aBytes[i + 2] = bMedia;
        }
    }
    
    public byte[] copyDataRasterToByteArray(Raster ras) {

        byte[] baDataRasterSource;

        if (ras.getDataBuffer().getDataType() != DataBuffer.TYPE_BYTE) {
            System.out.println("Imagen no compatible");
            return null;   //saltar excepcion mas q valor "null"
        }

        System.out.println("Imagen compatible");

        baDataRasterSource = ((DataBufferByte) ras.getDataBuffer()).getData();
        return baDataRasterSource;
    }
    
    public void getInfoImg() {

        //obtiene el rast de la imagen
        this.rastDataImg = this.imgOriginal.getRaster();

        //obtiene la informacion del array de bytes del raster
        this.baRaster = this.copyDataRasterToByteArray(rastDataImg);

        //convertTogrey(baRaster);
    }

    public void loadImage() {

        try {
            this.imgOriginal = ImageIO.read(new File("img/fondo2.jpeg"));
        } catch (Exception e) {
            System.out.println("Img no cargada");
        }
    }

    @Override
    public void paint(Graphics g) {

        g.drawImage(this.imgOriginal, 0, 0, this.imgOriginal.getWidth(), this.imgOriginal.getHeight(), this);
    }

    public Lienzo getLienzo() {
        return this;
    }

}
