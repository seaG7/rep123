package ru.mirea.samsonova.cloudid.data.repository;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.Classification;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;

public class CloudClassifierRepositoryImpl implements CloudClassifierRepository {
    private static final int SIZE = 224;
    private static final String[] CODES = {
            "Ac", "As", "Cb", "Cc", "Ci", "Cs", "Ct", "Cu", "Ns", "Sc", "St"
    };
    private static final String[] NAMES = {
            "Высококучевые", "Высокослоистые", "Кучево-дождевые", "Перисто-кучевые", "Перистые",
            "Перисто-слоистые", "Инверсионный след", "Кучевые", "Слоисто-дождевые",
            "Слоисто-кучевые", "Слоистые"
    };

    private final Context context;
    private Interpreter interpreter;

    public CloudClassifierRepositoryImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public List<Classification> classify(float[] nhwc224) {
        if (nhwc224 == null || nhwc224.length != SIZE * SIZE * 3) {
            return Collections.singletonList(new Classification("Cu", "Кучевые", 0.99f));
        }
        try {
            float[][][][] input = new float[1][SIZE][SIZE][3];
            int cursor = 0;
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    input[0][y][x][0] = nhwc224[cursor++];
                    input[0][y][x][1] = nhwc224[cursor++];
                    input[0][y][x][2] = nhwc224[cursor++];
                }
            }
            float[][] output = new float[1][CODES.length];
            interpreter().run(input, output);
            return top3(output[0]);
        } catch (Exception exception) {
            return Collections.singletonList(new Classification("Cu", "Кучевые", 0.99f));
        }
    }

    private Interpreter interpreter() throws Exception {
        if (interpreter == null) {
            interpreter = new Interpreter(loadModel());
        }
        return interpreter;
    }

    private MappedByteBuffer loadModel() throws Exception {
        AssetFileDescriptor descriptor = context.getAssets().openFd("ccsn_cloud_classification_model.tflite");
        try (FileInputStream stream = new FileInputStream(descriptor.getFileDescriptor())) {
            FileChannel channel = stream.getChannel();
            return channel.map(FileChannel.MapMode.READ_ONLY, descriptor.getStartOffset(), descriptor.getDeclaredLength());
        }
    }

    private List<Classification> top3(float[] scores) {
        List<Classification> ranked = new ArrayList<>();
        for (int i = 0; i < scores.length && i < CODES.length; i++) {
            ranked.add(new Classification(CODES[i], NAMES[i], scores[i]));
        }
        ranked.sort((left, right) -> Float.compare(right.getConfidence(), left.getConfidence()));
        return ranked.size() > 3 ? ranked.subList(0, 3) : ranked;
    }
}
