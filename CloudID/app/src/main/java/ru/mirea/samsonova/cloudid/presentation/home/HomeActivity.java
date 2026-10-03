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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.mirea.samsonova.cloudid.R;
import ru.mirea.samsonova.cloudid.presentation.atlas.AtlasFragment;
import ru.mirea.samsonova.cloudid.presentation.catalog.CatalogFragment;
import ru.mirea.samsonova.cloudid.presentation.details.DetailsFragment;
import ru.mirea.samsonova.cloudid.presentation.profile.ProfileFragment;
import ru.mirea.samsonova.cloudid.presentation.sky.SkyFragment;

public class HomeActivity extends AppCompatActivity {
    private static final String TAB_SKY = "sky";
    private static final String TAB_CATALOG = "catalog";
    private static final String TAB_ATLAS = "atlas";
    private static final String TAB_PROFILE = "profile";

    private ImageButton navSky;
    private ImageButton navCatalog;
    private ImageButton navAtlas;
    private ImageButton navProfile;
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
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            cardOpen = detailsOnScreen();
            nav.post(this::applyChrome);
        });
        ViewCompat.setOnApplyWindowInsetsListener(container, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            boolean card = showingCard();
            view.setPadding(0, card ? 0 : bars.top, 0, 0);
            nav.setVisibility(card ? View.GONE : View.VISIBLE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) nav.getLayoutParams();
            params.bottomMargin = bars.bottom + dp(14);
            nav.setLayoutParams(params);
            return insets;
        });

        navSky.setOnClickListener(v -> openTab(TAB_SKY));
        navCatalog.setOnClickListener(v -> openTab(TAB_CATALOG));
        navAtlas.setOnClickListener(v -> openTab(TAB_ATLAS));
        navProfile.setOnClickListener(v -> openTab(TAB_PROFILE));

        if (savedInstanceState == null) {
            openTab(TAB_SKY);
        } else {
            cardOpen = getSupportFragmentManager().getBackStackEntryCount() > 0;
            paint(savedInstanceState.getString("tab", TAB_SKY));
            applyChrome();
        }
    }

    public void openDetails(String code, String photo) {
        cardOpen = true;
        applyChrome();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentContainer, DetailsFragment.newInstance(code, photo))
                .addToBackStack("details")
                .commit();
    }

    public void onDetailsClosed() {
        cardOpen = false;
        View nav = findViewById(R.id.bottomNav);
        nav.post(this::applyChrome);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (navSky.isSelected()) {
            outState.putString("tab", TAB_SKY);
        } else if (navCatalog.isSelected()) {
            outState.putString("tab", TAB_CATALOG);
        } else if (navAtlas.isSelected()) {
            outState.putString("tab", TAB_ATLAS);
        } else {
            outState.putString("tab", TAB_PROFILE);
        }
    }

    private void openTab(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate();
        }
        cardOpen = false;
        applyChrome();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.screen_in, R.anim.screen_out);
        Fragment target = manager.findFragmentByTag(tag);
        for (Fragment fragment : manager.getFragments()) {
            if (fragment instanceof DetailsFragment) {
                continue;
            }
            transaction.hide(fragment);
        }
        if (target == null) {
            transaction.add(R.id.fragmentContainer, fragmentFor(tag), tag);
        } else {
            transaction.show(target);
        }
        transaction.commit();
        paint(tag);
    }

    private Fragment fragmentFor(String tag) {
        if (TAB_CATALOG.equals(tag)) {
            return new CatalogFragment();
        }
        if (TAB_ATLAS.equals(tag)) {
            return new AtlasFragment();
        }
        if (TAB_PROFILE.equals(tag)) {
            return new ProfileFragment();
        }
        return new SkyFragment();
    }

    private void paint(String tag) {
        navSky.setSelected(TAB_SKY.equals(tag));
        navCatalog.setSelected(TAB_CATALOG.equals(tag));
        navAtlas.setSelected(TAB_ATLAS.equals(tag));
        navProfile.setSelected(TAB_PROFILE.equals(tag));
    }

    private boolean detailsOnScreen() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof DetailsFragment && fragment.isAdded() && !fragment.isRemoving()) {
                return true;
            }
        }
        return false;
    }

    private boolean showingCard() {
        return cardOpen || detailsOnScreen();
    }

    private void applyChrome() {
        View container = findViewById(R.id.fragmentContainer);
        View nav = findViewById(R.id.bottomNav);
        boolean card = showingCard();
        nav.setVisibility(card ? View.GONE : View.VISIBLE);
        if (card) {
            container.setPadding(0, 0, 0, 0);
        } else {
            ViewCompat.requestApplyInsets(container);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
