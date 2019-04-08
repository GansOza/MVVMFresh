package anew.gans.mvvm.base;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;


public abstract class BaseActivityViewModel extends BaseObservable
{
    private WeakReference<AppCompatActivity> activityWeakReference;

    public BaseActivityViewModel(AppCompatActivity appCompatActivity)
    {
        this.activityWeakReference = new WeakReference<>(appCompatActivity);
    }
    protected AppCompatActivity getContext()
    {
        return activityWeakReference.get();
    }

    protected FragmentManager getSupportFragmentManager()
    {
        return getContext().getSupportFragmentManager();
    }

    protected void setTitle(String title) {
        activityWeakReference.get().setTitle(title);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data)
    {


    }


}
