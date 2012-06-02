package org.metawatch.manager.actions;

import org.metawatch.manager.MediaControl;
import org.metawatch.manager.apps.InternalApp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class InternalActions {
	
	public static class PingAction extends Action {
		Ringtone r = null;
		int volume = -1;
		
		public String getName() {
			if( isSilent() ) {
				return "Ping phone";
			}
			else {
				return "Stop alarm";
			}
		}
		
		public String bulletIcon() {
			return isSilent() ? "bullet_circle.bmp"
					          : "bullet_circle_open.bmp";
		}
	
		public int performAction(Context context) {
			if (isSilent()) {
				Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
				AudioManager as = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				volume = as.getStreamVolume(AudioManager.STREAM_RING);
				as.setStreamVolume(AudioManager.STREAM_RING, as.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
				r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
				r.play();
			}
			else {
				r.stop();
				r = null;
				
				AudioManager as = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				as.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
			}
			return InternalApp.BUTTON_USED;
		}
		
		private boolean isSilent() {
			return (r==null || r.isPlaying() == false);
		}
	}

	public static class SpeakerphoneAction extends Action {
		public SpeakerphoneAction(Context context) {
			audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		}
		
		private AudioManager audioManager = null;
		
		public String getName() {
			return audioManager !=null && audioManager.isSpeakerphoneOn() 
					? "Disable speakerphone"
			        : "Enable speakerphone";
		}
		
		public String bulletIcon() {
			return audioManager !=null && audioManager.isSpeakerphoneOn() 
					? "bullet_circle_open.bmp"
			        : "bullet_circle.bmp";
		}

		public int performAction(Context context) {
			MediaControl.ToggleSpeakerphone(context);
			return InternalApp.BUTTON_USED;
		}
	}
	
	public static class ClickerAction extends Action {
		int count = 0;
		long timestamp = 0;
		
		public String getName() {
			return "Clicker: "+count;
		}
		
		public String bulletIcon() {
			return "bullet_circle.bmp";
		}
	
		public int performAction(Context context) {
			count++;
			timestamp = System.currentTimeMillis();
			
			return InternalApp.BUTTON_USED;
		}
		
		public int getSecondaryType() {
			return Action.SECONDARY_RESET;
		}
		public int performSecondary(Context context) {
			count = 0;
			timestamp = 0;
			return InternalApp.BUTTON_USED;
		}
		
		public long getTimestamp() {
			return timestamp;
		}
	}

	public static class WoodchuckAction extends Action {
		private static final String QUESTION = "How much wood would a woodchuck chuck if a woodchuck could chuck wood?";
		private static final String ANSWER = "A woodchuck could chuck no amount of wood, since a woodchuck can't chuck wood.";
		String name = QUESTION;
		
		public String getName() {
			return name;
		}
		
		public String bulletIcon() {
			return "bullet_square.bmp";
		}

		public int performAction(Context context) {
			name = ANSWER;
			return InternalApp.BUTTON_USED;
		}
		
		public int getSecondaryType() {
			return Action.SECONDARY_RESET;
		}
		public int performSecondary(Context context) {
			name = QUESTION;
			return InternalApp.BUTTON_USED;
		}
	}
	
	public static class MapsAction extends Action {
		public String getName() {
			return "Launch Google Maps on phone";
		}
		
		public String bulletIcon() {
			return "bullet_square.bmp";
		}

		public int performAction(Context context) {					
			Intent mapsIntent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
			context.startActivity(mapsIntent);
			return InternalApp.BUTTON_USED;
		}
	}
}