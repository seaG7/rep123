package ru.mirea.samsonova.cloudid;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import ru.mirea.samsonova.cloudid.data.repository.AtlasRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.AuthRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.CloudClassifierRepositoryImpl;
import ru.mirea.samsonova.cloudid.data.repository.CloudRepositoryImpl;
import ru.mirea.samsonova.cloudid.domain.GetCloudCatalogUseCase;
import ru.mirea.samsonova.cloudid.domain.GetMyAtlasUseCase;
import ru.mirea.samsonova.cloudid.domain.GetProfileUseCase;
import ru.mirea.samsonova.cloudid.domain.IdentifyCloudUseCase;
import ru.mirea.samsonova.cloudid.domain.LoginUseCase;
import ru.mirea.samsonova.cloudid.domain.SaveSightingUseCase;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.models.User;
import ru.mirea.samsonova.cloudid.domain.repository.AtlasRepository;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AuthRepository authRepository = new AuthRepositoryImpl(this);
        CloudRepository cloudRepository = new CloudRepositoryImpl(this);
        AtlasRepository atlasRepository = new AtlasRepositoryImpl(this);
        CloudClassifierRepository classifierRepository = new CloudClassifierRepositoryImpl(this);

        EditText editTextLogin = findViewById(R.id.editTextLogin);
        TextView textViewResult = findViewById(R.id.textViewResult);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new LoginUseCase(authRepository)
                        .execute(editTextLogin.getText().toString(), "test");
                User user = new GetProfileUseCase(authRepository).execute();
                textViewResult.setText(String.format("Login result %s, user %s",
                        result, user.getLogin()));
            }
        });

        findViewById(R.id.buttonIdentify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloudType type = new IdentifyCloudUseCase(classifierRepository, cloudRepository).execute();
                textViewResult.setText(String.format("Match: %s (%s)\n%s",
                        type.getName(), type.getLatin(), type.getExtract()));
            }
        });

        findViewById(R.id.buttonCatalog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<CloudType> catalog = new GetCloudCatalogUseCase(cloudRepository).execute();
                StringBuilder builder = new StringBuilder();
                for (CloudType type : catalog) {
                    builder.append(type.getCode())
                            .append(" — ")
                            .append(type.getName())
                            .append("\n");
                }
                textViewResult.setText(builder.toString());
            }
        });

        findViewById(R.id.buttonAtlas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Sighting> atlas = new GetMyAtlasUseCase(atlasRepository, authRepository).execute();
                if (atlas.isEmpty()) {
                    textViewResult.setText("Атлас пуст (гость не видит сохранённое)");
                    return;
                }
                StringBuilder builder = new StringBuilder();
                for (Sighting item : atlas) {
                    builder.append(item.getCloudCode())
                            .append(" — ")
                            .append(item.getCloudName())
                            .append(" (")
                            .append(item.getNote())
                            .append(")\n");
                }
                textViewResult.setText(builder.toString());
            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new SaveSightingUseCase(atlasRepository, authRepository)
                        .execute(new Sighting(3, "Cb", "Кучево-дождевые", "после дождя"));
                textViewResult.setText(String.format("Save result %s", result));
            }
        });
    }
}
