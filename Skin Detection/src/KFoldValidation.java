import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Arrays;
import java.awt.Color;
import java.util.Scanner;

public class KFoldValidation extends Component {

    double skin[][][] = new double[256][256][256];
    double nonSkin[][][] = new double[256][256][256];
    double probaability[][][] = new double[256][256][256];
    int count=0,total=0,totalNS=0;

    public static void main(String[] foo) throws IOException {
        new KFoldValidation();
    }

    public void printPixelARGB(int pixel, int maskPixel) {
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        int redMask = (maskPixel >> 16) & 0xff;
        int greenMask = (maskPixel >> 8) & 0xff;
        int blueMask = (maskPixel) & 0xff;
        if(redMask>=230&&greenMask>=230&&blueMask>=230)
        {
            nonSkin[red][green][blue]+=1;
        }
        else
            skin[red][green][blue]+=1;
    }

    void showArray()
    {
        /*for(int i=0;i<256;i++)
        {
            for(int j=0;j<256;j++)
            {
                for(int k=0;k<256;k++)
                {
                    System.out.println(skin[i][j][k]);
                    count++;
                }
            }
        }
        for(int i=0;i<256;i++)
        {
            for(int j=0;j<256;j++)
            {
                for(int k=0;k<256;k++)
                {
                    System.out.println(probaability[i][j][k]);
                }
            }
        }
        System.out.println(count);*/
    }

    void calculateProbability()
    {
        for(int i=0;i<256;i++)
        {
            for(int j=0;j<256;j++)
            {
                for(int k=0;k<256;k++)
                {
                    total+=skin[i][j][k];
                    totalNS+=nonSkin[i][j][k];
                }
            }
        }
        System.out.println(total+"\t"+totalNS);
        for(int i=0;i<256;i++)
        {
            for(int j=0;j<256;j++)
            {
                for(int k=0;k<256;k++)
                {
                    skin[i][j][k]/=total;
                    nonSkin[i][j][k]/=totalNS;
                }
            }
        }
        for(int i=0;i<256;i++)
        {
            for(int j=0;j<256;j++)
            {
                for(int k=0;k<256;k++)
                {
                    if(skin[i][j][k]==0&&nonSkin[i][j][k]==0)
                        probaability[i][j][k]=0;
                    else if(nonSkin[i][j][k]==0)
                        probaability[i][j][k]=skin[i][j][k];
                    else
                        probaability[i][j][k]=skin[i][j][k]/nonSkin[i][j][k];
                }
            }
        }
    }

    private void marchThroughImage(BufferedImage image, BufferedImage mask) {
        int w = image.getWidth();
        int h = image.getHeight();
        //System.out.println("width, height: " + w + ", " + h);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                //System.out.println("x,y: " + j + ", " + i);
                int pixel = image.getRGB(j, i);
                int pixelMask = mask.getRGB(j,i);
                printPixelARGB(pixel,pixelMask);
            }
        }
    }

    public KFoldValidation() throws IOException {
        Scanner sc=new Scanner(System.in);
        int fold=sc.nextInt();
        File[] listOfMyFiles=null;
        File[] listOfMyFilesMask=null;
        File folder = new File("src/Images");
        File folderMask = new File("src/Mask");
        listOfMyFiles = folder.listFiles();
        listOfMyFilesMask = folderMask.listFiles();
        int testLength=(listOfMyFiles.length)/fold;
        int trainLength=(listOfMyFiles.length)-testLength;
        int z=0;
        while(true){
            if(z>fold)break;
            //int i=(testLength*z);i<(testLength*(z+1));i++ For test
            else
            {
                for(int i=0;i<listOfMyFiles.length;i++) {
                    if(i>=(testLength*z)&&i<(testLength*(z+1)))continue;
                    else
                    {
                        //System.out.println("File: " + listOfMyFiles[i].getName() + "Mask: "+listOfMyFilesMask[i].getName());
                        String str = "Images/"+listOfMyFiles[i].getName();
                        String str1 = "Mask/"+listOfMyFilesMask[i].getName();
                        BufferedImage image = ImageIO.read(this.getClass().getResource(str));
                        BufferedImage mask = ImageIO.read(this.getClass().getResource(str1));
                        marchThroughImage(image,mask);
                    }
                }
                calculateProbability();
                for(int k=(testLength*z);k<(testLength*(z+1));k++) {
                    String str2 = "Images/"+listOfMyFiles[k].getName();
                    BufferedImage img = ImageIO.read(this.getClass().getResource(str2));
                    int w = img.getWidth();
                    int h = img.getHeight();
                    for (int i = 0; i < h; i++) {
                        for (int j = 0; j < w; j++) {
                            //System.out.println("x,y: " + j + ", " + i);
                            int pixel = img.getRGB(j, i);
                            //System.out.println("");
                            int red = (pixel >> 16) & 0xff;
                            int green = (pixel >> 8) & 0xff;
                            int blue = (pixel) & 0xff;
                            Color temp;
                            if(probaability[red][green][blue]>.55)
                            {
                                temp = new Color(255, 255, 255);
                            }
                            else
                            {
                                temp = new Color(0, 0, 0);
                            }
                            img.setRGB(j, i, temp.getRGB());
                        }
                    }
                    ImageIO.write(img, "jpg", new File("image"+ String.valueOf(k)+".bmp"));
                }
            }
        }
        showArray();
    }
}





