package com.example.app6;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class MyOverlayService extends Service {
    private View view;
    private WindowManager windowManager;
    public MyOverlayService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return;
        }
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        view = inflater.inflate(R.layout.overlay_layout, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        Button openAppButton = view.findViewById(R.id.close_button);
        openAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.app6");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }

                stopSelf();
            }
        });

        // Получение имени пользователя из SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "Пользователь");

        // Обновление текста на баннере
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(userName + ", не забудь дочитать книжку!");

        windowManager.addView(view, params);
        windowManager.updateViewLayout(view, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null && view.getWindowToken() != null) {
            windowManager.removeView(view);
        }
    }
}
