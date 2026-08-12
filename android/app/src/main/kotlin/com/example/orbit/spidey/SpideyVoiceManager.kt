package com.example.orbit.spidey

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class SpideyVoiceManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isInitialized = true
            Log.d("SpideyVoice", "TTS Initialized")
        } else {
            Log.e("SpideyVoice", "TTS Initialization Failed")
        }
    }

    fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "spidey_utterance")
        } else {
            Log.w("SpideyVoice", "TTS not initialized yet")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
