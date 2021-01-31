package com.vvdev.coolor.interfaces;

import com.vvdev.coolor.activity.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ColorSpec { // https://htmlcolorcodes.com/fr/selecteur-de-couleur/

    private final static String TAG = ColorSpec.class.getName();

    private String hexa;
    private int[] hsv; // also hsb
    private int[] rgb;
    private int[] hsl;
    private int[] cmyk;
    private int[] cielab;

    private String[] complementary;
    private String[] triadic;
    private String[] tints;
    private String[] tones;
    private String[] shades;
    private String[] compound;
    private String[] analogous;

    public ColorSpec(int[] rgb) {
        setRGB(rgb);
        setHexa(ColorUtility.getHexFromRGB(rgb));
        setup();
    }

    public ColorSpec(String hexa) {
        setHexa(hexa);
        setRGB(ColorUtility.getRGBFromHex(hexa));
        setup();
    }

    private void setup(){
        setHSV(ColorUtility.getHsvFromRGB(getRGB()));
        setHSL(ColorUtility.getHslFromRGB(getRGB()));
        setCmyk(ColorUtility.getCMYKFromRGB(getRGB()));
        setCielab(ColorUtility.getLABFromRGB(getRGB()));

        setShades(ColorUtility.gradientApproximatelyGenerator(getHexa(),Gradient.getShadesValue(),6));
        setTones(ColorUtility.gradientApproximatelyGenerator(getHexa(),Gradient.getTonesValue(),6));
        setTints(ColorUtility.gradientApproximatelyGenerator(getHexa(),Gradient.getTintsValue(),6));
        setTriadic(ColorUtility.getTriadicFromRGB(getRGB()));
        setComplementary(ColorUtility.getComplementaryFromRGB(getRGB()));
        setCompound(ColorUtility.getAnalogousFromRGB(getRGB()));
        setAnalogous(ColorUtility.getCompoundFromRGB(getRGB()));
    }

    /**
     * shades[0] = this.hexa
     * triadic[ 1 / 2 /3 / 4 / 5 ] = generate
     */

    private void setShades(String[] shades){ // Nuances
        this.shades = shades;
    }

    private void setTones(String[] tones) { // Tonalités
        this.tones=tones;
    }

    private void setTints(String[] tints) {
        this.tints=tints;
    }

    /**
     * triadic[0] = this.hexa
     * triadic[ 1 / 2 ] = generate
     */
    private void setTriadic(String[] triadic) { // Triadique
        this.triadic=triadic;
    }

    private void setComplementary(String[] complementary) { // Complémentaire
        this.complementary=complementary;
    }

    public String getHexa() {
        return hexa;
    }

    private void setHexa(String hexa) {
        this.hexa = hexa.toUpperCase();
    }

    public int[] getHSV() {
        return hsv;
    }

    private void setHSV(int[] HSV) {
        this.hsv = HSV;
    }

    public int[] getRGB() {
        return rgb;
    }

    private void setRGB(int[] RGB) {
        this.rgb = RGB;
    }

    public int[] getHSL() {
        return hsl;
    }

    public void setHSL(int[] hsl) {
        this.hsl = hsl;
    }

    public String[] getComplementary() {
        return complementary;
    }

    public String[] getTriadic() {
        return triadic;
    }

    public String[] getTones() {
        return tones;
    }

    public String[] getTints() {
        return tints;
    }

    public String[] getShades(){
        return shades;
    }

    public int[] getCmyk() {
        return cmyk;
    }

    public void setCmyk(int[] cmyk) {
        this.cmyk = cmyk;
    }

    public int[] getCielab() {
        return cielab;
    }

    public void setCielab(int[] cielab) {
        this.cielab = cielab;
    }

    public String[] getCompound() {
        return compound;
    }

    public void setCompound(String[] compound) {
        this.compound = compound;
    }

    public String[] getAnalogous() {
        return analogous;
    }

    public void setAnalogous(String[] analogous) {
        this.analogous = analogous;
    }

    public ArrayList<String[]> getAllGeneratedColors(){
        ArrayList<String[]> toReturn = new ArrayList<>(Arrays.asList(
                getShades(),
                getTones(),
                getTints(),
                getTriadic(),
                getComplementary(),
                getCompound(),
                getAnalogous()));
        if(MainActivity.Instance.get()!=null){
            ArrayList<Gradient> customGradients = Gradients.getInstance(MainActivity.Instance.get()).getAllCustom();
            for(int x=0;x<customGradients.size();x++){
                toReturn.add(ColorUtility.gradientApproximatelyGenerator(getHexa(),customGradients.get(x).getHexaValue(),6));
            }
        }
        return toReturn;
    }


    /**
     * Used to check is the object is correct
     * @param toVerify object to verify
     * @param longCheck if true, check all hexa value length to know if it's correct or not
     * @return
     */
    public static boolean isCorrect(ColorSpec toVerify,boolean longCheck){
       /* if(toVerify.getHexa().length()<6){
            Log.e(TAG,"isCorrect() detect error.\nHexa color length of object is <6. Value ="+toVerify.getHexa()
            +"\nObject to verify toString()= "+toVerify.toString());
            return false;
        }
        if(toVerify.getRGB().length<3){
            return false;
        }
        if(toVerify.getHSV().length<3){
            return false;
        }
        if(toVerify.getHSL().length<3){
            return false;
        }

        if(toVerify.getAllMethodName().size()!=toVerify.getAllGeneratedColors().size()){ // you might forgot to add new method in getGenerateMethod() or getAllGeneratedColors()
            Log.e(TAG,"isCorrect() detect error.\nYou might forgot to add new method in getGenerateMethod() or getAllGeneratedColors().\nObject to verify to string = "+toVerify.toString());
            return false;
        }
        if(longCheck){
            for(int x=0;x<toVerify.getAllMethodName().size();x++){
                for(int y=0;y<toVerify.getAllGeneratedColors().get(x).length;x++){
                    if(toVerify.getAllGeneratedColors().get(x)[y].length()<6){
                        Log.e(TAG,"isCorrect() detect error."
                                + "\nMethod at position "+x+" and hexa value at "+y+" have a length <6"
                        +"\nWrong string value ="+toVerify.getAllGeneratedColors().get(x)[y]
                        +"\nObject to verify to string ="+toVerify.toString());
                        return false;
                    }
                }
            }
        }*/
        return true;
    }
}
