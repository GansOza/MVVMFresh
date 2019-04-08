package anew.gans.mvvm.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.lang.ref.WeakReference;

/**
 * Created by psk on 11/19/2017.
 */

public class BaseFragmentViewModel extends BaseObservable
{
    private WeakReference<Fragment> fragmentWeakReference;

    public BaseFragmentViewModel(Fragment fragment) {
        fragmentWeakReference = new WeakReference<>(fragment);
    }

    protected FragmentActivity getActivityContext() {
        return fragmentWeakReference.get().getActivity();
    }

    protected Context getContext() {
        return fragmentWeakReference.get().getContext();
    }

    protected FragmentManager getSupportedFragmentManager() {
        return fragmentWeakReference.get().getActivity().getSupportFragmentManager();
    }

    protected FragmentManager getChildFragmentManager() {
        return fragmentWeakReference.get().getChildFragmentManager();
    }

    protected Fragment getParentFragment() {
        return fragmentWeakReference.get().getParentFragment();
    }

    protected Fragment getFragment() {
        return fragmentWeakReference.get();
    }

    protected void startActivityForResult(Intent intent, int requestCode) {
        fragmentWeakReference.get().startActivityForResult(intent, requestCode);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
