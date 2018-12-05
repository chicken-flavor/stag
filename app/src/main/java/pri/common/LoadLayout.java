package pri.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class LoadLayout extends FrameLayout {

    private View mAddable;
    private ViewGroup mEmpty;
    private ViewGroup mError;
    private View mNetNotWork;
    private View mLoading;
    private View mMask;

    @NonNull
    private View mContent;

    private int mCurrentState = -1;

    public LoadLayout(@NonNull Context context) {
        this(context, null);
    }

    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadLayout, 0, 0);

        int addableId = a.getResourceId(R.styleable.LoadLayout_load_addable, R.layout.view_loadlayout_addable);
        int emptyId = a.getResourceId(R.styleable.LoadLayout_load_empty, R.layout.view_loadlayout_empty);
        int errorId = a.getResourceId(R.styleable.LoadLayout_load_error, R.layout.view_loadlayout_addable);
        int netnotworkId = a.getResourceId(R.styleable.LoadLayout_load_netnotwork, R.layout.view_loadlayout_netnotwork);
        int loadingId = a.getResourceId(R.styleable.LoadLayout_load_loading, R.layout.view_loadlayout_loading);
        int contentId = a.getResourceId(R.styleable.LoadLayout_load_content, R.layout.view_loadlayout_content);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        inflater.inflate(addableId, this, true);
        inflater.inflate(emptyId, this, true);
        inflater.inflate(errorId, this, true);
        inflater.inflate(netnotworkId, this, true);
        inflater.inflate(loadingId, this, true);
        inflater.inflate(contentId, this, true);
        inflater.inflate(R.layout.view_loadlayout_mask, this, true);

        mAddable = getChildAt(0);
        mEmpty = (ViewGroup) getChildAt(1);
        mError = (ViewGroup) getChildAt(2);
        mNetNotWork = getChildAt(3);
        mLoading = getChildAt(4);
        mContent = getChildAt(5);
        // Mask must be the last one
        mMask = getChildAt(6);

        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        for (int i = 0, count = getChildCount(); i < count; i++) {
            View view = getChildAt(i);
            view.setVisibility(GONE);
        }
    }

    public View getContent() {
        return mContent;
    }

    public void setMask(boolean boom) {
        mMask.setVisibility(boom ? VISIBLE : GONE);
    }

    public void setEmpty(EmtpyBuider buider) {
        if (buider.iconId > 0) {
            ((ImageView) mEmpty.getChildAt(0)).setImageResource(buider.iconId);
        }
        if (buider.textId > 0) {
            ((TextView) mEmpty.getChildAt(1)).setText(buider.textId);
        }
    }

    public void setError(ErrorBuider buider) {
        if (buider.iconId > 0) {
            ((ImageView) mError.getChildAt(0)).setImageResource(buider.iconId);
        }
        if (buider.textId > 0) {
            ((TextView) mError.getChildAt(1)).setText(buider.textId);
        }
        mError.getChildAt(2).setOnClickListener(buider.listener);
    }

    public void setState(@State int state) {
        if (mCurrentState == state)
            return;

        mCurrentState = state;
        // not contain mask
        for (int i = 0, count = getChildCount() - 1; i < count; i++) {
            View view = getChildAt(i);
            if (i == state) {
                view.setVisibility(VISIBLE);
            } else {
                view.setVisibility(GONE);
            }
        }
    }

    @IntDef({State.Addable, State.Empty, State.Error, State.NetNotWork, State.Loading, State
            .Content})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int Addable = 0x00000000;
        int Empty = 0x0000001;
        int Error = 0x00000002;
        int NetNotWork = 0x00000003;
        int Loading = 0x00000004;
        int Content = 0x0000005;
        int Mask = 0xffffffff;
    }

    public static class EmtpyBuider {
        @DrawableRes
        private int iconId;
        @StringRes
        private int textId;

        public EmtpyBuider icon(@DrawableRes int iconId) {
            this.iconId = iconId;
            return this;
        }

        public EmtpyBuider text(@StringRes int textId) {
            this.textId = textId;
            return this;
        }
    }

    public static class ErrorBuider {
        @DrawableRes
        private int iconId;
        @StringRes
        private int textId;
        private OnClickListener listener;

        public ErrorBuider icon(@DrawableRes int iconId) {
            this.iconId = iconId;
            return this;
        }

        public ErrorBuider text(@StringRes int textId) {
            this.textId = textId;
            return this;
        }

        public ErrorBuider listener(OnClickListener listener) {
            this.listener = listener;
            return this;
        }
    }
}
