package com.pedrodev.appchat.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pedrodev.appchat.R;
import com.pedrodev.appchat.activities.postDetailActivity;
import com.pedrodev.appchat.models.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {


    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    // public class SliderAdapterExample extends SliderViewAdapter<com.pedrodev.appchat.adapters.SliderAdapterExample.SliderAdapterVH>
    public SliderAdapter(Context context, List<SliderItem> sliderItems) {
        this.context = context;
        mSliderItems = sliderItems;
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        // Here you can instance the layout to use in your slider
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        // Here you go to de instance methods to use for layouts
        SliderItem sliderItem = mSliderItems.get(position);
        if (sliderItem.getImageUrl() != null) {
            if (!sliderItem.getImageUrl().isEmpty()) {
                Picasso.with(context).load(sliderItem.getImageUrl()).into(viewHolder.imageViewSlider);
            }
        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size

        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewSlider;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewSlider = itemView.findViewById(R.id.imageViewSlider);

            this.itemView = itemView;
        }
    }

}







/*

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);
//
//        viewHolder.textViewDescription.setText(sliderItem.getDescription());
//        viewHolder.textViewDescription.setTextSize(16);
//        viewHolder.textViewDescription.setTextColor(Color.WHITE);
//        Glide.with(viewHolder.itemView)
//                .load(sliderItem.getImageUrl())
//                .fitCenter()
//                .into(viewHolder.imageViewBackground);
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

 */