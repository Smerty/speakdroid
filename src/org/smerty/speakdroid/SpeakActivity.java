package org.smerty.speakdroid;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;

public class SpeakActivity extends Activity implements OnInitListener,
    OnUtteranceCompletedListener {

  private TextToSpeech mTts;

  String textToSpeak = null;

  private static int MY_DATA_CHECK_CODE = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent inboundIntent = getIntent();
    textToSpeak = inboundIntent.getStringExtra(Intent.EXTRA_TEXT);

    if (textToSpeak != null && textToSpeak.length() > 0) {
      Intent checkIntent = new Intent();
      checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
      startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    } else {
      finish();
    }
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MY_DATA_CHECK_CODE) {
      if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
        mTts = new TextToSpeech(this, this);
      } else {
        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installIntent);
      }
    }
  }

  @Override
  public void onInit(int arg0) {
    mTts.setLanguage(Locale.US);
    mTts.setOnUtteranceCompletedListener(this);
    HashMap<String, String> params = new HashMap<String, String>();
    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "theid");
    mTts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params);
  }

  @Override
  public void onUtteranceCompleted(String utteranceId) {
    finish();
  }

}
