package com.example.app6;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopOverlayService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopOverlayService();

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("some_int", 0);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, Fragment1.class, bundle).commit();
        }

    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void startOverlayService() {
        Intent serviceIntent = new Intent(this, MyOverlayService.class);
        startService(serviceIntent);
    }

    // Метод для остановки службы
    private void stopOverlayService() {
        Intent serviceIntent = new Intent(this, MyOverlayService.class);
        stopService(serviceIntent);
    }

    protected void onRestart() {
        super.onRestart();
        stopOverlayService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        startOverlayService();
    }
}
