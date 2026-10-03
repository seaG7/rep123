package ru.mirea.samsonova.cloudid.presentation.home;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import ru.mirea.samsonova.cloudid.R;

public class HomeActivity extends AppCompatActivity {
    private ImageButton navSky;
    private ImageButton navCatalog;
    private ImageButton navAtlas;
    private ImageButton navProfile;
    private NavController navController;
    private boolean cardOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        navSky = findViewById(R.id.nav_sky);
        navCatalog = findViewById(R.id.nav_catalog);
        navAtlas = findViewById(R.id.nav_atlas);
        navProfile = findViewById(R.id.nav_profile);

        View container = findViewById(R.id.fragmentContainer);
        View nav = findViewById(R.id.bottomNav);
        ViewCompat.setOnApplyWindowInsetsListener(container, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(0, cardOpen ? 0 : bars.top, 0, 0);
            nav.setVisibility(cardOpen ? View.GONE : View.VISIBLE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) nav.getLayoutParams();
            params.bottomMargin = bars.bottom + dp(14);
            nav.setLayoutParams(params);
            return insets;
        });

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
        navController = host.getNavController();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            cardOpen = id == R.id.detailsFragment;
            applyChrome();
            if (id == R.id.skyFragment) {
                paint(R.id.nav_sky);
            } else if (id == R.id.catalogFragment) {
                paint(R.id.nav_catalog);
            } else if (id == R.id.atlasFragment) {
                paint(R.id.nav_atlas);
            } else if (id == R.id.profileFragment) {
                paint(R.id.nav_profile);
            }
        });

        navSky.setOnClickListener(v -> openTab(R.id.skyFragment));
        navCatalog.setOnClickListener(v -> openTab(R.id.catalogFragment));
        navAtlas.setOnClickListener(v -> openTab(R.id.atlasFragment));
        navProfile.setOnClickListener(v -> openTab(R.id.profileFragment));
    }

    private void openTab(int destination) {
        NavOptions options = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(navController.getGraph().getStartDestinationId(), false, true)
                .setEnterAnim(R.anim.screen_in)
                .setExitAnim(R.anim.screen_out)
                .setPopEnterAnim(R.anim.screen_in)
                .setPopExitAnim(R.anim.screen_out)
                .build();
        navController.navigate(destination, null, options);
    }

    private void paint(int selected) {
        navSky.setSelected(selected == R.id.nav_sky);
        navCatalog.setSelected(selected == R.id.nav_catalog);
        navAtlas.setSelected(selected == R.id.nav_atlas);
        navProfile.setSelected(selected == R.id.nav_profile);
    }

    private void applyChrome() {
        View container = findViewById(R.id.fragmentContainer);
        View nav = findViewById(R.id.bottomNav);
        nav.setVisibility(cardOpen ? View.GONE : View.VISIBLE);
        if (cardOpen) {
            container.setPadding(0, 0, 0, 0);
        } else {
            ViewCompat.requestApplyInsets(container);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
