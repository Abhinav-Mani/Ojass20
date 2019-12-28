package ojass20.nitjsr.in.ojass.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import ojass20.nitjsr.in.ojass.Activities.EventsActivity;
import ojass20.nitjsr.in.ojass.Activities.GurugyanActivity;
import ojass20.nitjsr.in.ojass.Activities.ItineraryActivity;
import ojass20.nitjsr.in.ojass.Activities.MainActivity;
import ojass20.nitjsr.in.ojass.Activities.MapsActivity;
import ojass20.nitjsr.in.ojass.Helpers.HomePage;
import ojass20.nitjsr.in.ojass.R;
import ojass20.nitjsr.in.ojass.Utils.CircularLayout;

public class HomeFragment extends Fragment implements
        GestureDetector.OnGestureListener, View.OnClickListener {
    private static final long ANIM_DURATION = 500;
    private HomeFragInterface fragInterface;
    private ImageView cancelBtn;
    private ArrayList<HomePage> mItems = new ArrayList<>();
    private int mInd;
    private ImageView bigCircle, c1, c2, c3, c4;
    private ArrayList<ImageView> mCircles = new ArrayList<>();
    private GestureDetectorCompat mDetector;
    private RelativeLayout swipeArea;
    private ConstraintLayout cl;
    private ImageView swipeImage1, swipeImage2;
    private TextView txt;
    private ArrayList<Integer> seqCircle = new ArrayList<Integer>() {
        {
            add(1);
            add(2);
            add(3);
            add(4);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        onCancel();
        setUpArrayList();
        swipeArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDetector != null) {
                    mDetector.onTouchEvent(event);
                    return true;
                }
                return false;
            }
        });
        setUpView(0);
        detectBottomTabClick();
        return view;
    }


    private void init(View view) {
        fragInterface = (HomeFragInterface) getActivity();
        cancelBtn = view.findViewById(R.id.cancel_frag);
        bigCircle = view.findViewById(R.id.bg_circle);
        swipeArea = view.findViewById(R.id.swipe_area);
        swipeImage1 = view.findViewById(R.id.img_swipe1);
        swipeImage2 = view.findViewById(R.id.img_swipe2);
        txt = view.findViewById(R.id.home_frag_text);
        cl = view.findViewById(R.id.cl);
        c1 = view.findViewById(R.id.img1);
        c2 = view.findViewById(R.id.img2);
        c3 = view.findViewById(R.id.img3);
        c4 = view.findViewById(R.id.img4);
        mCircles.add(c1);
        mCircles.add(c2);
        mCircles.add(c3);
        mCircles.add(c4);
        mDetector = new GestureDetectorCompat(getContext(), this);
    }

    private void onCancel() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.onCancel();
            }
        });
    }

    private void setUpArrayList() {
        mItems.add(new HomePage("Events", "#FF0000", 0, R.drawable.ic_launcher_background));//red
        mItems.add(new HomePage("Gurugyan", "#00FF00", 1, R.drawable.ic_launcher_foreground));//green
        mItems.add(new HomePage("Itinerary", "#0000FF", 2, R.drawable.ic_launcher_background));//blue
        mItems.add(new HomePage("Maps", "#FFCC00", 3, R.drawable.ic_launcher_foreground));//yellow
    }

    private void detectBottomTabClick() {
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
    }

    private void swipeRight() {
        mInd++;
        int counter = -1;
        if (mInd >= mItems.size()) {
            mInd = 0;
            counter = -3;
        }
        imgAnimationRight();
        setUpView(counter);
        setTxtRight();
    }

    private void swipeLeft() {
        mInd--;
        int counter = 1;
        if (mInd < 0) {
            mInd = mItems.size() - 1;
            counter = 3;
        }
        imgAnimationLeft();
        setUpView(counter);
        setTxtLeft();

    }

    private void setTxtRight() {
        txt.setText(mItems.get(mInd).getmTitle());
    }

    private void setTxtLeft() {
        txt.setText(mItems.get(mInd).getmTitle());
    }

    private void imgAnimationRight() {
        final int img = mItems.get(mInd).getmImageId();
        swipeImage2.setImageResource(img);
        //img animation
        //translate swipeImg2 from right to center
        AnimationSet set1 = translateAnim(0, -200, 1, 0);
        AnimationSet set2 = translateAnim(200, 0, 0, 1);
        set1.setDuration(ANIM_DURATION);
        set2.setDuration(ANIM_DURATION);
        set1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                swipeImage1.setImageResource(img);
                swipeImage2.setAlpha(0.0f);
                swipeImage1.setAlpha(1.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        swipeImage1.startAnimation(set1);
        swipeImage2.startAnimation(set2);
    }

    private void imgAnimationLeft() {
        final int img = mItems.get(mInd).getmImageId();
        swipeImage2.setImageResource(img);

        AnimationSet set1 = translateAnim(0, 200, 1, 0);
        AnimationSet set2 = translateAnim(-200, 0, 0, 1);
        set1.setDuration(ANIM_DURATION);
        set2.setDuration(ANIM_DURATION);
        set1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                swipeImage1.setImageResource(img);
                swipeImage2.setAlpha(0.0f);
                swipeImage1.setAlpha(1.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        swipeImage1.startAnimation(set1);
        swipeImage2.startAnimation(set2);
    }

    private AnimationSet translateAnim(float fromTr, float toTr, float fromAl, float toAl) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation tr = new TranslateAnimation(fromTr, toTr, 0, 0);
        set.addAnimation(tr);
        AlphaAnimation al = new AlphaAnimation(fromAl, toAl);
        set.addAnimation(al);
        return set;
    }

    public void setUpView(int counter) {
        if (counter == 0) {
            txt.setText(mItems.get(mInd).getmTitle());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                swipeImage1.setImageDrawable(getActivity().getDrawable(mItems.get(mInd).getmImageId()));
                swipeImage2.setImageDrawable(getActivity().getDrawable(mItems.get(mInd).getmImageId()));
            }
        }
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        for (int i = 0; i < mCircles.size(); i++) {
            mCircles.get(i).setColorFilter(Color.parseColor(mItems.get(i).getmCircleColor()));
            animators.add(animateView(mCircles.get(i), 500, counter * 60));
            if (i != mInd) {
                animators.add(changeRadius(mCircles.get(i), 500, 128));
                animators.add(changeHeight(mCircles.get(i), 500, 20));
                animators.add(changeWidth(mCircles.get(i), 500, 20));
            } else {
                animators.add(changeRadius(mCircles.get(i), 500, 128));
                animators.add(changeHeight(mCircles.get(i), 500, 30));
                animators.add(changeWidth(mCircles.get(i), 500, 30));
            }
        }
        for (int i = 0; i < animators.size(); i++) {
            ValueAnimator anim = animators.get(i);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mDetector = null;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mDetector = new GestureDetectorCompat(getContext(), HomeFragment.this);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        }
    }

    private ValueAnimator animateView(final ImageView imageView, long duration, long angle) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.circleAngle, (int) lP.circleAngle + (int) angle);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.circleAngle = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private ValueAnimator changeRadius(final ImageView imageView, long duration, int newRadius) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.circleRadius, (int) MainActivity.convertDpToPixel(newRadius, getContext()));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.circleRadius = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private ValueAnimator changeHeight(final ImageView imageView, long duration, int newHeight) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.height, (int) MainActivity.convertDpToPixel(newHeight, getContext()));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.height = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    private ValueAnimator changeWidth(final ImageView imageView, long duration, int newWidth) {
        ConstraintLayout.LayoutParams lP = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt((int) lP.width, (int) MainActivity.convertDpToPixel(newWidth, getContext()));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.width = val;
                imageView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        return anim;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        if (x >= swipeImage1.getLeft() && x <= swipeImage1.getRight() && y >= swipeImage1.getTop() && y <= swipeImage1.getBottom()) {
            switch (mInd) {
                case 0:
                    startActivity(new Intent(getContext(), EventsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(getContext(), GurugyanActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(getContext(), ItineraryActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(getContext(), MapsActivity.class));
                    break;
                default:
                    Log.e("LOL", "Bhai sahab ye kis line mein aa gye aap?");
            }
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() < e2.getX()) {
            swipeLeft();
        } else {
            swipeRight();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int iClicked = -1;
        for (int i = 0; i < mCircles.size(); i++) {
            if (v.getId() == mCircles.get(i).getId()) {
                iClicked = i;
                break;
            }
        }
        if (iClicked > mInd)
            swipeRight();
        else if (iClicked < mInd)
            swipeLeft();
    }

    public interface HomeFragInterface {
        void onCancel(); // close fragment
    }
}