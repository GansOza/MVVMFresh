package anew.gans.mvvm.base;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by psk on 11/19/2017.
 */

public interface BaseViewModelListener
{
    void showDialog();

    void hideDialog();

    //void showToast(String message);

    void showToastSuccess(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon);
    void showToastError(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon);


}
