package com.vvdev.coolor.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vvdev.coolor.R;
import com.vvdev.coolor.interfaces.ColorSpec;
import com.vvdev.coolor.interfaces.ColorUtility;
import com.vvdev.coolor.interfaces.Gradients;
import com.vvdev.coolor.interfaces.SavedData;
import com.vvdev.coolor.ui.alertdialog.ColorInfoDialog;
import com.vvdev.coolor.ui.alertdialog.CreateGradientDialog;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import androidx.recyclerview.widget.RecyclerView;

public class PaletteRVAdapter extends RecyclerView.Adapter<PaletteRVAdapter.MyViewHolderPalette> {

    private static final String TAG = PaletteRVAdapter.class.getName();

    private final Activity activity;

    public ArrayList<MyViewHolderPalette> myViewHolderPaletteArrayList = new ArrayList<>();

    public PaletteRVAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return new SavedData(activity).getColorsSize();
    }

    @Override
    public MyViewHolderPalette onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_palette_itemrecycle, parent, false);
        return new MyViewHolderPalette(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderPalette holder, int position) {
        ColorSpec currentColorSpec = new SavedData(activity).getColors().get(position);
        holder.display(currentColorSpec);
    }

    public void updateSpinner(){
        for(int x=0;x<myViewHolderPaletteArrayList.size();x++){
            myViewHolderPaletteArrayList.get(x).updateSpinners();
        }
    }

    public class MyViewHolderPalette extends RecyclerView.ViewHolder {

        private ColorSpec currentColor;

        private CircleImageView colorPreview;
        private ImageView trash;
        private TextView colorName;
        private TextView hsv;
        private TextView rgb;
        private TextView hexa;
        private TextView more;
        private TextView createGradient;
        private ConstraintLayout piExtend;
        private TextView moreInformation;

        private ArrayList<View> generate = new ArrayList<>();

        private Spinner extendSpinner;
        private ArrayList<ConstraintLayout> extendInclude = new ArrayList<>();
        private ArrayList<View> extendView = new ArrayList<>();
        private ArrayList<TextView> extendHex = new ArrayList<>();
        private ArrayList<TextView> extendRGB = new ArrayList<>();

        private boolean itemDeleted = false; // used to prevent bug. User can spam click the trash button and it delete multiple colors in colors data.

        public MyViewHolderPalette(final View itemView) { //
            super(itemView);

            myViewHolderPaletteArrayList.add(this);

            colorPreview = itemView.findViewById(R.id.piColorPreview);

            colorName = itemView.findViewById(R.id.piColorName);
            hsv = itemView.findViewById(R.id.piHSV);
            rgb = itemView.findViewById(R.id.piRGB);
            hexa = itemView.findViewById(R.id.piHex);
            more = itemView.findViewById(R.id.piMore);
            trash = itemView.findViewById(R.id.piTrash);
            moreInformation = itemView.findViewById(R.id.piExtendMoreInformation);
            createGradient = itemView.findViewById(R.id.piExtendMoreCreateGradient);

            generate.add(itemView.findViewById(R.id.piGenerate0));
            generate.add(itemView.findViewById(R.id.piGenerate1));
            generate.add(itemView.findViewById(R.id.piGenerate2));
            generate.add(itemView.findViewById(R.id.piGenerate3));
            generate.add(itemView.findViewById(R.id.piGenerate4));
            generate.add(itemView.findViewById(R.id.piGenerate5));

            piExtend = itemView.findViewById(R.id.piExtend);

            extendSpinner = itemView.findViewById(R.id.piSpinner);
            extendInclude.add((ConstraintLayout) itemView.findViewById(R.id.piExtendGenerate0));
            extendInclude.add((ConstraintLayout) itemView.findViewById(R.id.piExtendGenerate1));
            extendInclude.add((ConstraintLayout) itemView.findViewById(R.id.piExtendGenerate2));
            extendInclude.add((ConstraintLayout) itemView.findViewById(R.id.piExtendGenerate3));
            extendInclude.add((ConstraintLayout) itemView.findViewById(R.id.piExtendGenerate4));
            extendInclude.add((ConstraintLayout) itemView.findViewById(R.id.piExtendGenerate5));

            for (int x = 0; x < extendInclude.size(); x++) {
                extendView.add(extendInclude.get(x).findViewById(R.id.piExtendView));
                extendHex.add((TextView) extendInclude.get(x).findViewById(R.id.piExtendHex));
                extendRGB.add((TextView) extendInclude.get(x).findViewById(R.id.piExtendRGB));
            }

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (piExtend.getVisibility() == View.VISIBLE) {
                        piExtend.setVisibility(View.GONE);
                    } else {
                        piExtend.setVisibility(View.VISIBLE);
                    }
                }
            });

            trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "trash imageView clicked");
                    if (!itemDeleted) {
                        itemDeleted = true;
                        Log.i(TAG, "color selected isn't deleted, start to delete");
                        SavedData savedData = new SavedData(activity);
                        int position = getLayoutPosition();
                        savedData.removeColor(position);
                    }
                }
            });


            // set the extend spinner on item click listener and change each extend include to the colors propriety of selected item
            extendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    int selectedItem = parentView.getSelectedItemPosition();
                    String[] colorsOfItem = currentColor.getAllGeneratedColors().get(selectedItem);
                    showNumberExtendInclude(colorsOfItem.length);
                    changeExtendInclude(colorsOfItem);
                    updateListener(colorsOfItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

            moreInformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"TextView more information clicked");
                    ColorInfoDialog cid = new ColorInfoDialog(activity,currentColor);
                    cid.show();
                }
            });

            createGradient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateGradientDialog cgd = new CreateGradientDialog(activity,currentColor);
                    cgd.show();
                }
            });
        }

        public void display(ColorSpec colorSpec) {
            currentColor = colorSpec;

            // get hexa, hsv, rgb of color
            String hexaFromColorSpec = colorSpec.getHexa();
            int[] hsvFromColorSpec = colorSpec.getHSV();
            int[] rgbFromColorSpec = colorSpec.getRGB();

            // setup preview of color
            Bitmap bitmapOfPreview = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
            bitmapOfPreview.eraseColor(Color.parseColor(hexaFromColorSpec));
            colorPreview.setImageBitmap(bitmapOfPreview);

            // setup text
            colorName.setText(ColorUtility.nearestColor(hexaFromColorSpec)[0]);
            String toHSV = "HSV : " + hsvFromColorSpec[0] + ", " + hsvFromColorSpec[1] + ", " + hsvFromColorSpec[2];
            hsv.setText(toHSV);
            String toRGB = "RGB : " + rgbFromColorSpec[0] + ", " + rgbFromColorSpec[1] + ", " + rgbFromColorSpec[2];
            rgb.setText(toRGB);
            String toHexa = "HEX : " + hexaFromColorSpec;
            hexa.setText(toHexa);

            // setup extend spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, Gradients.getInstance(activity).getAllGradientsName());
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            extendSpinner.setAdapter(spinnerAdapter);

            showNumberExtendInclude(6); // set the good number of include for generated colors method shades
            String[] toShow; // setup preview generated colors by the method of generation shades
            if (ColorUtility.isNearestFromBlackThanWhite(colorSpec.getHexa())) { // check if the color is closer to black than white
                extendSpinner.setSelection(2); // set spinner to position of Tints ( it will also set extendInclude to Tints mode )
                toShow = colorSpec.getTints(); // setup preview generated colors by the method of generation Tints
            } else {
                extendSpinner.setSelection(0); // set spinner to position of Shades ( it will also set extendInclude to Shades mode )
                toShow = colorSpec.getShades(); // setup preview generated colors by the method of generation Shades
            }
            for (int x = 0; x < generate.size(); x++) { // setup preview generated colors by the method of generation ( Shades / Tints )
                generate.get(x).setBackgroundColor(Color.parseColor(toShow[x]));
            }
        }

        /**
         * Change the visibility of extend include depending of generated colors length
         *
         * @param number generated colors length
         */
        private void showNumberExtendInclude(int number) {
            for (int x = 0; x < number; x++) {
                extendInclude.get(x).setVisibility(View.VISIBLE);
            }
            for (int x = number; x < extendInclude.size(); x++) {
                extendInclude.get(x).setVisibility(View.INVISIBLE);
            }
            //piExtend.setLayoutParams(new ConstraintLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        /**
         * Change each extend view to the proper colors of generated colors
         *
         * @param colors generated colors
         */
        private void changeExtendInclude(String[] colors) {
            for (int x = 0; x < colors.length; x++) {
                extendView.get(x).setBackgroundColor(Color.parseColor(colors[x]));
                extendHex.get(x).setText(colors[x]);
                int[] rgbOfColor = ColorUtility.getRGBFromHex(colors[x]);
                String rgbText = rgbOfColor[0] + ", " + rgbOfColor[1] + "," + rgbOfColor[2];
                extendRGB.get(x).setText(rgbText);
            }
        }

        /**
         * used to set extend item on listener or not
         *
         * @param colors
         */
        private void updateListener(final String[] colors) {
            for (int x = 0; x < extendInclude.size(); x++) {
                if (extendInclude.get(x).getVisibility() == View.VISIBLE) {
                    final String currentColor = colors[x];
                    extendInclude.get(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new SavedData(activity).addColor(currentColor);
                        }
                    });
                } else {
                    extendInclude.get(x).setOnClickListener(null);
                }
            }
        }

        public void updateSpinners(){
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, Gradients.getInstance(activity).getAllGradientsName());
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            extendSpinner.setAdapter(spinnerAdapter);
        }
    }
}