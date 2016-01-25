package com.raelifin.coi;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by HP on 8/27/2015.
 */
public class VoiceManager implements RecognitionListener {

    public SpeechRecognizer recognizer;
    private VoiceListener listener;

    public VoiceManager(final VoiceListener listener, final Assets assets) {
        this.listener = listener;

        // Recognizer initialization is a time-consuming and it involves IO, so we execute it in async task

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    listener.voiceError("Failed to init recognizer " + result);
                } else {
                    listener.voiceReady();
                }
            }
        }.execute();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                        // To disable logging of raw audio comment out this call (takes a lot of space on the device).setRawLogDir(assetsDir)
                        // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-45f)
                        // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        // Create grammar-based search for digit recognition
        recognizer.addGrammarSearch("normal", new File(assetsDir, "digits.gram"));
    }

    public void destroy() {
        recognizer.cancel();
        recognizer.shutdown();
    }

    @Override
    public void onBeginningOfSpeech() {}

    @Override
    public void onPartialResult(Hypothesis hypothesis) {}

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            listener.hearSpeech(hypothesis.getHypstr());
        }
    }

    @Override
    public void onError(Exception error) {
        listener.voiceError(error.getMessage());
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onTimeout() {
        recognizer.stop();
    }
}