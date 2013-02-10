package com.amsgames.world.bank;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class GameAudio {

	public static int COLLISION_ONE;
	public static int COLLISION_TWO;
	public static int COLLISION_THREE;
	public static int COLLISION_FOUR;
	public static int COLLISION_CORRECT;
	public static int COLLISION_WRONG;
	public static int VACUUM;
	public static int THEME_EASY;
	public static int THEME_NORMAL;
	public static int THEME_HARD;
	
	private static SoundPool  soundPool;
	private static Context context;
	private static int streamId;
	
	public static void initializeSound(Context c) {
		
		if (GameConfig.audio) {
			
			soundPool = new SoundPool(50, AudioManager.STREAM_MUSIC, 100);
			
			context = c;
			
			COLLISION_ONE = soundPool.load(context, R.raw.collision_1, 1);
			COLLISION_TWO = soundPool.load(context, R.raw.collision_2, 1);
			COLLISION_THREE = soundPool.load(context, R.raw.collision_3, 1);
			COLLISION_FOUR = soundPool.load(context, R.raw.collision_4, 1);
			COLLISION_CORRECT = soundPool.load(context, R.raw.collision_correct, 1);
			COLLISION_WRONG = soundPool.load(context, R.raw.collision_wrong, 1);
			VACUUM = soundPool.load(context, R.raw.vacuum, 1);
			THEME_EASY = soundPool.load(context, R.raw.theme_easy, 1);
			THEME_NORMAL = soundPool.load(context, R.raw.theme_normal, 1);
			THEME_HARD = soundPool.load(context, R.raw.theme_hard, 1);
			
		}
		
	}
	
	public static void playBackground(int soundId) {
		
		if (GameConfig.audio) {
			
			GameConfig.currentBackgroundSound = soundId;
			
			AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			float currentVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = currentVolume / maxVolume;
			
			streamId = soundPool.play(soundId, volume, volume, 1, -1, 1f);
			
		}
		
	}
	
	public static void stopBackground() {
		
		soundPool.stop(streamId);
		
	}
	
	public static void pauseBackground() {
		
		if (GameConfig.audio) {
			
			soundPool.pause(streamId);
			
		}
		
	}
	
	public static void resumeBackground() {
		
		if (GameConfig.audio) {
			
			soundPool.resume(streamId);
			
		}
		
	}
	
	public static void playFX(int soundId) {
		
		if (GameConfig.audio) {
			
			AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			float currentVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = currentVolume / maxVolume;
			
			soundPool.play(soundId, volume, volume, 1, 0, 1f);
			
		}
		
	}
	
	public static void destroySound() {
		
		if (GameConfig.audio) {
			
			soundPool.release();
			
		}
		
	}
}
