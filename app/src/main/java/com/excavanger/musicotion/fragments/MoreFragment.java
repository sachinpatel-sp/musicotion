package com.excavanger.musicotion.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.excavanger.musicotion.R;
import com.excavanger.musicotion.utils.Common;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView dropDown;
    private LinearLayout qualityLayout;
    private ValueAnimator mAnimator;
    private TextView extreme,best,good,fair,low,reportBug,equalizer;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        dropDown = view.findViewById(R.id.drop_down);
        qualityLayout = view.findViewById(R.id.quality_layout);
        extreme = view.findViewById(R.id.extreme);
        best = view.findViewById(R.id.best);
        good = view.findViewById(R.id.good);
        fair = view.findViewById(R.id.fair);
        low = view.findViewById(R.id.low);
        equalizer = view.findViewById(R.id.equalizer_tv);
        reportBug = view.findViewById(R.id.bug_tv);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        if(Common.quality.equalsIgnoreCase("320")){
            extreme.setTextColor(getResources().getColor(R.color.red_A400));
            best.setTextColor(getResources().getColor(R.color.heading));
            good.setTextColor(getResources().getColor(R.color.heading));
            fair.setTextColor(getResources().getColor(R.color.heading));
            low.setTextColor(getResources().getColor(R.color.heading));
        }else if(Common.quality.equalsIgnoreCase("128")){
            extreme.setTextColor(getResources().getColor(R.color.heading));
            best.setTextColor(getResources().getColor(R.color.red_A400));
            good.setTextColor(getResources().getColor(R.color.heading));
            fair.setTextColor(getResources().getColor(R.color.heading));
            low.setTextColor(getResources().getColor(R.color.heading));
        }else if(Common.quality.equalsIgnoreCase("64")){
            extreme.setTextColor(getResources().getColor(R.color.heading));
            best.setTextColor(getResources().getColor(R.color.heading));
            good.setTextColor(getResources().getColor(R.color.red_A400));
            fair.setTextColor(getResources().getColor(R.color.heading));
            low.setTextColor(getResources().getColor(R.color.heading));
        }else if(Common.quality.equalsIgnoreCase("32")){
            extreme.setTextColor(getResources().getColor(R.color.heading));
            best.setTextColor(getResources().getColor(R.color.heading));
            good.setTextColor(getResources().getColor(R.color.heading));
            fair.setTextColor(getResources().getColor(R.color.red_A400));
            low.setTextColor(getResources().getColor(R.color.heading));
        }else if(Common.quality.equalsIgnoreCase("16")){
            extreme.setTextColor(getResources().getColor(R.color.heading));
            best.setTextColor(getResources().getColor(R.color.heading));
            good.setTextColor(getResources().getColor(R.color.heading));
            fair.setTextColor(getResources().getColor(R.color.heading));
            low.setTextColor(getResources().getColor(R.color.red_A400));
        }

        extreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.quality = "320";
                extreme.setTextColor(getResources().getColor(R.color.red_A400));
                best.setTextColor(getResources().getColor(R.color.heading));
                good.setTextColor(getResources().getColor(R.color.heading));
                fair.setTextColor(getResources().getColor(R.color.heading));
                low.setTextColor(getResources().getColor(R.color.heading));
                preferences.edit().putString("quality","320").apply();
            }
        });

        best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.quality = "128";
                extreme.setTextColor(getResources().getColor(R.color.heading));
                best.setTextColor(getResources().getColor(R.color.red_A400));
                good.setTextColor(getResources().getColor(R.color.heading));
                fair.setTextColor(getResources().getColor(R.color.heading));
                low.setTextColor(getResources().getColor(R.color.heading));
                preferences.edit().putString("quality","128").apply();
            }
        });

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.quality = "64";
                extreme.setTextColor(getResources().getColor(R.color.heading));
                best.setTextColor(getResources().getColor(R.color.heading));
                good.setTextColor(getResources().getColor(R.color.red_A400));
                fair.setTextColor(getResources().getColor(R.color.heading));
                low.setTextColor(getResources().getColor(R.color.heading));
                preferences.edit().putString("quality","64").apply();
            }
        });

        fair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.quality = "32";
                extreme.setTextColor(getResources().getColor(R.color.heading));
                best.setTextColor(getResources().getColor(R.color.heading));
                good.setTextColor(getResources().getColor(R.color.heading));
                fair.setTextColor(getResources().getColor(R.color.red_A400));
                low.setTextColor(getResources().getColor(R.color.heading));
                preferences.edit().putString("quality","32").apply();
            }
        });

        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.quality = "16";
                extreme.setTextColor(getResources().getColor(R.color.heading));
                best.setTextColor(getResources().getColor(R.color.heading));
                good.setTextColor(getResources().getColor(R.color.heading));
                fair.setTextColor(getResources().getColor(R.color.heading));
                low.setTextColor(getResources().getColor(R.color.red_A400));
                preferences.edit().putString("quality","16").apply();
            }
        });

        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qualityLayout.getVisibility() == View.GONE) {
                    dropDown.setImageResource(R.drawable.ic_arrow_drop_up);
                    expand();
                } else {
                    dropDown.setImageResource(R.drawable.ic_arrow_drop_down);
                    collapse();
                }
            }
        });

        qualityLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        qualityLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        qualityLayout.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        qualityLayout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0, qualityLayout.getMeasuredHeight());
                        return true;
                    }
                });

        reportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sachin.18je0703@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Found In Musicotion");
                intent.putExtra(Intent.EXTRA_TEXT,"Dear Team Musicotion,\n");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        equalizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AudioEffect
                        .ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);

                if ((intent.resolveActivity(getActivity().getPackageManager()) != null)) {
                    startActivityForResult(intent, 0);
                } else {
                    // No equalizer found :(
                }
            }
        });

        return view;
    }



    private void expand() {

        qualityLayout.setVisibility(View.VISIBLE);
        mAnimator.start();
    }

    private void collapse() {
        int finalHeight = qualityLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                qualityLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = qualityLayout.getLayoutParams();
                layoutParams.height = value;
                qualityLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}

/*Intent intent = new Intent(AudioEffect
    .ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);

if ((intent.resolveActivity(getPackageManager()) != null)) {
    startActivityForResult(intent, REQUEST_EQ);
} else {
    // No equalizer found :(
}*/